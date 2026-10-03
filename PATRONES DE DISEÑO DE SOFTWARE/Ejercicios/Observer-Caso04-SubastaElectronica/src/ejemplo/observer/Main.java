package ejemplo.observer;

import java.math.BigDecimal;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        Subasta subasta = new Subasta();
        Participante ana = new Participante("Ana");
        Participante bruno = new Participante("Bruno");
        Participante carla = new Participante("Carla");

        subasta.attach(ana);
        subasta.attach(bruno);
        ana.realizarNuevaOferta(subasta, new BigDecimal("100.00"));
        mostrar(ana, bruno);

        // Carla se incorpora durante la ejecución y recibirá cambios futuros.
        subasta.attach(carla);
        bruno.realizarNuevaOferta(subasta, new BigDecimal("125.50"));
        mostrar(ana, bruno, carla);

        // Una oferta inferior se rechaza y no genera notificaciones.
        boolean aceptada = carla.realizarNuevaOferta(subasta, new BigDecimal("120.00"));
        System.out.println("Oferta de 120.00 aceptada: " + aceptada);

        // Bruno se retira; ya no recibe el siguiente cambio.
        subasta.detach(bruno);
        carla.realizarNuevaOferta(subasta, new BigDecimal("140.00"));
        mostrar(ana, bruno, carla);
    }

    private static void mostrar(Participante... participantes) {
        for (Participante participante : participantes) {
            String valor = participante.obtenerUltimaActualizacion()
                    .map(estado -> estado.obtenerValorActual().toPlainString())
                    .orElse("sin información");
            System.out.println(participante.obtenerNombre() + " conoce: " + valor
                    + " | notificaciones: " + participante.obtenerNotificacionesRecibidas());
        }
        System.out.println();
    }
}
