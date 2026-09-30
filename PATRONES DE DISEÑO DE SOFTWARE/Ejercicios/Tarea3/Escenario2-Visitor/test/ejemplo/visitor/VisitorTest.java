package ejemplo.visitor;

import java.math.BigDecimal;
import java.util.List;

public final class VisitorTest {
    private static int aprobadas;
    private VisitorTest() { }

    public static void main(String[] args) {
        probar("costo de documento", () -> igual("4.00", costo(documento(false))));
        probar("costo de paquete frágil", () -> igual("25.00", costo(paquete(true))));
        probar("costo de carga refrigerada", () -> igual("75.00", costo(carga("4"))));
        probar("un lote acumula costos heterogéneos", () -> {
            LoteEnvios lote = loteEjemplo();
            VisitanteCalculoCosto visitante = new VisitanteCalculoCosto();
            lote.ejecutar(visitante);
            igual("104.00", visitante.obtenerTotal());
        });
        probar("documento confidencial requiere custodia", () ->
                contiene(inspeccionar(documento(true)), "requiere custodia especial"));
        probar("paquete sin refuerzo genera advertencia", () ->
                contiene(inspeccionar(paquete(false)), "requiere reforzar embalaje"));
        probar("temperatura fuera de intervalo genera advertencia", () ->
                contiene(inspeccionar(carga("9")), "temperatura fuera del intervalo operativo"));
        probar("elementos válidos quedan conformes", () -> {
            contiene(inspeccionar(documento(false)), "conforme");
            contiene(inspeccionar(paquete(true)), "conforme");
            contiene(inspeccionar(carga("-20")), "conforme");
            contiene(inspeccionar(carga("8")), "conforme");
        });
        probar("lote vacío produce resultados vacíos", () -> {
            LoteEnvios lote = new LoteEnvios();
            VisitanteCalculoCosto costo = new VisitanteCalculoCosto();
            VisitanteInspeccion inspeccion = new VisitanteInspeccion();
            lote.ejecutar(costo); lote.ejecutar(inspeccion);
            igual("0.00", costo.obtenerTotal());
            verificar(inspeccion.obtenerResultados().isEmpty());
        });
        probar("aceptar selecciona la sobrecarga concreta", () -> {
            VisitanteConteo visitante = new VisitanteConteo();
            documento(false).aceptar(visitante);
            paquete(true).aceptar(visitante);
            carga("4").aceptar(visitante);
            verificar(visitante.documentos == 1 && visitante.paquetes == 1 && visitante.cargas == 1);
        });
        probar("resultados de inspección no se pueden modificar", () -> {
            List<String> resultados = inspeccionar(documento(false));
            falla(UnsupportedOperationException.class, () -> resultados.add("alterado"));
        });
        probar("valida datos y referencias", () -> {
            falla(IllegalArgumentException.class, () -> new Documento(0, "Quito", false));
            falla(IllegalArgumentException.class, () -> new Documento(1, " ", false));
            falla(IllegalArgumentException.class, () -> new PaqueteFragil(
                    BigDecimal.ONE, "Quito", new BigDecimal("-1"), true));
            falla(NullPointerException.class, () -> documento(false).aceptar(null));
            falla(NullPointerException.class, () -> new LoteEnvios().agregar(null));
            falla(NullPointerException.class, () -> new LoteEnvios().ejecutar(null));
        });
        System.out.println("Resultado: " + aprobadas + "/12 pruebas aprobadas.");
    }

    private static Documento documento(boolean confidencial) {
        return new Documento(200, "Cuenca", confidencial);
    }
    private static PaqueteFragil paquete(boolean reforzado) {
        return new PaqueteFragil(new BigDecimal("5"), "Quito", new BigDecimal("500"), reforzado);
    }
    private static CargaRefrigerada carga(String temperatura) {
        return new CargaRefrigerada(new BigDecimal("80"), "Guayaquil", new BigDecimal("100"),
                new BigDecimal("50"), new BigDecimal(temperatura));
    }
    private static LoteEnvios loteEjemplo() {
        LoteEnvios lote = new LoteEnvios();
        lote.agregar(documento(true)); lote.agregar(paquete(false)); lote.agregar(carga("4"));
        return lote;
    }
    private static BigDecimal costo(Envio envio) {
        VisitanteCalculoCosto visitante = new VisitanteCalculoCosto();
        envio.aceptar(visitante);
        return visitante.obtenerTotal();
    }
    private static List<String> inspeccionar(Envio envio) {
        VisitanteInspeccion visitante = new VisitanteInspeccion();
        envio.aceptar(visitante);
        return visitante.obtenerResultados();
    }
    private static void contiene(List<String> resultados, String texto) {
        verificar(resultados.size() == 1 && resultados.get(0).contains(texto));
    }
    private static void igual(String esperado, BigDecimal real) {
        verificar(new BigDecimal(esperado).compareTo(real) == 0);
    }
    private static void probar(String nombre, Runnable prueba) {
        prueba.run(); aprobadas++; System.out.println("OK: " + nombre);
    }
    private static void verificar(boolean condicion) { if (!condicion) { throw new AssertionError(); } }
    private static void falla(Class<? extends Throwable> tipo, Runnable accion) {
        try { accion.run(); } catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Excepción inesperada", error);
        }
        throw new AssertionError("Faltó " + tipo.getSimpleName());
    }

    private static final class VisitanteConteo implements VisitanteEnvio {
        private int documentos;
        private int paquetes;
        private int cargas;
        @Override public void visitar(Documento documento) { documentos++; }
        @Override public void visitar(PaqueteFragil paquete) { paquetes++; }
        @Override public void visitar(CargaRefrigerada carga) { cargas++; }
    }
}
