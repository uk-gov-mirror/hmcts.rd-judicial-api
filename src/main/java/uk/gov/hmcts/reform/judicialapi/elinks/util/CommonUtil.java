package uk.gov.hmcts.reform.judicialapi.elinks.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class CommonUtil {


    public String getUpdatedDateFormat(String lastUpdated) {

        if (StringUtils.isAlpha(lastUpdated)) {
            return ZonedDateTime.parse(lastUpdated,
                            DateTimeFormatter.ofPattern(
                                    "EEE MMM dd HH:mm:ss z uuuu",
                                    Locale.UK))
                    .toLocalDate()
                    .format(DateTimeFormatter.BASIC_ISO_DATE);
        }
        return lastUpdated;
    }
}
