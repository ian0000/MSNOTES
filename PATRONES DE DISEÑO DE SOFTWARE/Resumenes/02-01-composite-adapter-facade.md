# Clase 02.01: Composite, Adapter y Facade

**Unidad 02: Patrones de diseño estructurales.**

Fuente: [Unidad_02_01_PDS.pdf](../ClasesDiapositivas/Unidad_02_01_PDS.pdf), 28 diapositivas. Material de Mauricio Ortiz Ochoa. Referencias de lectura: introducción, pp. 4–6; Composite, pp. 8–12; Adapter, pp. 14–18; Facade, pp. 20–24.

## Idea central

Los patrones estructurales organizan relaciones entre clases y objetos. Composite permite tratar uniformemente partes y conjuntos; Adapter traduce una interfaz existente al contrato que espera el cliente; Facade ofrece un acceso más simple a un subsistema.

La pregunta ya no es cómo crear objetos, sino cómo relacionarlos para reducir dependencias y facilitar cambios. Las nuevas capas también tienen costos: más objetos, indirecciones y contratos que comprender.

## 1. Panorama de los patrones estructurales

Los patrones estructurales de clases emplean principalmente herencia; los de objetos emplean composición y delegación. **Delegar** significa encargar parte del trabajo a otro objeto manteniendo responsabilidades diferenciadas.

La presentación sitúa los tres patrones de esta clase dentro de un catálogo de siete. Bridge separa dimensiones de variación; Decorator añade responsabilidades combinables; Flyweight comparte estado común para reducir memoria; Proxy proporciona un sustituto que controla o gestiona el acceso. Esas menciones sirven como mapa del catálogo, no como desarrollo completo de los patrones restantes.

Para reconocer el problema conviene preguntar si se necesita una jerarquía parte–todo, una traducción de interfaz o una entrada de alto nivel. Los tres pueden contener referencias a otros objetos, pero esa semejanza estructural no los vuelve equivalentes.

## 2. Composite: partes y conjuntos con un contrato común

### Problema del editor gráfico

Un editor contiene líneas, círculos y textos, pero también dibujos formados por varias figuras. Un dibujo puede incluir otros dibujos. Todos necesitan operaciones comunes, como dibujarse y calcular su tamaño en bytes.

Si el cliente distingue continuamente entre una figura y una colección, el recorrido de la estructura invade su lógica. Composite permite tratar ambos a través de una abstracción común y desplazar el recorrido a los elementos compuestos.

### Participantes y relaciones UML

- **Component:** contrato común; en el ejemplo, `FiguraComponent`.
- **Hoja:** elemento individual sin hijos, como `Linea`, `Circulo` o `Texto`.
- **Composite:** elemento compuesto, como `Dibujo`, que contiene otros componentes.
- **Cliente:** utiliza las operaciones de la abstracción sin distinguir cada clase concreta.

En el diagrama, hojas y dibujos especializan `FiguraComponent`. `Dibujo` mantiene una colección de esa misma abstracción: por eso puede contener tanto hojas como dibujos. Las operaciones `add` y `delete` se muestran en el compuesto, mientras `dibujar()` y `getPeso()` son comunes.

La relación recursiva se reconoce porque el compuesto pertenece al tipo de componentes que puede contener. **Recursión** significa que la misma operación puede volver a aplicarse a una estructura de igual naturaleza, hasta llegar a una hoja.

### Recorrido explicado

1. Se construye un dibujo principal con una línea y un dibujo secundario.
2. El dibujo secundario contiene un círculo y un texto.
3. El cliente invoca `dibujar()` sobre el dibujo principal como `FiguraComponent`.
4. El dibujo principal delega en la línea y en el dibujo secundario.
5. El secundario repite la delegación en el círculo y el texto.
6. Las hojas ejecutan directamente su comportamiento.

Para el tamaño se sigue un recorrido semejante. **Ejemplo numérico didáctico:** si las hojas pesan 10, 20 y 30 bytes, la suma es 60 bytes, suponiendo que el modelo no añade sobrecosto al contenedor. Es una ilustración del recorrido, no una medida del código del profesor.

### Aplicabilidad y consecuencias

Los casos de procesos universitarios, tareas compuestas, archivos y carpetas, y menús comparten jerarquías recursivas. Composite aporta uniformidad y simplifica al cliente.

No toda lista justifica el patrón: debe existir una operación significativa para hojas y compuestos. Como precaución didáctica, si se permite que un dibujo se contenga a sí mismo, el recorrido puede no terminar. También hay que decidir cómo se agregan resultados y qué restricciones tienen los hijos.

Declarar operaciones de gestión de hijos en el contrato común puede hacer uniforme la interfaz, pero deja operaciones sin sentido para algunas hojas. Colocarlas solo en el compuesto, como en el diagrama, evita obligar a una línea a «agregar hijos». Es una decisión de diseño, no un requisito único del patrón.

## 3. Adapter: traducir interfaces incompatibles

### Problema de las consultas bancarias

La universidad necesita consultar si una cuota está pagada. Cada banco ofrece operaciones o parámetros diferentes. El sistema interno no debería incorporar esas diferencias en todos sus clientes ni modificar los módulos de terceros.

Adapter introduce una traducción entre el contrato esperado y la interfaz existente. El componente externo ya resuelve una necesidad útil; lo incompatible es la forma de solicitarla.

| Participante | Función | Ejemplo del material |
|---|---|---|
| Target | Contrato que usa el cliente | `PagoBanco` |
| Adaptee | Componente existente | `BancoPichincha`, `BancoPacifico` |
| Adapter | Traduce y delega | `PichinchaAdapter`, `PacificoAdapter` |
| Cliente | Solicita la consulta común | Cliente que verifica la cuota |

