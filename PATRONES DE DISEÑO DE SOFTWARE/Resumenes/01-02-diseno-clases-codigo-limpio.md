# Clase 01.02: Diseño de clases y código limpio

**Unidad 01: Patrones de diseño creacionales.**

Fuente: [Unidad_01_02_PDS.pdf](../ClasesDiapositivas/Unidad_01_02_PDS.pdf), 35 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: diseño y UML, pp. 4–15; actividad, pp. 16–17; código limpio, pp. 19–31.

## Idea central

Diseñar transforma requisitos en responsabilidades, contratos y relaciones que pueden implementarse. El código limpio mantiene esas decisiones comprensibles durante la evolución del sistema. Un programa que funciona hoy puede resultar difícil de corregir mañana si mezcla responsabilidades, expone su estado o depende de detalles innecesarios.

## 1. Requisitos, diseño y complejidad

Los requisitos expresan **qué** debe lograr el sistema; el diseño determina **cómo** se organizará la solución; el código materializa esas decisiones. El modelo no es un plano definitivo: cambia cuando se aprende más sobre el problema.

La presentación recupera cuatro dificultades del software: complejidad, conformidad con el entorno, cambio continuo e invisibilidad. La última explica por qué necesitamos modelos: ninguna imagen muestra simultáneamente toda la estructura y todo el comportamiento de un sistema.

La IA puede acelerar tareas, pero no decide por sí sola cuáles son las restricciones reales ni qué consecuencias conviene aceptar. El criterio de diseño sigue siendo necesario aunque producir código sea más rápido.

**UML**, lenguaje unificado de modelado, aporta una notación compartida; no prescribe una metodología. Un diagrama de clases representa estructura estática: clases, interfaces, datos, operaciones y relaciones. Debe mostrar lo necesario para comprender una decisión, sin intentar copiar cada detalle del código.

En el diagrama de barcos, el controlador conoce `IBarcoServicio`, mientras `BarcoServicio` implementa ese contrato y gestiona barcos. El modelo ilustra una separación entre quien solicita una operación, quien la ofrece y los objetos sobre los que trabaja.

## 2. Constructores, sobrecarga y sobrescritura

El **constructor** establece el estado inicial de una instancia. Tiene el nombre de la clase y no declara retorno. Si una clase ordinaria de Java no declara constructores, el lenguaje proporciona uno sin parámetros; al declarar alguno, ese constructor automático deja de generarse. La inicialización también debe respetar las exigencias de la superclase.

Su responsabilidad principal es evitar que el objeto empiece inválido. Si un detalle requiere cantidad positiva, permitir construirlo con una cantidad negativa y esperar una corrección posterior debilita el modelo.

La **sobrecarga** permite varias operaciones con el mismo nombre y diferentes parámetros. Deben conservar una intención común. Delegar las variantes en una implementación principal evita repetir validaciones. Cambiar solo el retorno no distingue métodos sobrecargados en Java.

La **sobrescritura** permite que una subclase implemente de manera específica una operación heredada. Mantiene su firma, admite un retorno compatible y no reduce visibilidad. `@Override` ayuda a detectar errores; `this` representa la instancia actual y `super.metodo()` permite invocar la implementación base.

| Concepto | Qué cambia | Para qué sirve |
|---|---|---|
| Sobrecarga | Lista de parámetros | Ofrecer entradas alternativas con una misma intención |
| Sobrescritura | Implementación heredada | Especializar comportamiento mediante polimorfismo |

El **polimorfismo** permite llamar a una operación a través del tipo base y ejecutar el comportamiento del objeto real. Esto evita que cada cliente tenga que preguntar continuamente qué subtipo recibió.

## 3. Clases abstractas e interfaces

Una clase abstracta representa un concepto que no se instancia directamente. Puede tener estado, constructor, métodos implementados y métodos abstractos. Una subclase concreta debe completar las operaciones pendientes. El ejemplo `Persona` comparte información y cálculo de edad, mientras `Empleado` especializa la información que devuelve.

Una **interfaz** define un contrato de comportamiento sin imponer una clase base compartida. Una clase puede implementar varias interfaces. En las diapositivas, `AutoServicio` realiza el contrato de `IAutoServicio`; la línea discontinua con triángulo vacío apunta a la interfaz.

En Java, una interfaz puede incluir métodos abstractos, `default`, `static` y `private`; sus campos son constantes `public static final`. Esto no la convierte en un sustituto general de una clase con estado de instancia.

La elección depende del significado: una clase abstracta sirve para una familia con una relación válida de especialización y elementos comunes; una interfaz permite expresar capacidades que distintos tipos pueden ofrecer. Crear una interfaz sin una necesidad concreta tampoco mejora automáticamente el diseño.

## 4. Composición y agregación

Ambas describen relaciones entre un todo y sus partes, pero difieren en propiedad y ciclo de vida.

La **composición** utiliza un rombo negro en el extremo del todo. Este controla las partes dentro del modelo; una parte pertenece como máximo a un compuesto al mismo tiempo. El ejemplo `Pedido`–`DetallePedido` indica que los detalles tienen sentido como partes de ese pedido. El pedido contiene uno o más detalles, y cada detalle pertenece a un pedido.

La **agregación** utiliza un rombo blanco. Las partes pueden existir independientemente del conjunto. En `Equipo`–`Jugador`, el equipo agrupa jugadores, pero la desaparición del equipo no implica eliminar a las personas del modelo.

