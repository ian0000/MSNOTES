# Resumen — Clase 03: Microservicios

Fuente: [`presentaciones/03-microservicios.md`](../presentaciones/03-microservicios.md)

## Idea central

Los microservicios permiten desplegar, escalar y evolucionar capacidades del negocio de manera independiente, pero trasladan la complejidad hacia la red, los datos y la operación. Deben adoptarse cuando sus beneficios compensen ese costo, no como punto de partida automático.

## Evolución de las arquitecturas

- **Monolito:** sencillo de desarrollar, depurar y desplegar al inicio, pero escala y se despliega como una unidad.
- **Monolito modular:** mantiene límites claros entre módulos dentro del mismo proceso y facilita una extracción posterior.
- **SOA:** utilizaba servicios grandes y un ESB central que terminó convirtiéndose en punto de acoplamiento.
- **Microservicios:** servicios pequeños alrededor de bounded contexts, bases de datos propias y despliegues independientes.

Para equipos pequeños, dominios poco entendidos o startups que todavía validan el producto, suele convenir un monolito modular.

## Principios de microservicios

- Responsabilidad enfocada en una capacidad de negocio.
- Base de datos por servicio; la API es el contrato.
- Despliegue y escalamiento independientes.
- Aislamiento de fallos.
- Propiedad completa por el equipo: “you build it, you run it”.

## Comunicación entre servicios

- **Síncrona:** REST o gRPC; entrega respuesta inmediata, pero acopla la disponibilidad de los participantes.
- **Asíncrona:** Kafka o RabbitMQ; reduce el acoplamiento temporal, pero introduce consistencia eventual y más complejidad operacional.

En la **orquestación**, un componente central dirige el flujo. En la **coreografía**, cada servicio reacciona a eventos. La primera es fácil de visualizar; la segunda reduce el acoplamiento central, aunque dificulta rastrear el proceso completo.

## Arquitectura orientada a eventos

Los eventos permiten que múltiples consumidores reaccionen sin modificar al productor. La interfaz puede mostrar un estado pendiente, consultar periódicamente, recibir actualizaciones mediante WebSocket o SSE, o aplicar *optimistic UI*.

- **RabbitMQ:** entrega mensajes y normalmente los elimina después del `ack`.
- **Kafka:** conserva un log particionado que puede releerse mediante offsets y consumer groups.

Kafka resulta especialmente útil para auditoría, integración y procesamiento de eventos históricos.

## CQRS, Event Sourcing y Saga

- **CQRS:** separa el modelo de escritura, orientado a consistencia, del modelo de lectura, optimizado para consultas.
- **Event Sourcing:** almacena eventos como fuente de verdad y reconstruye el estado; ofrece auditoría completa a cambio de más complejidad.
- **Saga:** coordina una operación distribuida mediante transacciones locales y acciones compensadoras.

## Resiliencia

Una dependencia lenta puede consumir hilos y provocar fallos en cascada. Los patrones principales son:

- Timeout para limitar la espera.
- Retry para fallos transitorios.
- Circuit Breaker para cortar llamadas a una dependencia degradada.
- Bulkhead para aislar recursos.
- Fallback para entregar una respuesta degradada.

Estos patrones deben combinarse con cuidado; los reintentos sin límites pueden aumentar la sobrecarga.

## Infraestructura

Spring Cloud aporta configuración centralizada, service discovery, gateway, clientes declarativos y resiliencia. Kubernetes reemplaza algunas funciones mediante Services, DNS, ConfigMaps, Secrets e Ingress. Un Service Mesh como Istio o Linkerd mueve mTLS, observabilidad y control de tráfico fuera del código de negocio.

## Serverless y FinOps

FaaS ejecuta código únicamente cuando ocurre un evento. Conviene para webhooks, tareas programadas y cargas esporádicas; no es ideal para estado complejo o latencia crítica por posibles cold starts.

FinOps trata el costo como decisión arquitectónica:

- Etiquetar recursos por propietario.
- Aplicar rightsizing.
- Utilizar instancias spot cuando sea posible.
- Consolidar servicios con poco tráfico.
- Medir costos de bases de datos, observabilidad y transferencia entre zonas.

## Para recordar

> Un microservicio no elimina complejidad: la distribuye. Si el equipo o el negocio todavía no necesita despliegue y escalamiento independientes, el monolito modular suele ser la opción más económica.
