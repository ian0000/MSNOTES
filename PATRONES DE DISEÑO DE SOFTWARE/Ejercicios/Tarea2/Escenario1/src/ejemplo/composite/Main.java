package ejemplo.composite;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        GrupoPresupuestario tecnologia = new GrupoPresupuestario("TI", "Tecnologías de la Información");
        GrupoPresupuestario infraestructura = new GrupoPresupuestario("INF", "Infraestructura");
        PartidaIndividual licencias = new PartidaIndividual("LIC", "Licencias", 500f);
        PartidaIndividual servidores = new PartidaIndividual("SRV", "Servidores", 2000f);
        PartidaIndividual almacenamiento = new PartidaIndividual("ALM", "Almacenamiento", 1000f);
        infraestructura.agregar(servidores);
        infraestructura.agregar(almacenamiento);
        tecnologia.agregar(licencias);
        tecnologia.agregar(infraestructura);

        Cliente cliente = new Cliente();
        System.out.println(cliente.consultar(tecnologia));
        System.out.println("\nLa misma operación con una partida individual:");
        System.out.println(cliente.consultar(licencias));
        infraestructura.quitar(almacenamiento);
        System.out.println("\nDespués de quitar Almacenamiento:");
        System.out.println(cliente.consultar(tecnologia));
    }
}