No debe deducirse una composición simplemente porque una clase contiene una lista. Primero se analiza el dominio y después se elige la notación. Asimismo, el ciclo de vida conceptual no determina por sí solo una instrucción de borrado en una base de datos.

## 5. Caso de pedidos en línea

La actividad reúne las relaciones anteriores. Este es un **desarrollo didáctico del enunciado**, que sirve para justificar un diseño:

1. `Cliente` se asocia con sus pedidos; cada pedido pertenece a un cliente.
2. `Pedido` se compone de detalles. Cada detalle registra producto, cantidad y precio aplicado.
3. `ProductoFisico` y `ProductoDigital` especializan el cálculo de entrega. La operación común permite usar ambos mediante la abstracción de producto.
4. `Catalogo` agrupa productos que pueden existir independientemente; por ello se propone agregación.
5. Para confirmar, el sistema usa un servicio externo de pagos. Si se recibe como parámetro temporal, se representa una dependencia; si se conserva como atributo, se representa una asociación navegable.

Guardar el precio aplicado en el detalle diferencia el valor de una compra del precio actual del catálogo. La presentación lo solicita explícitamente; esta separación ayuda a comprender por qué el detalle tiene responsabilidades propias.

La entrega de clase requiere visibilidades, tipos, retornos, multiplicidades y las cinco relaciones: asociación, composición, agregación, generalización y dependencia. Además, se deben explicar tres decisiones y registrar una recomendación de IA evaluada por el grupo.

## 6. Código limpio y deuda técnica

El código se lee y modifica muchas veces. **Deuda técnica** es el costo futuro asociado a decisiones que dificultan esa evolución. La matriz del material distingue deuda deliberada o inadvertida, y prudente o imprudente.

Aceptar temporalmente una limitación con riesgos documentados y un plan de reducción es distinto de omitir diseño sin evaluar consecuencias. También puede aparecer deuda porque nuevos conocimientos revelan una solución mejor. No toda deuda implica negligencia.

**Refactorizar** mejora la estructura interna manteniendo el comportamiento observable. La regla del Boy Scout propone mejorar gradualmente el código que se toca; no exige reescribir todo el sistema.

## 7. Prácticas que hacen comprensible el código

Los nombres deben comunicar intención y usar términos consistentes del dominio. `calcularTotal()` resulta más preciso que `procesar()`. Las clases suelen usar sustantivos y los métodos verbos; los booleanos pueden expresar preguntas como `canRetry`.

Las funciones deben tener un propósito claro, pocos parámetros y un nivel de abstracción coherente. Las cláusulas de guarda permiten rechazar condiciones inválidas antes del flujo principal. Separar consultas de modificaciones evita que una operación aparentemente informativa cambie el estado sin advertirlo.

**DRY** busca una representación autorizada de cada regla o conocimiento. Dos fragmentos parecidos no siempre representan el mismo concepto: conviene unirlos cuando tienen la misma razón de cambio, no solo porque coinciden algunas líneas.

Los comentarios útiles explican motivos, restricciones o riesgos que el código no expresa claramente. Los comentarios que repiten instrucciones o describen una versión anterior pueden desinformar. El formato debe ser consistente y seguir las convenciones del proyecto.

Una clase cohesionada reúne operaciones relacionadas con un estado y propósito comunes. Una estructura de datos, en cambio, transporta valores con poco comportamiento. La elección debe ser consciente. Devolver colecciones internas modificables o generar setters indiscriminados puede deshacer la encapsulación.

La **Ley de Demeter** aconseja colaborar con objetos conocidos directamente. Como ejemplo didáctico, una larga cadena de accesos a detalles internos puede indicar que falta una operación que exprese la intención del cliente.

## 8. Errores, límites y pruebas

Las excepciones deben aportar contexto, conservar su causa y capturarse cuando exista una respuesta útil. Un `catch` vacío oculta fallos. La ausencia de un valor debe representarse explícitamente cuando corresponda, y los mensajes no deben revelar datos sensibles.

Los límites con proveedores requieren contratos propios y adaptadores que eviten propagar tipos y configuraciones externos por todo el dominio. Las pruebas de aprendizaje o contrato comprueban los supuestos sobre esas integraciones.

El ciclo **TDD** consiste en rojo, verde y refactorización: escribir una prueba y comprobar su fallo, implementar lo necesario para aprobarla y mejorar la estructura conservando las pruebas aprobadas. **FIRST** resume pruebas rápidas, independientes, repetibles, con resultado inequívoco y escritas oportunamente.

## Síntesis

La mantenibilidad depende de responsabilidades claras, contratos útiles, estado protegido y dependencias controladas. UML permite discutir el diseño; el código y las pruebas deben conservar lo que ese diseño promete.

## Preguntas de repaso

1. **¿Qué protege un constructor?** La validez del estado inicial.
2. **¿Por qué un detalle es composición y un jugador puede ser agregación?** Por la diferencia de pertenencia y ciclo de vida conceptual.
3. **¿Sobrecarga equivale a polimorfismo por sobrescritura?** No: una cambia las entradas y la otra especializa una operación heredada.
4. **¿DRY obliga a unir cualquier código parecido?** No: debe tratarse del mismo conocimiento y razón de cambio.
5. **¿Qué conserva una refactorización?** El comportamiento observable.
6. **¿Qué comprueba FIRST?** Cualidades que hacen útiles y sostenibles las pruebas, no solo que existan.

[Volver al índice](README.md)
