package medical.app.medicalappfx.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class UuidAdapter extends TypeAdapter<UUID> {
    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(value == null ? null : value.toString());
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        return UUID.fromString(in.nextString());
    }
}
