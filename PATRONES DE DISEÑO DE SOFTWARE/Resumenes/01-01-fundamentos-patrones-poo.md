# Clase 01.01: Fundamentos de patrones, POO y UML

**Unidad 01: Patrones de diseño creacionales.**

Fuente: [Unidad_01_01_PDS.pdf](../ClasesDiapositivas/Unidad_01_01_PDS.pdf), 44 diapositivas.
Material de Mauricio Ortiz Ochoa. Referencias de lectura: organización, pp. 3–9; patrones, pp.
10–22; POO y UML, pp. 23–35; actividad, pp. 36–39.

## Idea central

Un patrón de diseño conserva una solución general que ha resultado útil frente a un problema
recurrente. Para aplicarlo correctamente hay que comprender el contexto, las restricciones y las
consecuencias. Memorizar el nombre o reproducir un diagrama no demuestra que la solución sea
adecuada para un sistema concreto.

La clase conecta esta idea con la programación orientada a objetos (POO) y UML: los objetos permiten
implementar responsabilidades y colaboraciones; los diagramas permiten discutirlas antes de
convertirlas en código.

## 1. Propósito de la asignatura

El curso busca aprender a seleccionar, modelar e implementar patrones de acuerdo con necesidades de
diseño. Sus resultados de aprendizaje recorren tres familias: creacionales, estructurales y de
comportamiento. La primera controla la creación; la segunda organiza las relaciones; la tercera
distribuye responsabilidades e interacciones.

La inteligencia artificial aparece como apoyo al análisis, la implementación y la revisión. El
estudiante conserva la responsabilidad de definir el problema, evaluar alternativas y justificar
decisiones. Una respuesta generada puede parecer correcta y, aun así, introducir reglas que el
negocio nunca solicitó.

El cronograma organiza los contenidos progresivamente: fundamentos y diseño de clases, patrones
creacionales, SOLID, patrones estructurales y, después, patrones de comportamiento. Los anexos
presentan bibliografía y herramientas como Java, un IDE, diagramación UML y repositorios. Son
recursos de apoyo; el objetivo conceptual es aprender a razonar sobre el diseño.

## 2. Qué es un patrón y por qué depende del contexto

Las diapositivas introducen bolsas inflables para helicópteros, un vehículo de exploración de Marte
y automóviles. Comparten la idea de amortiguar un impacto, pero las condiciones y consecuencias
cambian: aerodinámica, masa adicional, rebotes o sustitución del dispositivo después de usarlo.

La lección es que reutilizar una solución exige adaptarla. En software sucede lo mismo: un mecanismo
que reduce dependencias puede añadir clases, configuración y dificultad de lectura. Hay que evaluar
si el beneficio compensa ese costo.

Un patrón describe:

- **Contexto:** situación en la que aparece el problema.
- **Problema:** dificultad recurrente que se quiere resolver.
  - **Fuerzas:** necesidades y restricciones que compiten, como flexibilidad y simplicidad.
- **Solución:** responsabilidades, elementos y colaboraciones propuestos.
- **Consecuencias:** beneficios, limitaciones y compromisos que resultan de aplicarlo.

Una solución aislada no se convierte automáticamente en patrón. El valor está en capturar
experiencia reutilizable y explicar cuándo funciona. Tampoco es un algoritmo listo para pegar: un
algoritmo detalla pasos para una operación; el patrón describe una organización de diseño que admite
implementaciones diferentes.

## 3. Origen y documentación GoF

El material sitúa el origen del lenguaje de patrones en la arquitectura y el urbanismo, con
Christopher Alexander y sus colaboradores. Posteriormente, Beck y Cunningham trasladaron estas ideas
al software. Gamma, Helm, Johnson y Vlissides, conocidos como **Gang of Four (GoF)**, consolidaron
un catálogo de 23 patrones de diseño orientado a objetos.

GoF resume cuatro elementos esenciales: nombre, problema, solución y consecuencias. La plantilla
ampliada de la diapositiva 20 añade propósito, nombres alternativos, motivación, aplicabilidad,
estructura, participantes, colaboraciones, implementación, ejemplo de código, usos conocidos y
patrones relacionados.

Para estudiar, conviene separar **participantes** de **colaboraciones**. Los primeros indican quién
interviene y qué responsabilidad tiene; las segundas explican cómo cooperan. Un dibujo con clases
sin explicar las llamadas entre ellas deja incompleta la comprensión del patrón.

| Familia           | Cantidad en GoF | Pregunta que ayuda a reconocerla                 |
| ----------------- | --------------: | ------------------------------------------------ |
| Creacionales      |               5 | ¿Quién decide cómo se crean los objetos?         |
| Estructurales     |               7 | ¿Cómo se relacionan y componen clases y objetos? |
| De comportamiento |              11 | ¿Cómo colaboran y distribuyen el trabajo?        |

Estas categorías ayudan a buscar alternativas; no sustituyen el análisis del problema. Agregar un
patrón sin una necesidad concreta puede producir sobrediseño.

## 4. Objetos, clases y abstracción

Un **objeto** es una entidad relevante para el sistema, con estado, comportamiento e identidad. El
estado son sus datos actuales; el comportamiento son las operaciones que realiza; la identidad
permite distinguirlo de otros objetos incluso si algunos valores coinciden.

