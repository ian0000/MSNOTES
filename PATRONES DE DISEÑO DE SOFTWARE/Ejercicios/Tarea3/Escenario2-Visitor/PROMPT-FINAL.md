# Prompt final · Escenario 2

Revisa mi UML del escenario de operaciones sobre envíos y genera una implementación Java 11 o superior en `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios/Tarea3/Escenario2-Visitor`, sin modificar otros ejercicios ni la captura original.

Aplica Visitor con la interfaz `Envio`; los elementos `Documento`, `PaqueteFragil` y `CargaRefrigerada`; la interfaz `VisitanteEnvio`; los visitantes `VisitanteCalculoCosto` y `VisitanteInspeccion`; y la estructura `LoteEnvios`. Cada elemento debe implementar `aceptar` llamando a la sobrecarga `visitar(this)` correspondiente, sin `instanceof` ni `switch`.

Mantén mi UML como modelo conceptual. Los tipos omitidos, retornos, datos necesarios para las fórmulas, acumuladores de resultados y conexiones exactas pueden completarse en Java y documentarse en un diagrama complementario. No redibujes mi captura ni presentes esas precisiones como correcciones pendientes; las tendré en cuenta en próximos trabajos.

Usa estas reglas didácticas: documento = 2,00 + 0,01 por gramo; paquete frágil = 3,00 por kilogramo + 2 % del valor declarado; carga refrigerada = 0,80 por kilómetro + 0,15 por litro + 20,00. La inspección debe advertir sobre documento confidencial, paquete sin refuerzo y temperatura menor que -20 °C o mayor que 8 °C. Usa `BigDecimal` para importes.

Incluye un lote de demostración, pruebas significativas de costos, inspecciones, límites, lote vacío, doble despacho y validaciones, y un script PowerShell que compile para Java 11, ejecute el ejemplo y las pruebas sin dependencias externas.

Entrega un README con participantes, funcionamiento, comandos, resultados reales, ventajas, costos y casos en los que Visitor no conviene. Registra qué propuestas conservé fuera del UML y cómo se resolvieron en código, sin inventar aceptaciones ni motivos.
