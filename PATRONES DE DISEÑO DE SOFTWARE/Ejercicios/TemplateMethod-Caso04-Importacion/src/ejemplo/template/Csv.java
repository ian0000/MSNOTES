package ejemplo.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Lector del dialecto del ejemplo: coma, comillas dobles, LF o CRLF y UTF-8. */
final class Csv {
    private Csv() { }

    static List<List<String>> leer(String texto) throws IOException {
        if (texto.startsWith("\uFEFF")) { texto = texto.substring(1); }
        List<List<String>> filas = new ArrayList<>();
        List<String> fila = new ArrayList<>();
        StringBuilder campo = new StringBuilder();
        boolean citado = false, cerrado = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (citado) {
                if (c == '"') {
                    if (i + 1 < texto.length() && texto.charAt(i + 1) == '"') {
                        campo.append('"'); i++;
                    } else { citado = false; cerrado = true; }
                } else { campo.append(c); }
            } else if (c == ',') {
                fila.add(campo.toString()); campo.setLength(0); cerrado = false;
            } else if (c == '\n' || c == '\r') {
                if (c == '\r' && i + 1 < texto.length() && texto.charAt(i + 1) == '\n') { i++; }
                fila.add(campo.toString()); filas.add(fila);
                fila = new ArrayList<>(); campo.setLength(0); cerrado = false;
            } else if (c == '"' && campo.length() == 0 && !cerrado) {
                citado = true;
            } else {
                if (cerrado || c == '"') { throw new IOException("Comillas CSV mal ubicadas"); }
                campo.append(c);
            }
        }
        if (citado) { throw new IOException("Campo CSV con comillas sin cerrar"); }
        if (!fila.isEmpty() || campo.length() > 0 || cerrado) {
            fila.add(campo.toString()); filas.add(fila);
        }
        return filas;
    }

    static String campo(String valor) {
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}
