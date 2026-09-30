package ejemplo.chain;

import java.math.BigDecimal;

/** ConcreteHandler para solicitudes de hasta 50.000 dólares. */
public final class ComiteCompras extends Aprobador {
    private static final BigDecimal LIMITE = new BigDecimal("50000.00");

    @Override protected boolean puedeAprobar(SolicitudCompra solicitud) {
        return solicitud.obtenerValorEstimado().compareTo(LIMITE) <= 0;
    }

    @Override protected ResultadoAprobacion resolver(SolicitudCompra solicitud) {
        return ResultadoAprobacion.aprobadaPor("Comité de compras");
    }
}
