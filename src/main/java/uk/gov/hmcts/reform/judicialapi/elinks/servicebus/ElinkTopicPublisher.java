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
            log.info("******************************{}:: messages sent with transaction");
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
        log.info("****************************{}:: partitioning jrdMessageBatchSize is 100 so creatign batches of 100");
        partition(judicalIds, jrdMessageBatchSize)
            .forEach(data -> {
                PublishingData judicialDataChunk = new PublishingData();
                judicialDataChunk.setUserIds(data);
                serviceBusMessages.add(new ServiceBusMessage(new Gson().toJson(judicialDataChunk)));
            });
        log.info("***********************{}:: serviceBusMessages size or number of batches",serviceBusMessages.size());
        if (serviceBusMessages.size() > maxBatchesPerTransaction) {
            log.info("££££££££££££££££££££££{}:: The number of batches exceeds 100 hence we "
                + "send in saperate transactions  ");
            //The number of batches in a transaction will exceed 100 hence we send ion saperate transactions
            List<ServiceBusMessage> currentBatch = new ArrayList<>();
            //ServiceBusTransactionContext elinktransactionNewContext = null;
            for (ServiceBusMessage message : serviceBusMessages) {
                currentBatch.add(message);
                log.info("_____________________{}:: iterating the partition and adding to "
                    + "a currentBatchlist  ",currentBatch.size());
                if (currentBatch.size() == maxBatchesPerTransaction) {

                    log.info("!!!!!!!!!!!!!!!!!!!!{}:: sending the 100 partitions "
                        + "to azure sb in a same transaction    ",currentBatch.size());
                    // The batch is full, so we create a new batch and send the batch.
                    sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
                    commitTransaction(transactionContext);

                    log.info("!!!!!!!!!!!!!!!!!!!!{}::now  creatign a new transaction "
                        + " and new batch for the partition ",currentBatch.size());
                    transactionContext =
                        elinkserviceBusSenderClient.createTransaction();
                    // create a new batch
                    elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
                    log.info("!!!!!!!!!!!!!!!!!!!!{}::clearign currentBatch   ",currentBatch.size());
                    currentBatch.clear();

                    if (elinkmessageBatch.tryAddMessage(message)) {
                        log.info("!!!!!!!!!!!!!!!!!!!!{}:: checking the size of "
                            + "partition and addign to new batch elinkmessageBatch ",currentBatch.size());
                        continue;
                    }
                    log.info("!!!!!!!!!!!!!!!!!!!!{}:: batch seems to be "
                        + "full so sendign msg to azure SB new transaction",currentBatch.size());
                    // The batch is full, so we create a new batch and send the batch.
                    sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
                    log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!{}:: now creatign new batch ",currentBatch.size());
                    // create a new batch
                    elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
                    // Add that message that we couldn't before.
                    if (!elinkmessageBatch.tryAddMessage(message)) {
                        log.error("{}:: Message is too large for"
                                + " an empty batch. Skipping. Max size: {}. Job id::{}",
                            loggingComponentName, elinkmessageBatch.getMaxSizeInBytes(), jobId);
                    }


                } else {
                    log.info("££££££££££££££££££££££{}:: the number of partitions "
                        + "still nto exceeded 100 so sending in same batch and transaction ",currentBatch.size());
                    if (elinkmessageBatch.tryAddMessage(message)) {
                        log.info("££££££££££££££££££££££{}:: checking the size of "
                            + "partition and addign to batch elinkmessageBatch ",currentBatch.size());
                        continue;
                    }
                    addMessagesToBatch(elinkmessageBatch,serviceBusSenderClient,transactionContext,message);
                }

            }
            // Send remaining records if any
            if (!currentBatch.isEmpty()) {
                for (ServiceBusMessage elinkmessage : currentBatch) {
                    elinkmessageBatch.tryAddMessage(elinkmessage);
                }
                log.info("^^^^^^^^^^^^^^^^{}:: if any remining msgs then create "
                    + "new transaction and send to azure ",elinkmessageBatch.getCount());
                transactionContext = elinkserviceBusSenderClient.createTransaction();
                sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
                commitTransaction(transactionContext);
            }

        } else {
            log.info("%%%%%%%%%%%%%%%%%%%{}:: The number of batches does not exceeds"
                + " 100 hence we send in single transactions old code not touched ");
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
            log.info("%%%%%%%%%%%%%%%%%%%{}:: iteratign each ServiceBusMessage partition  ");
            if (elinkmessageBatch.tryAddMessage(message)) {
                log.info("%%%%%%%%%%%%%%%%%%%{}:: checked size of each "
                    + "partition with msgs and added to batch elinkmessageBatch ");
                continue;
            }
            addMessagesToBatch(elinkmessageBatch,serviceBusSenderClient,transactionContext,message);
        }
        log.info("%%%%%%%%%%%%%%%%%%%{}::out of for loop sending the remaining messages to azure service bus ");
        sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, jobId);
    }

    private void addMessagesToBatch(ServiceBusMessageBatch elinkmessageBatch,
                                    ServiceBusSenderClient serviceBusSenderClient,
                                    ServiceBusTransactionContext transactionContext,
                                    ServiceBusMessage message) {
        log.info("%%%%%%%%%%%%%%%%%%%{}::size of elinkmessageBatch full "
            + "so sending to azure service bus still same transaction");
        // The batch is full, so we create a new batch and send the batch.
        sendMessageToAsb(serviceBusSenderClient, transactionContext, elinkmessageBatch, "1234");
        log.info("%%%%%%%%%%%%%%%%%%%{}::now creatign a new batch ");
        // create a new batch
        elinkmessageBatch = serviceBusSenderClient.createMessageBatch();
        // Add that message that we couldn't before.
        if (!elinkmessageBatch.tryAddMessage(message)) {
            log.error("{}:: Message is too large for an empty batch. Skipping. Max size: {}. Job id::{}",
                loggingComponentName, elinkmessageBatch.getMaxSizeInBytes(), "1234");
        }
        log.info("%%%%%%%%%%%%%%%%%%%{}::continuing to add the next partiotion ");
    }

    private void commitTransaction(ServiceBusTransactionContext txContext) {
        if (Objects.nonNull(txContext)) {
            elinkserviceBusSenderClient.commitTransaction(txContext);
            log.info("{}:: Transaction committed successfully", loggingComponentName);
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
