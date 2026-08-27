# Resumen — Clase 08: Resiliencia en Sistemas Distribuidos

Fuente: [`presentaciones/08-resiliencia.md`](../presentaciones/08-resiliencia.md)

## Idea central

En un sistema distribuido los fallos son inevitables. La resiliencia busca que una dependencia lenta o caída no derrumbe toda la aplicación y que las operaciones distribuidas puedan recuperar una condición válida mediante compensaciones.

## Fallos en cascada

Si un servicio espera indefinidamente a una dependencia, consume hilos, conexiones y memoria. La saturación se propaga hacia los servicios que lo llaman hasta afectar todo el sistema. La defensa combina varios patrones, cada uno con una responsabilidad específica.

## Timeout y Retry

El timeout limita cuánto tiempo se espera una respuesta. Como referencia, una llamada interna puede utilizar uno o dos segundos y una API externa entre cinco y diez, pero el valor correcto debe obtenerse mediante métricas.

Retry sirve únicamente para fallos transitorios, como timeout, red o `503`. No deben reintentarse errores de negocio como `400` o `404`.

El backoff exponencial aumenta la espera entre intentos y el jitter añade aleatoriedad. Esto evita que muchos clientes vuelvan a llamar simultáneamente y provoquen una nueva sobrecarga.

## Circuit Breaker

Estados principales:

- **CLOSED:** las llamadas pasan y se mide la tasa de fallos.
- **OPEN:** las llamadas fallan inmediatamente sin contactar a la dependencia.
- **HALF-OPEN:** se permiten pocas llamadas de prueba para comprobar la recuperación.

Cuando el circuito está abierto, el servicio dependiente puede recuperarse y el cliente recibe rápidamente un fallback. Resilience4j permite configurar ventana de evaluación, umbral de errores, tiempo en abierto y llamadas de prueba.

## Bulkhead y Fallback

Bulkhead separa recursos para que una integración problemática no consuma toda la capacidad:

- **SemaphoreBulkhead:** limita la concurrencia y es suficiente para la mayoría de casos.
- **ThreadPoolBulkhead:** utiliza un pool y una cola propios para mayor aislamiento de CPU o I/O.

Fallback devuelve una respuesta degradada, datos en caché o un estado pendiente. No debe ocultar errores críticos; su uso tiene que ser observable.

## Combinación de patrones

Un flujo frecuente es limitar concurrencia, aplicar timeout, reintentar fallos transitorios y proteger la dependencia con Circuit Breaker. Los límites deben evitar tormentas de reintentos y agotamiento de recursos.

## Transacciones distribuidas

Cada microservicio administra su propia base de datos, por lo que una transacción ACID global resulta difícil. Durante una partición de red, CAP obliga a elegir entre consistencia inmediata y disponibilidad.

Two-Phase Commit coordina una fase de preparación y otra de confirmación. Presenta problemas:

- Bloqueo si falla el coordinador.
- Dos rondas de comunicación.
- Acoplamiento al protocolo XA.
- Menor disponibilidad.

Por estas razones no suele escalar bien en arquitecturas de microservicios.

## Patrón Saga

Una Saga divide la operación en transacciones locales. Cada paso exitoso define una acción compensadora para deshacer su efecto lógico si un paso posterior falla. La consistencia es eventual y puede existir temporalmente un estado parcial.

### Coreografía

Los servicios publican y consumen eventos sin coordinador central. Reduce el acoplamiento, pero el flujo global se vuelve difícil de visualizar y depurar.

### Orquestación

Un orquestador decide qué paso ejecutar y qué compensación activar. El flujo es más claro, aunque el orquestador concentra conocimiento y puede convertirse en un componente crítico.

## Para recordar

> Timeout libera recursos, Retry supera fallos transitorios, Circuit Breaker corta dependencias degradadas, Bulkhead aísla capacidad y Saga mantiene consistencia eventual mediante compensaciones.
