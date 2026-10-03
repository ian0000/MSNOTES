package ejemplo.observer;

/** Observer: contrato común para todos los interesados en la subasta. */
public interface ValorObserver {
    void cambioOferta(EstadoSubasta estado);
}
