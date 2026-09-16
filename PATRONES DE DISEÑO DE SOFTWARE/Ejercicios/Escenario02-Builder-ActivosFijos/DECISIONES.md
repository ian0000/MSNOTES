# Registro de decisiones · Escenario 02

Las decisiones se basan en las respuestas del grupo y en sus capturas V1 y V2. Las explicaciones son justificaciones técnicas redactadas para documentar el resultado; no se inventan motivos personales. La [comparación visual y funcional](README.md) separa lo dibujado de lo implementado.

| Propuesta | Decisión o aclaración | Justificación y resultado |
|---|---|---|
| Conservar Builder moderno | Conservado | La construcción con numerosos opcionales y pasos explícitos corresponde al patrón. No se identificó una selección equivocada ni se exigió un Director. |
| Hacer públicos los pasos de configuración | Aceptada y reflejada en V2 | El grupo reconoció que deben ser visibles para utilizarlos desde la solución. La API fluida se invoca desde el cliente, por lo que los métodos no pueden ser privados. |
| Eliminar el código institucional repetido | Aceptada y reflejada en V2 | El grupo confirmó la eliminación. La duplicación no representaba dos conceptos distintos; se mantiene un atributo y su paso de configuración. |
| Completar parámetros de los pasos | Completada al traducir a Java | No hubo respuesta individual sobre esta firma. La implementación agrega el parámetro necesario para recibir el dato indicado por cada operación, sin cambiar su responsabilidad ni su retorno. No se presenta como una aceptación textual independiente. |
| Añadir proveedor | Aclarada e incorporada por cobertura del escenario | El grupo preguntó si debía aparecer en UML. Se explicó que conviene incluirlo cuando el diagrama enumera todos los datos requeridos, pero no exige una clase propia. Se agrega un texto opcional por estar en el enunciado. No se registra como aprobación de una nueva entidad. |
| Unificar «nombre o descripción» en un campo | Propuesta modificada | El grupo eligió conservar dos campos. Se descarta la fusión para mantener esa distinción del modelo; el requisito del escenario se cumple permitiendo nombre, descripción o ambos. Esta es la justificación técnica de la elección, no un motivo personal añadido. |
| Reemplazar `?` por notación formal UML | Pospuesta como observación para siguientes trabajos | El grupo indicó que la opcionalidad se entiende y que tendrá en cuenta la notación en trabajos posteriores. Se conserva la intención del modelo y no se convierte una preferencia de notación en una exigencia de rediseño. |
| Concretar tipos de fecha y unidad de garantía | Delegada al código | El grupo consideró excesivo detenerse en esos detalles del dibujo y pidió resolverlos en la implementación. Se usa `LocalDate` y garantía en meses, sin pedir otra modificación de la captura. |
| Corregir la dirección de la flecha | Aceptada y reflejada en V2 | El grupo reconoció el error de orientación. V2 apunta del Builder al producto. El código concreta esa relación como dependencia de creación. |
| Producto inmutable y estado en el Builder | Aceptada | El producto conserva su configuración al terminar, mientras el Builder puede preparar otra construcción sin modificar productos anteriores. Se verifica esa independencia con pruebas. |
| Comparar V1 y V2 al finalizar | Solicitada expresamente | Ambas imágenes se conservan sin cambios y se documentan por separado las mejoras visibles, lo pendiente en V2 y los detalles completados en Java. |

## Alcance de las alternativas no elegidas

No se eligió fusionar nombre y descripción. Tampoco se impuso un constructor del Builder con nombre obligatorio, porque conservar los dos campos y el «o» del enunciado permite construir un activo únicamente con descripción. Se mantienen los pasos originales y se validan los obligatorios al finalizar.

No se registran como rechazos del patrón las decisiones de dejar unidades al implementador o reservar la notación `[0..1]` para próximos diagramas. No hubo un rechazo expreso de Builder.

## Detalles técnicos documentados

El precio debe estar informado y ser no negativo; cero no equivale a falta de precio. La garantía opcional distingue `null` de cero meses. No se inventan restricciones de fecha futura, unicidad de código o relaciones entre los datos técnicos. Los ejemplos usan datos ficticios y el código no persiste activos.
