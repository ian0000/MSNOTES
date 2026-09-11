# Clase 01.05: Code smells, STUPID y principios SOLID

**Unidad 01: Patrones de diseño creacionales.**

Fuente: [Unidad_01_05_PDS.pdf](../ClasesDiapositivas/Unidad_01_05_PDS.pdf), 26 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: introducción, pp. 4–5; SRP, pp. 7–9; OCP, pp. 11–13; LSP, pp. 15–16; ISP, pp. 18–19; DIP, pp. 21–22.

## Idea central

SOLID reúne cinco principios para controlar el impacto del cambio mediante responsabilidades claras, contratos coherentes y dependencias orientadas hacia abstracciones. No constituye una receta para multiplicar interfaces ni obliga a fragmentar cualquier clase. Cada decisión debe justificarse por los cambios esperados y el costo que introduce.

Un **principio** orienta el razonamiento; un **patrón** propone una estructura reutilizable para un problema. Un patrón puede ayudar a aplicar un principio, pero utilizarlo no demuestra por sí mismo que el diseño sea bueno.

## 1. Señales de problemas: code smells y STUPID

Un **code smell** es una señal observable que invita a revisar el diseño. No prueba automáticamente que exista un error. Una clase grande puede reunir responsabilidades cohesionadas; una clase pequeña puede depender de muchos detalles ajenos.

STUPID agrupa seis problemas frecuentes:

| Letra | Problema | Qué conviene observar |
|---|---|---|
| S | Abuso de Singleton | Estado global y dependencias que quedan ocultas |
| T | Tight Coupling | Componentes que deben cambiar conjuntamente |
| U | Untestability | Dificultad para probar comportamientos de forma aislada |
| P | Premature Optimization | Complejidad añadida antes de conocer la necesidad real |
| I | Indescriptive Naming | Nombres que no explican intención |
| D | Duplication | La misma regla mantenida en varios lugares |

**Acoplamiento** expresa cuánto depende un componente de otros; **cohesión**, cuánto se relacionan sus responsabilidades entre sí. Se busca reducir dependencias innecesarias y mantener juntas las decisiones que pertenecen a un mismo propósito.

El material también menciona métodos o clases inflados, obsesión por tipos primitivos, demasiados parámetros, *feature envy*, intimidad inapropiada y cadenas de mensajes. Como explicación didáctica, *feature envy* describe una operación que parece interesarse más por los datos de otra clase que por los propios; la obsesión primitiva aparece cuando conceptos con reglas quedan reducidos a números o textos sin significado explícito.

Estas señales se investigan en contexto. Separar o abstraer sin comprenderlas puede sustituir un problema por otro.

## 2. SRP: responsabilidad única

**Single Responsibility Principle** propone una razón coherente para cambiar, asociada a un actor o grupo de interesados. No significa tener un solo método ni un número reducido de líneas.

La clase `Persona` del ejemplo calcula edad, almacena en un archivo y muestra información. Esas decisiones cambian por motivos diferentes: reglas del dominio, mecanismo de persistencia y formato de presentación.

El razonamiento es:

1. Identificar qué cambio puede solicitar cada interesado.
2. Distinguir cambios de negocio de cambios tecnológicos o visuales.
3. Mantener en `Persona` los datos y reglas propios del concepto.
4. Separar el almacenamiento en `PersonaArchivo`.
5. Revisar si el formato de salida también necesita una responsabilidad propia.

La diapositiva 8 separa la persistencia, pero conserva una operación de información en `Persona`. Por tanto, ilustra un paso de separación, no la obligación de colocar todo método de presentación en el dominio. Decidir una separación adicional requiere conocer si ese formato cambia por motivos independientes.

El beneficio es modificar una responsabilidad con menor impacto sobre las demás. El riesgo de aplicar SRP mecánicamente es crear demasiadas clases triviales y dificultar la comprensión del flujo completo.

### Actividad de PedidoService

La clase propuesta valida cantidades, calcula descuentos e impuestos, persiste, cobra, notifica y genera una factura PDF. La **orientación didáctica** es separar reglas comerciales, almacenamiento, integración de pagos, notificaciones y representación documental según sus razones de cambio.

Puede mantenerse un servicio que coordine el caso de uso: coordinar no equivale a implementar internamente todas esas responsabilidades. Una separación innecesaria sería extraer cada comprobación aritmética a una clase distinta sin una razón de cambio propia. La actividad pide precisamente justificar tanto las separaciones útiles como una que resultaría excesiva.

## 3. OCP: abierto a extensión y cerrado a modificación

**Open/Closed Principle** busca incorporar variantes sin modificar repetidamente un núcleo estable. No prohíbe corregir errores ni cambiar reglas existentes. El objetivo es proteger los puntos donde aparece una variación probable y relevante.

El ejemplo comienza con un servicio que decide mediante condiciones si notifica por correo o SMS. Cada canal nuevo obliga a editar ese servicio.

La solución visual introduce `Notificador`, con `enviar(mensaje)`. `NotificadorEmail` y `NotificadorSMS` implementan el contrato, mientras `ServicioNotificaciones` depende de él. Para añadir otro canal, se incorpora una implementación y se configura su selección. El servicio puede seguir trabajando con la misma operación.

El beneficio es localizar las variantes; el costo es mantener una abstracción y su configuración. No se elimina todo cambio del sistema: se evita que cada variante altere el flujo estable.

La actividad de costos de entrega pide reconocer la variación entre productos físicos, digitales y nuevas modalidades. Como propuesta didáctica, un contrato de cálculo de entrega permite añadir modalidades sin acumular condiciones en el consumidor. Debe comprobarse que las variantes previstas justifican esa separación.

