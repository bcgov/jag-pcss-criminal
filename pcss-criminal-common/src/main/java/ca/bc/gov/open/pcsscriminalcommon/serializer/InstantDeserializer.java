package ca.bc.gov.open.pcsscriminalcommon.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        try {
            if (jsonParser.getText().split("-")[0].length() < 4) {
                String dt = jsonParser.getText().replaceAll("(\\.\\d{3})\\d*", "$1"); // Truncate ms to 3 digits
                var sfd = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSS a", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                return sfd.parse(dt).toInstant();
            } else {
                var sfd = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                return sfd.parse(jsonParser.getText()).toInstant();
            }
        } catch (ParseException e) {
            try {
                var sfd = new SimpleDateFormat("dd-MMM-yy", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("GMT-7"));
                return sfd.parse(jsonParser.getText()).toInstant();
            } catch (ParseException e2) {
                log.error(e2.getLocalizedMessage());
            }
        }
        return null;
    }
}
