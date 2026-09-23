package ejemplo.adapter.proveedores;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Sustituto local de una biblioteca externa con API propia, tratada como fija. */
public class Proveedor2 {
    private final List<MarcacionProveedor2> marcaciones;

    public Proveedor2(List<MarcacionProveedor2> marcaciones) {
        this.marcaciones = List.copyOf(marcaciones);
    }

    public List<MarcacionProveedor2> recuperarMarcaciones(String documento, String fechaISO) {
        Objects.requireNonNull(documento, "documento");
        LocalDate fecha = LocalDate.parse(fechaISO);
        return marcaciones.stream()
                .filter(marca -> documento.equals(marca.getDocumento()))
                .filter(marca -> LocalDateTime.parse(marca.getInstanteISO()).toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }
}
