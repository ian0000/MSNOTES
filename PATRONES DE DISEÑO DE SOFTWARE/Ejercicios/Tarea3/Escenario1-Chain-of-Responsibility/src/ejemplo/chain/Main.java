package ejemplo.chain;

import java.math.BigDecimal;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        CoordinadorArea coordinador = new CoordinadorArea();
        DirectorAdministrativo director = new DirectorAdministrativo();
        ComiteCompras comite = new ComiteCompras();
        coordinador.setSiguiente(director);
        director.setSiguiente(comite);

        ServicioCompras servicio = new ServicioCompras(coordinador);
        procesar(servicio, solicitud("SC-001", "850.00"));
        procesar(servicio, solicitud("SC-002", "7500.00"));
        procesar(servicio, solicitud("SC-003", "40000.00"));
        procesar(servicio, solicitud("SC-004", "70000.00"));
    }

    private static SolicitudCompra solicitud(String codigo, String valor) {
        return new SolicitudCompra(codigo, "Tecnología", "Compra institucional " + codigo,
                new BigDecimal(valor));
    }

    private static void procesar(ServicioCompras servicio, SolicitudCompra solicitud) {
        ResultadoAprobacion resultado = servicio.procesar(solicitud);
        System.out.printf("%s (%s): %s%n", solicitud.obtenerCodigo(),
                solicitud.obtenerValorEstimado().toPlainString(), resultado.obtenerMensaje());
    }
}
