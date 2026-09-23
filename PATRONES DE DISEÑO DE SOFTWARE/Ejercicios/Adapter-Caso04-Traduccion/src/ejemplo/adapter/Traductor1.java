package ejemplo.adapter;

import java.util.Locale;
import java.util.Map;

/** Adaptee simulado. Diccionario ES -> EN; no es una API real de traduccion. */
public class Traductor1 {
    private static final Map<String, String> FRASES = Map.of(
            "hola", "hello", "buenos dias", "good morning", "gracias", "thank you");

    public String traducir(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Debe proporcionar un texto");
        }
        String resultado = FRASES.get(texto.strip().toLowerCase(Locale.ROOT));
        if (resultado == null) {
            throw new IllegalArgumentException("Traductor1 simulado: frase no disponible");
        }
        return resultado;
    }
}