## 4. LSP: sustitución de Liskov

**Liskov Substitution Principle** exige que los objetos de un subtipo puedan usarse donde se espera el tipo base sin romper su contrato. El contrato incluye más que nombres de métodos: condiciones de uso, resultados esperados y reglas que deben conservarse.

Una **precondición** es lo que debe cumplirse antes de una operación. Un **invariante** es una propiedad que el objeto debe mantener en los estados válidos. Un subtipo no debe exigir condiciones más restrictivas que las prometidas por la abstracción ni introducir fallos inesperados para usos válidos.

La imagen de la diapositiva 15 muestra una jerarquía de personas con un atributo de sueldo en la base y la marca como problemática. La lectura prudente es revisar si todas las especializaciones comparten realmente ese concepto; el diagrama por sí solo no especifica todos los contratos conductuales.

**Ejemplo complementario didáctico:** si una abstracción promete calcular una remuneración para cualquier instancia válida, incluir un subtipo que siempre lanza «operación no soportada» obliga al cliente a excluirlo. La solución debe revisar la abstracción o separar capacidades, en lugar de añadir comprobaciones de tipo por todas partes.

Las reglas Java de sobrescritura ayudan, pero no garantizan LSP. Deben coincidir parámetros, conservarse la visibilidad y mantenerse un retorno igual o covariante. Las excepciones verificadas no pueden ampliarse de forma incompatible. Un programa puede compilar y aun incumplir expectativas de negocio.

LSP no exige resultados idénticos: diferentes subtipos pueden calcular de manera distinta si cumplen el contrato común.

## 5. ISP: segregación de interfaces

**Interface Segregation Principle** propone que cada cliente dependa de operaciones coherentes con sus necesidades. Una interfaz amplia puede forzar implementaciones vacías, errores de «no aplica» y cambios sobre clientes que nunca usan la función modificada.

El ejemplo visual agrupa inicialmente `calcularEdad()` y `calcularSueldo()` en `IPersona`. Eso fuerza a participantes como `Estudiante` a asumir capacidades que pueden no corresponderles.

La solución distingue `IPersona`, para edad, e `IColaborador`, para sueldo. Un tipo que necesita ambos contratos puede implementarlos; otro depende únicamente del que le corresponde. La herencia de datos comunes y la implementación de capacidades son decisiones diferentes.

El beneficio es disminuir dependencias innecesarias. La precaución es no convertir ISP en «una interfaz por método»: las operaciones deben agruparse por necesidades reales de los clientes.

SRP pregunta por qué cambia una responsabilidad; ISP pregunta qué necesita cada consumidor del contrato. Se relacionan, pero enfocan problemas distintos.

## 6. DIP: inversión de dependencias

**Dependency Inversion Principle** orienta las dependencias hacia contratos estables. Los módulos de alto nivel contienen políticas de negocio; los de bajo nivel resuelven detalles como archivos, red o integraciones. Las políticas no deberían quedar atadas a esas implementaciones.

En el ejemplo, depender directamente de `PersonaArchivoTexto` y `PersonaArchivoObjetos` obliga a conocer modalidades concretas de almacenamiento. El diagrama final introduce la interfaz `PersonaArchivo`, que ambas implementan.

El flujo conceptual es:

1. Expresar qué operación de almacenamiento necesita el consumidor.
2. Definirla mediante un contrato estable.
3. Adaptar las implementaciones de texto y objetos a ese contrato.
4. Proporcionar la implementación elegida al consumidor.
5. Utilizar el contrato sin incorporar detalles del formato en la lógica principal.

La inversión se refiere a las dependencias del diseño: el detalle se ajusta a la abstracción. No significa invertir el orden de las llamadas en ejecución.

Como aclaración didáctica, **inyección de dependencias** es un mecanismo para entregar colaboradores, por ejemplo por constructor. DIP es el principio que orienta de qué tipos y contratos se depende. Se puede inyectar una clase concreta y continuar fuertemente acoplado a ella; tampoco se necesita un framework para aplicar DIP.

## 7. Cómo distinguir los principios

| Principio | Pregunta de revisión |
|---|---|
| SRP | ¿Qué actores o motivos diferentes hacen cambiar este módulo? |
| OCP | ¿Cada variante nueva obliga a modificar el núcleo estable? |
| LSP | ¿Cualquier subtipo respeta lo que promete el contrato? |
| ISP | ¿El cliente depende de operaciones que no necesita? |
| DIP | ¿La política depende de un contrato o de detalles tecnológicos? |

Una misma refactorización puede favorecer varios principios. Eso no obliga a aplicarlos todos como pasos separados. Conviene empezar por un problema observable, proponer una mejora y comprobar su efecto sobre la claridad, las dependencias y las pruebas.

## Síntesis

SOLID ayuda a explicar cómo debería propagarse el cambio. El resultado deseable es un diseño comprensible y modificable, con las abstracciones necesarias para el contexto y sin fragmentación innecesaria.

## Preguntas de repaso

1. **¿Un code smell demuestra mal diseño?** No: es una señal para investigar.
2. **¿SRP obliga a tener un solo método?** No: agrupa responsabilidades por una razón coherente de cambio.
3. **¿OCP prohíbe editar una clase?** No: busca evitar modificaciones repetidas por nuevas variantes.
4. **¿Compilar demuestra LSP?** No: también hay que respetar el contrato conductual.
5. **¿ISP exige una interfaz por operación?** No: exige contratos coherentes para sus clientes.
6. **¿DIP e inyección son equivalentes?** No: uno orienta dependencias y la otra proporciona colaboradores.

[Volver al índice](README.md)
