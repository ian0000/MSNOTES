# Escenario 2 · Registro de decisiones

El usuario indicó que la mayoría de las recomendaciones eran precisiones de tipo o conexión que podían resolverse en código, que no las consideraba relevantes para este UML y que las tendría en cuenta en trabajos posteriores. Por ello la captura V1 se conserva sin cambios como UML final aceptado.

| Propuesta de revisión | Elección | Justificación registrada |
|---|---|---|
| Utilizar Visitor. | Aceptada. | Las operaciones cambian con mayor frecuencia que los tipos de envío y se requiere doble despacho. |
| Añadir `void` a `aceptar` y `visitar`. | No incorporada al UML; aplicada en Java. | Se consideró una precisión de firma consultable en la implementación. |
| Tipar todos los atributos. | No incorporada al UML; aplicada en Java. | El usuario prefirió mantener un modelo conceptual y considerar el detalle en futuros diagramas. |
| Añadir valor declarado, volumen y distancia al dibujo. | No incorporada al UML; aplicada en Java. | Esos datos se necesitan para ejecutar las fórmulas ya definidas, sin cambiar el patrón. |
| Mostrar dónde se guardan el total y los resultados. | No incorporada al UML; aplicada en Java. | Cada visitante conserva su propio acumulador, visible en la implementación. |
| Cambiar `ejecutar(v: Visitante)` por `VisitanteEnvio`. | No incorporada al UML; aplicada en Java. | El código utiliza el nombre real de la interfaz para compilar. |
| Eliminar la caja `Envio` duplicada y corregir la conexión del lote. | No incorporada al UML; resuelta en Java. | Existe una sola interfaz `Envio`; `LoteEnvios` mantiene `List<Envio>`. |
| Quitar etiquetas `Use` de las realizaciones. | No incorporada al UML. | Se reservó como mejora de notación para siguientes trabajos. En Java se utiliza `implements`. |

No se cambió la lógica aceptada: el lote admite elementos heterogéneos, cada elemento acepta un visitante y cada visitante reúne una operación completa. No se utiliza selección manual mediante `instanceof`.
