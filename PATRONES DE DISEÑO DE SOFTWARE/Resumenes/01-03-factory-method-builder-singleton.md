# Clase 01.03: Factory Method, Builder y Singleton

**Unidad 01: Patrones de diseño creacionales.**

Fuente: [Unidad_01_03_PDS.pdf](../ClasesDiapositivas/Unidad_01_03_PDS.pdf), 32 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: introducción, pp. 4–9; Factory Method, pp. 11–15; Builder, pp. 17–22; Singleton, pp. 24–28.

## Idea central

Los patrones creacionales organizan las decisiones de creación para que el cliente no dependa innecesariamente de clases concretas. Factory Method decide **qué producto crear** mediante subclases; Builder organiza **cómo construirlo** progresivamente; Singleton controla **cuántas instancias existen** dentro de un alcance definido.

No eliminan `new`: trasladan su uso y las decisiones que lo acompañan a un lugar con una responsabilidad clara. Si crear el objeto ya es sencillo y estable, añadir una estructura creacional puede complicar el diseño sin aportar valor.

## 1. Cómo analizar un patrón creacional

La creación puede variar mediante **herencia**, cuando una subclase decide qué producto instanciar, o mediante **delegación**, cuando otro objeto recibe la responsabilidad de construirlo. El catálogo también incluye Abstract Factory y Prototype, desarrollados en la clase siguiente.

El procedimiento propuesto comienza por el problema y el contexto. Después se evalúan alternativas, se selecciona el patrón y se modelan participantes y colaboraciones en UML. La IA puede formular preguntas sobre supuestos pendientes y ayudar a producir código y pruebas. Finalmente se contrasta la implementación con el diseño acordado.

Una pregunta útil es: si mañana cambia esta decisión, ¿qué clases habría que modificar? El patrón aporta valor cuando concentra o aísla esa variación con un costo aceptable.

## 2. Factory Method: delegar qué producto se crea

### Problema y participantes

La empresa del ejemplo utiliza contratos fijos, temporales y por factura. Todos ofrecen `calcularSueldo()`, pero su comportamiento varía. Instanciar directamente cada contrato desde muchos clientes dispersaría el conocimiento de las clases concretas.

Factory Method establece dos jerarquías relacionadas:

| Participante | Papel | Correspondencia en el ejemplo |
|---|---|---|
| Product | Contrato común del resultado | `Contrato` |
| ConcreteProduct | Variante concreta del producto | `ContratoFijo`, `ContratoTemporal`, `ContratoFactura` |
| Creator | Declara el método de creación | `CreadorContrato` |
| ConcreteCreator | Decide qué producto instanciar | Creadores de cada tipo de contrato |

El método `crearContrato()` devuelve la abstracción `Contrato`. En UML, los creadores concretos especializan al creador base, y cada uno tiene una dependencia de creación hacia su producto correspondiente. Heredar del creador no significa heredar del producto: son responsabilidades distintas.

### Ejemplo explicado

1. La configuración selecciona el creador apropiado, por ejemplo el de contratos temporales.
2. El cliente solicita un contrato mediante `crearContrato()`.
3. El creador concreto instancia `ContratoTemporal` y lo devuelve como `Contrato`.
4. El cliente llama a `calcularSueldo()` usando el contrato común.
5. Para incorporar otro producto se añade su clase y el creador correspondiente; el código que trabaja con la abstracción puede conservarse.

La selección inicial del creador sigue teniendo que existir en algún lugar. El patrón reduce su propagación; no hace desaparecer toda configuración.

### Cuándo utilizarlo y qué cuesta

Conviene cuando una clase no puede anticipar el producto concreto o cuando las subclases deben especializar esa decisión. Los casos de logística, documentos, enemigos y reportes comparten un proceso general con un punto de creación variable.

Su beneficio es desacoplar el uso de la construcción concreta y facilitar extensiones. Su costo es mantener más clases y, en ocasiones, una jerarquía de creadores paralela a la de productos.

Como aclaración didáctica, un método central con `switch` que devuelve diferentes objetos puede ser una fábrica sencilla, pero no expresa por sí solo el Factory Method de esta clase. Aquí la decisión varía por herencia y polimorfismo. Tampoco cualquier método llamado `crear()` constituye automáticamente el patrón.

## 3. Builder: separar el proceso de construcción

### Problema y estructura clásica

El ejemplo construye objetos `Renta` para casas, departamentos y terrenos. Una renta puede incluir canon, alícuota y servicios. Algunos componentes son obligatorios y otros dependen del inmueble. Un constructor con muchos parámetros puede resultar difícil de leer y admitir combinaciones poco claras.

Builder distribuye el trabajo entre:

- **Builder:** contrato con pasos de construcción.
- **ConcreteBuilder:** implementa esos pasos y mantiene el producto en preparación.
- **Director:** coordina la secuencia usando el contrato del constructor.
- **Producto:** objeto complejo resultante.

En el diagrama, `RentaBuilder` ofrece operaciones como `construirCanon()`, `construirAlicuota()`, `construirServicios()` y `obtenerRenta()`. `RentaDirector` coordina el proceso, mientras los constructores de casa y departamento implementan las diferencias. Los cargos y reglas concretos deben proceder del caso, no inferirse del nombre del inmueble.

### Ejemplo explicado

