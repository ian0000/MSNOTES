# Correspondencia del código · Visitor

El [UML final aceptado](uml-final.png) permanece como modelo conceptual. Este diagrama complementario documenta los tipos, conexiones y resultados trasladados exclusivamente a Java.

```mermaid
classDiagram
    class Envio {
        <<interface>>
        +aceptar(VisitanteEnvio) void
    }
    class Documento
    class PaqueteFragil
    class CargaRefrigerada
    class VisitanteEnvio {
        <<interface>>
        +visitar(Documento) void
        +visitar(PaqueteFragil) void
        +visitar(CargaRefrigerada) void
    }
    class VisitanteCalculoCosto {
        -BigDecimal total
        +obtenerTotal() BigDecimal
    }
    class VisitanteInspeccion {
        -List~String~ resultados
        +obtenerResultados() List~String~
    }
    class LoteEnvios {
        -List~Envio~ envios
        +agregar(Envio) void
        +ejecutar(VisitanteEnvio) void
    }
    Documento ..|> Envio
    PaqueteFragil ..|> Envio
    CargaRefrigerada ..|> Envio
    VisitanteCalculoCosto ..|> VisitanteEnvio
    VisitanteInspeccion ..|> VisitanteEnvio
    Envio ..> VisitanteEnvio : acepta
    LoteEnvios o-- "0..*" Envio : envios
    LoteEnvios ..> VisitanteEnvio : ejecuta
```

Este recurso explica la implementación; no se atribuye al usuario como un UML redibujado o aceptado.
