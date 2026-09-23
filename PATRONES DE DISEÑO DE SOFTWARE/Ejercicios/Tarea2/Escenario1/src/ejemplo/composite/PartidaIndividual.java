package ejemplo.composite;

/** Hoja: su total es exactamente la asignación que recibe. */
public final class PartidaIndividual extends PartidaPresupuestaria {
    private final float valorAsignado;

    public PartidaIndividual(String codigo, String descripcion, float valorAsignado) {
        super(codigo, descripcion);
        if (!Float.isFinite(valorAsignado) || valorAsignado < 0) {
            throw new IllegalArgumentException("El valor debe ser finito y no negativo");
        }
        this.valorAsignado = valorAsignado;
    }

    @Override public float calcularValorTotal() { return valorAsignado; }
    @Override public String mostrarEstructura() { return linea(valorAsignado); }
}
