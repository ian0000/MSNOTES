package ejemplo.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/** AbstractClass: fija la secuencia; T es la representación que lee cada formato. */
public abstract class ImportacionInformacion<T> {
    private final Path destino;

    protected ImportacionInformacion(Path destino) {
        this.destino = Objects.requireNonNull(destino, "destino").toAbsolutePath().normalize();
    }

    /** Template Method: las subclases no pueden sustituir ni reordenar este flujo. */
    public final List<Registro> importar(Path archivo) throws IOException {
        Objects.requireNonNull(archivo, "archivo");
        if (!validarArchivo(archivo)) {
            throw new IOException("Se requiere un archivo regular, legible y no vacío: " + archivo);
        }
        if (archivo.toAbsolutePath().normalize().equals(destino)
                || (Files.exists(destino) && Files.isSameFile(archivo, destino))) {
            throw new IOException("El destino no puede ser el archivo de entrada");
        }
        T datos = leerDatos(archivo);
        List<Registro> registros = List.copyOf(transformarDatos(datos));
        almacenarDatos(registros);
        return registros;
    }

    protected final boolean validarArchivo(Path archivo) throws IOException {
        return Files.isRegularFile(archivo) && Files.isReadable(archivo) && Files.size(archivo) > 0;
    }

    protected abstract T leerDatos(Path archivo) throws IOException;
    protected abstract List<Registro> transformarDatos(T datos);

    /** Paso común: guarda los registros normalizados en un CSV UTF-8 nuevo. */
    protected final void almacenarDatos(List<Registro> registros) throws IOException {
        StringBuilder contenido = new StringBuilder("codigo,nombre\n");
        for (Registro registro : registros) {
            contenido.append(Csv.campo(registro.obtenerCodigo())).append(',')
                    .append(Csv.campo(registro.obtenerNombre())).append('\n');
        }
        Files.createDirectories(destino.getParent());
        // CREATE_NEW permite repetir el ejercicio sin sobrescribir archivos ajenos.
        Files.writeString(destino, contenido, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    }
}
