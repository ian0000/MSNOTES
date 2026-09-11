# Clase 02.02: Bridge y Decorator

**Unidad 02: Patrones de diseño estructurales.**

Fuente: [Unidad_02_02_PDS.pdf](../ClasesDiapositivas/Unidad_02_02_PDS.pdf), 18 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: Bridge, pp. 4–8; Decorator, pp. 10–14; comparación final, p. 15.

## Idea central

Bridge separa dos dimensiones de variación para que evolucionen independientemente. Decorator añade responsabilidades combinables alrededor de un objeto conservando su contrato. Ambos favorecen la composición y evitan crear una subclase por cada combinación, pero organizan variaciones diferentes.

Para elegirlos hay que identificar qué cambia. «Tipo de programa y canal de inscripción» son dos ejes que pueden combinarse. «Notificación básica con formato y datos adicionales» describe capacidades que se agregan a un objeto.

## 1. Herencia y composición como formas de extender

La **herencia** especializa un tipo existente: una subclase asume el contrato de su base y añade o modifica comportamiento compatible. La **composición de objetos** permite que uno mantenga referencias a otros y les delegue parte del trabajo.

En esta discusión de patrones, composición se usa como mecanismo general de colaboración. No toda referencia exige una composición UML con propiedad exclusiva del ciclo de vida. El rombo negro de los diagramas expresa la elección del modelo mostrado; una implementación concreta debe justificar su semántica de pertenencia.

La herencia puede resultar clara con pocas variantes estables. El problema aparece cuando se intenta representar cada combinación de dimensiones o capacidades mediante una clase distinta. Bridge y Decorator ofrecen dos respuestas a ese crecimiento.

## 2. Bridge: separar dimensiones independientes

### Problema de las inscripciones

La universidad gestiona inscripciones de Grado y Posgrado mediante canales Presencial, En línea y Móvil. Una jerarquía que mezcle ambos criterios necesitaría clases para cada combinación: grado presencial, grado móvil, posgrado presencial y así sucesivamente.

Con dos tipos y tres canales hay seis combinaciones. Si se añade Tecnologías como nuevo tipo de programa, una jerarquía combinatoria necesita variantes para todos los canales. Bridge separa la decisión académica de la implementación del canal.

El beneficio no consiste en eliminar las combinaciones posibles, sino en dejar de expresarlas todas mediante clases específicas. Se elige un objeto para cada dimensión y se conectan.

### Participantes del patrón

| Participante | Responsabilidad | Correspondencia en el material |
|---|---|---|
| Abstracción | Operación de alto nivel y referencia al implementador | `Inscripcion` |
| Abstracción refinada | Especialización del comportamiento de alto nivel | `InscripcionGrado`, `InscripcionPosgrado` |
| Implementador | Contrato de la segunda dimensión | `InscripcionImpl` |
| Implementador concreto | Variante de esa implementación | Canales presencial, en línea y móvil |

El cliente trabaja con `Inscripcion`. Esta ofrece `inscribir()` y una operación de control del nivel de estudio. Conserva una referencia de tipo `InscripcionImpl`, cuyo contrato incluye `generarUI()` y `procesarInscripcion()`.

Las operaciones de ambas jerarquías no tienen que coincidir: la abstracción expresa la necesidad de alto nivel y se apoya en operaciones más específicas del implementador. Esta distinción es esencial para no reducir Bridge a dos clases con métodos idénticos.

### Ejemplo explicado paso a paso

El siguiente recorrido es una **interpretación didáctica del diagrama**, no una transcripción de una implementación ejecutada:

1. Se selecciona una inscripción de Posgrado.
2. Se le proporciona un implementador del canal En línea.
3. El cliente solicita la operación de inscripción a la abstracción.
4. La especialización aplica el control académico que le corresponde.
5. La abstracción delega el trabajo del canal en el implementador elegido.
6. Para ofrecer el mismo tipo de programa por Móvil, se combina con el implementador móvil.

Las reglas académicas concretas y el orden exacto de validaciones deben definirse al implementar. El diagrama muestra la separación de responsabilidades, no todas las condiciones del proceso.

Para añadir Tecnologías se incorpora una abstracción refinada compatible con el contrato de canales. Para añadir otro canal se incorpora un implementador compatible con la abstracción. Este crecimiento independiente es la razón para aplicar Bridge.

### Cuándo usarlo y qué cuesta

Los casos del material presentan notificaciones por tipo y canal, reportes por contenido y formato, controles por nivel y dispositivo, y figuras por forma y mecanismo de renderizado. En cada caso pueden identificarse dos dimensiones que conviene extender independientemente.

El patrón reduce duplicación entre combinaciones y oculta detalles del implementador. A cambio, introduce dos jerarquías, referencias y configuración. Una forma didáctica de visualizarlo es comparar un crecimiento de combinaciones `m × n` con mantener `m` y `n` variantes separadas, además de sus abstracciones. No es una fórmula exacta para contar todas las clases de cualquier sistema.

Debe evitarse si solo existe una dimensión relevante, si las combinaciones son pocas y estables o si las variaciones están tan ligadas que separarlas no aclara el diseño.

## 3. Decorator: añadir responsabilidades mediante envolturas

### Problema de las notificaciones

Una inscripción genera una notificación básica. Según el contexto puede requerir formato HTML, datos para correo, un número telefónico o una cuenta de red social. Las capacidades pueden utilizarse individualmente o combinarse.

