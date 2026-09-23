# Escenario 1 · Registro de decisiones

La confirmación final fue: **«si me parecen bien acepto»**, en respuesta a aceptar los UML propuestos, la gestión de componentes y la regla de asistencia. Este documento distingue decisiones del usuario, evolución visible de las capturas y detalles técnicos de implementación. No se inventan motivos personales ni rechazos.

| Propuesta | Evolución y elección | Justificación técnica |
|---|---|---|
| Composite | Se conserva el patrón elegido. | Una misma abstracción representa hojas y agrupaciones; las operaciones se delegan recursivamente. |
| Herencia del grupo | Aceptada con «cierto»; aplicada desde V2. | Un grupo también debe ser un componente para anidarse en otro grupo. |
| Valor únicamente en la hoja | Aceptada con «tienes razon». Las capturas todavía lo duplicaban; corregido en Java y en el UML aceptado. | El grupo suma sus hijos y no tiene un valor adicional propio en el enunciado. |
| `mostrarEstructura(): String` | Aceptada con «ok»; cambio parcial en V2 y completo en V3. | La operación puede representar tanto hojas como grupos sin retornar un objeto del tipo equivocado. |
| Colección y agregar/quitar | Primero se pidió aclaración, no se rechazó. Aceptada con la confirmación final. | Materializa la relación entre el grupo y sus componentes. |
| Multiplicidad `0..*` | V3 y la cuarta captura separaban `0` y `*`; corrección aceptada. | Cada extremo de una relación tiene una multiplicidad independiente. |
| Rombo y propiedad exclusiva | El usuario indicó que lo tendría en cuenta, sin considerarlo prioritario. Se mantiene asociación sin imponer un padre único. | Composite no obliga por sí mismo a composición UML de ciclo de vida exclusivo. |
| Cambiar el nombre de la base | Alternativa no adoptada; no hay rechazo explícito. | `PartidaPresupuestaria` permite conservar la terminología original y cumple su función. |
| Sustituir `float` por `BigDecimal` | Recomendación no incorporada en la propuesta final aceptada; no hay rechazo explícito del usuario. | Se conserva la firma de su ejercicio. El límite es la precisión decimal de `float`. |

## Detalles de implementación

- Se validan código y descripción no vacíos y valores finitos no negativos. Son controles del ejemplo, no requisitos adicionales atribuidos al profesor.
- Se impiden ciclos directos e indirectos. Se rechaza agregar el mismo objeto dos veces al mismo grupo para evitar una suma duplicada accidental; la identidad del código no se trata como una regla global de unicidad.
- `quitar` actúa por identidad del objeto; si no está entre los hijos directos no cambia nada. No borra otros componentes ni modifica las referencias de otros grupos.
- No se impone pertenencia exclusiva. Un objeto compartido entre ramas se cuenta en cada recorrido; no hay deduplicación global. El ejemplo normal usa un árbol.
- Se conserva el orden de inserción al mostrar la estructura. La detección de ciclos utiliza un auxiliar protegido `contiene`.

No quedan decisiones bloqueantes para esta implementación. Las capturas se mantienen como evidencia de lo entregado; la aceptación de las correcciones no significa que hayan sido redibujadas por el usuario.
