# Escenario 2 · Operaciones sobre envíos heterogéneos

**Patrón seleccionado:** Visitor.

## Definición del problema y enunciado propuesto

Una empresa de mensajería administra diferentes tipos de envío. Los **documentos** registran peso, destino y si contienen información confidencial. Los **paquetes frágiles** registran peso, valor declarado y si poseen embalaje reforzado. Las **cargas refrigeradas** registran volumen, distancia de transporte y temperatura requerida.

Todos forman parte de un lote de envíos, pero las operaciones que la empresa realiza sobre ellos varían según el tipo concreto. Para calcular el costo, un documento utiliza una tarifa relacionada con el peso; un paquete frágil incorpora manipulación especial y seguro sobre el valor declarado; una carga refrigerada considera distancia, volumen y mantenimiento de la cadena de frío.

Además del cálculo de costo, el área de operaciones necesita generar una inspección. Un documento confidencial requiere custodia; un paquete frágil sin embalaje reforzado debe advertirse; una carga cuya temperatura esté fuera del intervalo admitido necesita revisión especializada. En el futuro se prevén otras operaciones transversales, como generar un manifiesto aduanero, estimar la huella ambiental o exportar información para una aseguradora.

Los tipos principales de envío son relativamente estables, mientras que las operaciones y reportes cambian con frecuencia. Se desea añadir una operación sin incorporar otro conjunto de métodos a todas las clases de envío y sin crear servicios centrales llenos de `instanceof` o `switch` por tipo.

Diseñar una solución orientada a objetos que permita ejecutar distintas operaciones sobre cada clase concreta de envío, manteniendo la jerarquía de elementos enfocada en sus propios datos. La solución debe demostrar el doble despacho: cada elemento acepta un visitante y este ejecuta la operación específica para el tipo concreto recibido.

## Contexto y responsabilidades que generan el problema

Los elementos de envío representan entidades del dominio. El cálculo de tarifas, la inspección y los futuros reportes son operaciones que atraviesan toda la jerarquía, pero no forman parte de la identidad esencial de un envío.

Las interacciones relevantes son:

1. Un cliente dispone de un lote con elementos de tipos distintos.
2. Selecciona una operación representada por un visitante concreto.
3. Recorre el lote y solicita a cada elemento que acepte al visitante.
4. El elemento llama en el visitante al método correspondiente a su clase concreta.
5. El visitante aplica la regla específica y acumula o produce un resultado.

El visitante no debe determinar el tipo mediante `instanceof`. La selección del método ocurre por la combinación de la llamada a `aceptar` y la sobrecarga `visitar` correspondiente: esta colaboración se conoce como **doble despacho**.

## Por qué una solución directa dificultaría la evolución

Una primera alternativa sería añadir `calcularCosto`, `inspeccionar`, `generarManifiesto`, `calcularHuella` y otras operaciones a cada clase de envío. Esto obliga a modificar la jerarquía por razones ajenas a sus datos y mezcla reglas de áreas diferentes dentro de las entidades.

Otra alternativa sería crear un servicio con condiciones:

```text
si elemento es Documento → aplicar regla de documento
si elemento es PaqueteFragil → aplicar regla de frágil
si elemento es CargaRefrigerada → aplicar regla refrigerada
```

Cada nueva operación repetiría la selección de tipos. Los servicios conocerían todas las clases concretas y el compilador no garantizaría fácilmente que una operación contemple todos los tipos.

Visitor agrupa una operación completa en una clase. Añadir otra operación implica crear otro visitante que implemente una visita para cada elemento existente, sin modificar esos elementos.

## Justificación del patrón

Visitor es adecuado cuando la estructura de elementos es estable y se agregan operaciones con mayor frecuencia que tipos. Permite mantener juntas las reglas de costo o inspección, aunque sean diferentes para cada elemento.

El patrón hace explícito su principal costo: añadir un nuevo tipo de envío exige modificar la interfaz Visitor y todas sus implementaciones. Esta solución se justifica solamente bajo el supuesto de que `Documento`, `PaqueteFragil` y `CargaRefrigerada` cambian menos que las operaciones.

## Participantes esperados en el dominio

Los nombres son propuestas para elaborar el UML inicial; se revisarán con la V1 enviada por el grupo.

