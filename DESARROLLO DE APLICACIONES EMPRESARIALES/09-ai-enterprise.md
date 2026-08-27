# Resumen — Clase 09: Inteligencia Artificial en Aplicaciones Empresariales

Fuente: [`presentaciones/09-ai-enterprise.md`](../presentaciones/09-ai-enterprise.md)

## Idea central

Los LLMs pueden incorporarse como componentes de una aplicación, pero no se comportan como código determinista. Su integración exige controlar costo, latencia, privacidad, alucinaciones, permisos y evaluaciones de calidad.

## Formas de integrar inteligencia artificial

- **Modelo propio:** máximo control, pero gran inversión en datos, cómputo y especialistas.
- **LLM mediante API:** integración rápida y costo variable por tokens, con dependencia del proveedor.
- **Modelo open-weight:** mayor control y posibilidad de self-hosting, a cambio de operar infraestructura y actualizaciones.

La elección depende del volumen, privacidad, capacidad técnica y costo total. Self-hosting solo se justifica cuando el uso o las restricciones compensan mantener GPUs y plataforma propia.

## LLM frente a código tradicional

El código convencional entrega el mismo resultado para el mismo input, permite excepciones predecibles y se prueba con comparaciones exactas. Un LLM puede producir respuestas distintas, tiene latencia de segundos y cobra por tokens.

Por ello necesita:

- Evaluaciones semánticas.
- Métricas de calidad y costo.
- Registro de prompts, modelo y versión.
- Validación estricta de las respuestas.

## Patrones principales

### RAG

Recupera fragmentos relevantes de documentos propios y los incluye como contexto del modelo. Normalmente utiliza división de documentos, embeddings, una base vectorial, búsqueda y generación de respuesta. Reduce alucinaciones, pero no las elimina.

### Agentes y herramientas

El modelo decide cuándo llamar APIs empresariales, por ejemplo consultar inventario o crear un ticket. Las herramientas deben aplicar autenticación, autorización, validación y privilegio mínimo. Las acciones irreversibles necesitan confirmación humana externa al LLM.

### Pipelines

El modelo actúa como clasificador o extractor dentro de un proceso controlado. Puede convertir facturas, correos o tickets en JSON estructurado. Es útil cuando el input es lenguaje natural ambiguo y las reglas manuales serían difíciles de mantener.

Los tres patrones pueden combinarse en una misma solución.

## Herramientas en Java

- **Spring AI:** integración natural con Spring, modelos, RAG, ChatClient y herramientas.
- **LangChain4j:** biblioteca independiente con soporte maduro para agentes, RAG y múltiples proveedores.

Estas bibliotecas reducen código de integración, pero no reemplazan las decisiones de seguridad, evaluación y gobierno.

## Latencia, costo y determinismo

El tiempo de respuesta incluye Time To First Token y generación progresiva. Puede mejorarse con streaming, caché y modelos pequeños para tareas sencillas.

Para extracción se recomienda temperatura baja y salida JSON validada. Un `seed` puede ayudar a reproducir resultados, pero no garantiza determinismo absoluto.

## Alucinaciones y privacidad

Las alucinaciones son un riesgo que debe administrarse con RAG, citas, evaluaciones automáticas y revisión humana en decisiones críticas.

Antes de enviar información a un modelo deben revisarse:

- Datos personales o confidenciales incluidos en el prompt.
- Retención y uso de datos por el proveedor.
- Contratos de procesamiento de datos.
- Regulación aplicable y ubicación de la información.

## Seguridad para LLMs

OWASP incluye riesgos como prompt injection, exposición de información, envenenamiento de datos, manejo incorrecto de salidas, agencia excesiva y consumo no limitado.

Mitigaciones importantes:

- Separar instrucciones de datos del usuario.
- Validar formato y contenido de la salida.
- Ofrecer al agente únicamente las herramientas necesarias.
- Requerir confirmación humana para acciones críticas.
- Registrar prompts y establecer límites de tokens, tiempo y presupuesto.

## Cuándo no utilizar un LLM

No conviene para cálculos exactos, reglas simples, consultas que pueden resolverse directamente con SQL, latencia extremadamente baja o decisiones médicas, legales y financieras sin supervisión.

## Para recordar

> Si el comportamiento puede expresarse claramente como una función determinista, debe implementarse con código. Un LLM aporta valor cuando el problema requiere interpretar lenguaje natural o manejar ambigüedad.
