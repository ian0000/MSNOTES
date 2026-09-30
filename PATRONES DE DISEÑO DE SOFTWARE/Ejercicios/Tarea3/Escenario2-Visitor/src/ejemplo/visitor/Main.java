package ejemplo.visitor;

import java.math.BigDecimal;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        LoteEnvios lote = new LoteEnvios();
        lote.agregar(new Documento(200, "Cuenca", true));
        lote.agregar(new PaqueteFragil(new BigDecimal("5"), "Quito",
                new BigDecimal("500"), false));
        lote.agregar(new CargaRefrigerada(new BigDecimal("80"), "Guayaquil",
                new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("4")));

        VisitanteCalculoCosto costo = new VisitanteCalculoCosto();
        lote.ejecutar(costo);
        System.out.println("Costo total: $" + costo.obtenerTotal().toPlainString());

        VisitanteInspeccion inspeccion = new VisitanteInspeccion();
        lote.ejecutar(inspeccion);
        System.out.println("Inspección:");
        inspeccion.obtenerResultados().forEach(resultado -> System.out.println("- " + resultado));
    }
}
