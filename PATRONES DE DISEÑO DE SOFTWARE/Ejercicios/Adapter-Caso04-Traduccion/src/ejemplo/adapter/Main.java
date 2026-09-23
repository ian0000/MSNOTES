package ejemplo.adapter;

public final class Main {
    public static void main(String[] args) {
        String texto = args.length == 0 ? "hola" : String.join(" ", args);
        Cliente cliente1 = new Cliente(new Traductor1Adapter(new Traductor1()));
        Cliente cliente2 = new Cliente(new Traductor2Adapter(new Traductor2()));
        try {
            System.out.println("Proveedores simulados: espanol -> ingles");
            System.out.println("Texto: " + texto);
            System.out.println("Proveedor 1: " + cliente1.solicitarTraduccion(texto));
            System.out.println("Proveedor 2: " + cliente2.solicitarTraduccion(texto));
        } catch (IllegalArgumentException error) {
            System.err.println(error.getMessage());
            System.err.println("Frases disponibles: hola, buenos dias, gracias");
            System.exit(2);
        }
    }
}
