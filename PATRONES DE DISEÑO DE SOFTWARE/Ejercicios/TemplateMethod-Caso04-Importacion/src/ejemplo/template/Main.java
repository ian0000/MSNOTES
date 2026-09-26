package ejemplo.template;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        if (args.length != 0 && args.length != 3) {
            throw new IllegalArgumentException("Uso: Main [csv|json|xml entrada destino.csv]");
        }
        if (args.length == 3) {
            ejecutar(args[0], Path.of(args[1]), Path.of(args[2]));
            return;
        }
        Files.createDirectories(Path.of("out", "resultados"));
        Path carpeta = Files.createTempDirectory(Path.of("out", "resultados"), "ejecucion-");
        for (String formato : List.of("csv", "json", "xml")) {
            ejecutar(formato, Path.of("datos", "registros." + formato), carpeta.resolve(formato + ".csv"));
        }
    }

    private static void ejecutar(String formato, Path entrada, Path destino) throws Exception {
        ImportacionInformacion<?> importador;
        switch (formato) {
            case "csv": importador = new ImportCSV(destino); break;
            case "json": importador = new ImportJSON(destino); break;
            case "xml": importador = new ImportXML(destino); break;
            default: throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
        // El cliente solo llama a la plantilla, nunca a los pasos internos.
        List<Registro> registros = importador.importar(entrada);
        System.out.println(formato + ": " + registros.size() + " registros -> " + destino);
        for (Registro registro : registros) {
            System.out.println("  " + registro.obtenerCodigo() + " | " + registro.obtenerNombre());
        }
    }
}
