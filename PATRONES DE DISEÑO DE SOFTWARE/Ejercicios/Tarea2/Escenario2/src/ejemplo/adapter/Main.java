package ejemplo.adapter;

import ejemplo.adapter.proveedores.MarcacionProveedor2;
import ejemplo.adapter.proveedores.Proveedor1;
import ejemplo.adapter.proveedores.Proveedor2;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        LocalDate fecha = LocalDate.of(2026, 9, 23);
        Proveedor1 antiguo = new Proveedor1(List.of(new RegistroAsistencia(
                "COL001", fecha, LocalTime.of(8, 0), LocalTime.of(17, 0))));
        Proveedor2 nuevo = new Proveedor2(List.of(
                new MarcacionProveedor2("COL001", "2026-09-23T17:15:00", "SALIDA"),
                new MarcacionProveedor2("COL001", "2026-09-23T13:00:00", "ENTRADA"),
                new MarcacionProveedor2("COL001", "2026-09-23T08:05:00", "ENTRADA"),
                new MarcacionProveedor2("COL001", "2026-09-23T12:00:00", "SALIDA"),
                new MarcacionProveedor2("COL002", "2026-09-23T08:20:00", "ENTRADA")));

        Cliente clienteAntiguo = new Cliente(new Proveedor1Adapter(antiguo));
        Cliente clienteNuevo = new Cliente(new Proveedor2Adapter(nuevo));
        System.out.println("Proveedor compatible: " + clienteAntiguo.consultar("COL001", fecha));
        System.out.println("Proveedor adaptado: " + clienteNuevo.consultar("COL001", fecha));
        System.out.println("Sin salida: " + clienteNuevo.consultar("COL002", fecha));
        System.out.println("Sin marcas: " + clienteNuevo.consultar("COL003", fecha));
    }
}
