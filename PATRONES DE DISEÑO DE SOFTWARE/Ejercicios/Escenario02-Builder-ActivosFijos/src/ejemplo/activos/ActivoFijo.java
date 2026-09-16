package ejemplo.activos;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Producto inmutable. Los datos opcionales no informados se consultan como null. */
public final class ActivoFijo {
    private final String codigoInstitucional;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precioAdquisicion;
    private final String marca;
    private final String modelo;
    private final String numeroSerie;
    private final String ubicacion;
    private final String custodioResponsable;
    private final LocalDate fechaAdquisicion;
    private final Integer periodoGarantia;
    private final String proveedor;
    private final String caracteristicasTecnicas;
    private final String observaciones;

    // Constructor interno: el cliente utiliza ActivoFijoBuilder.
    ActivoFijo(String codigoInstitucional, String nombre, String descripcion,
               BigDecimal precioAdquisicion, String marca, String modelo,
               String numeroSerie, String ubicacion, String custodioResponsable,
               LocalDate fechaAdquisicion, Integer periodoGarantia, String proveedor,
               String caracteristicasTecnicas, String observaciones) {
        this.codigoInstitucional = codigoInstitucional;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioAdquisicion = precioAdquisicion;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.ubicacion = ubicacion;
        this.custodioResponsable = custodioResponsable;
        this.fechaAdquisicion = fechaAdquisicion;
        this.periodoGarantia = periodoGarantia;
        this.proveedor = proveedor;
        this.caracteristicasTecnicas = caracteristicasTecnicas;
        this.observaciones = observaciones;
    }

    public String getCodigoInstitucional() { return codigoInstitucional; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecioAdquisicion() { return precioAdquisicion; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getNumeroSerie() { return numeroSerie; }
    public String getUbicacion() { return ubicacion; }
    public String getCustodioResponsable() { return custodioResponsable; }
    public LocalDate getFechaAdquisicion() { return fechaAdquisicion; }

    /** Meses; null significa no informado y 0 indica cero meses de cobertura. */
    public Integer getPeriodoGarantia() { return periodoGarantia; }

    public String getProveedor() { return proveedor; }
    public String getCaracteristicasTecnicas() { return caracteristicasTecnicas; }
    public String getObservaciones() { return observaciones; }
}
