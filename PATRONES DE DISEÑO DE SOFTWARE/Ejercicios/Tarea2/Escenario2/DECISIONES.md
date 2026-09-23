# Escenario 2 · Registro de decisiones

La confirmación final fue: **«si me parecen bien acepto»**, después de presentar ambos UML corregidos y preguntar por la regla de primera entrada/última salida. Se implementa esa aceptación. Las justificaciones siguientes son técnicas; no se atribuyen motivaciones personales no expresadas.

| Propuesta | Evolución y elección | Justificación técnica |
|---|---|---|
| Adapter | Se conserva el patrón elegido. | Permite integrar una biblioteca incompatible manteniendo estable el contrato institucional. |
| Separar contrato y datos | Primero se pidió aclaración. V2 renombró la interfaz; V3 añadió una clase sin completar la separación. La propuesta final fue aceptada. | `Contrato` realiza la consulta y `RegistroAsistencia` representa su resultado, sin consultar al proveedor. |
| Firma común | Aceptada con «si tienes razon». Las capturas posteriores mostraban texto cortado; el código y el UML editable completan los tipos. | Una implementación de interfaz debe respetar sus parámetros y retorno. |
| Nombres distintos de proveedores y adaptadores | Aceptada con «es verdad» y corregida desde V2. | Distingue la biblioteca adaptada del objeto que la envuelve. |
| Operación propia y colección de marcaciones | Se preguntó si era detalle de código. La propuesta completa fue aceptada. | El UML muestra la incompatibilidad entre APIs; el algoritmo de conversión se desarrolla en Java. |
| Mantener ambos adaptadores | El usuario planteó conservar uniformidad; las versiones mantuvieron dos y la propuesta final aceptada los conserva. | El cliente recibe siempre `Contrato`; el primer adaptador solo delega y el segundo transforma. |
| Proveedor antiguo implementando directamente el contrato | Alternativa válida no elegida en el modelo aceptado; no se registra como rechazo explícito. | Reduciría una envoltura, pero se decidió conservar la estructura con dos adaptadores. |
| Primera entrada, última salida, ausencia explícita | Propuesta didáctica aceptada en la confirmación final. | Una colección desordenada puede normalizarse sin inventar horas cuando faltan marcas. |

## Detalles y límites de la simulación

- Los proveedores son simulaciones locales, sin conexión a equipos ni SDK reales. La API de `Proveedor2` se trata como externa y fija; el adaptador la utiliza sin cambiarla.
- `Proveedor2` recibe la fecha en texto ISO y entrega marcas con documento, fecha/hora ISO local y tipo `ENTRADA` o `SALIDA`. El adaptador recibe `LocalDate` y retorna el resultado institucional. Es una diferencia real de firma y de estructura de datos dentro del ejemplo.
- Se selecciona la primera entrada y última salida de la persona en la misma fecha. No se calculan horas trabajadas ni se emparejan pausas. No se unen turnos que cruzan medianoche ni se convierten zonas horarias; esas reglas no fueron solicitadas.
- Sin marca de un tipo, su hora permanece ausente. Sin ninguna marca se devuelven persona y fecha con ambas horas ausentes. Los getters usan `Optional`, mientras que los campos internos pueden ser `null`.
- Los fallos del proveedor, las fechas malformadas y los tipos desconocidos de la consulta se propagan como errores; no se convierten en un registro vacío. El cliente no recibe valores ficticios para ocultar un fallo.
- Las marcas repetidas no cambian los extremos. No se exige un orden de llegada. Si los datos presentan una salida anterior al ingreso, se mantienen los extremos registrados: el ejemplo no inventa una corrección de jornada.

No hubo rechazos explícitos. Las preguntas de aclaración y los cambios incompletos de las capturas no se presentan como rechazos. No quedan decisiones bloqueantes para el alcance acordado.
