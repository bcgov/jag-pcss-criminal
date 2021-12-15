package ca.bc.gov.open.pcsscriminalcommon.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        try {

            SimpleDateFormat d = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSS a", Locale.US);
            d.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));

            return d.parse(jsonParser.getText()).toInstant();

        } catch (ParseException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
