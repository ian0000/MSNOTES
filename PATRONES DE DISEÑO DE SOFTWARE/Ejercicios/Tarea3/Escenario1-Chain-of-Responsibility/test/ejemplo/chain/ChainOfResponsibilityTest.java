package ejemplo.chain;

import java.math.BigDecimal;

public final class ChainOfResponsibilityTest {
    private static int aprobadas;
    private ChainOfResponsibilityTest() { }

    public static void main(String[] args) {
        probar("coordinador aprueba hasta 1000", () -> responsable("1000.00", "Coordinador de área"));
        probar("director recibe una solicitud delegada", () -> responsable("1000.01", "Director administrativo"));
        probar("director aprueba su límite", () -> responsable("10000.00", "Director administrativo"));
        probar("comité recibe una solicitud delegada", () -> responsable("10000.01", "Comité de compras"));
        probar("comité aprueba su límite", () -> responsable("50000.00", "Comité de compras"));
        probar("valor superior queda sin aprobar", () -> {
            ResultadoAprobacion resultado = servicioCompleto().procesar(solicitud("50000.01"));
            verificar(!resultado.estaAprobada());
            verificar(resultado.obtenerResponsable() == null);
            verificar(resultado.obtenerMensaje().contains("Ningún nivel"));
        });
        probar("la cadena puede omitir al director", () -> {
            CoordinadorArea coordinador = new CoordinadorArea();
            coordinador.setSiguiente(new ComiteCompras());
            ResultadoAprobacion resultado = new ServicioCompras(coordinador).procesar(solicitud("7500"));
            verificar("Comité de compras".equals(resultado.obtenerResponsable()));
        });
        probar("se detiene en el primer aprobador capaz", () -> {
            Aprobador primero = new AprobadorPrueba(true, "Primero");
            AprobadorPrueba segundo = new AprobadorPrueba(true, "Segundo");
            primero.setSiguiente(segundo);
            ResultadoAprobacion resultado = primero.manejar(solicitud("10"));
            verificar("Primero".equals(resultado.obtenerResponsable()));
            verificar(segundo.invocaciones == 0);
        });
        probar("un manejador sin siguiente informa no resuelto", () ->
                verificar(!new CoordinadorArea().manejar(solicitud("2000")).estaAprobada()));
        probar("rechaza autorreferencia", () -> {
            Aprobador aprobador = new CoordinadorArea();
            falla(IllegalArgumentException.class, () -> aprobador.setSiguiente(aprobador));
        });
        probar("valida datos obligatorios y valor", () -> {
            falla(IllegalArgumentException.class, () ->
                    new SolicitudCompra(" ", "Área", "Compra", BigDecimal.ONE));
            falla(IllegalArgumentException.class, () ->
                    new SolicitudCompra("S", "Área", "Compra", BigDecimal.ZERO));
            falla(NullPointerException.class, () ->
                    new SolicitudCompra("S", "Área", "Compra", null));
        });
        probar("servicio y manejador rechazan datos nulos", () -> {
            falla(NullPointerException.class, () -> new ServicioCompras(null));
            falla(NullPointerException.class, () -> servicioCompleto().procesar(null));
        });
        System.out.println("Resultado: " + aprobadas + "/12 pruebas aprobadas.");
    }

    private static void responsable(String monto, String esperado) {
        ResultadoAprobacion resultado = servicioCompleto().procesar(solicitud(monto));
        verificar(resultado.estaAprobada());
        verificar(esperado.equals(resultado.obtenerResponsable()));
    }

    private static ServicioCompras servicioCompleto() {
        CoordinadorArea coordinador = new CoordinadorArea();
        DirectorAdministrativo director = new DirectorAdministrativo();
        ComiteCompras comite = new ComiteCompras();
        coordinador.setSiguiente(director);
        director.setSiguiente(comite);
        return new ServicioCompras(coordinador);
    }

    private static SolicitudCompra solicitud(String monto) {
        return new SolicitudCompra("SC", "Área", "Compra", new BigDecimal(monto));
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

    private static final class AprobadorPrueba extends Aprobador {
        private final boolean acepta;
        private final String nombre;
        private int invocaciones;

        private AprobadorPrueba(boolean acepta, String nombre) {
            this.acepta = acepta;
            this.nombre = nombre;
        }

        @Override protected boolean puedeAprobar(SolicitudCompra solicitud) {
            invocaciones++;
            return acepta;
        }

        @Override protected ResultadoAprobacion resolver(SolicitudCompra solicitud) {
            return ResultadoAprobacion.aprobadaPor(nombre);
        }
    }
}
