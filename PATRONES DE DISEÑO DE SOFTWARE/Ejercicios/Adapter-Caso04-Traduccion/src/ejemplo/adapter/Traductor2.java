package ejemplo.adapter;

import java.util.Locale;
import java.util.Map;

/** Segundo proveedor simulado; se conserva la firma que aparece en el UML. */
public class Traductor2 {
    private static final Map<String, String> FRASES = Map.of(
            "hola", "hello", "buenos dias", "good morning", "gracias", "thank you");

    public String traducir(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Debe proporcionar un texto");
        }
        String resultado = FRASES.get(texto.strip().toLowerCase(Locale.ROOT));
        if (resultado == null) {
            throw new IllegalArgumentException("Traductor2 simulado: frase no disponible");
        }
        return resultado;
    }
}
