package ejemplo.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Compuesto: guarda componentes y delega en ellos las operaciones recursivas. */
public final class GrupoPresupuestario extends PartidaPresupuestaria {
    private final List<PartidaPresupuestaria> elementos = new ArrayList<>();

    public GrupoPresupuestario(String codigo, String descripcion) {
        super(codigo, descripcion);
    }

    public void agregar(PartidaPresupuestaria elemento) {
        Objects.requireNonNull(elemento, "elemento");
        if (elemento.contiene(this)) {
            throw new IllegalArgumentException("La incorporación produciría un ciclo");
        }
        if (elementos.stream().anyMatch(actual -> actual == elemento)) {
            throw new IllegalArgumentException("Este objeto ya es hijo directo del grupo");
        }
        elementos.add(elemento);
    }

    public void quitar(PartidaPresupuestaria elemento) {
        Objects.requireNonNull(elemento, "elemento");
        elementos.removeIf(actual -> actual == elemento);
    }

    @Override public float calcularValorTotal() {
        float total = 0;
        for (PartidaPresupuestaria elemento : elementos) {
            total += elemento.calcularValorTotal();
        }
        if (!Float.isFinite(total)) {
            throw new ArithmeticException("El total excede el rango de float");
        }
        return total;
    }

    @Override public String mostrarEstructura() {
        StringBuilder resultado = new StringBuilder(linea(calcularValorTotal()));
        for (PartidaPresupuestaria elemento : elementos) {
            resultado.append("\n  ").append(elemento.mostrarEstructura().replace("\n", "\n  "));
        }
        return resultado.toString();
    }

    @Override protected boolean contiene(PartidaPresupuestaria elemento) {
        if (super.contiene(elemento)) { return true; }
        return elementos.stream().anyMatch(hijo -> hijo.contiene(elemento));
    }
}