Crear una subclase para cada combinación produciría una jerarquía creciente. Decorator representa cada responsabilidad adicional mediante un objeto que envuelve a otro y conserva la interfaz común.

### Participantes y relaciones

- **Component:** contrato de los objetos que pueden decorarse; el ejemplo utiliza `Notificacion` y `send()`.
- **ConcreteComponent:** comportamiento base, representado por `NotificacionImpl`.
- **Decorator:** conserva una referencia a `Component` y delega las operaciones; aparece como `NotificacionDecorator`.
- **ConcreteDecorator:** añade una responsabilidad, como `NotificacionHTML`, `NotificacionConEmail`, `NotificacionConTelefono` o `NotificacionConCuenta`.

El decorador cumple una doble condición: **es un componente** para el cliente y **contiene un componente** al que delega. Como el objeto envuelto también puede ser decorador, se pueden formar cadenas.

En UML, hay una relación de especialización o realización hacia el contrato común y una relación de contención hacia ese mismo contrato. Esta combinación permite sustituir una notificación simple por una decorada sin exigir al cliente un método diferente para enviarla.

### Ejemplo de composición y recorrido

La siguiente notación es **pseudocódigo didáctico de composición**, no código tomado del repositorio enlazado en las diapositivas:

```text
base = NotificacionImpl()
conFormato = NotificacionHTML(base)
completa = NotificacionConEmail(conFormato, direccionCorreo)
completa.send()
```

1. Se crea la notificación básica.
2. Se envuelve con el decorador de formato.
3. Se envuelve el resultado con el decorador que incorpora información de correo.
4. El cliente invoca `send()` mediante el contrato común.
5. El decorador exterior añade su responsabilidad antes o después de delegar.
6. La llamada continúa hacia los componentes interiores, hasta alcanzar la base.

Las diapositivas muestran capacidades y estructura, pero no especifican toda la semántica del envío. Agregar una dirección no demuestra por sí solo que exista una integración real con un proveedor de correo. Cada implementación debe precisar qué hace al delegar y qué resultado produce.

## 4. Orden, independencia y costos de Decorator

El patrón permite modificar un objeto individual sin cambiar todos los objetos de su clase. Dos notificaciones pueden compartir la misma implementación base y tener combinaciones diferentes de responsabilidades.

Como explicación complementaria, el orden de los decoradores puede influir en el resultado. En los casos de procesamiento de imágenes, añadir una marca de agua y después recortar puede comportarse de forma diferente a recortar primero. Por ello no se debe asumir que todas las combinaciones son intercambiables.

Agregar un decorador tampoco implica necesariamente modificar el objeto original: normalmente se crea una envoltura y se usa esa nueva referencia. Retirar una capacidad puede requerir reconstruir la cadena apropiada; el patrón no proporciona automáticamente una operación universal para quitar cualquier envoltura interna.

El material propone documentos con marca de agua, firma, cifrado o auditoría; personajes con equipamiento; imágenes con filtros; y flujos con compresión, cifrado o validación. Los ejemplos comparten responsabilidades opcionales que deben combinarse conservando un contrato útil.

Sus costos incluyen más objetos pequeños, un recorrido de llamadas más difícil de seguir y posibles interacciones entre capas. Conviene evitarlo para una capacidad fija o cuando unas pocas variantes estables se expresan con mayor claridad de otra forma.

## 5. Bridge, Decorator y Adapter

| Patrón | Intención | Relación clave |
|---|---|---|
| Bridge | Separar dos dimensiones independientes | Una abstracción delega en otro contrato |
| Decorator | Combinar responsabilidades adicionales | Una envoltura conserva el contrato del objeto envuelto |
| Adapter | Integrar una interfaz existente incompatible | Traduce del contrato esperado al disponible |

Bridge organiza deliberadamente variaciones independientes. Adapter conecta un componente existente con una interfaz diferente de la esperada. Aunque ambos delegan, responden a problemas distintos.

Decorator conserva el contrato para sumar capacidades. Bridge no exige que la abstracción y el implementador tengan la misma interfaz. Esa diferencia permite distinguirlos aun cuando ambos dibujos muestran un objeto que contiene otro.

El dominio por sí solo no determina el patrón. «Notificaciones» puede aparecer en Bridge cuando varían tipo y canal, o en Decorator cuando se agregan capacidades opcionales. Primero se identifica la variación y después se justifica la estructura.

## Síntesis

Bridge conecta dos jerarquías para evitar clases por combinación. Decorator construye cadenas de responsabilidades alrededor de un componente. La composición aporta flexibilidad cuando hace explícitas esas variaciones y sus reglas de colaboración.

## Preguntas de repaso

1. **¿Qué evidencia sugiere Bridge?** Dos dimensiones independientes cuya combinación multiplica subclases.
2. **¿Abstracción e implementador deben tener los mismos métodos?** No: sus contratos expresan niveles de responsabilidad diferentes.
3. **¿Qué permite encadenar decoradores?** Que cada decorador cumpla el contrato del componente que envuelve.
4. **¿El orden de decoradores siempre es irrelevante?** No: las responsabilidades pueden interactuar.
5. **¿Añadir un decorador cambia todos los objetos de la clase?** No: afecta la composición elegida para ese objeto.
6. **¿Por qué Adapter no equivale a Bridge?** Adapter resuelve incompatibilidad existente; Bridge separa variaciones del diseño.

[Volver al índice](README.md)