Una **clase** define la estructura y el comportamiento comunes de sus instancias. En el ejemplo
visual, `Persona` contiene `nombre`, `edad` y `peso`, y ofrece `saludar()`. El diagrama separa
nombre, atributos y operaciones en compartimentos; el código Java expresa esa misma organización
mediante una clase, campos y métodos.

**Abstraer** significa seleccionar las características relevantes para un propósito. No consiste en
reunir todos los datos posibles. Como elaboración didáctica, una persona puede representarse
mediante su historial académico en una matrícula, o mediante sus datos de entrega en una tienda. El
contexto decide qué información interesa.

Un modelo útil también asigna comportamiento: si un pedido conoce sus cantidades y precios, tiene
sentido analizar si le corresponde calcular su total. Convertir todas las clases en contenedores de
datos y concentrar toda la lógica en una sola clase puede ocultar las responsabilidades del dominio.

## 5. Cómo leer las relaciones UML

Una **asociación** representa una relación estructural: los objetos necesitan conocerse o colaborar.
La relación `Cliente realiza Pedido` puede incorporar roles, multiplicidades y navegabilidad.

La **multiplicidad** expresa cuántas instancias pueden relacionarse con una instancia del extremo
opuesto:

| Notación     | Significado                 |
| ------------ | --------------------------- |
| `1`          | Exactamente una             |
| `0..1`       | Ninguna o una               |
| `0..*` o `*` | Ninguna o varias            |
| `1..*`       | Una o varias                |
| `m..n`       | Entre un mínimo y un máximo |

En la diapositiva 29, el `1` junto a `Cliente` significa que cada pedido pertenece a un cliente; el
`0..*` junto a `Pedido` permite que un cliente todavía no tenga pedidos. Leer ambos extremos evita
invertir la regla.

La **navegabilidad** indica desde qué objeto se puede acceder a otro. Una flecha hacia `Pedido`
señala que `Cliente` lo conoce; una colección permite conocer varios pedidos. Esta flecha no
demuestra por sí sola quién construye el objeto ni quién controla su vida.

Una **dependencia** indica que un elemento utiliza otro, por ejemplo como parámetro o variable
local. La diapositiva 31 muestra un `Reporte` que recibe un `ExportadorPDF` para exportar. La línea
discontinua apunta al elemento utilizado. Una asociación suele implicar una referencia mantenida;
una dependencia puede limitarse a una operación.

## 6. Encapsulación, visibilidad y herencia

La **encapsulación** protege el estado y establece operaciones válidas para modificarlo. No basta
con declarar campos privados si después un setter acepta cualquier valor. El ejemplo de `Persona`
rechaza una edad negativa antes de asignarla: la validación hace efectiva la protección.

La visibilidad UML utiliza `+` para público, `-` para privado y `#` para protegido. En Java, el
acceso de paquete se obtiene al omitir el modificador. Como precisión de lenguaje, `protected`
también permite acceso desde el mismo paquete; el acceso desde subclases de otros paquetes tiene
condiciones específicas.

La **herencia** representa una generalización: una subclase es una especialización del tipo base. El
diagrama utiliza un triángulo vacío dirigido a la superclase. `Empleado` y `Estudiante` especializan
`Persona`; comparten características y añaden información propia.

La justificación debe ser conceptual y respetar lo que espera el cliente del tipo base. Tener campos
parecidos no basta para establecer herencia. También conviene recordar que heredar estado privado no
permite a la subclase acceder directamente a esos campos.

## 7. Caso de la tienda: razonamiento guiado

La actividad pide clientes, productos, pedidos y pagos. El siguiente desarrollo es una **propuesta
didáctica sobre el enunciado**, no una solución oficial:

1. Identificar los conceptos y asignar al pedido el cálculo de su total y el control de sus cambios
   de estado.
2. Relacionar cada pedido con un cliente y permitir varios pedidos por cliente.
3. Representar producto y cantidad de forma conjunta. Introducir un detalle de pedido es una
   alternativa para no confundir la cantidad comprada con una propiedad del producto.
4. Expresar el pago mediante un comportamiento común y variantes para tarjeta y transferencia,
   justificando la relación elegida.
5. Proteger cantidades y cambios de estado con operaciones que comprueben las reglas acordadas.

El grupo debe elaborar primero su propuesta y después usar IA para revisarla. La evidencia
solicitada incluye diagrama, operación con validación, prompt y una recomendación aceptada y otra
rechazada o modificada.

## Síntesis

Los patrones reutilizan experiencia; POO organiza responsabilidades; UML comunica decisiones. Un
buen modelo permite explicar qué representa cada elemento, por qué existe cada relación y qué
consecuencias tiene la solución.

## Preguntas de repaso

1. **¿Qué diferencia aplicar un patrón de copiar código?** Aplicarlo exige relacionar problema,
   contexto y consecuencias; copiar código puede ignorarlos.
2. **¿Dónde se lee cuántos pedidos puede tener un cliente?** En la multiplicidad del extremo
   `Pedido`.
3. **¿Una flecha navegable significa que se crea el objeto destino?** No necesariamente; expresa
   acceso o conocimiento.
4. **¿Campos privados garantizan encapsulación?** No: las operaciones también deben proteger las
   reglas del objeto.
5. **¿Cuándo se justifica una herencia?** Cuando existe una especialización válida que respeta las
   expectativas del tipo base.

[Volver al índice](README.md)
