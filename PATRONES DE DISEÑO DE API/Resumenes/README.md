# Guías de estudio - Patrones de diseño de API

Siete guías explicativas, una por cada PDF de [DOCS](../DOCS), que cubren las **103 páginas** del material. Incluyen definiciones, ejemplos desarrollados, comparaciones, consecuencias de diseño y preguntas de repaso con respuestas.

## Índice y orden recomendado

| Orden | Guía | Contenido principal | PDF fuente | Páginas |
|---|---|---|---|---:|
| 1 | [Introducción a las APIs](01-introduccion-apis.md) | Contratos, peticiones, interoperabilidad, audiencias, monetización y ciclo de vida | [Unidad 1 Introducción APIs](<../DOCS/Unidad 1 Introducción APIs.pdf>) | 20 |
| 2 | [Arquitectura, capas y patrones](02-01-arquitectura-capas-patrones.md) | Capas, REST, estilos arquitectónicos, Gateway, Registry, BFF, CQRS y Circuit Breaker | [Unidad 2 Arquitectura y Patrones (2)](<../DOCS/Unidad 2 Arquitectura y Patrones (2).pdf>) | 20 |
| 3 | [Comunicación y resiliencia](02-02-estilos-comunicacion-resiliencia.md) | REST, GraphQL, gRPC, eventos, WebSockets, webhooks, FaaS, Retry, composición y Event Sourcing | [Unidad 2 Arquitectura y Patrones v1_compressed](<../DOCS/Unidad 2 Arquitectura y Patrones v1_compressed.pdf>) | 31 |
| 4 | [Diseño de una API como producto](03-diseno-api-producto-contratos.md) | Propuesta de valor, API-first, contratos, paginación, experiencia del desarrollador y evolución | [Unidad 3 Diseño de una APIs](<../DOCS/_Unidad 3 Diseño de una APIs v_compressed (1).pdf>) | 15 |
| 5 | [Seguridad, autenticación y autorización](04-seguridad-autenticacion-autorizacion.md) | Defensa por capas, claves, Basic, Bearer, OAuth, JWT y sesiones | [SeguridadApis](<../DOCS/SeguridadApis.pdf>) | 11 |
| 6 | [FinCorp: consistencia y coordinación](05-caso-fincorp-sagas-cqrs.md) | Saga por coreografía, idempotencia, CQRS, concurrencia y alternativas ante un débito fallido | [FinCorp Architectural Strategy](<../DOCS/FinCorp_Architectural_Strategy.pdf>) | 3 |
| 7 | [VitalsGuard: IoT y contención de fallos](06-caso-vitalsguard-iot-resiliencia.md) | MQTT, eventos, procesamiento de flujos, latencia, Circuit Breaker, Bulkhead y observabilidad | [VitalsGuard Critical Architecture](<../DOCS/VitalsGuard_Critical_Architecture.pdf>) | 3 |

Los dos documentos de la Unidad 2 se mantienen separados porque aportan contenidos diferentes y complementarios. Su nombre de archivo no permite afirmar cuál es más reciente ni cuál sustituye al otro. Seguridad y los casos prácticos se identifican por su tema: no se les asigna una clase que el documento no indique.

## Cómo estudiar con estas guías

1. Lee la idea central e identifica el problema que intenta resolver el tema.
2. Sigue el ejemplo y explica el recorrido de una petición o evento con tus propias palabras.
3. Compara las alternativas: qué responsabilidad asume cada una y qué costo introduce.
4. Responde las preguntas antes de leer sus respuestas breves.
5. Consulta las páginas indicadas del PDF para volver al diagrama o formulación del material.

Para una primera lectura, sigue el orden de la tabla. Para aplicar los conceptos, estudia después FinCorp y VitalsGuard: obligan a combinar patrones y evaluar lo que ocurre cuando falla una operación.

## Dónde encontrar cada tema

| Si quieres entender... | Consulta |
|---|---|
| Qué promete una API y cómo se usa | [Introducción](01-introduccion-apis.md) y [contratos](03-diseno-api-producto-contratos.md) |
| REST frente a GraphQL, gRPC o eventos | [Comunicación y resiliencia](02-02-estilos-comunicacion-resiliencia.md) |
| Gateway frente a BFF y composición de respuestas | [Arquitectura y patrones](02-01-arquitectura-capas-patrones.md) y [comunicación](02-02-estilos-comunicacion-resiliencia.md) |
| CQRS frente a Event Sourcing | [Comunicación y resiliencia](02-02-estilos-comunicacion-resiliencia.md) y [FinCorp](05-caso-fincorp-sagas-cqrs.md) |
| Qué hacer cuando una dependencia se vuelve lenta | [Comunicación y resiliencia](02-02-estilos-comunicacion-resiliencia.md) y [VitalsGuard](06-caso-vitalsguard-iot-resiliencia.md) |
| Autenticación frente a autorización, OAuth, JWT y Bearer | [Seguridad](04-seguridad-autenticacion-autorizacion.md) |
| Cambios compatibles, paginación y facilidad de integración | [Diseño como producto](03-diseno-api-producto-contratos.md) |
| Cómo evitar repetir un efecto o coordinar operaciones distribuidas | [FinCorp](05-caso-fincorp-sagas-cqrs.md) |

## Fuentes y alcance

Los PDF locales son la fuente principal. Cada guía enlaza su presentación e indica las páginas asociadas a los temas. Se revisaron el texto, los ejemplos y los diagramas; varios documentos contienen diapositivas como imágenes.

Los ejemplos añadidos se identifican como **elaboración didáctica**, **ejemplo didáctico** o **análisis didáctico**. Sirven para explicar el mecanismo; no se atribuyen al profesor ni constituyen implementaciones ejecutadas. En FinCorp y VitalsGuard, las alternativas desarrollan las preguntas abiertas del caso y no se presentan como una solución oficial del documento.

Las aclaraciones técnicas puntuales incluyen enlaces a documentación oficial cuando es necesario distinguir una simplificación del material de una regla general. Por ejemplo, GraphQL admite consultas mediante GET, CQRS no exige siempre dos bases de datos y el flujo OAuth de PayPal distingue la obtención del token del uso posterior de Bearer.

Las tablas cualitativas de rendimiento, las cifras de latencia y las menciones a estándares se interpretan dentro del ejercicio. No se convierten en resultados de pruebas, garantías universales ni certificaciones. Para tomar una decisión real harían falta requisitos concretos, mediciones y validación del entorno.

Los PDF originales permanecen intactos. Esta carpeta contiene únicamente material de estudio en Markdown.
