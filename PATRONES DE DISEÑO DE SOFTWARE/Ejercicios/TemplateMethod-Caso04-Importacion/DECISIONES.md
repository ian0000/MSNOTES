# Decisiones de implementación

La instrucción actual fue corregir el UML y generar el código del caso 4 de Unidad 03.01. Se aplica esa autorización sin atribuir aprobaciones individuales ni rechazos no expresados. Las decisiones de Tarea2 no se transfieren como requisitos específicos de este nuevo ejercicio.

| Decisión | Procedencia | Justificación |
|---|---|---|
| Template Method y cuatro clases principales | Se conserva la intención del UML y el caso de la presentación. | El algoritmo mantiene un orden común y varían dos pasos mediante herencia. |
| Método público `importar` final | Corrección necesaria para representar el patrón solicitado. | Controla validar, leer, transformar y almacenar; no permite que una subclase cambie la secuencia. |
| Lectura y transformación abstractas únicamente en la base | Corrección de las operaciones del UML. | Las clases `ImportCSV`, `ImportJSON` e `ImportXML` aportan implementaciones instanciables. |
| Datos intermedios con `T` y resultado `List<Registro>` | Interpretación técnica para concretar firmas incompletas. | Evita `bit[]`, tipos inseguros y campos temporales compartidos entre importaciones. |
| Validación y almacenamiento protegidos/final | Implementación del comportamiento común. | El punto público de entrada es la plantilla. |
| Registros con código/nombre y esquemas diferentes de entrada | Elaboración didáctica; no está especificada en la diapositiva. | Permite mostrar diferencias reales de lectura y transformación. |
| Almacenamiento en CSV normalizado UTF-8 | Elaboración didáctica. | Demuestra almacenamiento real sin requerir una base de datos. No sobreescribe destinos existentes. |
| Jackson Core 2.21.4 | Elección de implementación, con versión y SHA-256 fijados. | Lee JSON real, sus escapes y errores sin desarrollar otro analizador JSON. No requiere Maven. |
| CSV con coma/comillas y XML mediante DOM del JDK | Elección de implementación. | Mantiene visibles los formatos leídos y evita infraestructura ajena al ejercicio. |
| Errores detienen el flujo | Concreción del contrato del ejemplo. | No almacena si falla la validación, lectura o transformación. |

La validación inicial comprueba el archivo; no se afirma que determine toda su validez sintáctica. El formato lo elige el cliente al crear el importador, sin inferirlo de la extensión. Una colección válida vacía genera una salida con cabecera.

No se introducen Strategy ni Factory Method: el pequeño selector de `Main` solo elige la subclase para la demostración. El algoritmo permanece en la base. No hubo rechazos explícitos ni otra versión del UML entregada para este caso; la captura se conserva como original y el diagrama nuevo se identifica como corrección del asistente.