| Participante del patrón | Correspondencia propuesta | Responsabilidad |
|---|---|---|
| Element | `ElementoEnvio` | Declarar `aceptar(visitor)`. |
| ConcreteElement | `Documento` | Guardar peso, destino y confidencialidad; dirigir la visita a `visitarDocumento`. |
| ConcreteElement | `PaqueteFragil` | Guardar peso, valor y embalaje; dirigirla a `visitarPaqueteFragil`. |
| ConcreteElement | `CargaRefrigerada` | Guardar volumen, distancia y temperatura; dirigirla a `visitarCargaRefrigerada`. |
| Visitor | `EnvioVisitor` | Declarar una visita por cada tipo concreto de elemento. |
| ConcreteVisitor | `CalculadorCostoVisitor` | Aplicar reglas de tarifa y acumular el costo. |
| ConcreteVisitor | `InspeccionEnvioVisitor` | Generar observaciones o alertas de inspección. |
| ObjectStructure | `LoteEnvios` | Mantener una colección de elementos y permitir recorrerlos. |
| Client | Programa principal o servicio logístico | Crear visitantes y aplicarlos al lote. |

## Reglas didácticas para la implementación posterior

Estas fórmulas no provienen de una empresa real; se fijan para que el código pueda verificarse de manera objetiva:

| Tipo | Regla de costo propuesta |
|---|---|
| Documento | Base de 2,00 más 0,01 por gramo. |
| Paquete frágil | 3,00 por kilogramo más seguro de 2 % del valor declarado. |
| Carga refrigerada | 0,80 por kilómetro, 0,15 por litro y 20,00 fijos por cadena de frío. |

Reglas de inspección propuestas:

- Un documento confidencial genera la observación «requiere custodia especial».
- Un paquete frágil sin embalaje reforzado genera «requiere reforzar embalaje».
- Una carga refrigerada cuya temperatura requerida sea menor que -20 °C o mayor que 8 °C genera «temperatura fuera del intervalo operativo».
- Un elemento sin advertencias genera un resultado de inspección conforme.

Los importes se representarán con un tipo decimal apropiado. Peso, distancia, volumen y temperatura deben validarse con valores coherentes. Las fórmulas podrán ajustarse antes del código si el grupo propone otras reglas en su UML o documentación final.

## Colaboración y doble despacho

Para un `Documento`, el método `aceptar(visitor)` ejecuta conceptualmente `visitor.visitarDocumento(this)`. Para un `PaqueteFragil`, ejecuta `visitor.visitarPaqueteFragil(this)`. La primera selección depende del elemento que recibe `aceptar`; la segunda identifica la sobrecarga específica del visitante.

El mismo lote puede aceptar primero un `CalculadorCostoVisitor` y después un `InspeccionEnvioVisitor`. Los elementos no contienen las fórmulas ni las reglas de inspección. Cada visitante mantiene su propio resultado: un total monetario, una lista de resultados o el modelo que se acuerde en el UML.

Es importante que el contrato Visitor incluya una operación por cada elemento concreto. Utilizar únicamente `visitar(ElementoEnvio)` y resolver con condiciones internas perdería el doble despacho que se intenta demostrar.

## Validación que deberá discutirse en la entrega final

Visitor resolverá la incorporación de operaciones transversales sin modificar continuamente las clases de envío. Mejorará la cohesión de cada operación y eliminará selecciones manuales de tipo. Como costos, introducirá interfaces, métodos de aceptación y acoplamiento del visitante al conjunto de elementos concretos.

No se recomendaría si aparecen nuevos tipos de envío con mucha frecuencia, si solo existe una operación simple o si las operaciones pertenecen naturalmente a cada entidad. Cuando la jerarquía es inestable, mantener todos los visitantes puede costar más que el beneficio obtenido.

## Preguntas para revisar el futuro UML

1. ¿Cada elemento concreto implementa `aceptar(EnvioVisitor)`?
2. ¿La interfaz visitante contiene una operación diferente para cada elemento concreto?
3. ¿Las clases de envío evitan implementar directamente costo e inspección?
4. ¿El lote mantiene una colección con multiplicidad `0..*` de `ElementoEnvio`?
5. ¿Los visitantes concretos realizan `EnvioVisitor`?
6. ¿El modelo muestra la dependencia de `ElementoEnvio` hacia el visitante y no usa `instanceof` como solución principal?
7. ¿Está explícito dónde conserva cada visitante su resultado?

[Volver al índice de Deber 3](../README.md)
