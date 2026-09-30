# Escenario 1 · Aprobación escalonada de compras institucionales

**Patrón seleccionado:** Chain of Responsibility.

## Definición del problema y enunciado propuesto

Una universidad necesita procesar solicitudes de compra para materiales de oficina, licencias, equipos tecnológicos, mobiliario y otros bienes necesarios para sus diferentes áreas. Cada solicitud registra un código, el área solicitante, la descripción de la compra y el valor estimado.

La institución tiene varios niveles de autorización. Un coordinador de área puede resolver solicitudes de hasta **1.000 dólares**. Las solicitudes que superen ese límite deben ser revisadas por un director administrativo, quien puede resolver valores de hasta **10.000 dólares**. Las solicitudes mayores deben enviarse al comité de compras, cuya autoridad llega hasta **50.000 dólares**. Si el valor supera el límite del comité, la solicitud queda sin resolver dentro de este módulo y debe informarse que necesita una instancia extraordinaria no modelada en el ejercicio.

El sistema no debería obligar al servicio de compras a conocer todos los niveles mediante una secuencia de `if/else`. Cada nivel debe determinar si tiene autoridad para resolver la solicitud. Si puede hacerlo, genera el resultado y termina el recorrido; si no puede, delega la misma solicitud al siguiente nivel configurado.

La universidad administra varios campus. Aunque inicialmente todos pueden usar coordinador, director y comité en ese orden, se prevé que un campus pequeño omita al director o que en el futuro se incorpore una nueva autoridad entre dos niveles. Se desea cambiar la composición u orden de aprobación sin reescribir la lógica que envía una solicitud.

Diseñar una solución orientada a objetos que permita construir una cadena de responsables, enviar una solicitud al primer elemento y hacer que avance hasta encontrar una autoridad capaz de resolverla. La solución debe permitir agregar, retirar o reordenar niveles de aprobación con cambios localizados.

## Contexto y responsabilidades que generan el problema

La solicitud contiene los datos del trámite, pero no debe elegir quién la aprueba. El servicio de compras inicia el proceso, pero tampoco debería conocer límites y clases concretas. Cada autoridad conoce su propia capacidad y al siguiente receptor de la cadena.

Las interacciones relevantes son:

1. El cliente entrega una solicitud al primer responsable configurado.
2. El responsable compara el importe con su límite de autorización.
3. Si tiene autoridad, produce un resultado que identifica quién resolvió la solicitud.
4. Si no la tiene, la transmite sin modificarla al siguiente responsable.
5. Si termina la cadena, se devuelve un resultado que indica que el módulo no pudo resolverla.

Este escenario utilizará la variante de Chain of Responsibility en la que **un único manejador resuelve la petición y detiene el recorrido**. No se propone una tubería en la que todos los niveles aprueban la misma compra.

## Por qué una solución directa dificultaría la evolución

Una implementación centralizada podría parecer sencilla:

```text
si monto <= 1000 → coordinador
si no, si monto <= 10000 → director
si no, si monto <= 50000 → comité
si no → instancia extraordinaria
```

Sin embargo, ese bloque mezcla el envío de la solicitud con las reglas de todas las autoridades. Cambiar un límite, añadir una dirección financiera, usar otra cadena en un campus o retirar temporalmente un nivel exige modificar la misma clase. El cliente también queda acoplado a los nombres concretos y al orden de los responsables.

Con Chain of Responsibility, el cliente conoce solamente el primer eslabón. Cada manejador asume una decisión local: resolver o delegar. La estructura de la cadena puede configurarse al iniciar el módulo.

## Justificación del patrón

Chain of Responsibility es apropiado porque existen varios receptores potenciales, el receptor correcto depende de la solicitud y el emisor no debería seleccionarlo mediante condiciones. El patrón desacopla la petición de quien finalmente la atiende y permite variar la cadena independientemente del cliente.

La utilidad del patrón no está solo en reemplazar un `if`. La cadena representa una configuración que puede cambiar por campus o por política institucional. Un nuevo nivel implementa el mismo contrato y se conecta en el punto necesario.

## Participantes esperados en el dominio

Estos participantes son una guía para elaborar el UML; sus nombres y firmas se revisarán cuando el grupo entregue V1.

| Participante del patrón | Correspondencia propuesta | Responsabilidad |
|---|---|---|
| Request | `SolicitudCompra` | Transportar código, área, descripción e importe. |
| Handler | `AprobadorCompra` | Declarar cómo configurar el siguiente y procesar una solicitud. |
| BaseHandler opcional | `AprobadorBase` | Guardar la referencia al siguiente y centralizar la delegación. |
| ConcreteHandler | `AprobadorCoordinador` | Resolver importes de hasta 1.000. |
| ConcreteHandler | `AprobadorDirector` | Resolver importes de hasta 10.000 cuando lleguen a su nivel. |
| ConcreteHandler | `AprobadorComite` | Resolver importes de hasta 50.000. |
| Result | `ResultadoAprobacion` | Indicar si fue resuelta, responsable y explicación. |
| Client | `ServicioCompras` o programa principal | Configurar la cadena y enviar la solicitud al primer manejador. |

## Reglas que deberán reflejar el UML y el código

- El importe debe ser positivo y se representará como un tipo decimal adecuado para dinero.
- Cada solicitud debe tener código, área y descripción.
- Los límites pertenecen a los manejadores, no al cliente.
- Cada manejador conoce como máximo a un siguiente manejador; el último puede no tener siguiente.
- Una solicitud aceptada no continúa por la cadena.
- Una solicitud superior a 50.000 termina con resultado no resuelto; no se inventará otra autoridad sin modificar primero el escenario.
- La cadena puede construirse con otro orden o sin alguno de los niveles, aunque una configuración incoherente será responsabilidad de quien la construya.
- En el primer código se ejecutará una única solicitud por vez; no se requiere concurrencia ni persistencia.

## Colaboración prevista

Como ejemplo, una compra de 7.500 llega primero al coordinador. El coordinador detecta que excede su autoridad y la delega al director. El director puede resolverla, genera el resultado y no llama al comité. Para una solicitud de 70.000, los tres niveles delegan y el último informa que la cadena no tiene autoridad suficiente.

El cliente recibe un resultado común sin preguntar qué clase concreta procesó la solicitud. Esa colaboración deberá ser visible en el UML mediante la autorrelación del Handler hacia el siguiente Handler y mediante la dependencia del cliente respecto del contrato común.

## Validación que deberá discutirse en la entrega final

El patrón resolverá la selección desacoplada del responsable y permitirá reconfigurar la secuencia. Mejorará la extensibilidad frente a un bloque central de condiciones, pero introducirá más objetos y hará menos evidente qué manejador resolverá una petición sin recorrer la configuración.

No se recomendaría si existe una única autoridad estable, si nunca cambia el orden o si un condicional pequeño representa de forma más clara una regla permanente. Tampoco es suficiente por sí solo cuando una compra necesita aprobación acumulativa de todos los niveles; esa sería otra colaboración y debería modelarse explícitamente.

## Preguntas para revisar el futuro UML

1. ¿La referencia al siguiente utiliza el tipo abstracto `AprobadorCompra`?
2. ¿El cliente depende solo del primer Handler o conoce todos los manejadores concretos?
3. ¿La solicitud y el resultado están separados de quien procesa?
4. ¿Queda claro cuándo se detiene la cadena?
5. ¿La multiplicidad hacia el siguiente es `0..1`?
6. ¿Los límites están en los manejadores y no repetidos en el cliente?

[Volver al índice de Deber 3](../README.md)
