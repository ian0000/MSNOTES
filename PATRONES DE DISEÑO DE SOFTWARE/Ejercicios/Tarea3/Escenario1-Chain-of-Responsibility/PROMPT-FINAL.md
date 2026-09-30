# Prompt final · Escenario 1

Revisa mi UML inicial y mi UML final del escenario de aprobación de compras institucionales, y genera una implementación Java 11 o superior en `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios/Tarea3/Escenario1-Chain-of-Responsibility`, sin modificar otros ejercicios ni las capturas originales.

Aplica Chain of Responsibility con `SolicitudCompra`, `ServicioCompras`, una clase abstracta `Aprobador` y los manejadores `CoordinadorArea`, `DirectorAdministrativo` y `ComiteCompras`. Los límites inclusivos son 1.000, 10.000 y 50.000. Un aprobador resuelve y detiene el recorrido; si ninguno puede hacerlo, devuelve un resultado no aprobado. La cadena debe poder configurarse u omitir niveles sin cambiar el servicio.

Mantén el UML final como modelo conceptual. Solo acepto como cambio visual eliminar la operación provisional `method(type): type`. La herencia, navegabilidad, multiplicidades, tipos completos y retornos pueden precisarse en Java y en un diagrama complementario, sin presentarlos como cambios pendientes de mi captura. Usa `BigDecimal` para importes y un resultado común que identifique aprobación, responsable y mensaje.

Incluye un ejemplo con solicitudes de 850, 7.500, 40.000 y 70.000; pruebas significativas de límites, delegación, detención, reconfiguración y validaciones; y un script PowerShell que compile para Java 11, ejecute el ejemplo y las pruebas sin dependencias externas.

Entrega un README que explique participantes, funcionamiento, ejecución, resultados reales, costos del patrón y cuándo no usarlo. Compara V1, UML final y Java. Registra por separado qué propuestas acepté, cuáles trasladé al código y por qué, sin inventar razones ni afirmar que acepté redibujar aspectos que decidí dejar conceptuales.
