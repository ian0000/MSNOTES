package ejemplo.adapter;

import java.util.List;

public final class AdapterTest {
    private static int aprobadas;

    public static void main(String[] args) {
        probar("ambos proveedores funcionan mediante el contrato comun", () -> {
            for (SolicitudTraduccion adaptador : adaptadores()) {
                Cliente cliente = new Cliente(adaptador);
                comprobar("hello".equals(cliente.solicitarTraduccion(" HOLA ")), "hola");
                comprobar("good morning".equals(cliente.solicitarTraduccion("buenos dias")), "saludo");
                comprobar("thank you".equals(cliente.solicitarTraduccion("gracias")), "gracias");
            }
        });
        probar("el cliente depende solo de SolicitudTraduccion", () -> {
            Cliente cliente = new Cliente(texto -> "alternativa: " + texto);
            comprobar("alternativa: ejemplo".equals(cliente.solicitarTraduccion("ejemplo")), "otro contrato");
        });
        probar("cada adapter delega exactamente el texto al proveedor correcto", () -> {
            String[] recibido = new String[2];
            int[] llamadas = new int[2];
            Traductor1 uno = new Traductor1() {
                @Override public String traducir(String texto) {
                    recibido[0] = texto; llamadas[0]++; return "resultado-1";
                }
            };
            Traductor2 dos = new Traductor2() {
                @Override public String traducir(String texto) {
                    recibido[1] = texto; llamadas[1]++; return "resultado-2";
                }
            };
            comprobar("resultado-1".equals(new Cliente(new Traductor1Adapter(uno)).solicitarTraduccion(" Texto A ")), "retorno 1");
            comprobar("resultado-2".equals(new Cliente(new Traductor2Adapter(dos)).solicitarTraduccion("Texto B")), "retorno 2");
            comprobar(" Texto A ".equals(recibido[0]) && "Texto B".equals(recibido[1]), "sin alterar el argumento");
            comprobar(llamadas[0] == 1 && llamadas[1] == 1, "una llamada por proveedor");
        });
        probar("no convierte un fallo del proveedor en una traduccion", () -> {
            IllegalStateException fallo = new IllegalStateException("Fallo simulado");
            Traductor1 uno = new Traductor1() {
                @Override public String traducir(String texto) { throw fallo; }
            };
            Traductor2 dos = new Traductor2() {
                @Override public String traducir(String texto) { throw fallo; }
            };
            for (SolicitudTraduccion adapter : List.of(new Traductor1Adapter(uno), new Traductor2Adapter(dos))) {
                try {
                    new Cliente(adapter).solicitarTraduccion("hola");
                    throw new AssertionError("Debia propagarse el fallo");
                } catch (IllegalStateException error) {
                    comprobar(error == fallo, "conservar el error original");
                }
            }
        });
        probar("rechaza dependencias nulas y entradas no disponibles", () -> {
            esperar(NullPointerException.class, () -> new Cliente(null));
            esperar(NullPointerException.class, () -> new Traductor1Adapter(null));
            esperar(NullPointerException.class, () -> new Traductor2Adapter(null));
            for (SolicitudTraduccion adapter : adaptadores()) {
                esperar(IllegalArgumentException.class, () -> adapter.solicitarTraduccion(null));
                esperar(IllegalArgumentException.class, () -> adapter.solicitarTraduccion(" "));
                esperar(IllegalArgumentException.class, () -> adapter.solicitarTraduccion("frase desconocida"));
            }
        });
        System.out.println("Resultado: " + aprobadas + "/5 pruebas aprobadas.");
    }

    private static List<SolicitudTraduccion> adaptadores() {
        return List.of(new Traductor1Adapter(new Traductor1()), new Traductor2Adapter(new Traductor2()));
    }

    private static void probar(String nombre, Runnable caso) {
        caso.run(); aprobadas++; System.out.println("OK: " + nombre);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) { throw new AssertionError(mensaje); }
    }

    private static void esperar(Class<? extends Throwable> tipo, Runnable caso) {
        try { caso.run(); }
        catch (Throwable error) {
            if (tipo.isInstance(error)) { return; }
            throw new AssertionError("Error inesperado", error);
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
