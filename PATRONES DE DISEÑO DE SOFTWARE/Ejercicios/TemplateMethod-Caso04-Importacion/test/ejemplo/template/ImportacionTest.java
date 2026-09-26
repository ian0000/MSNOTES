package ejemplo.template;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ImportacionTest {
    private static int aprobadas;
    private static Path carpeta;
    private ImportacionTest() { }
    @FunctionalInterface private interface Prueba { void ejecutar() throws Exception; }

    public static void main(String[] args) throws Exception {
        carpeta = Files.createTempDirectory(Path.of("out"), "pruebas-");
        probar("los tres formatos producen la misma salida", () -> {
            String esperado = "codigo,nombre\n\"P001\",\"Teclado\"\n\"P002\",\"Monitor, 24 pulgadas\"\n";
            for (String formato : List.of("csv", "json", "xml")) {
                Path destino = salida();
                List<Registro> registros = crear(formato, destino).importar(Path.of("datos", "registros." + formato));
                verificar(registros.size() == 2 && registros.get(0).obtenerCodigo().equals("P001"));
                verificar(Files.readString(destino).equals(esperado));
                falla(UnsupportedOperationException.class, () -> registros.add(new Registro("X", "X")));
            }
        });
        probar("colecciones vacías válidas", () -> {
            for (Map.Entry<String, String> dato : Map.of("csv", "codigo,nombre\n", "json", "[]", "xml", "<registros/>").entrySet()) {
                Path destino = salida();
                verificar(crear(dato.getKey(), destino).importar(entrada(dato.getValue())).isEmpty());
                verificar(Files.readString(destino).equals("codigo,nombre\n"));
            }
        });
        probar("CSV con BOM, comas, comillas y líneas internas", () -> {
            String texto = "\uFEFFcodigo,nombre\r\nA,\"Pantalla, \"\"Grande\"\"\"\r\nB,\"Línea 1\nLínea 2\"\r\n";
            List<Registro> registros = new ImportCSV(salida()).importar(entrada(texto));
            verificar(registros.get(0).obtenerNombre().equals("Pantalla, \"Grande\""));
            verificar(registros.get(1).obtenerNombre().equals("Línea 1\nLínea 2"));
        });
        probar("JSON interpreta escapes y no depende del orden", () -> {
            String texto = "[{\"nombreCompleto\":\"L\\u00e1piz \\\"azul\\\"\",\"id\":\"A\"}]";
            Registro registro = new ImportJSON(salida()).importar(entrada(texto)).get(0);
            verificar(registro.obtenerNombre().equals("Lápiz \"azul\""));
        });
        probar("XML interpreta entidades de texto", () -> {
            String texto = "<registros><registro codigo=\"A\"><nombre>Teclado &amp; ratón</nombre></registro></registros>";
            verificar(new ImportXML(salida()).importar(entrada(texto)).get(0).obtenerNombre().equals("Teclado & ratón"));
        });
        probar("validar impide leer entradas inválidas", () -> {
            Path destino = salida(); List<String> pasos = new ArrayList<>();
            ImportacionInformacion<String> espia = espia(destino, pasos, false, false);
            for (Path invalido : List.of(carpeta.resolve("inexistente"), carpeta, entrada(""))) {
                falla(IOException.class, () -> espia.importar(invalido));
            }
            verificar(pasos.isEmpty() && !Files.exists(destino));
        });
        probar("CSV inválido no se almacena", () -> {
            for (String texto : List.of("codigo,nombre\nA,\"sin cerrar", "codigo,nombre\nA,B,C", "id,nombre\nA,B", "codigo,nombre\nA,\"B\"x")) {
                rechazar("csv", texto);
            }
        });
        probar("JSON inválido o fuera del esquema no se almacena", () -> {
            for (String texto : List.of("[", "{}", "[] []", "[{\"id\":1,\"nombreCompleto\":\"A\"}]",
                    "[{\"id\":\"A\"}]", "[{\"id\":\"A\",\"id\":\"B\",\"nombreCompleto\":\"C\"}]")) {
                rechazar("json", texto);
            }
        });
        probar("XML inválido o fuera del esquema no se almacena", () -> {
            for (String texto : List.of("<registros>", "<otraRaiz/>", "<registros><registro><nombre>A</nombre></registro></registros>",
                    "<registros><registro codigo=\"A\"/></registros>", "<registros><registro codigo=\"A\"><nombre><otro/></nombre></registro></registros>")) {
                rechazar("xml", texto);
            }
        });
        probar("XML no resuelve entidades externas", () -> rechazar("xml",
                "<!DOCTYPE registros [<!ENTITY externo SYSTEM 'file:///no-debe-leerse'>]>"
                + "<registros><registro codigo=\"A\"><nombre>&externo;</nombre></registro></registros>"));
        probar("la transformación exige campos no vacíos", () -> {
            rechazar("csv", "codigo,nombre\nA,   ");
            rechazar("json", "[{\"id\":\" \",\"nombreCompleto\":\"A\"}]");
            rechazar("xml", "<registros><registro codigo=\"A\"><nombre> </nombre></registro></registros>");
        });
        probar("orden leer-transformar-almacenar y plantilla final", () -> {
            List<String> pasos = new ArrayList<>(); Path destino = salida();
            espia(destino, pasos, false, false).importar(entrada("dato"));
            verificar(pasos.equals(List.of("leer", "transformar")) && Files.exists(destino));
            verificar(Modifier.isFinal(ImportacionInformacion.class.getMethod("importar", Path.class).getModifiers()));
            verificar(Modifier.isFinal(ImportacionInformacion.class.getDeclaredMethod("almacenarDatos", List.class).getModifiers()));
        });
        probar("error de lectura detiene transformación y almacenamiento", () -> {
            List<String> pasos = new ArrayList<>(); Path destino = salida();
            falla(IOException.class, () -> espia(destino, pasos, true, false).importar(entrada("dato")));
            verificar(pasos.equals(List.of("leer")) && !Files.exists(destino));
        });
        probar("error de transformación detiene almacenamiento", () -> {
            List<String> pasos = new ArrayList<>(); Path destino = salida();
            falla(IllegalArgumentException.class, () -> espia(destino, pasos, false, true).importar(entrada("dato")));
            verificar(pasos.equals(List.of("leer", "transformar")) && !Files.exists(destino));
        });
        probar("no sobrescribe destino ni entrada", () -> {
            Path fuente = entrada("codigo,nombre\nA,B"); Path destino = entrada("conservar");
            falla(IOException.class, () -> new ImportCSV(destino).importar(fuente));
            verificar(Files.readString(destino).equals("conservar"));
            falla(IOException.class, () -> new ImportCSV(fuente).importar(fuente));
            verificar(Files.readString(fuente).equals("codigo,nombre\nA,B"));
        });
        probar("reutilizar el importador no mezcla datos antiguos", () -> {
            Path destino = salida(); ImportJSON importador = new ImportJSON(destino);
            importador.importar(entrada("[{\"id\":\"A\",\"nombreCompleto\":\"B\"}]"));
            String anterior = Files.readString(destino);
            falla(IOException.class, () -> importador.importar(entrada("[")));
            verificar(Files.readString(destino).equals(anterior));
        });
        probar("fallo de almacenamiento se propaga", () -> {
            Path archivoNoCarpeta = entrada("dato");
            falla(IOException.class, () -> new ImportCSV(archivoNoCarpeta.resolve("salida.csv"))
                    .importar(entrada("codigo,nombre\nA,B")));
        });
        System.out.println("Resultado: " + aprobadas + "/17 pruebas aprobadas.");
    }

    private static ImportacionInformacion<?> crear(String formato, Path destino) {
        switch (formato) {
            case "csv": return new ImportCSV(destino);
            case "json": return new ImportJSON(destino);
            case "xml": return new ImportXML(destino);
            default: throw new IllegalArgumentException(formato);
        }
    }
    private static ImportacionInformacion<String> espia(Path destino, List<String> pasos, boolean falloLectura, boolean falloTransformacion) {
        return new ImportacionInformacion<String>(destino) {
            @Override protected String leerDatos(Path archivo) throws IOException {
                verificar(!Files.exists(destino)); pasos.add("leer");
                if (falloLectura) { throw new IOException("Lectura fallida"); }
                return Files.readString(archivo);
            }
            @Override protected List<Registro> transformarDatos(String datos) {
                verificar(pasos.equals(List.of("leer")) && datos.equals("dato") && !Files.exists(destino));
                pasos.add("transformar");
                if (falloTransformacion) { throw new IllegalArgumentException("Transformación fallida"); }
                return List.of(new Registro("A", "B"));
            }
        };
    }
    private static void rechazar(String formato, String contenido) throws Exception {
        Path destino = salida();
        falla(Exception.class, () -> crear(formato, destino).importar(entrada(contenido)));
        verificar(!Files.exists(destino));
    }
    private static Path entrada(String contenido) throws IOException {
        return Files.writeString(Files.createTempFile(carpeta, "entrada-", ".txt"), contenido, StandardCharsets.UTF_8);
    }
    private static Path salida() throws IOException {
        return Files.createTempDirectory(carpeta, "caso-").resolve("salida.csv");
    }
    private static void probar(String nombre, Prueba prueba) throws Exception {
        prueba.ejecutar(); aprobadas++; System.out.println("OK: " + nombre);
    }
    private static void verificar(boolean condicion) { if (!condicion) { throw new AssertionError(); } }
    private static void falla(Class<? extends Exception> tipo, Prueba prueba) throws Exception {
        try { prueba.ejecutar(); } catch (Exception error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Excepción inesperada", error);
        }
        throw new AssertionError("Faltó " + tipo.getSimpleName());
    }
}
