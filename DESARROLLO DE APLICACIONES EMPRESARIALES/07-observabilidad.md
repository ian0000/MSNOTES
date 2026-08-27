# Resumen — Clase 07: Observabilidad en Sistemas Distribuidos

Fuente: [`presentaciones/07-observabilidad.md`](../presentaciones/07-observabilidad.md)

## Idea central

La observabilidad permite inferir el estado interno de un sistema a partir de sus salidas. A diferencia del monitoreo tradicional, no solo responde si un servidor está activo, sino por qué una solicitud específica falló o tardó demasiado en recorrer varios servicios.

## Las tres señales

- **Logs:** eventos discretos con contexto; explican qué ocurrió.
- **Métricas:** valores numéricos agregados; muestran el comportamiento general.
- **Trazas:** recorrido de una solicitud por distintos componentes; indican dónde se consumió el tiempo.

Las métricas alertan, los logs explican y las trazas ubican. Ninguna señal sustituye a las demás.

## Logs estructurados

Los mensajes de texto sin formato son difíciles de buscar y correlacionar cuando existen muchos pods. Los logs estructurados utilizan campos como:

- Timestamp y nivel.
- Nombre del servicio y pod.
- Usuario, pedido o identificador de negocio.
- `traceId` y `spanId`.
- Tipo de error y duración.

El formato JSON permite consultar y agregar información automáticamente. Un mismo `traceId` conecta los logs producidos por gateway, pedidos, pagos e inventario durante una solicitud.

## Métricas

Tipos principales:

- **Counter:** valor acumulativo que solo aumenta, como solicitudes o errores totales.
- **Gauge:** valor instantáneo que sube y baja, como conexiones o memoria utilizada.
- **Histogram:** distribución en buckets que permite calcular percentiles de latencia.
- **Summary:** calcula percentiles en el cliente.

Los promedios pueden ocultar usuarios con experiencias muy lentas. Los percentiles p95 y p99 permiten observar la cola de latencia. Spring Boot Actuator y Micrometer exponen métricas que Prometheus recolecta periódicamente mediante scraping.

## Trazas distribuidas

Un **trace** representa la operación completa. Cada paso es un **span** con inicio, fin, atributos y relación con su padre.

La propagación de contexto transmite identificadores mediante HTTP, gRPC o mensajería. Si un servicio no propaga el contexto, la traza se fragmenta. Añadir atributos de negocio, como `order.id`, facilita relacionar el problema técnico con su impacto real.

## Sampling

Guardar todas las trazas puede ser costoso:

- **Head-based:** decide al inicio si captura la traza.
- **Tail-based:** revisa el resultado y conserva errores o latencias altas.
- **Rate limiting:** conserva un máximo de trazas por segundo.
- **Always-on:** captura todo; apropiado principalmente para desarrollo.

Las trazas con errores y alta latencia son las de mayor valor diagnóstico.

## OpenTelemetry

OpenTelemetry unifica instrumentación para logs, métricas y trazas. OTLP estandariza el transporte y evita depender de un proveedor específico. Puede utilizarse mediante:

- Instrumentación automática con Java Agent.
- SDK y spans manuales para operaciones del negocio.
- Integraciones con HTTP, JDBC, Kafka y gRPC.

## Stacks de observabilidad

- **ELK:** Elasticsearch, Logstash y Kibana; potente para búsqueda full-text y cumplimiento, pero costoso de operar y almacenar.
- **LGTM:** Loki, Grafana, Tempo y Mimir o Prometheus; menor costo de logs y buena correlación de señales.
- **Datadog o New Relic:** servicios gestionados con menor esfuerzo operacional, facturados por volumen.

La selección depende de necesidades de búsqueda, presupuesto, regulación y capacidad del equipo para operar la plataforma.

## Para recordar

> Un sistema observable permite pasar de “algo está lento” a identificar qué solicitud, servicio, dependencia y operación de negocio produjeron la demora.
