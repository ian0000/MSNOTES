# Clase 03.01: Strategy y Template Method

**Unidad 03: Patrones de diseño de comportamiento.**

Fuente: [Unidad_03_01_PDS.pdf](../ClasesDiapositivas/Unidad_03_01_PDS.pdf), 21 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: introducción a patrones de comportamiento, pp. 4-6; Strategy, pp. 7-12; Template Method, pp. 13-18; comparación y cierre, p. 19.

## Idea central

Los patrones de comportamiento organizan algoritmos, responsabilidades e interacciones entre objetos. Strategy y Template Method permiten variar una parte del comportamiento sin dispersar condicionales o duplicar un proceso completo, pero ubican esa variación en lugares distintos.

**Strategy** encapsula algoritmos intercambiables en objetos colaboradores. El contexto puede cambiar de estrategia mediante composición, incluso durante la ejecución. **Template Method** define en una clase base el orden estable de un algoritmo y permite que las subclases implementen determinados pasos mediante herencia.

La pregunta clave no es solamente «¿hay varias formas de hacer algo?». Hay que decidir quién controla la variación. Si el comportamiento debe sustituirse como un objeto, Strategy suele ser apropiado. Si la secuencia completa debe permanecer fija y solo cambian algunos pasos, Template Method expresa mejor el problema.

## 1. Patrones de comportamiento

Las diapositivas distinguen tres propósitos GoF. Los patrones creacionales abstraen la creación; los estructurales organizan combinaciones de clases y objetos; los de comportamiento distribuyen algoritmos e interacciones. Esta unidad se concentra en la última categoría.

Un patrón de comportamiento evita que una sola clase concentre todas las decisiones. Algunos patrones distribuyen comportamiento principalmente con herencia y otros con composición y delegación. Template Method es un patrón de comportamiento de clase: la variación se resuelve en subclases. Strategy es un patrón de comportamiento de objetos: el contexto delega en un objeto configurado.

Aplicar un patrón no consiste en añadir clases por costumbre. Debe existir una variación real, una responsabilidad que convenga separar o una colaboración que necesite mantenerse flexible. Si una alternativa única y estable se expresa con un método sencillo, la abstracción adicional puede ser innecesaria.

## 2. Strategy: algoritmos intercambiables

### Problema del descuento universitario

El material presenta diferentes políticas de descuento por pronto pago:

- Grado aplica 10 % sobre la cuota, independientemente de cuántas cuotas se paguen.
- Posgrado aplica 15 % únicamente cuando se pagan al menos tres cuotas.
- Tecnologías aplica 10 % solo para asignaturas del primer ciclo.

Una operación única con condiciones para programa, cuotas y ciclo crecería cada vez que apareciera otra política. Además, mezclaría la selección de la política con su cálculo. Strategy separa cada algoritmo bajo un contrato común.

### Participantes y colaboración

| Participante | Responsabilidad |
|---|---|
| Context | Mantiene una referencia a `Strategy`, proporciona los datos necesarios y delega el cálculo. |
| Strategy | Declara la operación común que todas las políticas deben cumplir. |
| ConcreteStrategy | Implementa una variante concreta del algoritmo. |
| Cliente | Selecciona la estrategia y configura el contexto. |

En UML, el contexto tiene una relación hacia la interfaz Strategy. Las estrategias concretas realizan esa interfaz mediante línea discontinua y triángulo hueco. El contexto no debe depender de todas las clases concretas ni preguntar mediante `if` qué estrategia recibió: utiliza el contrato común.

### Ejemplo explicado paso a paso

El siguiente pseudocódigo es una **elaboración didáctica**, no código extraído del repositorio enlazado en las diapositivas:

```text
politica = DescuentoPosgrado()
calculadora = CalculadoraDescuento(politica)
descuento = calculadora.calcular(cuota, numeroCuotas, ciclo)
```

1. El cliente elige la política de Posgrado.
2. La proporciona al contexto `CalculadoraDescuento`.
3. El contexto recibe los datos de la operación.
4. Delega el cálculo mediante la interfaz de estrategia.
5. La estrategia de Posgrado comprueba si existen al menos tres cuotas.
6. El contexto recibe el resultado sin conocer la fórmula concreta.

Cambiar a Grado significa proporcionar otro objeto compatible; no exige modificar la calculadora. También puede cambiarse la estrategia de un contexto existente si su diseño permite sustituir la referencia.

### Cuándo aplicarlo

Strategy resulta útil cuando existen varias variantes intercambiables, varias clases difieren principalmente por un algoritmo, una clase acumula condicionales de selección o se espera añadir políticas sin modificar el contexto. Los casos de la presentación muestran recomendaciones de películas, políticas FIFO/LIFO/demanda, modalidades de pago y aceleración turbo/normal/económica.

En el caso de los vehículos, cambiar la política durante la carrera es una señal especialmente clara: la aceleración pertenece a un colaborador sustituible. Crear una subclase de vehículo por política haría rígida la combinación y dificultaría cambiarla durante la ejecución.

### Consecuencias y errores habituales

Strategy facilita extensión, pruebas aisladas y sustitución de algoritmos. A cambio, introduce más objetos y obliga al cliente o a otra parte de la configuración a saber qué estrategia seleccionar. El contexto y las estrategias también necesitan acordar qué datos intercambian; pasar demasiada información puede debilitar la separación.

No toda condición merece una estrategia. Un condicional pequeño, estable y comprensible puede ser suficiente. Tampoco debe confundirse Strategy con Factory Method: el primero encapsula comportamiento; el segundo delega la creación de un producto. Una fábrica puede seleccionar estrategias, pero eso no cambia la intención de cada patrón.

## 3. Template Method: secuencia estable, pasos variables

