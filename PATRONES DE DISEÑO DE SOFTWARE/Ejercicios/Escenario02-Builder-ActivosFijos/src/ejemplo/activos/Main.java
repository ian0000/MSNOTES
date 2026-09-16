package ejemplo.activos;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Main {
    public static void main(String[] args) {
        ActivoFijo computador = new ActivoFijoBuilder()
                .codigoInstitucional("AF001")
                .nombre("Computador")
                .descripcion("Portatil del laboratorio")
                .precioAdquisicion(new BigDecimal("1200.00"))
                .marca("Dell")
                .modelo("Latitude")
                .numeroSerie("DEMO-001")
                .ubicacion("Cuenca - Laboratorio 1")
                .custodioResponsable("Responsable de laboratorio")
                .fechaAdquisicion(LocalDate.of(2026, 9, 1))
                .periodoGarantia(36)
                .proveedor("Proveedor de ejemplo")
                .caracteristicasTecnicas("Procesador de 8 nucleos; 16 GB de RAM")
                .observaciones("Datos ficticios para el ejercicio")
                .construir();

        ActivoFijo mesa = new ActivoFijoBuilder()
                .codigoInstitucional("AF002")
                .descripcion("Mesa de reuniones")
                .precioAdquisicion(new BigDecimal("250.00"))
                .caracteristicasTecnicas("Madera; 180 x 90 cm")
                .ubicacion("Sala de reuniones")
                .custodioResponsable("Administracion")
                .construir();

        mostrar(computador);
        mostrar(mesa);
    }

    private static void mostrar(ActivoFijo activo) {
        System.out.println("Activo: " + activo.getCodigoInstitucional());
        System.out.println("Nombre: " + informado(activo.getNombre()));
        System.out.println("Descripcion: " + informado(activo.getDescripcion()));
        System.out.println("Precio: " + activo.getPrecioAdquisicion().toPlainString());
        System.out.println("Marca: " + informado(activo.getMarca()));
        System.out.println("Garantia (meses): " + informado(activo.getPeriodoGarantia()));
        System.out.println("Proveedor: " + informado(activo.getProveedor()));
        System.out.println("Caracteristicas: " + informado(activo.getCaracteristicasTecnicas()));
        System.out.println();
    }

    private static String informado(Object valor) {
        return valor == null ? "no informado" : valor.toString();
    }
}
