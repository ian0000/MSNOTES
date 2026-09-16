package ejemplo.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Comprobaciones ejecutables sin bibliotecas adicionales ni dependencia de -ea. */
public final class MenuBuilderTest {
    private static int aprobadas;

    public static void main(String[] args) {
        probar("construye todos los componentes", () -> {
            Menu menu = new MenuBuilder().conEntrada("Sopa")
                    .conPlatoPrincipal("Arroz").conBebida("Agua")
                    .conPostre("Fruta").conComplementos(List.of("Pan")).construir();
            comprobar("Sopa".equals(menu.getEntrada()), "entrada");
            comprobar("Arroz".equals(menu.getPlatoPrincipal()), "plato principal");
            comprobar("Agua".equals(menu.getBebida()), "bebida");
            comprobar("Fruta".equals(menu.getPostre()), "postre");
            comprobar(List.of("Pan").equals(menu.getComplementos()), "complementos");
        });
        probar("permite omitir componentes no definidos como obligatorios", () -> {
            Menu menu = new MenuBuilder().construir();
            comprobar(menu.getEntrada() == null && menu.getPlatoPrincipal() == null
                    && menu.getBebida() == null && menu.getPostre() == null, "opcionales");
            comprobar(menu.getComplementos().isEmpty(), "lista vacía");
        });
        probar("cada construcción es independiente del builder", () -> {
            MenuBuilder builder = new MenuBuilder().conBebida("Agua");
            Menu primero = builder.construir();
            Menu segundo = builder.construir();
            comprobar(primero != segundo, "instancias distintas");
            Menu tercero = builder.conBebida("Jugo").construir();
            comprobar("Agua".equals(primero.getBebida()), "producto anterior intacto");
            comprobar("Jugo".equals(tercero.getBebida()), "nueva configuración");
        });
        probar("la lista de entrada no modifica la configuración", () -> {
            List<String> lista = new ArrayList<>(List.of("Pan"));
            MenuBuilder builder = new MenuBuilder().conComplementos(lista);
            lista.add("Salsa");
            Menu menu = builder.construir();
            lista.clear();
            comprobar(List.of("Pan").equals(menu.getComplementos()), "copia defensiva");
        });
        probar("no se pueden modificar complementos del producto", () -> {
            Menu menu = new MenuBuilder().conComplementos(List.of("Pan")).construir();
            esperar(UnsupportedOperationException.class,
                    () -> menu.getComplementos().add("Salsa"));
        });
        probar("repetir un paso reemplaza su selección", () -> {
            MenuBuilder builder = new MenuBuilder().conBebida("Agua")
                    .conComplementos(List.of("Pan"));
            Menu anterior = builder.construir();
            Menu actual = builder.conBebida("Jugo").conComplementos(List.of()).construir();
            comprobar("Jugo".equals(actual.getBebida()), "última bebida");
            comprobar(actual.getComplementos().isEmpty(), "reemplazo de complementos");
            comprobar(List.of("Pan").equals(anterior.getComplementos()), "producto intacto");
        });
        probar("rechaza selecciones textuales nulas o en blanco", () -> {
            for (String valor : Arrays.asList(null, "", " \t ")) {
                esperar(IllegalArgumentException.class, () -> new MenuBuilder().conEntrada(valor));
                esperar(IllegalArgumentException.class, () -> new MenuBuilder().conPlatoPrincipal(valor));
                esperar(IllegalArgumentException.class, () -> new MenuBuilder().conBebida(valor));
                esperar(IllegalArgumentException.class, () -> new MenuBuilder().conPostre(valor));
            }
        });
        probar("rechaza listas inválidas sin modificar la selección previa", () -> {
            MenuBuilder builder = new MenuBuilder().conComplementos(List.of("Pan"));
            esperar(IllegalArgumentException.class, () -> builder.conComplementos(null));
            esperar(IllegalArgumentException.class,
                    () -> builder.conComplementos(Arrays.asList("Salsa", null)));
            esperar(IllegalArgumentException.class,
                    () -> builder.conComplementos(List.of("Salsa", " ")));
            comprobar(List.of("Pan").equals(builder.construir().getComplementos()),
                    "no queda una actualización parcial");
        });
        System.out.println("Resultado: " + aprobadas + "/8 pruebas aprobadas.");
    }

    private static void probar(String nombre, Runnable caso) {
        caso.run();
        aprobadas++;
        System.out.println("OK: " + nombre);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void esperar(Class<? extends Throwable> tipo, Runnable accion) {
        try {
            accion.run();
        } catch (Throwable error) {
            if (tipo.isInstance(error)) {
                return;
            }
            throw new AssertionError("Se esperaba " + tipo.getSimpleName(), error);
        }
        throw new AssertionError("No se lanzó " + tipo.getSimpleName());
    }
}
