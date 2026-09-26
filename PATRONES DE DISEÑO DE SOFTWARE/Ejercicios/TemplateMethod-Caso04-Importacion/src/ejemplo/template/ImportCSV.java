package ejemplo.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ImportCSV extends ImportacionInformacion<List<List<String>>> {
    public ImportCSV(Path destino) { super(destino); }

    @Override protected List<List<String>> leerDatos(Path archivo) throws IOException {
        return Csv.leer(Files.readString(archivo, StandardCharsets.UTF_8));
    }

    @Override protected List<Registro> transformarDatos(List<List<String>> filas) {
        if (filas.isEmpty() || !filas.get(0).equals(List.of("codigo", "nombre"))) {
            throw new IllegalArgumentException("La cabecera CSV debe ser codigo,nombre");
        }
        List<Registro> registros = new ArrayList<>();
        for (int i = 1; i < filas.size(); i++) {
            List<String> fila = filas.get(i);
            if (fila.size() != 2) { throw new IllegalArgumentException("Columnas incorrectas en fila " + (i + 1)); }
            registros.add(new Registro(fila.get(0), fila.get(1)));
        }
        return registros;
    }
}
