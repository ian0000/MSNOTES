package ejemplo.builder;

import java.util.List;

/** Producto terminado: sus componentes no cambian después de construirlo. */
public final class Menu {
    private final String entrada;
    private final String platoPrincipal;
    private final String bebida;
    private final String postre;
    private final List<String> complementos;

    // Acceso de paquete: el cliente utiliza MenuBuilder para crear el producto.
    Menu(String entrada, String platoPrincipal, String bebida,
         String postre, List<String> complementos) {
        this.entrada = entrada;
        this.platoPrincipal = platoPrincipal;
        this.bebida = bebida;
        this.postre = postre;
        this.complementos = List.copyOf(complementos);
    }

    /** Devuelve null cuando no se seleccionó entrada. */
    public String getEntrada() {
        return entrada;
    }

    /** Devuelve null cuando no se seleccionó plato principal. */
    public String getPlatoPrincipal() {
        return platoPrincipal;
    }

    /** Devuelve null cuando no se seleccionó bebida. */
    public String getBebida() {
        return bebida;
    }

    /** Devuelve null cuando no se seleccionó postre. */
    public String getPostre() {
        return postre;
    }

    /** Lista no modificable; está vacía si no se seleccionaron complementos. */
    public List<String> getComplementos() {
        return complementos;
    }
}
