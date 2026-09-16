package ejemplo.builder;

import java.util.ArrayList;
import java.util.List;

/** Constructor fluido reutilizable. No se debe compartir entre hilos. */
public final class MenuBuilder {
    private String entrada;
    private String platoPrincipal;
    private String bebida;
    private String postre;
    private List<String> complementos = List.of();

    public MenuBuilder conEntrada(String entrada) {
        this.entrada = validarTexto(entrada, "entrada");
        return this;
    }

    public MenuBuilder conPlatoPrincipal(String platoPrincipal) {
        this.platoPrincipal = validarTexto(platoPrincipal, "platoPrincipal");
        return this;
    }

    public MenuBuilder conBebida(String bebida) {
        this.bebida = validarTexto(bebida, "bebida");
        return this;
    }

    public MenuBuilder conPostre(String postre) {
        this.postre = validarTexto(postre, "postre");
        return this;
    }

    /** Reemplaza los complementos y copia la lista recibida. */
    public MenuBuilder conComplementos(List<String> complementos) {
        if (complementos == null) {
            throw new IllegalArgumentException("complementos no puede ser null");
        }
        List<String> copia = new ArrayList<>();
        for (String complemento : complementos) {
            copia.add(validarTexto(complemento, "complemento"));
        }
        // Se asigna después de validar todo, para no dejar un cambio parcial.
        this.complementos = List.copyOf(copia);
        return this;
    }

    /** Crea un producto nuevo sin reiniciar la configuración del builder. */
    public Menu construir() {
        return new Menu(entrada, platoPrincipal, bebida, postre, complementos);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede ser null ni estar vacío");
        }
        return valor;
    }
}