1. El cliente selecciona un constructor para el tipo de renta.
2. El director solicita los pasos de canon, alícuota y servicios.
3. Cada constructor configura las partes según la variante que representa.
4. El cliente obtiene la renta terminada.
5. Otro constructor puede reutilizar el mismo proceso general con una configuración diferente.

La idea es separar el **proceso** del **resultado**. El director conoce el orden de los pasos; el constructor sabe cómo materializarlos. El producto conserva los datos y el comportamiento que le correspondan una vez construido.

### Variante sin Director

El material también muestra una variante en la que el cliente configura atributos mediante métodos encadenados y finaliza con `build()` o `construir()`. El diagrama de `PersonaBuilder` ilustra esa construcción explícita. La ausencia de un director no invalida esta variante, pero conviene distinguirla de la estructura clásica.

Como elaboración didáctica, la operación final debería comprobar los campos obligatorios antes de entregar el producto. Un Builder no garantiza por sí mismo inmutabilidad, validación ni aislamiento entre construcciones: son decisiones que la implementación debe resolver.

Builder resulta útil en reservas de vuelos con extras, personajes configurables, reportes con secciones opcionales y menús con distintas combinaciones. Añade claridad cuando hay muchos pasos o parámetros. Puede ser excesivo para un objeto pequeño cuyo constructor ya expresa bien lo necesario.

El costo incluye código adicional, estado intermedio y reglas de finalización. Si se reutiliza un constructor, debe evitarse que queden datos de la construcción anterior. Estas precauciones explican por qué el patrón requiere algo más que encadenar setters.

## 4. Singleton: controlar una instancia compartida

### Problema, participante y colaboración

La motivación del material es compartir un recurso, como la configuración general, cuya creación debe estar controlada. Singleton concentra en una clase la referencia a la instancia, el control de construcción y una operación de acceso, normalmente `getInstance()`.

El constructor no público evita la creación directa ordinaria por parte de los clientes. La referencia compartida y la operación de acceso aparecen como estáticas en el diagrama. Los clientes solicitan el objeto y reciben la misma referencia dentro del alcance previsto.

La solución visual presenta `ContadorVisita`, con una referencia estática a su instancia y un valor de contador. Ese ejemplo permite distinguir la identidad única del objeto del valor mutable que almacena.

### Ejemplo explicado y límites

1. Varios módulos necesitan consultar una configuración común.
2. En lugar de crear configuraciones independientes, solicitan la instancia compartida.
3. La clase controla cuándo se construye y qué referencia se devuelve.
4. Los módulos consultan el recurso a través de esa referencia.

Como precisión didáctica, mantener una única instancia no hace automáticamente seguras sus operaciones concurrentes. Un contador compartido puede perder actualizaciones si varios hilos modifican su valor sin una estrategia adecuada. La unicidad tampoco equivale a una única instancia entre varios procesos o servidores.

El material aconseja evitar Singleton cuando solo se busca una variable global, cuando concentra demasiadas responsabilidades o cuando dificulta las pruebas. La **inyección de dependencias** permite proporcionar explícitamente un objeto a sus consumidores y puede resolver mejor la necesidad de compartirlo.

### Evaluación de los casos de clase

Las siguientes respuestas son **orientaciones didácticas a las preguntas del material**:

- **Configuración de escritorio:** una instancia compartida puede ser razonable si el alcance y la coherencia lo requieren; también puede pasarse explícitamente a los módulos.
- **Gestor de impresión:** coordinar una impresora puede justificar un gestor común, pero sigue siendo necesario ordenar los trabajos y tratar errores.
- **Sesión web:** una única sesión global mezclaría el estado de usuarios diferentes. La sesión debe respetar el aislamiento de cada usuario.
- **Conexión a base de datos:** compartir una sola conexión puede limitar la concurrencia y mezclar operaciones. Un conjunto administrado de conexiones permite atender varias operaciones sin confundir el gestor con cada conexión.

## 5. Comparación y errores habituales

| Patrón | Variación principal | Mecanismo | Riesgo si se aplica sin necesidad |
|---|---|---|---|
| Factory Method | Tipo de producto | Creadores especializados | Exceso de jerarquías |
| Builder | Pasos y configuración | Construcción progresiva | Estado intermedio y código adicional |
| Singleton | Número de instancias | Creación y acceso controlados | Estado global y dependencias ocultas |

Una misma aplicación puede necesitar varios patrones, pero cada uno debe responder a un problema distinto. No se elige Builder porque haya muchas subclases, ni Singleton porque sea cómodo acceder desde cualquier lugar. Primero se identifica la responsabilidad de creación que necesita control.

## Síntesis

Factory Method especializa la decisión de creación; Builder separa y hace explícita la construcción; Singleton restringe instancias en un alcance. Comprender su intención permite reconocer cuándo aportan flexibilidad y cuándo añaden complejidad innecesaria.

## Preguntas de repaso

1. **¿Qué devuelve el Factory Method al cliente?** Una abstracción de producto, aunque cree una variante concreta.
2. **¿El cliente deja de seleccionar cualquier cosa?** No: alguien selecciona o configura el creador.
3. **¿Qué diferencia Director y Builder?** El director coordina pasos; el constructor implementa cómo se construyen las partes.
4. **¿Builder exige Director?** La variante clásica lo contempla; el material también presenta una variante sin él.
5. **¿Singleton garantiza seguridad concurrente?** No; la creación y el acceso al estado requieren análisis propio.
6. **¿Una sesión de usuario debe ser global?** No: debe mantener el aislamiento de usuarios.

[Volver al índice](README.md)
