# UML corregido · Observer, subasta electrónica

![UML corregido](uml-corregido.svg)

La imagen es vectorial y editable: [abrir SVG](uml-corregido.svg). La [captura original](uml-original.png) se conserva intacta.

## Versión editable en Mermaid

```mermaid
classDiagram
    class CambioDeValor {
        <<abstract>>
        -List~ValorObserver~ participantes
        +attach(ValorObserver participante) void
        +detach(ValorObserver participante) void
        +obtenerNumeroParticipantes() int
        #notifyObservers(EstadoSubasta estado) void
    }
    class Subasta {
        -BigDecimal valorActual
        -EstadoSubasta estadoActual
        +realizarOferta(String ofertante, BigDecimal valor) boolean
        +obtenerValorActual() BigDecimal
        +obtenerEstadoActual() Optional~EstadoSubasta~
    }
    class ValorObserver {
        <<interface>>
        +cambioOferta(EstadoSubasta estado) void
    }
    class Participante {
        -String nombre
        -EstadoSubasta ultimaActualizacion
        -int notificacionesRecibidas
        +cambioOferta(EstadoSubasta estado) void
        +realizarNuevaOferta(Subasta subasta, BigDecimal valor) boolean
    }
    class EstadoSubasta {
        -long numeroOferta
        -String ofertante
        -BigDecimal valorActual
    }
    CambioDeValor <|-- Subasta
    CambioDeValor o-- "0..*" ValorObserver : participantes
    ValorObserver <|.. Participante
    Subasta ..> EstadoSubasta : crea y notifica
    Participante --> Subasta : realiza oferta
```

## Cómo se interpreta

- `CambioDeValor` es el **Subject** abstracto: conoce únicamente la interfaz `ValorObserver`.
- `Subasta` es el **ConcreteSubject**: mantiene la oferta máxima y notifica cuando acepta una superior.
- `ValorObserver` es el contrato de actualización.
- Cada objeto `Participante` es un **ConcreteObserver**. No se necesita una clase distinta por participante ni una clase por cada posible reacción.
- `EstadoSubasta` es el mensaje inmutable enviado en la notificación. Incluye número de oferta aceptada, ofertante y valor actual.

La asociación utiliza agregación compartida (`o--`): los participantes existen independientemente y pueden retirarse. No se usa composición de ciclo de vida. La multiplicidad `0..*` permite una subasta sin inscritos y la incorporación dinámica de nuevos participantes.

El método se llama `notifyObservers`, no `notify`. En Java, `Object.notify()` es `final`; intentar declarar otro `notify()` sin parámetros provocaría un error de compilación. También evita confundir la notificación del patrón con el mecanismo de sincronización de hilos de Java.

El participante actualiza su copia al recibir el aviso y decide después si hace otra oferta mediante `realizarNuevaOferta`. No se oferta automáticamente dentro de `cambioOferta`, porque eso podría crear una cadena recursiva difícil de controlar. El escenario exige que pueda decidir, no que siempre contraoferte.

## Secuencia

```text
Participante → Subasta.realizarOferta(nombre, valor)
  ├─ si valor <= valorActual: devuelve false y no notifica
  └─ si valor > valorActual:
       1. actualiza valorActual
       2. crea EstadoSubasta
       3. notifyObservers(estado)
       4. cada inscrito recibe cambioOferta(estado)
       5. devuelve true
```
