package ejemplo.activos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public final class ActivoFijoTest {
    private static int aprobadas;

    public static void main(String[] args) {
        probar("campos obligatorios y opcionales completos", () -> {
            ActivoFijo a = base().nombre("PC").descripcion("Equipo")
                    .marca("Marca").modelo("Modelo").numeroSerie("S1")
                    .ubicacion("Lab").custodioResponsable("Ana")
                    .fechaAdquisicion(LocalDate.of(2026, 1, 1)).periodoGarantia(36)
                    .proveedor("Proveedor").caracteristicasTecnicas("16 GB")
                    .observaciones("Prueba").construir();
            comprobar("AF001".equals(a.getCodigoInstitucional()), "codigo");
            comprobar("PC".equals(a.getNombre()) && "Equipo".equals(a.getDescripcion()), "nombre y descripcion");
            comprobar(a.getPrecioAdquisicion().compareTo(new BigDecimal("1200.00")) == 0, "precio");
            comprobar("Marca".equals(a.getMarca()) && "Modelo".equals(a.getModelo()), "marca y modelo");
            comprobar("S1".equals(a.getNumeroSerie()) && "Lab".equals(a.getUbicacion()), "serie y ubicacion");
            comprobar("Ana".equals(a.getCustodioResponsable()), "custodio");
            comprobar(LocalDate.of(2026, 1, 1).equals(a.getFechaAdquisicion()), "fecha");
            comprobar(Integer.valueOf(36).equals(a.getPeriodoGarantia()), "garantia");
            comprobar("Proveedor".equals(a.getProveedor()), "proveedor");
            comprobar("16 GB".equals(a.getCaracteristicasTecnicas()) && "Prueba".equals(a.getObservaciones()), "textos");
        });
        probar("acepta solo nombre o solo descripcion", () -> {
            ActivoFijo porNombre = base().nombre("PC").construir();
            ActivoFijo porDescripcion = base().descripcion("Mesa").construir();
            comprobar(porNombre.getDescripcion() == null, "descripcion omitida");
            comprobar(porDescripcion.getNombre() == null, "nombre omitido");
        });
        probar("rechaza la falta de cada obligatorio", () -> {
            esperar(IllegalStateException.class, () -> new ActivoFijoBuilder()
                    .nombre("PC").precioAdquisicion(BigDecimal.ONE).construir());
            esperar(IllegalStateException.class, () -> base().construir());
            esperar(IllegalStateException.class, () -> new ActivoFijoBuilder()
                    .codigoInstitucional("A").nombre("PC").construir());
        });
        probar("los opcionales omitidos no se inventan", () -> {
            ActivoFijo a = base().nombre("Mesa").construir();
            comprobar(a.getMarca() == null && a.getModelo() == null && a.getNumeroSerie() == null, "identificacion");
            comprobar(a.getUbicacion() == null && a.getCustodioResponsable() == null, "asignacion");
            comprobar(a.getFechaAdquisicion() == null && a.getPeriodoGarantia() == null, "fechas");
            comprobar(a.getProveedor() == null && a.getCaracteristicasTecnicas() == null
                    && a.getObservaciones() == null, "detalles");
        });
        probar("nuevos productos independientes al reutilizar el builder", () -> {
            ActivoFijoBuilder b = base().nombre("PC").ubicacion("Lab A");
            ActivoFijo antes = b.construir();
            comprobar(antes != b.construir(), "instancia nueva");
            ActivoFijo despues = b.ubicacion("Lab B").construir();
            comprobar("Lab A".equals(antes.getUbicacion()), "anterior intacto");
            comprobar("Lab B".equals(despues.getUbicacion()), "seleccion actualizada");
        });
        probar("valida precio y conserva decimales exactos", () -> {
            esperar(IllegalArgumentException.class, () -> base().precioAdquisicion(null));
            esperar(IllegalArgumentException.class, () -> base().precioAdquisicion(new BigDecimal("-0.01")));
            ActivoFijo a = base().nombre("PC").precioAdquisicion(new BigDecimal("0.10")).construir();
            comprobar(a.getPrecioAdquisicion().compareTo(new BigDecimal("0.10")) == 0, "decimal exacto");
            comprobar(base().nombre("PC").precioAdquisicion(BigDecimal.ZERO).construir()
                    .getPrecioAdquisicion().signum() == 0, "cero explicito permitido");
        });
        probar("garantia cero es distinta de garantia no informada", () -> {
            esperar(IllegalArgumentException.class, () -> base().periodoGarantia(-1));
            esperar(IllegalArgumentException.class, () -> base().fechaAdquisicion(null));
            comprobar(Integer.valueOf(0).equals(base().nombre("Mesa").periodoGarantia(0)
                    .construir().getPeriodoGarantia()), "cero informado");
            comprobar(base().nombre("Mesa").construir().getPeriodoGarantia() == null, "desconocido");
        });
        probar("valida textos antes de modificar el builder", () -> {
            ActivoFijoBuilder b = base().nombre("PC").proveedor("Original");
            for (String valor : Arrays.asList(null, "", " \t ")) {
                esperar(IllegalArgumentException.class, () -> b.codigoInstitucional(valor));
                esperar(IllegalArgumentException.class, () -> b.nombre(valor));
                esperar(IllegalArgumentException.class, () -> b.descripcion(valor));
                esperar(IllegalArgumentException.class, () -> b.marca(valor));
                esperar(IllegalArgumentException.class, () -> b.modelo(valor));
                esperar(IllegalArgumentException.class, () -> b.numeroSerie(valor));
                esperar(IllegalArgumentException.class, () -> b.ubicacion(valor));
                esperar(IllegalArgumentException.class, () -> b.custodioResponsable(valor));
                esperar(IllegalArgumentException.class, () -> b.proveedor(valor));
                esperar(IllegalArgumentException.class, () -> b.caracteristicasTecnicas(valor));
                esperar(IllegalArgumentException.class, () -> b.observaciones(valor));
            }
            comprobar("Original".equals(b.construir().getProveedor()), "estado previo conservado");
        });
        System.out.println("Resultado: " + aprobadas + "/8 pruebas aprobadas.");
    }

    private static ActivoFijoBuilder base() {
        return new ActivoFijoBuilder().codigoInstitucional("AF001")
                .precioAdquisicion(new BigDecimal("1200.00"));
    }

    private static void probar(String nombre, Runnable caso) {
        caso.run();
        aprobadas++;
        System.out.println("OK: " + nombre);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) { throw new AssertionError(mensaje); }
    }

    private static void esperar(Class<? extends Throwable> tipo, Runnable accion) {
        try { accion.run(); }
        catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Se esperaba " + tipo.getSimpleName(), error);
        }
        throw new AssertionError("No se lanzo " + tipo.getSimpleName());
    }
}
