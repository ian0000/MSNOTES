package ejemplo.visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** ConcreteVisitor que reúne todas las reglas de costo. */
public final class VisitanteCalculoCosto implements VisitanteEnvio {
    private BigDecimal total = BigDecimal.ZERO;

    @Override public void visitar(Documento documento) {
        sumar(new BigDecimal("2.00").add(
                new BigDecimal(documento.obtenerPesoGramos()).multiply(new BigDecimal("0.01"))));
    }

    @Override public void visitar(PaqueteFragil paquete) {
        BigDecimal transporte = paquete.obtenerPesoKg().multiply(new BigDecimal("3.00"));
        BigDecimal seguro = paquete.obtenerValorDeclarado().multiply(new BigDecimal("0.02"));
        sumar(transporte.add(seguro));
    }

    @Override public void visitar(CargaRefrigerada carga) {
        BigDecimal transporte = carga.obtenerDistanciaKm().multiply(new BigDecimal("0.80"));
        BigDecimal volumen = carga.obtenerVolumenLitros().multiply(new BigDecimal("0.15"));
        sumar(transporte.add(volumen).add(new BigDecimal("20.00")));
    }

    public BigDecimal obtenerTotal() {
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private void sumar(BigDecimal valor) {
        total = total.add(valor);
    }
}
