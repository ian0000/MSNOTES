package ejemplo.builder;

import java.util.List;

/** Cliente: elige qué pasos ejecutar y utiliza el producto final. */
public final class Main {
    public static void main(String[] args) {
        Menu completo = new MenuBuilder()
                .conEntrada("Sopa de verduras")
                .conPlatoPrincipal("Pollo con arroz")
                .conBebida("Jugo de naranja")
                .conPostre("Flan")
                .conComplementos(List.of("Ensalada", "Pan"))
                .construir();

        Menu ligero = new MenuBuilder()
                .conPlatoPrincipal("Ensalada de garbanzos")
                .conBebida("Agua")
                .construir();

        MenuBuilder base = new MenuBuilder()
                .conPlatoPrincipal("Pasta con vegetales")
                .conBebida("Agua");
        Menu sinPostre = base.construir();
        Menu conPostre = base.conPostre("Fruta").construir();

        mostrar("Menú completo", completo);
        mostrar("Menú ligero", ligero);
        mostrar("Base antes de añadir postre", sinPostre);
        mostrar("Nueva construcción con postre", conPostre);
    }

    private static void mostrar(String titulo, Menu menu) {
        System.out.println("=== " + titulo + " ===");
        System.out.println("Entrada: " + seleccionado(menu.getEntrada()));
        System.out.println("Plato principal: " + seleccionado(menu.getPlatoPrincipal()));
        System.out.println("Bebida: " + seleccionado(menu.getBebida()));
        System.out.println("Postre: " + seleccionado(menu.getPostre()));
        System.out.println("Complementos: " + menu.getComplementos());
        System.out.println();
    }

    private static String seleccionado(String valor) {
        return valor == null ? "sin seleccionar" : valor;
    }
}
