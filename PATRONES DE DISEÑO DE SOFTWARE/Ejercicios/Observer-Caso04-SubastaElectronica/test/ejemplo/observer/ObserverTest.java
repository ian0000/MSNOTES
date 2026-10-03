package ejemplo.observer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ObserverTest {
    private static int aprobadas;
    private ObserverTest() { }

    public static void main(String[] args) {
        probar("primera oferta actualiza y notifica", () -> {
            Subasta subasta = new Subasta(); Participante ana = participante("Ana");
            subasta.attach(ana);
            verificar(ana.realizarNuevaOferta(subasta, dinero("100.00")));
            verificar(subasta.obtenerValorActual().compareTo(dinero("100.00")) == 0);
            verificar(ana.obtenerNotificacionesRecibidas() == 1);
            verificar(ana.obtenerUltimaActualizacion().orElseThrow().obtenerOfertante().equals("Ana"));
        });
        probar("notifica a varios observadores", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"), b = participante("B");
            subasta.attach(a); subasta.attach(b); subasta.realizarOferta("A", dinero("10"));
            verificar(a.obtenerNotificacionesRecibidas() == 1 && b.obtenerNotificacionesRecibidas() == 1);
        });
        probar("oferta igual o menor se rechaza sin notificar", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"); subasta.attach(a);
            verificar(subasta.realizarOferta("A", dinero("10")));
            verificar(!subasta.realizarOferta("B", dinero("10")));
            verificar(!subasta.realizarOferta("B", dinero("9.99")));
            verificar(a.obtenerNotificacionesRecibidas() == 1 && subasta.obtenerValorActual().equals(dinero("10")));
        });
        probar("suscripción durante ejecución recibe solo cambios futuros", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"), b = participante("B");
            subasta.attach(a); subasta.realizarOferta("A", dinero("10")); subasta.attach(b);
            verificar(b.obtenerUltimaActualizacion().isEmpty());
            subasta.realizarOferta("B", dinero("11"));
            verificar(a.obtenerNotificacionesRecibidas() == 2 && b.obtenerNotificacionesRecibidas() == 1);
        });
        probar("desuscripción detiene cambios futuros", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"); subasta.attach(a);
            subasta.realizarOferta("A", dinero("10")); subasta.detach(a); subasta.realizarOferta("B", dinero("11"));
            verificar(a.obtenerNotificacionesRecibidas() == 1);
            verificar(a.obtenerUltimaActualizacion().orElseThrow().obtenerValorActual().equals(dinero("10")));
        });
        probar("suscripción duplicada no duplica avisos", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A");
            subasta.attach(a); subasta.attach(a); subasta.realizarOferta("A", dinero("10"));
            verificar(subasta.obtenerNumeroParticipantes() == 1 && a.obtenerNotificacionesRecibidas() == 1);
        });
        probar("retirar ausente no cambia la lista", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"), b = participante("B");
            subasta.attach(a); subasta.detach(b);
            verificar(subasta.obtenerNumeroParticipantes() == 1);
        });
        probar("funciona sin participantes", () -> {
            Subasta subasta = new Subasta();
            verificar(subasta.realizarOferta("A", dinero("10")));
            verificar(subasta.obtenerNumeroParticipantes() == 0);
        });
        probar("estado conserva secuencia de ofertas aceptadas", () -> {
            Subasta subasta = new Subasta(); subasta.realizarOferta("A", dinero("10"));
            verificar(!subasta.realizarOferta("B", dinero("9")));
            subasta.realizarOferta("C", dinero("12"));
            EstadoSubasta estado = subasta.obtenerEstadoActual().orElseThrow();
            verificar(estado.obtenerNumeroOferta() == 2 && estado.obtenerOfertante().equals("C"));
        });
        probar("participante decide ofertar después de ser notificado", () -> {
            Subasta subasta = new Subasta(); Participante a = participante("A"), b = participante("B");
            subasta.attach(a); subasta.attach(b); a.realizarNuevaOferta(subasta, dinero("20"));
            BigDecimal nueva = b.obtenerUltimaActualizacion().orElseThrow().obtenerValorActual().add(dinero("5"));
            verificar(b.realizarNuevaOferta(subasta, nueva));
            verificar(subasta.obtenerEstadoActual().orElseThrow().obtenerOfertante().equals("B"));
        });
        probar("un observador puede retirarse durante el aviso", () -> {
            Subasta subasta = new Subasta(); List<String> avisos = new ArrayList<>();
            ValorObserver retirarse = new ValorObserver() {
                @Override public void cambioOferta(EstadoSubasta estado) {
                    avisos.add("sale"); subasta.detach(this);
                }
            };
            ValorObserver permanece = estado -> avisos.add("permanece");
            subasta.attach(retirarse); subasta.attach(permanece);
            subasta.realizarOferta("A", dinero("10")); subasta.realizarOferta("B", dinero("11"));
            verificar(avisos.equals(List.of("sale", "permanece", "permanece")));
        });
        probar("observador añadido durante aviso espera al siguiente cambio", () -> {
            Subasta subasta = new Subasta(); Participante nuevo = participante("Nuevo");
            ValorObserver agregar = estado -> subasta.attach(nuevo);
            subasta.attach(agregar); subasta.realizarOferta("A", dinero("10"));
            verificar(nuevo.obtenerNotificacionesRecibidas() == 0);
            subasta.realizarOferta("B", dinero("11"));
            verificar(nuevo.obtenerNotificacionesRecibidas() == 1);
        });
        probar("valida participantes y ofertas", () -> {
            Subasta subasta = new Subasta();
            falla(NullPointerException.class, () -> subasta.attach(null));
            falla(NullPointerException.class, () -> subasta.detach(null));
            falla(IllegalArgumentException.class, () -> participante(" "));
            falla(IllegalArgumentException.class, () -> subasta.realizarOferta(" ", dinero("10")));
            falla(NullPointerException.class, () -> subasta.realizarOferta("A", null));
            falla(IllegalArgumentException.class, () -> subasta.realizarOferta("A", BigDecimal.ZERO));
            falla(IllegalArgumentException.class, () -> subasta.realizarOferta("A", dinero("-1")));
            falla(IllegalArgumentException.class, () -> new EstadoSubasta(0, "A", dinero("1")));
            falla(IllegalArgumentException.class, () -> new EstadoSubasta(1, "A", BigDecimal.ZERO));
        });
        System.out.println("Resultado: " + aprobadas + "/13 pruebas aprobadas.");
    }

    private static Participante participante(String nombre) { return new Participante(nombre); }
    private static BigDecimal dinero(String valor) { return new BigDecimal(valor); }
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
}
