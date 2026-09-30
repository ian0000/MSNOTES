package ejemplo.visitor;

import java.math.BigDecimal;
import java.util.Objects;

final class Validacion {
    private Validacion() { }

    static String texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }

    static BigDecimal positivo(BigDecimal valor, String campo) {
        Objects.requireNonNull(valor, campo);
        if (valor.signum() <= 0) {
            throw new IllegalArgumentException(campo + " debe ser positivo");
        }
        return valor;
    }

    static BigDecimal noNegativo(BigDecimal valor, String campo) {
        Objects.requireNonNull(valor, campo);
        if (valor.signum() < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo");
        }
        return valor;
    }
}
