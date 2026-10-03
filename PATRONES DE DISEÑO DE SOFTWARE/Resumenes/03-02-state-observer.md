# Clase 03.02: State y Observer

**Unidad 03: Patrones de diseño de comportamiento.**

Fuente: [Unidad_03_02_PDS.pdf](../ClasesDiapositivas/Unidad_03_02_PDS.pdf), 19 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: State, pp. 3-8; Observer, pp. 9-14; comparación y cierre, p. 15.

## Idea central

State y Observer distribuyen comportamiento que de otro modo terminaría concentrado o acoplado en una clase. **State** permite que un objeto responda de manera diferente según su situación interna, delegando en un objeto que representa el estado actual. **Observer** permite que varios objetos interesados reaccionen cuando otro objeto cambia, sin que el emisor dependa de sus implementaciones concretas.

La distinción del cierre de la presentación es práctica: si cambia el comportamiento porque cambió la situación interna de un objeto, conviene estudiar State. Si un cambio debe propagarse a varios interesados, conviene estudiar Observer. Ambos pueden coexistir, pero resuelven relaciones diferentes.

## 1. State: comportamiento dependiente de la situación actual

### Problema del certificado electrónico

Una solicitud de certificado puede estar `Iniciado`, `En trámite` o `Firmado`. Las mismas acciones producen resultados diferentes:

- En `Iniciado` puede generarse el documento, pero todavía no visualizarse ni descargarse.
- En `En trámite` puede visualizarse con marca de agua y descargarse una versión provisional.
- En `Firmado` puede visualizarse y descargarse el documento final.

Sin State, cada operación podría repetir condiciones sobre el estado. Añadir otra situación obligaría a modificar varios métodos. El problema no es únicamente almacenar un valor como texto o `enum`; es que ese valor gobierna un conjunto de comportamientos relacionados.

### Participantes y colaboración

| Participante | Responsabilidad |
|---|---|
| Context | Expone la interfaz usada por el cliente, conserva el State actual y delega operaciones. |
| State | Declara el contrato común para los comportamientos asociados a un estado. |
| ConcreteState | Implementa lo que ocurre en una situación específica y, si corresponde, provoca una transición. |
| Cliente | Interactúa principalmente con el contexto, no con estados concretos. |

En UML, el contexto contiene una referencia a `State`. Los estados concretos realizan esa interfaz. La colaboración usa **composición y delegación**: cuando el cliente solicita una operación, el contexto la envía al objeto que representa su estado actual.

El rombo negro mostrado en la diapositiva expresa una composición en ese modelo. No debe interpretarse como una obligación universal del patrón; la propiedad y ciclo de vida del objeto State dependen de la implementación.

### Recorrido paso a paso

El siguiente ejemplo es una **elaboración didáctica** basada en el problema del certificado:

```text
solicitud = SolicitudCertificado(EstadoIniciado)
solicitud.visualizar()
solicitud.generar()
solicitud.cambiarEstado(EstadoEnTramite)
solicitud.visualizar()
```

1. El contexto inicia con un objeto `EstadoIniciado`.
2. `visualizar()` se delega al estado actual y este indica que aún no está disponible.
3. `generar()` se delega al mismo estado y ejecuta el comportamiento permitido.
4. Una transición sustituye la referencia por `EstadoEnTramite`.
5. La siguiente llamada a `visualizar()` utiliza la nueva implementación sin que el cliente pregunte por el estado.

### ¿Quién controla las transiciones?

La presentación admite dos diseños. El contexto puede decidir y validar las transiciones, o un estado concreto puede recibir una referencia al contexto y provocar el cambio. La elección debe ser explícita.

Centralizar transiciones en el contexto facilita ver el mapa completo. Permitir que los estados cambien el contexto mantiene junto el comportamiento y su transición, pero puede dispersar el flujo. Mezclar ambos enfoques sin una regla clara dificulta seguir el ciclo de vida.

### Cuándo usarlo y qué cuesta

