# Unidad 2 · Estilos de comunicación, atributos de calidad y resiliencia

**Fuente:** [Unidad 2 Arquitectura y Patrones v1_compressed.pdf](<../DOCS/Unidad 2 Arquitectura y Patrones v1_compressed.pdf>) · 31 páginas. Autora indicada en la portada: Ing. Patsy Malena Prieto MSc.

**Ruta de lectura:** decisiones y calidad, pp. 4-6; estilos, pp. 7-13; comparaciones, pp. 14-18; NeoMarket y patrones, pp. 19-31.

## 1. Idea central: elegir implica aceptar costos

Un **trade-off** es una decisión que favorece una propiedad a cambio de esfuerzo, riesgo o pérdida en otra. Reducir llamadas desde un móvil puede aumentar procesamiento en el backend; distribuir servicios puede mejorar independencia de despliegue y complicar el diagnóstico de errores.

Las pp. 5-6 usan atributos de calidad como brújula: adecuación funcional, eficiencia, usabilidad, fiabilidad, seguridad, mantenibilidad, compatibilidad y portabilidad. Se resumen las ocho características tal como aparecen en el documento, sin convertir la infografía en una evaluación normativa o una certificación.

**Latencia** es el tiempo que tarda una operación. **Throughput** es la cantidad de trabajo procesado por unidad de tiempo. **Disponibilidad** indica si el servicio está utilizable. Son diferentes: un servicio disponible puede responder muy lentamente.

Las matrices del PDF son orientaciones cualitativas. No aportan mediciones de implementaciones comparables; sus colores no demuestran que una tecnología sea siempre superior.

## 2. REST: integración por recursos

La p. 7 propone REST para integrar sistemas POS de tiendas asociadas con NeoMarket. Los recursos se identifican mediante URI y se manipulan con métodos HTTP. Una API puede documentar operaciones de productos, pedidos e inventario con OpenAPI.

**Ejemplo didáctico:** `GET /productos/17` obtiene una representación que el cliente puede interpretar sin conocer la base de datos. Una política de caché puede reducir consultas repetidas al catálogo.

Es adecuado cuando se busca un contrato HTTP comprensible para consumidores heterogéneos. Los costos pueden aparecer al transferir representaciones grandes o necesitar varias consultas para una pantalla. Estos costos también dependen del diseño de la API: no toda respuesta REST debe devolver todos los campos del dominio.

## 3. GraphQL: expresar los datos de una consulta

GraphQL permite consultar un esquema tipado seleccionando campos y relaciones. Un **resolver** ejecuta la lógica que obtiene un campo. La p. 8 lo relaciona con un BFF móvil para catálogos anidados.

**Over-fetching** significa recibir información innecesaria. **Under-fetching** significa que una respuesta no ofrece todo lo requerido y obliga a más llamadas. GraphQL puede reducir ambos cuando el contrato y la ejecución están bien diseñados.

```graphql
# Elaboración didáctica: obtener lo necesario para una tarjeta de producto.
query {
  producto(id: "17") {
    nombre
    precio
    stockDisponible
  }
}
```

El servidor valida la consulta contra el esquema, ejecuta resolvers y construye una respuesta con esos campos. Una sola petición del cliente puede desencadenar muchas operaciones internas: hay que controlar profundidad, cantidad de elementos y consultas repetidas.

