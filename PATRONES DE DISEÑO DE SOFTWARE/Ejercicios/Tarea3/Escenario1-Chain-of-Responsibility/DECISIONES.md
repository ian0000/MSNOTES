# Escenario 1 · Registro de decisiones

La respuesta final del usuario fue: **«el único del primero que veo con sentido es el de la operación provisional; los demás creo que puedes verlos en código tranquilamente»**. Por ello se distingue el UML conceptual aceptado de los detalles necesarios para ejecutar Java.

| Propuesta de revisión | Elección | Justificación registrada |
|---|---|---|
| Utilizar Chain of Responsibility. | Aceptada. | El responsable depende del importe y la cadena debe poder reconfigurarse. |
| Eliminar `method(type): type`. | Aceptada y aplicada en el UML final. | Era una operación provisional sin significado en el dominio. |
| Cambiar las líneas `Use` por generalización UML. | No incorporada al UML final; aplicada en Java. | El usuario considera suficiente que la herencia se vea en el código. Las clases concretas usan `extends Aprobador`. |
| Corregir navegabilidad entre servicio, solicitud y aprobador. | No incorporada al UML final; aplicada en Java. | Se prefirió mantener el diagrama conceptual. `ServicioCompras` conserva al primer aprobador y recibe la solicitud como parámetro. |
| Mostrar multiplicidad `0..1` hacia el siguiente. | No incorporada al UML final; aplicada en Java. | La ausencia de sucesor se representa con `null` en el último manejador. |
| Completar retornos de `procesar`, `manejar`, `resolver` y `setSiguiente`. | No incorporada al UML final; definida en Java. | El usuario indicó que esos detalles pueden consultarse en la implementación. |
| Añadir `ResultadoAprobacion` al UML. | No aceptada para el dibujo; incorporada como clase auxiliar de código. | Permite comunicar el desenlace sin cambiar la lógica ni exigir que el cliente conozca clases concretas. |
| Sustituir `decimal` por `BigDecimal` y tipar límites. | No incorporada al UML final; aplicada en Java. | Es una precisión técnica de implementación para trabajar con dinero. |

No se cambió la lógica elegida: coordinador hasta 1.000, director hasta 10.000, comité hasta 50.000 y resultado no resuelto por encima de ese valor. Un único manejador aprueba y detiene la cadena.
