package uk.gov.hmcts.reform.judicialapi.elinks.servicebus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusMessageBatch;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.ServiceBusTransactionContext;
import com.google.gson.Gson;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.judicialapi.elinks.configuration.PublishingData;
import uk.gov.hmcts.reform.judicialapi.elinks.exception.ElinksException;
import uk.gov.hmcts.reform.judicialapi.elinks.util.ElinkDataIngestionSchedularAudit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.partition;
import static uk.gov.hmcts.reform.judicialapi.elinks.util.RefDataElinksConstants.UNAUTHORIZED_ERROR;

@Slf4j
@Component
public class ElinkTopicPublisher {

    @Value("${logging-component-name}")
    String loggingComponentName;
    @Value("${jrd.publisher.jrd-message-batch-size}")
    int jrdMessageBatchSize;
    @Value("${jrd.publisher.azure.service.bus.topic}")
    String topic;
    @Value("${jrd.publisher.jrd-batches-per-transaction}")
    int maxBatchesPerTransaction;
    @Autowired
    ElinkDataIngestionSchedularAudit elinkDataIngestionSchedularAudit;

    @Autowired
    private ServiceBusSenderClient elinkserviceBusSenderClient;

    public void sendMessage(@NotNull List<String> judicalIds, String jobId) {
        ServiceBusTransactionContext elinktransactionContext = null;
        try {
            elinktransactionContext = elinkserviceBusSenderClient.createTransaction();
            publishMessageToTopic(judicalIds, elinkserviceBusSenderClient, elinktransactionContext, jobId);
        } catch (Exception exception) {
            log.error("{}:: Publishing message to service bus topic failed with exception: {}:: Job Id {}",
                loggingComponentName, exception.getMessage(), jobId);
            if (Objects.nonNull(elinktransactionContext) && Objects.nonNull(elinkserviceBusSenderClient)) {
                elinkserviceBusSenderClient.rollbackTransaction(elinktransactionContext);
            }
            throw new ElinksException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_ERROR, UNAUTHORIZED_ERROR);
        }
    }

    private void publishMessageToTopic(List<String> judicalIds,
                                       ServiceBusSenderClient serviceBusSenderClient,
                                       ServiceBusTransactionContext transactionContext,
                                       String jobId) {

        ServiceBusMessageBatch elinkmessageBatch;
        try {
            elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
        } catch (Exception ex) {
            throw new ElinksException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_ERROR, ex.getMessage());
        }
        List<ServiceBusMessage> serviceBusMessages = new ArrayList<>();
        partition(judicalIds, jrdMessageBatchSize)
            .forEach(data -> {
                PublishingData judicialDataChunk = new PublishingData();
                judicialDataChunk.setUserIds(data);
                serviceBusMessages.add(new ServiceBusMessage(new Gson().toJson(judicialDataChunk)));
            });
        if (serviceBusMessages.size() > maxBatchesPerTransaction) {
            //The number of batches in a transaction will exceed 100 hence we send ion saperate transactions
            List<ServiceBusMessage> currentBatch = new ArrayList<>();
            for (ServiceBusMessage message : serviceBusMessages) {
                currentBatch.add(message);
                if (!elinkmessageBatch.tryAddMessage(message)) {
                    log.error("{}:: Message is too large for an empty batch. Skipping. Max size: {}. Job id::{}",
                        loggingComponentName, elinkmessageBatch.getMaxSizeInBytes(), jobId);
                }
                if (currentBatch.size() == maxBatchesPerTransaction) {
                    // The batch is full, so we create a new batch and send the batch.
                    sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
                    commitTransaction(transactionContext);
                    transactionContext = elinkserviceBusSenderClient.createTransaction();
                    elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
                    currentBatch.clear();
                }

            }
            if (currentBatch.size() > 0) {
                if (transactionContext == null) {
                    transactionContext = elinkserviceBusSenderClient.createTransaction();
                }
                sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
                commitTransaction(transactionContext);
            }
        } else {
            prepareMessageBatch(elinkmessageBatch, serviceBusSenderClient, transactionContext,
                jobId, serviceBusMessages);
            commitTransaction(transactionContext);
        }
    }

    private void prepareMessageBatch(ServiceBusMessageBatch elinkmessageBatch,
                                     ServiceBusSenderClient serviceBusSenderClient,
                                     ServiceBusTransactionContext transactionContext,
                                     String jobId,List<ServiceBusMessage> serviceBusMessages) {

        for (ServiceBusMessage message : serviceBusMessages) {
            if (elinkmessageBatch.tryAddMessage(message)) {
                continue;
            }
            addMessagesToBatch(elinkmessageBatch,serviceBusSenderClient,transactionContext,message);
        }
        sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
    }

    private void addMessagesToBatch(ServiceBusMessageBatch elinkmessageBatch,
                                    ServiceBusSenderClient serviceBusSenderClient,
                                    ServiceBusTransactionContext transactionContext,
                                    ServiceBusMessage message) {
        // The batch is full, so we create a new batch and send the batch.
        sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, "1234");
        // create a new batch
        elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
        // Add that message that we couldn't before.
        if (!elinkmessageBatch.tryAddMessage(message)) {
            log.error("{}:: Message is too large for an empty batch. Skipping. Max size: {}. Job id::{}",
                loggingComponentName, elinkmessageBatch.getMaxSizeInBytes(), "1234");
        }
    }

    private void commitTransaction(ServiceBusTransactionContext txContext) {
        if (Objects.nonNull(txContext)) {
            elinkserviceBusSenderClient.commitTransaction(txContext);
        }
    }

    private void sendMessageToAsb(ServiceBusSenderClient serviceBusSenderClient,
                                  ServiceBusTransactionContext transactionContext,
                                  ServiceBusMessageBatch messageBatch,
                                  String jobId) {
        if (messageBatch.getCount() > 0) {
            serviceBusSenderClient.sendMessages(messageBatch, transactionContext);
            log.info("{}:: Sent a batch of messages to the topic: {} ::Job id::{}", loggingComponentName, topic, jobId);
        }
    }
}