El adaptador de objetos implementa el contrato y mantiene una referencia al componente adaptado. En UML son relaciones diferentes: realización hacia la interfaz y asociación hacia el objeto externo.

### Ejemplo explicado con el diagrama

El contrato común es `pagado(id, cuota)`. El módulo de Pichincha ofrece `obtienePago(id, cuota)`, mientras el de Pacífico muestra `pagado(cuota, id)`.

1. El cliente solicita `pagado(id, cuota)` sin conocer el banco concreto.
2. Si recibe `PichinchaAdapter`, este delega en `obtienePago(id, cuota)`.
3. Si recibe `PacificoAdapter`, este reordena los argumentos para llamar a `pagado(cuota, id)`.
4. El adaptador devuelve el resultado mediante el contrato esperado.

El ejemplo muestra que adaptar no siempre requiere una transformación complicada. Renombrar operaciones o reordenar parámetros puede ser suficiente, siempre que se preserve el significado.

### Aplicabilidad y consecuencias

Los ejercicios incluyen autenticación, pagos, correo y traducción con proveedores diferentes. Adapter permite un contrato propio y aísla cambios de bibliotecas, APIs o sistemas heredados.

El costo es mantener traducciones y verificar que los resultados sigan significando lo mismo. Como ampliación didáctica, un fallo al consultar un banco no debería convertirse silenciosamente en «cuota impaga»: adaptar implica preservar la semántica de errores y estados, además de las firmas.

Puede ser innecesario si ambos componentes pueden modificarse fácilmente para acordar una interfaz común. Tampoco conviene llenarlo de reglas comerciales ajenas a la integración; su responsabilidad principal es la adaptación.

## 4. Facade: una entrada simple a un subsistema

### Problema de la información de personas

La universidad mantiene módulos separados para estudiantes, docentes, colaboradores y proveedores. Un cliente que busca una persona tendría que conocer las operaciones y detalles de esos módulos.

Facade ofrece una operación de alto nivel y coordina el acceso interno. El cliente expresa qué necesita; la fachada conoce qué componentes deben intervenir.

Los participantes son la **fachada**, las **clases del subsistema** y el **cliente**. La fachada conoce al subsistema; los componentes internos no necesitan conocer la fachada. Tampoco es obligatorio que exista una interfaz común entre todos ellos.

### Ejemplo explicado

El diagrama presenta `PersonaFacade`, con referencias a `EstudianteService`, `DocenteService`, `ColaboradorService` y `ProveedorService`. Ofrece `buscarPersona(id): InfoPersona`.

1. El cliente envía el identificador a la fachada.
2. La fachada coordina las consultas a los servicios adecuados.
3. Cada servicio realiza su búsqueda y conserva su responsabilidad especializada.
4. La fachada devuelve la información mediante la operación de alto nivel.

El diagrama no define una política completa para coincidencias múltiples ni errores. Por ello no debe suponerse un orden de prioridad oficial entre estudiantes, docentes y otros roles. Si se implementara el caso, habría que acordar esas reglas.

### Aplicabilidad y consecuencias

Los ejercicios de reserva de vuelos, imágenes, pagos y matrícula reúnen varias operaciones detrás de una entrada más simple. Por ejemplo, matricular puede requerir revisar requisitos, obligaciones financieras y cupos antes de registrar asignaturas.

La fachada reduce lo que el consumidor debe conocer y evita repetir la coordinación habitual. Las operaciones especializadas siguen perteneciendo a los componentes internos. Un cliente avanzado puede acceder directamente al subsistema cuando resulte necesario.

El riesgo es crear una clase que absorba todas las reglas del sistema. Una fachada con responsabilidades ilimitadas pierde claridad. Como precisión didáctica, coordinar varias llamadas tampoco garantiza una transacción atómica ni deshace automáticamente pasos previos cuando algo falla; esas políticas requieren un diseño adicional.

## 5. Comparación y errores frecuentes

| Patrón | Problema principal | Qué gana el cliente |
|---|---|---|
| Composite | Partes y conjuntos en una jerarquía | Una operación uniforme sobre ambos |
| Adapter | Interfaz existente incompatible | Un contrato que ya puede utilizar |
| Facade | Subsistema con varias entradas y pasos | Una operación de alto nivel más simple |

Adapter traduce; Facade simplifica y coordina. Una fachada puede utilizar adaptadores internamente, porque son responsabilidades compatibles. Composite, en cambio, organiza una estructura recursiva y distribuye operaciones entre sus elementos.

No basta con que una clase «envuelva» a otra para identificar el patrón. Hay que explicar la intención, el contrato que conserva o transforma, y la colaboración concreta que propone.

## Síntesis

Composite organiza una jerarquía; Adapter resuelve incompatibilidad; Facade reduce conocimiento del subsistema. Su utilidad depende de identificar un problema estructural real y mantener delimitada la responsabilidad añadida.

## Preguntas de repaso

1. **¿Qué permite que un dibujo contenga otros dibujos?** Que el compuesto también sea un componente del contrato común.
2. **¿Cualquier colección es Composite?** No: hacen falta una jerarquía parte–todo y operaciones uniformes con sentido.
3. **¿Quién conoce la interfaz original del banco?** El adaptador correspondiente.
4. **¿Adapter y Facade cumplen el mismo objetivo?** No: uno traduce una interfaz y la otra simplifica el acceso al subsistema.
5. **¿La fachada realiza todo el trabajo?** Coordina; los componentes especializados mantienen la funcionalidad real.
6. **¿Una fachada garantiza atomicidad?** No: esa propiedad requiere decisiones específicas del proceso.

[Volver al índice](README.md)