### Problema del cálculo de sueldos

La empresa del ejemplo siempre sigue cuatro etapas: calcular salario básico, calcular valores adicionales, calcular descuentos y obtener sueldo final. Para empleados fijos, el básico es horas por 10 dólares, existe un bono de 5 y se descuenta 9 % del salario básico por seguridad social. Para empleados temporales, el básico es horas por 12, sin bono ni ese descuento.

Duplicar las cuatro etapas en cada tipo de empleado repetiría la estructura y permitiría que una variante alterara accidentalmente el orden. Template Method mantiene una única secuencia en la clase base y delega los pasos que realmente cambian.

### Participantes y relaciones

| Participante | Responsabilidad |
|---|---|
| AbstractClass | Implementa `templateMethod()` con el orden general y declara operaciones primitivas. |
| ConcreteClass | Implementa o redefine los pasos variables necesarios. |
| Cliente | Invoca la operación plantilla mediante una instancia concreta. |

La relación UML es generalización: las clases concretas heredan de la clase abstracta. El triángulo hueco apunta hacia `AbstractClass`. La plantilla debe tener implementación; los pasos variables pueden ser abstractos. En Java suele declararse la plantilla `final` cuando el propósito es impedir que las subclases reordenen el algoritmo.

### Recorrido del algoritmo

Este pseudocódigo es una **elaboración didáctica**:

```text
calcularSueldo():
    basico = calcularSalarioBasico()
    adicionales = calcularAdicionales(basico)
    descuentos = calcularDescuentos(basico)
    return basico + adicionales - descuentos
```

1. El cliente crea un empleado fijo o temporal.
2. Invoca la misma operación `calcularSueldo()`.
3. La clase base controla el orden.
4. Las llamadas a pasos variables se resuelven en la subclase concreta.
5. La base combina los resultados y devuelve el sueldo final.

Puede haber **hooks**, operaciones con implementación predeterminada que una subclase redefine opcionalmente. Por ejemplo, `calcularAdicionales()` podría devolver cero por defecto. Un hook no debe permitir romper las invariantes del proceso.

### Casos y aplicación

Template Method conviene cuando existe un proceso general estable, algunas etapas cambian entre subclases, hay comportamiento común que debe centralizarse y se buscan puntos de extensión controlados. Las actividades incluyen registro de estudiantes, gestión de productos, generación de reportes e importación de archivos.

En la importación, la secuencia siempre valida, lee, transforma y almacena. CSV, JSON y XML cambian la lectura y transformación. La plantilla evita repetir el flujo completo. Una implementación desarrollada como **elaboración didáctica complementaria** está en [Template Method: importación de archivos](../Ejercicios/TemplateMethod-Caso04-Importacion/README.md); no constituye una solución oficial de las diapositivas.

### Consecuencias y límites

El patrón elimina duplicación del flujo y preserva su orden. Sin embargo, une las variantes mediante herencia y puede producir muchas subclases. También puede resultar rígido cuando se necesita combinar pasos independientemente o cambiar comportamiento dinámicamente.

Debe evitarse si cada variante necesita un algoritmo completamente diferente, si solo existen pocos pasos triviales o si la composición ofrece una separación más clara. Una subclase tampoco debería sobrescribir la plantilla completa: hacerlo elimina la garantía que motivó el patrón.

## 4. Strategy frente a Template Method

| Pregunta | Strategy | Template Method |
|---|---|---|
| ¿Qué varía? | Un algoritmo o política completa | Determinados pasos de una secuencia |
| Mecanismo principal | Composición y delegación | Herencia |
| ¿Puede cambiar durante la ejecución? | Sí, sustituyendo la estrategia | Normalmente no sin cambiar de objeto/clase concreta |
| ¿Quién controla el flujo? | El contexto delega el comportamiento | La clase base controla el algoritmo |
| Extensión | Nueva estrategia | Nueva subclase o redefinición de hook |
| Riesgo típico | Exceso de estrategias y configuración | Jerarquía rígida o subclases que rompen el flujo |

Ambos patrones pueden aparecer en un mismo sistema. Por ejemplo, una plantilla puede fijar las fases de procesamiento y delegar uno de sus pasos a una estrategia. Esa combinación es una posibilidad de diseño, no una exigencia de la presentación.

## Síntesis

Strategy responde: «¿qué comportamiento debe poder intercambiarse?». Template Method responde: «¿qué parte del proceso permanece estable y qué pasos pueden variar?». La primera respuesta coloca la variación en un objeto colaborador; la segunda, en subclases controladas por una plantilla. Elegir correctamente exige identificar el eje de cambio, no solo reconocer una forma UML.

## Preguntas de repaso

1. **¿Qué problema resuelve Strategy?** Encapsula variantes de un algoritmo para sustituirlas sin modificar el contexto.
2. **¿Quién selecciona normalmente la estrategia?** El cliente o un mecanismo de configuración que entrega una implementación al contexto.
3. **¿Por qué Strategy favorece el cambio en ejecución?** Porque el comportamiento está en una referencia que puede sustituirse por otro objeto compatible.
4. **¿Qué debe implementar la clase abstracta en Template Method?** La operación plantilla con el orden general del algoritmo.
5. **¿Qué implementan las subclases?** Los pasos primitivos o hooks cuya variación fue prevista por la base.
6. **¿Por qué conviene que la plantilla sea `final` en Java?** Para impedir que una subclase cambie el orden estable del proceso.
7. **¿Cuál es la diferencia esencial entre ambos patrones?** Strategy varía por composición; Template Method varía pasos mediante herencia.
8. **¿Cuándo evitar ambos?** Cuando una solución pequeña y estable resulta más clara que las abstracciones adicionales.

[Volver al índice](README.md)
