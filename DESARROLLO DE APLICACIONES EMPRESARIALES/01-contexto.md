# Resumen — Clase 01: Contexto de Aplicaciones Empresariales

Fuente: [`presentaciones/01-contexto.md`](../presentaciones/01-contexto.md)

## Idea central

Una aplicación empresarial no se define únicamente por su tamaño o tecnología, sino por la complejidad del negocio que debe representar, la cantidad de datos que conserva, sus integraciones y el costo de cambiarla. No existe una arquitectura universal: cada decisión implica beneficios, costos y riesgos que deben justificarse según el contexto.

## Características de una aplicación empresarial

- Mantiene datos históricos durante años por operación, auditoría o normativa.
- Atiende múltiples usuarios concurrentes con funciones y vistas diferentes.
- Integra sistemas modernos y heredados mediante diversos protocolos y formatos.
- Ejecuta procesos interactivos y trabajos desatendidos o por lotes.
- Implementa reglas de negocio complejas que cambian con el tiempo.
- Debe evolucionar sin perder información y, frecuentemente, sin interrumpir el servicio.

La lógica de negocio constituye la complejidad esencial del sistema. Las herramientas pueden reducir dificultades técnicas, pero no eliminan las reglas reales del negocio.

## No existen soluciones universales

La arquitectura apropiada cambia según el tipo de organización:

- Un comercio electrónico prioriza escala, experiencia de usuario e integraciones con pagos y logística.
- Un banco prioriza trazabilidad, regulación, seguridad y conservación histórica.
- Una startup prioriza rapidez para experimentar y cambiar el producto.

Una tecnología conveniente para uno de estos contextos puede resultar innecesaria o perjudicial en otro. La selección debe basarse en trade-offs y no en modas.

## Complejidad esencial y accidental

- **Complejidad esencial:** proviene del problema de negocio y no puede eliminarse.
- **Complejidad accidental:** aparece por herramientas, frameworks, procesos, infraestructura o malas decisiones técnicas.

El objetivo de la arquitectura es comprender la complejidad esencial y reducir la accidental. Agregar microservicios, capas o plataformas sin necesidad puede aumentar el costo sin aportar valor.

## Organización y arquitectura

La Ley de Conway indica que la estructura del software tiende a reflejar la forma en que se comunican los equipos. Si los equipos están separados por áreas y se coordinan con dificultad, el sistema probablemente mostrará fronteras y dependencias similares.

Team Topologies propone cuatro tipos de equipo:

- **Stream-aligned:** responsable de un flujo de valor del negocio.
- **Platform:** ofrece herramientas e infraestructura interna como producto.
- **Enabling:** ayuda a otros equipos a adoptar capacidades nuevas.
- **Complicated-subsystem:** trabaja en áreas que requieren conocimiento especializado.

El *Inverse Conway Maneuver* consiste en diseñar la estructura de equipos que se desea y permitir que la arquitectura evolucione hacia esa organización.

## Platform Engineering

Una plataforma interna reduce la carga cognitiva de los equipos al estandarizar despliegues, seguridad, observabilidad y acceso a infraestructura. La plataforma debe tratarse como un producto para desarrolladores, no como una colección de scripts impuesta por operaciones.

## Economía del software y FinOps

Cada decisión técnica afecta el costo de construir, operar y modificar el sistema. Los principios destacados son:

- Evitar diseñar anticipadamente para necesidades hipotéticas: YAGNI.
- Trabajar en ciclos cortos para descubrir temprano el problema real.
- Mantener bajo el costo del cambio.
- Dimensionar recursos según el consumo real.
- Etiquetar los recursos cloud por equipo y servicio responsable.
- Tratar el costo cloud como una métrica de arquitectura.

## Inteligencia artificial como componente

Un LLM introduce características distintas al software tradicional:

- El resultado no es completamente determinista.
- El costo depende del volumen de tokens.
- Puede agregar latencia y dependencia de un proveedor.
- Puede inventar información o exponer datos sensibles.
- Debe probarse con evaluaciones semánticas, reglas, revisión humana o un LLM evaluador.

Los principales patrones de integración son RAG, agentes con herramientas, clasificación o extracción dentro de pipelines y revisión automatizada de código.

## Para recordar

> Una buena arquitectura no es la que utiliza más tecnologías, sino la que mantiene controlados la complejidad y el costo del cambio en el contexto real del negocio.
