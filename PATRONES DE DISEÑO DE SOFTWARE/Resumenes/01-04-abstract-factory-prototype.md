# Clase 01.04: Abstract Factory y Prototype

**Unidad 01: Patrones de diseño creacionales.**

Fuente: [Unidad_01_04_PDS.pdf](../ClasesDiapositivas/Unidad_01_04_PDS.pdf), 18 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: Abstract Factory, pp. 4–8; Prototype, pp. 10–14; comparación final, p. 15.

## Idea central

Abstract Factory permite crear una **familia coherente de productos** usando contratos comunes. Prototype permite crear un objeto **copiando una instancia configurada**. Ambos reducen el conocimiento de clases concretas en el cliente, pero reutilizan cosas distintas: uno reutiliza una organización de creación; el otro, un estado inicial existente.

La elección depende del problema. Si se necesitan varios productos compatibles entre sí, conviene analizar familias. Si lo que se repite es una configuración compleja, conviene analizar copias y qué datos pueden compartirse.

## 1. Abstract Factory: crear familias completas

### Problema del hospital

El hospital necesita médicos y colaboradores con modalidades de contratación fija o temporal. Hay dos ejes: **tipo de producto**, médico o colaborador, y **familia**, fija o temporal. La configuración seleccionada debe producir elementos de la misma familia.

Si cada cliente construye por separado los productos, tiene que conocer todas las clases y recordar qué combinaciones son coherentes. Abstract Factory coloca esa decisión en una fábrica que ofrece una operación por tipo de producto.

| Familia seleccionada | Producto médico | Producto colaborador |
|---|---|---|
| Fija | `MedicoFijo` | `ColaboradorFijo` |
| Temporal | `MedicoTemporal` | `ColaboradorTemporal` |

La coherencia se refiere al conjunto que se desea construir en ese contexto. El ejemplo no implica que un hospital real no pueda tener simultáneamente trabajadores de modalidades distintas.

### Participantes y UML

**AbstractFactory** declara las operaciones de creación. El diagrama utiliza `ContratoAbstractFactory`, con `crearMedico()` y `crearColaborador()`.

**ConcreteFactory** implementa una familia. `FijoFactory` crea productos fijos y `TemporalFactory` crea productos temporales.

**AbstractProduct** es el contrato de cada tipo de producto. Aquí hay dos: `Medico` y `Colaborador`. Cada uno permite utilizar sus variantes mediante una abstracción común.

**ConcreteProduct** implementa un producto de una familia determinada. El cliente depende de los contratos de fábrica y productos; los triángulos del diagrama representan las especializaciones, mientras las dependencias muestran qué abstracciones utiliza el cliente.

### Ejemplo paso a paso

1. La configuración elige `FijoFactory`.
2. El cliente solicita un médico mediante `crearMedico()`.
3. Solicita un colaborador mediante `crearColaborador()` sobre la misma fábrica.
4. La fábrica entrega `MedicoFijo` y `ColaboradorFijo`.
5. El cliente usa las operaciones de `Medico` y `Colaborador` sin incorporar decisiones sobre sus clases concretas.
6. Para trabajar con la familia temporal, se proporciona `TemporalFactory` y se repite el flujo.

El patrón permite cambiar la familia usada en nuevas creaciones. No convierte automáticamente los objetos ya existentes a otra modalidad. Tampoco impide por arte de magia que otro código construya productos incompatibles: la coherencia depende de utilizar la fábrica seleccionada de forma consistente.

### Aplicabilidad, beneficios y costos

La presentación propone interfaces gráficas de Windows o macOS, equipamiento medieval o futurista, servicios de distintos proveedores de nube y mobiliario clásico o moderno. En todos los casos hay varios tipos de productos que deben combinarse según una familia.

El beneficio es centralizar la selección y ocultar las clases concretas. Añadir otra familia permite conservar el contrato si se mantienen los mismos tipos de producto.

Como análisis didáctico de sus consecuencias, añadir un **nuevo tipo de producto** suele ser más costoso: incorporar una operación como `crearOtroProfesional()` exige revisar el contrato y sus fábricas. Hay que distinguir ese cambio de añadir otra familia con los tipos ya previstos.

El patrón también incrementa la cantidad de interfaces y clases. Si solo se crea un objeto sencillo y no existe una familia relevante, una solución más pequeña puede ser suficiente. La mera presencia de muchos constructores no demuestra que Abstract Factory sea necesario.

## 2. Prototype: crear a partir de una instancia existente

### Problema de los jugadores

El ejemplo utiliza jugadores que comparten nacionalidad ecuatoriana, camiseta amarilla y selección Ecuador. Configurar esos datos repetidamente es redundante. Se prepara un jugador prototipo y se solicitan copias; después se personalizan nombre, número o posición.

Un **prototipo** es una instancia usada como punto de partida. No debe confundirse con un prototipo de interfaz de usuario ni con una maqueta de aplicación. Aquí se trata de un objeto cuyo estado permite crear otros objetos.

### Participantes y colaboración

- **Prototype:** declara la operación de copia, como `clonar()`.
- **ConcretePrototype:** conoce cómo duplicar el estado que representa.
- **Cliente:** conserva o recibe un prototipo y solicita nuevas instancias a través de su contrato.

