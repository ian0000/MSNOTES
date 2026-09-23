package ejemplo.composite;

public final class CompositeTest {
    private static int aprobadas;
    private CompositeTest() { }

    public static void main(String[] args) {
        probar("hoja y datos comunes", () -> {
            PartidaPresupuestaria hoja = new PartidaIndividual("A", "Asignación", 12.5f);
            igual(12.5f, hoja.calcularValorTotal());
            verificar("A".equals(hoja.obtenerCodigo()) && "Asignación".equals(hoja.obtenerDescripcion()));
        });
        probar("grupo vacío", () -> igual(0, grupo("Vacío").calcularValorTotal()));
        probar("total de tres niveles", () -> igual(3500, ejemplo().calcularValorTotal()));
        probar("estructura con sangría", () -> verificar(ejemplo().mostrarEstructura().equals(
                "TI - TI: 3500.00\n  LIC - Licencias: 500.00\n  INF - INF: 3000.00"
                + "\n    SRV - Servidores: 2000.00\n    ALM - Almacenamiento: 1000.00")));
        probar("quitar grupo recalcula total", () -> {
            GrupoPresupuestario raiz = grupo("R"), hijo = grupo("H");
            hijo.agregar(new PartidaIndividual("P", "Partida", 10));
            raiz.agregar(hijo);
            raiz.quitar(hijo);
            raiz.quitar(hijo); // Ausente: no cambia el grupo.
            igual(0, raiz.calcularValorTotal());
        });
        probar("mismo cliente para hoja y grupo", () -> {
            Cliente cliente = new Cliente();
            verificar(cliente.consultar(new PartidaIndividual("P", "P", 10)).endsWith("10.00"));
            verificar(cliente.consultar(ejemplo()).endsWith("3500.00"));
        });
        probar("rechaza ciclo directo e indirecto", () -> {
            GrupoPresupuestario a = grupo("A"), b = grupo("B"), c = grupo("C");
            falla(IllegalArgumentException.class, () -> a.agregar(a));
            a.agregar(b); b.agregar(c);
            falla(IllegalArgumentException.class, () -> c.agregar(a));
            igual(0, a.calcularValorTotal());
        });
        probar("rechaza hijo duplicado y nulo", () -> {
            GrupoPresupuestario g = grupo("G");
            PartidaIndividual p = new PartidaIndividual("P", "P", 10);
            g.agregar(p);
            falla(IllegalArgumentException.class, () -> g.agregar(p));
            falla(NullPointerException.class, () -> g.agregar(null));
            igual(10, g.calcularValorTotal());
        });
        probar("valida identidad y asignación", () -> {
            falla(IllegalArgumentException.class, () -> grupo(" "));
            falla(IllegalArgumentException.class, () -> new PartidaIndividual("P", null, 0));
            for (float valor : new float[] {-1, Float.NaN, Float.POSITIVE_INFINITY}) {
                falla(IllegalArgumentException.class, () -> new PartidaIndividual("P", "P", valor));
            }
        });
        probar("sin exclusividad de padre impuesta", () -> {
            GrupoPresupuestario a = grupo("A"), b = grupo("B");
            PartidaIndividual p = new PartidaIndividual("P", "Compartida", 10);
            a.agregar(p); b.agregar(p);
            igual(10, a.calcularValorTotal()); igual(10, b.calcularValorTotal());
        });
        probar("detecta desbordamiento", () -> {
            GrupoPresupuestario g = grupo("G");
            g.agregar(new PartidaIndividual("A", "A", Float.MAX_VALUE));
            g.agregar(new PartidaIndividual("B", "B", Float.MAX_VALUE));
            falla(ArithmeticException.class, g::calcularValorTotal);
        });
        System.out.println("Resultado: " + aprobadas + "/11 pruebas aprobadas.");
    }

    private static GrupoPresupuestario grupo(String codigo) { return new GrupoPresupuestario(codigo, codigo); }
    private static GrupoPresupuestario ejemplo() {
        GrupoPresupuestario ti = grupo("TI"), infra = grupo("INF");
        ti.agregar(new PartidaIndividual("LIC", "Licencias", 500));
        infra.agregar(new PartidaIndividual("SRV", "Servidores", 2000));
        infra.agregar(new PartidaIndividual("ALM", "Almacenamiento", 1000));
        ti.agregar(infra);
        return ti;
    }
    private static void probar(String nombre, Runnable prueba) {
        prueba.run(); aprobadas++; System.out.println("OK: " + nombre);
    }
    private static void verificar(boolean condicion) { if (!condicion) { throw new AssertionError(); } }
    private static void igual(float esperado, float real) { verificar(Math.abs(esperado - real) < 0.001f); }
    private static void falla(Class<? extends Throwable> tipo, Runnable accion) {
        try { accion.run(); } catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Excepción inesperada", error);
        }
        throw new AssertionError("Faltó " + tipo.getSimpleName());
    }
}
