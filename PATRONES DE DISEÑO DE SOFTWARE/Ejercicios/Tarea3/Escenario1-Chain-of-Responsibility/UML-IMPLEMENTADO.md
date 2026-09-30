# Correspondencia del código · Chain of Responsibility

El [UML final aceptado](uml-final.png) es el modelo conceptual entregado por el usuario. Este diagrama complementario muestra los tipos y retornos que se trasladaron exclusivamente a Java.

```mermaid
classDiagram
    class SolicitudCompra {
        -String codigo
        -String areaSolicitante
        -String descripcion
        -BigDecimal valorEstimado
    }
    class ServicioCompras {
        -Aprobador primerAprobador
        +procesar(SolicitudCompra) ResultadoAprobacion
    }
    class Aprobador {
        <<abstract>>
        -Aprobador siguiente
        +setSiguiente(Aprobador) void
        +manejar(SolicitudCompra) ResultadoAprobacion
        #puedeAprobar(SolicitudCompra) boolean
        #resolver(SolicitudCompra) ResultadoAprobacion
    }
    class CoordinadorArea
    class DirectorAdministrativo
    class ComiteCompras
    class ResultadoAprobacion {
        -boolean aprobada
        -String responsable
        -String mensaje
    }
    ServicioCompras --> "1" Aprobador : primerAprobador
    ServicioCompras ..> SolicitudCompra : procesa
    ServicioCompras ..> ResultadoAprobacion : devuelve
    Aprobador --> "0..1" Aprobador : siguiente
    Aprobador ..> SolicitudCompra : maneja
    Aprobador ..> ResultadoAprobacion : produce
    CoordinadorArea --|> Aprobador
    DirectorAdministrativo --|> Aprobador
    ComiteCompras --|> Aprobador
```

Este recurso no reemplaza ni modifica la captura final; sirve para revisar la correspondencia exacta con las firmas del código.