State es útil cuando el comportamiento cambia dinámicamente según un estado interno, varias operaciones repiten las mismas condiciones, las transiciones son relevantes y un nuevo estado obligaría a tocar muchos métodos. Los casos de la presentación incluyen pedidos, tickets de soporte, préstamos bibliotecarios y reproductores multimedia.

En el reproductor, `play`, `pause` y `stop` existen siempre, pero su resultado depende de estar detenido, reproduciendo o pausado. Las operaciones pertenecen al contrato State y cada clase concreta expresa la reacción correspondiente.

El patrón elimina grandes bloques condicionales y agrupa comportamiento coherente. A cambio, aumenta el número de clases y puede dispersar las transiciones. Debe evitarse cuando existen pocos estados con reglas simples, un `enum` y un condicional claro son suficientes o las transiciones casi nunca cambian.

## 2. Observer: propagación a múltiples interesados

### Problema de la factura pagada

Cuando una factura cambia a `Pagada`, Despacho inicia el envío, Servicio al Cliente comienza el seguimiento y Contabilidad registra asientos. Si `Factura` invocara directamente cada clase concreta, todo nuevo interesado exigiría modificarla.

Observer crea una dependencia uno-a-muchos. El sujeto mantiene una colección de observadores y los notifica mediante una interfaz común. Puede agregar o retirar interesados sin conocer las clases que reaccionan.

### Participantes y relaciones

| Participante | Responsabilidad |
|---|---|
| Subject | Mantiene la colección, permite suscribir/desuscribir y define el mecanismo de notificación. |
| ConcreteSubject | Conserva el estado relevante y dispara notificaciones cuando cambia. |
| Observer | Declara la operación común de actualización. |
| ConcreteObserver | Implementa una reacción y puede mantener información consistente con el sujeto. |

La generalización conecta el sujeto concreto con Subject. Una realización conecta cada observador concreto con la interfaz Observer. La asociación uno-a-muchos va del sujeto al contrato, no a clases concretas.

La diapositiva muestra `attach`, `detach`, `notify` y `update`. Son nombres conceptuales. En Java conviene evitar un método `notify()` sin parámetros porque `Object.notify()` es final y pertenece a la sincronización de hilos; puede utilizarse un nombre como `notificarObservadores`.

### Notificación push y pull

La colaboración del material indica que, después del aviso, un observador puede consultar al sujeto. Esto corresponde a una variante **pull**: la notificación señala que algo cambió y el observador obtiene los datos necesarios.

Como **elaboración didáctica**, también existe la variante **push**, donde el sujeto incluye datos del cambio en `update(evento)`. Push reduce consultas posteriores, pero acopla el contrato al contenido del evento. Pull mantiene una actualización más general, aunque cada interesado necesita acceder al sujeto. La presentación no obliga a una única variante.

### Ejemplo paso a paso

En el caso de disponibilidad de productos:

1. Los clientes interesados se suscriben al producto agotado.
2. El producto guarda referencias mediante la interfaz Observer.
3. Cuando vuelve a estar disponible, el sujeto cambia su estado.
4. Recorre la colección e invoca la actualización común.
5. Cada cliente decide cómo presentar o procesar el aviso.
6. Un cliente que ya no está interesado se desuscribe sin modificar la clase Producto.

El mismo esquema se aplica a calificaciones, sensores y subastas. Cambian el evento y las reacciones, no la relación estructural.

### Subasta electrónica

En la subasta, el sujeto concreto conserva la oferta más alta. Una oferta superior produce un cambio relevante y debe notificarse; una oferta rechazada no representa un nuevo estado observable. Los participantes actualizan su información y luego pueden decidir si vuelven a ofertar.

Una implementación desarrollada como **elaboración didáctica complementaria** está en [Observer: subasta electrónica](../Ejercicios/Observer-Caso04-SubastaElectronica/README.md). Utiliza una notificación push con ofertante y valor, permite incorporarse o retirarse durante la ejecución y evita contraofertar automáticamente dentro del callback. No es una solución oficial de las diapositivas.

