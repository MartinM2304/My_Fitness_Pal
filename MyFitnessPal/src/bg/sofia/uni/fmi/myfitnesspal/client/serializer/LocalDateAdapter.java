package bg.sofia.uni.fmi.myfitnesspal.client.serializer;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE;

    public static class Serializer implements JsonSerializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc,
            com.google.gson.JsonSerializationContext context) {
            return new JsonPrimitive(src.format(FORMATTER));
        }
    }

    public static class Deserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json,
            Type typeOfT, com.google.gson.JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString(), FORMATTER);
        }
    }
}
