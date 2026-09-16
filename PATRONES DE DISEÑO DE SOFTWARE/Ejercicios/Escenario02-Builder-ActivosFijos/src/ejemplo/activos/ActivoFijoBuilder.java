package ejemplo.activos;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Builder moderno: pasos públicos que conservan los nombres del UML del grupo. */
public final class ActivoFijoBuilder {
    private String codigoInstitucional;
    private String nombre;
    private String descripcion;
    private BigDecimal precioAdquisicion;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String ubicacion;
    private String custodioResponsable;
    private LocalDate fechaAdquisicion;
    private Integer periodoGarantia;
    private String proveedor;
    private String caracteristicasTecnicas;
    private String observaciones;

    public ActivoFijoBuilder codigoInstitucional(String valor) {
        codigoInstitucional = texto(valor, "codigoInstitucional");
        return this;
    }

    public ActivoFijoBuilder nombre(String valor) {
        nombre = texto(valor, "nombre");
        return this;
    }

    public ActivoFijoBuilder descripcion(String valor) {
        descripcion = texto(valor, "descripcion");
        return this;
    }

    public ActivoFijoBuilder precioAdquisicion(BigDecimal valor) {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException("precioAdquisicion debe ser no nulo y no negativo");
        }
        precioAdquisicion = valor;
        return this;
    }

    public ActivoFijoBuilder marca(String valor) {
        marca = texto(valor, "marca");
        return this;
    }

    public ActivoFijoBuilder modelo(String valor) {
        modelo = texto(valor, "modelo");
        return this;
    }

    public ActivoFijoBuilder numeroSerie(String valor) {
        numeroSerie = texto(valor, "numeroSerie");
        return this;
    }

    public ActivoFijoBuilder ubicacion(String valor) {
        ubicacion = texto(valor, "ubicacion");
        return this;
    }

    public ActivoFijoBuilder custodioResponsable(String valor) {
        custodioResponsable = texto(valor, "custodioResponsable");
        return this;
    }

    public ActivoFijoBuilder fechaAdquisicion(LocalDate valor) {
        if (valor == null) {
            throw new IllegalArgumentException("fechaAdquisicion no puede ser null");
        }
        fechaAdquisicion = valor;
        return this;
    }

    /** El período se expresa en meses. */
    public ActivoFijoBuilder periodoGarantia(int meses) {
        if (meses < 0) {
            throw new IllegalArgumentException("periodoGarantia no puede ser negativo");
        }
        periodoGarantia = meses;
        return this;
    }

    public ActivoFijoBuilder proveedor(String valor) {
        proveedor = texto(valor, "proveedor");
        return this;
    }

    public ActivoFijoBuilder caracteristicasTecnicas(String valor) {
        caracteristicasTecnicas = texto(valor, "caracteristicasTecnicas");
        return this;
    }

    public ActivoFijoBuilder observaciones(String valor) {
        observaciones = texto(valor, "observaciones");
        return this;
    }

    /** Valida los obligatorios y entrega una instancia nueva sin reiniciar el builder. */
    public ActivoFijo construir() {
        if (codigoInstitucional == null) {
            throw new IllegalStateException("Falta codigoInstitucional");
        }
        if (nombre == null && descripcion == null) {
            throw new IllegalStateException("Debe informar nombre o descripcion; puede informar ambos");
        }
        if (precioAdquisicion == null) {
            throw new IllegalStateException("Falta precioAdquisicion");
        }
        return new ActivoFijo(codigoInstitucional, nombre, descripcion, precioAdquisicion,
                marca, modelo, numeroSerie, ubicacion, custodioResponsable,
                fechaAdquisicion, periodoGarantia, proveedor, caracteristicasTecnicas, observaciones);
    }

    private static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede ser null ni estar en blanco");
        }
        return valor;
    }
}
