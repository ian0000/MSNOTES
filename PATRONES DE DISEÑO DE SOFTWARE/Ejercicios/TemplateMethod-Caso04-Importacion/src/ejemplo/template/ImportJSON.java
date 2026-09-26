package ejemplo.template;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadFeature;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ImportJSON extends ImportacionInformacion<List<Map<String, String>>> {
    private static final JsonFactory FACTORY = JsonFactory.builder()
            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION).build();

    public ImportJSON(Path destino) { super(destino); }

    @Override protected List<Map<String, String>> leerDatos(Path archivo) throws IOException {
        List<Map<String, String>> objetos = new ArrayList<>();
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8);
             JsonParser parser = FACTORY.createParser(lector)) {
            exigir(parser.nextToken(), JsonToken.START_ARRAY);
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                exigir(parser.currentToken(), JsonToken.START_OBJECT);
                Map<String, String> objeto = new LinkedHashMap<>();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    exigir(parser.currentToken(), JsonToken.FIELD_NAME);
                    String campo = parser.currentName();
                    exigir(parser.nextToken(), JsonToken.VALUE_STRING);
                    objeto.put(campo, parser.getText());
                }
                objetos.add(objeto);
            }
            if (parser.nextToken() != null) { throw new IOException("Contenido después del arreglo JSON"); }
        }
        return objetos;
    }

    @Override protected List<Registro> transformarDatos(List<Map<String, String>> objetos) {
        List<Registro> registros = new ArrayList<>();
        for (Map<String, String> objeto : objetos) {
            if (!objeto.keySet().equals(Set.of("id", "nombreCompleto"))) {
                throw new IllegalArgumentException("Cada objeto JSON necesita id y nombreCompleto");
            }
            registros.add(new Registro(objeto.get("id"), objeto.get("nombreCompleto")));
        }
        return registros;
    }

    private static void exigir(JsonToken actual, JsonToken esperado) throws IOException {
        if (actual != esperado) { throw new IOException("Se esperaba " + esperado + ", se recibió " + actual); }
    }
}