En el diagrama, `Jugador` declara `clonar(): Jugador`, y las variantes de selecciones especializan esa operación. El cliente depende de `Jugador`; no necesita reconstruir desde fuera los detalles de cada variante.

La presencia de esas subclases es parte del ejemplo. La intención del patrón es copiar una instancia configurada; no obliga a que cualquier aplicación tenga una subclase por cada configuración imaginable.

### Ejemplo paso a paso

1. Se prepara un prototipo con los atributos comunes de la selección.
2. El cliente solicita una copia mediante `clonar()`.
3. La implementación crea un objeto distinto con el estado inicial acordado.
4. Se personalizan los atributos propios del nuevo jugador.
5. Se comprueba que modificar la copia no altere indebidamente al prototipo ni a otras copias.

Asignar una referencia a otra variable no cumple este proceso. Como elaboración didáctica, `nuevo = prototipo` hace que ambas variables apunten al mismo objeto; no crea una instancia independiente. Esa diferencia entre identidad y valores es fundamental para entender Prototype.

## 3. Copia superficial y profunda

La presentación exige decidir qué estado se duplica y qué referencias se comparten, especialmente cuando hay objetos internos mutables. Las siguientes categorías desarrollan esa precaución.

Una **copia superficial** crea un objeto exterior nuevo, pero conserva referencias a algunos objetos internos. Una **copia profunda** duplica también las partes internas que deben ser independientes. La estrategia correcta depende del contrato de copia, no del nombre del método.

**Ejemplo complementario didáctico:** supongamos que el jugador contiene una lista mutable de habilidades. Si la copia conserva la misma lista y se añade una habilidad, el prototipo también observará el cambio. Crear una nueva lista separa el contenedor; si sus elementos son objetos mutables compartidos, todavía hay que decidir si también deben copiarse.

| Situación | Decisión que debe justificarse |
|---|---|
| Valor inmutable compartido | Puede compartirse sin modificaciones internas inesperadas |
| Colección mutable | Determinar si cada copia necesita su propio contenedor |
| Elementos mutables de una colección | Decidir si compartirlos conserva la independencia requerida |
| Referencia a un recurso externo | Definir explícitamente su tratamiento; copiar datos no duplica el recurso |

Copiar todo indiscriminadamente puede ser costoso o incorrecto. Compartirlo todo puede generar cambios cruzados. Por eso la operación debe expresar qué significa obtener una nueva instancia en ese dominio y conservar las reglas que correspondan.

## 4. Cuándo aporta valor Prototype

Los casos de clase incluyen personajes con habilidades y equipamiento inicial, documentos con secciones preconfiguradas, configuraciones de servidores y bicicletas personalizables. La característica común es disponer de una base ya configurada que conviene reutilizar.

También resulta útil cuando las clases o configuraciones se eligen durante la ejecución y se quiere evitar una jerarquía adicional de fábricas. El cliente solicita una copia sin conocer todas las decisiones necesarias para reconstruir el objeto.

Sus costos incluyen implementar y mantener la copia cuando cambia la estructura interna. No debe afirmarse que clonar siempre es más rápido: una copia compleja puede resultar costosa. El beneficio principal debe justificarse por la reutilización de configuración y la reducción de dependencias, y cualquier ventaja de rendimiento necesitaría medición.

## 5. Comparación con otros creacionales

| Patrón | Punto de partida | Decisión que organiza |
|---|---|---|
| Factory Method | Creador especializado | Qué producto concreto instanciar |
| Abstract Factory | Fábrica de una familia | Qué conjunto coherente de productos crear |
| Builder | Proceso y partes | Cómo construir progresivamente un producto |
| Prototype | Instancia configurada | Cómo obtener otra instancia desde su estado |

Abstract Factory y Factory Method pueden colaborar, pero no son sinónimos. Una fábrica de familias ofrece varias operaciones de creación relacionadas; Factory Method especializa una decisión mediante subclases.

Builder y Prototype también pueden dar objetos personalizados. La diferencia está en el mecanismo: Builder ensambla pasos; Prototype reutiliza el estado de una instancia. Un constructor directo sigue siendo válido cuando configurar el objeto no presenta dificultad especial.

## Síntesis

Abstract Factory ayuda a mantener coherencia entre productos relacionados. Prototype reutiliza configuraciones mediante copias cuyo alcance debe definirse. En ambos casos, el cliente gana independencia a cambio de una responsabilidad de creación más explícita.

## Preguntas de repaso

1. **¿Qué diferencia una familia de un tipo de producto?** La familia agrupa variantes compatibles; médico y colaborador son tipos de producto dentro de ella.
2. **¿Cambiar la fábrica transforma objetos existentes?** No: determina qué se crea a partir de esa selección.
3. **¿Qué cambio puede afectar a todas las fábricas?** Añadir un nuevo tipo de producto al contrato común.
4. **¿Asignar una referencia equivale a clonar?** No: sigue siendo el mismo objeto.
5. **¿Una nueva lista asegura independencia completa?** No si sus elementos mutables continúan compartidos y deberían ser independientes.
6. **¿Cuándo elegir Prototype frente a Builder?** Cuando conviene reutilizar una instancia configurada, en lugar de reconstruir sus partes paso a paso.

[Volver al índice](README.md)
