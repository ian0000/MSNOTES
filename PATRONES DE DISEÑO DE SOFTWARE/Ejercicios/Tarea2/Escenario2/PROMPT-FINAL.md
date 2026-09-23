# Prompt final · Escenario 2

Revisa mis UML V1, V2, V3 y la última captura del escenario de asistencia institucional y genera Java 11 o superior en `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios/Tarea2/Escenario2`. Conserva las imágenes y los demás ejercicios.

Aplica Adapter y mantén dos adaptadores. Acepto separar la interfaz `Contrato`, con `consultarAsistenciaDiaria(String identificacion, LocalDate fecha): RegistroAsistencia`, de la clase inmutable `RegistroAsistencia`, que contiene identificación, fecha y horas de ingreso/salida opcionales. El cliente depende de `Contrato`; la clase de resultado no consulta al servicio. `Proveedor1Adapter` delega al proveedor compatible y `Proveedor2Adapter` transforma la API del fabricante nuevo sin modificarla.

Simula los proveedores localmente, sin dispositivos ni servicios externos. La biblioteca nueva expone `recuperarMarcaciones(String documento, String fechaISO)` y devuelve una colección de `MarcacionProveedor2` con documento, instante ISO local y tipo `ENTRADA` o `SALIDA`. Acepto filtrar por persona y fecha, tomar la primera entrada y última salida aunque las marcas estén desordenadas y dejar ausentes las horas sin marca. Sin marcas, devuelve identificación y fecha con ambas horas ausentes. Diferencia ausencia de datos y errores del proveedor. No añadas reglas de turnos nocturnos, duración laboral o zonas horarias.

Incluye demostraciones de ambos proveedores, un caso incompleto y otro sin registros. Añade pruebas de transformación, filtrado, extremos, ausencia, independencia del cliente y propagación de errores. Entrega un script PowerShell para compilar y ejecutar ejemplos y pruebas sin dependencias externas.

El README debe explicar el flujo, las reglas didácticas y limitaciones, incluir comandos y resultados comprobados, enlazar el UML editable y comparar todas las versiones. Deja al final las correcciones concretas de mis diagramas. Documenta decisiones y justificaciones sin atribuir rechazos o motivos que no expresé. Lo descrito ya está autorizado; consulta únicamente otros cambios importantes de lógica que resulten necesarios.
