# Decisiones de diseño e implementación

La solicitud fue revisar el UML y generar el código del caso 4 de `Unidad_03_02_PDS.pdf`. Estas decisiones concretan los puntos que el enunciado deja abiertos; no se atribuyen al usuario rechazos ni motivaciones que no expresó.

| Decisión | Procedencia | Justificación |
|---|---|---|
| Aplicar Observer | Se conserva la elección del UML y coincide con el caso. | Un cambio de la oferta máxima debe propagarse a varios interesados dinámicos. |
| `CambioDeValor` como Subject abstracto | Se conserva la estructura propuesta. | Centraliza suscripción, retiro y notificación sin conocer observadores concretos. |
| Unificar la colección y `attach/detach` con `ValorObserver` | Corrección necesaria del UML. | El original guardaba `Observer`, pero recibía `FacturaObserver`, un tipo ajeno al caso. |
| Renombrar `notify()` como `notifyObservers()` | Corrección necesaria para Java. | `Object.notify()` es final y pertenece a sincronización, por lo que no puede sobrescribirse. |
| `Subasta` hereda las operaciones de gestión | Corrección de duplicación. | No necesita volver a declarar métodos ya definidos por la base. |
| `Participante` como ConcreteObserver | Interpretación directa del enunciado. | `ActualizarInformacion` y `RealizarNuevaOferta` son acciones, no participantes con identidad. Un participante puede hacer ambas. |
| Notificación push con `EstadoSubasta` | Elección didáctica. | Evita que cada observador mantenga una referencia al sujeto solo para consultar valor y ofertante. |
| Estado con ofertante, valor y secuencia | Elaboración didáctica. | Permite presentar y verificar qué oferta produjo cada cambio. |
| `BigDecimal` en lugar de `float` | Corrección de tipo para importes. | Conserva valores decimales como `125.50` sin error binario de punto flotante. |
| Solo una oferta estrictamente mayor cambia el estado | Regla asumida por «oferta más alta». | Las ofertas iguales o inferiores se rechazan y no producen notificaciones falsas. |
| El ofertante también recibe el aviso | Regla del ejemplo. | Todos los inscritos reciben el mismo cambio; el sujeto no incluye excepciones por clase o identidad. |
| No contraofertar dentro de la notificación | Límite deliberado. | Evita cascadas recursivas; el participante decide y llama después a `realizarNuevaOferta`. |
| Suscripción idempotente y retiro ausente sin error | Elección de gestión. | Previene avisos duplicados y hace seguro retirarse aunque el objeto ya no esté inscrito. |
| Recorrer una copia de observadores | Elección de robustez. | Suscribirse o retirarse dentro de una actualización no altera el aviso que ya comenzó. |

No se implementan cierre de subasta, incrementos mínimos, autenticación, persistencia, concurrencia ni ganador definitivo: no aparecen en el enunciado. Tampoco se garantiza que un observador que lance una excepción permita avisar a los siguientes; el ejemplo es síncrono y una excepción se propaga al emisor.

No hubo decisiones rechazadas explícitamente. Los nombres `ActualizarInformacion` y `RealizarNuevaOferta` se reemplazan en la corrección porque describen acciones y no objetos observadores; esta es una decisión técnica del asistente dentro de la revisión solicitada.