**Aclaración del material:** GraphQL no requiere usar siempre POST y no carece de toda caché. Las consultas pueden admitir GET y aprovechar caché HTTP según el servidor; las mutaciones usan POST en ese esquema de transporte. La [documentación oficial sobre HTTP](https://graphql.org/learn/serving-over-http/) explica esta distinción. GraphQL tampoco sustituye los permisos del dominio.

## 4. gRPC: llamadas remotas con contrato tipado

La p. 9 utiliza gRPC para comunicación interna de inventario durante el pago. Se definen servicios, mensajes, parámetros y retornos, habitualmente con Protocol Buffers. Un **stub** es el cliente generado que representa las llamadas remotas.

**Ejemplo didáctico:** un servicio de compras llama a `ConsultarDisponibilidad` mediante un mensaje que contiene producto y cantidad. El servidor devuelve disponibilidad y versión de inventario. Que la llamada parezca local no elimina red, retrasos ni fallos.

Puede favorecer mensajes compactos y contratos verificables entre servicios. Introduce generación de código y coordinación de versiones. No garantiza una latencia concreta: importa la red, el trabajo del servidor y la carga. Tampoco toda llamada gRPC tiene que bloquear un hilo; existen formas asíncronas y modalidades de streaming.

## 5. Event-Driven Architecture: colaborar mediante hechos

**EDA** es una arquitectura donde productores publican eventos y consumidores reaccionan a ellos. Un evento describe algo ocurrido, como `PedidoConfirmado`; un comando solicita que ocurra algo, como `ConfirmarPedido`.

La p. 10 muestra un productor, un broker y varios consumidores. Un **broker** recibe y distribuye mensajes. El productor no necesita ejecutar directamente todas las tareas posteriores.

Ejemplo didáctico: tras confirmar un pedido, inventario, notificaciones y analítica reaccionan por separado. Un consumidor lento puede procesar su trabajo después sin detener todas las operaciones. La cola absorbe una ráfaga limitada, pero si la entrada supera permanentemente la capacidad, el atraso sigue creciendo.

Sus costos son consistencia eventual, mensajes duplicados, orden y diagnóstico distribuido. **Consistencia eventual** significa que distintas representaciones pueden diferir durante un período antes de converger. El desacoplamiento temporal no elimina la dependencia del significado del evento.

## 6. WebSocket, webhook y serverless

### WebSocket — p. 11

Mantiene un canal bidireccional para intercambio continuo. El PDF lo aplica a telemetría de VitalsGuard. Puede evitar consultas repetidas y permitir envío en ambas direcciones.

Sus costos incluyen conexiones persistentes, reconexión, distribución de mensajes entre nodos y control del consumidor lento. Un servidor puede quedarse sin recursos aunque los mensajes sean pequeños. El ejemplo de salud se estudia como arquitectura, no como regla clínica validada.

### Webhook — p. 12

Es una notificación HTTP enviada a una dirección del receptor cuando ocurre un evento. El PDF muestra una pasarela que informa un pago aprobado.

El emisor envía el evento, el receptor valida autenticidad, lo almacena y confirma recepción. Un reintento puede entregar el mismo evento otra vez; la operación debe tolerar duplicados. Recibir `200` no demuestra necesariamente que todo el proceso posterior terminó.

**Comparación:** WebSocket suele ser apropiado para intercambio continuo entre participantes conectados; webhook, para eventos discretos entre servidores. No son las únicas alternativas posibles y la naturaleza del consumidor también importa: una aplicación móvil no siempre puede ofrecer una URL pública estable.

### Serverless / FaaS — p. 13

**FaaS** ejecuta funciones administradas ante solicitudes o eventos. Reduce parte de la administración de servidores y puede ajustar capacidad bajo demanda. Sus costos incluyen límites de ejecución, arranques en frío y dependencia de servicios del proveedor.

Un **cold start** es el trabajo inicial necesario antes de atender una ejecución sin un entorno ya preparado. El escalado no es infinito ni instantáneo: existen cuotas y límites. Una función también necesita permisos, validación y observabilidad.

## 7. Cómo interpretar las matrices y el árbol de decisión

Las pp. 14-17 comparan rendimiento, acoplamiento, caché y seguridad. La p. 18 destaca contrato, aislamiento temporal y transferencia de complejidad.

Una forma útil de aplicar el árbol es formular requisitos concretos:

| Necesidad | Alternativa para analizar | Pregunta que evita elegir automáticamente |
|---|---|---|
| Integración HTTP con socios | REST | ¿Qué recursos y errores necesita el consumidor? |
| Pantallas con datos anidados | GraphQL o BFF | ¿La flexibilidad compensa costo y controles? |
| Contrato interno de mensajes | gRPC | ¿Qué latencia y compatibilidad se han medido? |
| Trabajo diferido tras un hecho | EDA | ¿Cuánto atraso admite el negocio? |
| Intercambio continuo | WebSocket | ¿Cómo se recupera una conexión perdida? |
| Notificación puntual a un servidor | Webhook | ¿Cómo se autentica y deduplica el evento? |

Las alternativas pueden coexistir. El requisito debe conducir la elección, y una medición debe comprobar la expectativa.

## 8. NeoMarket: síntomas antes de patrones

La p. 19 presenta tres problemas: fallos por bloqueos en dependencias, datos excesivos para el móvil y lecturas que compiten con escrituras. Las pp. 20-31 agrupan respuestas en resiliencia, consumo y persistencia.

### Circuit Breaker — p. 22

Protege al consumidor de una dependencia que falla repetidamente. Mantiene estados cerrado, abierto y semiabierto. El gráfico usa un umbral de errores del 50 % en diez segundos como ejemplo; no es una configuración universal.

En NeoMarket, una validación lenta de socios agota recursos. Abrir el circuito permite fallar rápidamente mientras la dependencia se recupera. El fallback, respuesta alternativa, debe respetar el negocio: servir un catálogo antiguo puede ser aceptable; autorizar un pago sin validación puede no serlo.

### Retry, backoff, jitter y rate limiting — pp. 20 y 23

**Retry** repite una operación tras un fallo transitorio. **Backoff** aumenta la espera. **Jitter** añade variación aleatoria para evitar que todos reintenten a la vez. **Rate limiting** limita la tasa admitida.

Ejemplo didáctico: varios clientes que reintentan exactamente al segundo siguiente crean otra ráfaga. Distribuir esperas reduce esa coincidencia. Se deben limitar intentos y tiempo total; una operación que produce efectos necesita idempotencia antes de reintentarse con seguridad.

### BFF y API Composition — pp. 25-26

El BFF adapta la respuesta al canal; API Composition reúne información de varios servicios. El esquema compara tres consultas del móvil con una sola petición al orquestador, que consulta catálogo, reseñas e inventario internamente.

La composición puede paralelizar trabajo, pero necesita decidir qué pasa si falta una parte. No conviene que una reseña no disponible impida toda consulta del producto si el negocio permite una respuesta parcial.

### CQRS — p. 28

Separa modelos de comandos y consultas para optimizar cada carga. El material muestra almacenes distintos conectados mediante eventos. Esto puede reducir competencia por lectura y escritura, pero requiere sincronización y comunicar qué datos son definitivos.

CQRS no convierte una disponibilidad mostrada en garantía de compra. El comando debe volver a comprobar la regla en la fuente autoritativa.

### Event Sourcing — pp. 29-30

Conserva cambios como eventos y reconstruye el estado aplicándolos. El ejemplo del PDF registra comisión de 50, retiro de 10 y otra comisión de 410, obteniendo saldo 450.

```text
0 + 50 - 10 + 410 = 450
```

Guardar solo 450 responde cuál es el saldo; guardar los eventos permite explicar cómo llegó a ese valor. Event Sourcing agrega reglas para evolución de eventos, reconstrucción, privacidad y correcciones mediante nuevos hechos. No garantiza auditoría perfecta si los eventos son incorrectos o faltan controles de acceso.

Un sistema CRUD también puede tener una auditoría rigurosa. La diferencia esencial de Event Sourcing es que los eventos son la fuente de verdad del estado. EDA, CQRS y Event Sourcing se pueden combinar, pero ninguno obliga a adoptar los otros dos.

## Síntesis

El documento propone diagnosticar primero el problema y después elegir un mecanismo. La comunicación, la resiliencia, las respuestas al consumidor y la persistencia son decisiones relacionadas. Los patrones aportan beneficios concretos y trasladan complejidad a partes que deben operarse y medirse.

## Preguntas de repaso

1. **¿Latencia y throughput son equivalentes?** No: tiempo por operación frente a cantidad procesada por tiempo.
2. **¿Un evento es una orden?** No; describe un hecho, mientras el comando solicita una acción.
3. **¿Una cola resuelve sobrecarga permanente?** No; necesita capacidad, límites y una política de atraso.
4. **¿Qué aporta jitter?** Evita reintentos sincronizados que recrean una ráfaga.
5. **¿Circuit Breaker sustituye a retry?** No; uno suspende llamadas y el otro repite fallos seleccionados.
6. **¿BFF y Composition se excluyen?** No; un BFF puede componer respuestas.
7. **¿CQRS necesita Event Sourcing?** No; separa modelos de lectura y escritura independientemente del modo de persistencia.
8. **¿El saldo reconstruido demuestra auditoría perfecta?** No; depende de calidad, integridad y control de los eventos.
9. **¿GraphQL evita todo trabajo extra del servidor?** No; puede reducir datos transferidos y aumentar complejidad de ejecución.

[Volver al índice](README.md) · [Continuar con diseño de APIs](03-diseno-api-producto-contratos.md)
