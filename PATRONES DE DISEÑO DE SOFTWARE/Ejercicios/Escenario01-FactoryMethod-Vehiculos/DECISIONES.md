# Registro de decisiones · Escenario 01

Este registro distingue lo respondido por el grupo de la justificación técnica redactada para explicar la solución. No atribuye motivos personales adicionales ni presenta alternativas no seleccionadas como rechazos expresos. Las capturas originales se comparan en el [README](README.md).

| Propuesta | Decisión del grupo | Justificación y resultado |
|---|---|---|
| Conservar Factory Method | Conservado | El escenario requiere delegar la creación a variantes y trabajar con un producto común. No se identificó un error en la selección del patrón. |
| Usar `costoMatricula()` sin parámetros en todas las clases | Aceptada, con aclaración | El grupo consideró razonable que el cálculo tome los valores del propio objeto. Se aclaró que aquí los datos están en el vehículo construido mediante Factory Method, no en un Builder. Las firmas idénticas permiten sobrescritura y polimorfismo. V2 aún no refleja este cambio. |
| Corregir los creadores duplicados | Aceptada y reflejada en V2 | El grupo explicó que los nombres repetidos provenían de duplicar cajas sin renombrarlas. Se conservan `CreadorVehiculoAuto`, `CreadorVehiculoCamioneta` y `CreadorVehiculoCamion`. |
| Sustituir tipos de base de datos por tipos Java | Aceptada | El grupo reconoció la mezcla de notaciones. V2 cambia los textos a `String`; Java utiliza además `int` para años y `BigDecimal` para dinero. |
| Eliminar `field: type` | Aceptada como limpieza | El grupo indicó que eran campos generados por la herramienta y no parte del modelo. No se implementan. No se considera un cambio de lógica. |
| Precisar unidades y fórmulas | Delegada a la implementación | El grupo autorizó que el implementador las defina y no consideró necesario cambiarlas en el UML. Se documentan fórmulas académicas, unidades, redondeo y ejemplos; no se presentan como reglas del enunciado ni tarifas reales. |
| Incorporar un cliente visible en UML | No exigido en el dibujo; implementación delegada | El grupo dejó la elección al implementador y preguntó si era necesario mostrarlo. Se aclaró que puede omitirse en el diagrama resumido. El código sí demuestra selección durante la ejecución y uso del contrato común. |
| Decidir cómo reciben datos los creadores | Aceptada después de aclaración | Al principio el grupo pidió explicar la propuesta. Tras comparar las dos alternativas, eligió entregar datos al inicializar el creador y mantener `creadorVehiculo()` sin parámetros. Esto conserva la firma del UML y permite que cada creador prepare su producto con datos completos. |
| Pasar los datos como argumento de `creadorVehiculo(datos)` | Alternativa no elegida | Se eligió configurar el creador mediante su constructor. No se registra esta opción como un rechazo conceptual al patrón: ambas formas pueden implementar Factory Method, pero la elegida conserva la firma del grupo. |
| Comparar V1 y V2 en la revisión final | Solicitada expresamente | Se conservan ambas capturas, se describen cambios observados y se separa el esquema propuesto para Java de la V2 del grupo. |

## Detalles resueltos por el implementador

Se conserva el nombre `creadorVehiculo()` para mantener correspondencia con el modelo. El año de cálculo se recibe como dato explícito para hacer reproducible la antigüedad. El cliente registra objetos en memoria y no impone unicidad de placa. La selección de creadores se concentra en una configuración; no se dispersan instanciaciones de productos en el flujo principal.

Las validaciones de textos, años, avalúo y medidas, las fórmulas y el redondeo se documentan como decisiones de esta implementación académica. No hubo rechazo expreso de Factory Method ni autorización para sustituir el escenario por otro patrón.