### Consecuencias y riesgos

Observer reduce el acoplamiento entre emisor y receptores y permite una cantidad dinámica de interesados. Sin embargo, la ejecución se vuelve menos visible: una sola actualización puede activar muchas reacciones o generar cadenas de notificaciones.

Hay que definir el orden, el tratamiento de errores y el ciclo de vida de las suscripciones. Un objeto olvidado en la colección puede seguir recibiendo avisos o conservarse innecesariamente. Si un observador modifica al sujeto dentro de `update`, puede producir notificaciones en cascada. La diapositiva advierte estos riesgos y recomienda evitar el patrón cuando existe un único receptor estable o una llamada directa es más clara.

## 3. State frente a Observer

| Pregunta | State | Observer |
|---|---|---|
| ¿Qué provoca la variación? | El estado interno actual del contexto | Un cambio relevante que debe propagarse |
| Relación principal | Contexto delega en un State | Subject notifica a muchos Observer |
| Cardinalidad típica | Un estado actual a la vez | Cero o muchos observadores |
| Objetivo | Cambiar cómo se comporta el mismo objeto | Permitir que otros objetos reaccionen |
| Dinamismo | Transiciones entre estados | Suscripción y retiro de interesados |
| Riesgo principal | Transiciones dispersas | Cascadas y suscripciones olvidadas |

Un sistema puede utilizar ambos. Por ejemplo, un pedido puede aplicar State para comportarse según `Creado`, `Pagado` o `Enviado`, y Observer para informar a otros componentes cuando cambia de estado. State decide qué significa una operación en la situación actual; Observer distribuye la noticia del cambio.

No debe confundirse Observer con una lista de llamadas directas. El sujeto depende de la interfaz, no de `Despacho`, `Contabilidad` o cada participante concreto. Tampoco todo cambio de un atributo necesita un evento: deben notificarse únicamente cambios relevantes para el contrato del sistema.

## 4. Errores habituales al modelarlos

- Representar estados mediante clases, pero seguir ejecutando todo el comportamiento en un `switch` del contexto.
- Permitir que el cliente invoque estados concretos y omitir la delegación del contexto.
- No definir quién controla las transiciones o permitir transiciones imposibles.
- Guardar observadores concretos en lugar de la interfaz común.
- Usar tipos diferentes en la colección, `attach` y `detach`.
- Notificar antes de actualizar el estado, haciendo que los receptores consulten información antigua.
- Confundir «varios objetos observadores» con «varias clases observadoras»: una misma clase puede tener muchas instancias suscritas.

## Síntesis

State mueve el comportamiento dependiente de una situación a objetos de estado y permite sustituir el estado actual del contexto. Observer separa al objeto que origina un cambio de los múltiples interesados que reaccionan. El primero organiza decisiones internas; el segundo organiza propagación externa. En ambos casos, la utilidad proviene de responsabilidades claras, no del número de clases.

## Preguntas de repaso

1. **¿Qué señal indica State?** Varias operaciones cambian de comportamiento según la misma situación interna.
2. **¿Con quién interactúa normalmente el cliente en State?** Con el contexto, que delega en su estado actual.
3. **¿Quién puede controlar una transición?** El contexto o un estado concreto, según una decisión explícita de diseño.
4. **¿Qué relación define Observer?** Una dependencia uno-a-muchos entre un sujeto y objetos interesados.
5. **¿Por qué el sujeto depende de una interfaz?** Para incorporar o retirar observadores sin conocer sus clases concretas.
6. **¿Cuál es la diferencia entre push y pull?** Push envía datos en el aviso; pull permite consultarlos después en el sujeto.
7. **¿Qué riesgo comparten las notificaciones automáticas?** Pueden generar cascadas difíciles de seguir y deben gestionar bien las suscripciones.
8. **¿Cómo distinguir State de Observer?** State cambia el comportamiento del contexto; Observer propaga un cambio a otros objetos.

[Volver al índice](README.md)
