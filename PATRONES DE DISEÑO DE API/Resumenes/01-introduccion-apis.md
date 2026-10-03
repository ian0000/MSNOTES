# Unidad 1 · Introducción a sistemas basados en APIs

**Fuente:** [Unidad 1 Introducción APIs.pdf](<../DOCS/Unidad 1 Introducción APIs.pdf>) · 20 páginas. Las portadas identifican a la Ing. Patsy Malena Prieto MSc. como autora.

**Ruta de lectura:** definición y objetivos, pp. 4-6; anatomía y naturaleza, pp. 7-8; ecosistemas y monetización, pp. 9-14; casos de integración, pp. 15-18; ciclo de vida, p. 19.

## 1. Idea central: una API es un contrato de colaboración

Una **API**, interfaz de programación de aplicaciones, establece cómo un programa puede utilizar capacidades de otro. El consumidor conoce las operaciones disponibles, qué datos debe enviar, qué respuestas puede recibir y bajo qué condiciones tiene permiso para hacerlo. La implementación interna puede cambiar mientras el contrato siga siendo compatible.

La diapositiva del puente entre Software A y Software B enfatiza tres funciones: integración, estrategia e innovación. Por ejemplo, una plataforma de matrículas puede consultar disponibilidad de cupos en otro sistema sin compartir su base de datos ni estar escrita en el mismo lenguaje. La API transforma una capacidad interna en una forma de colaboración explícita.

No todas las APIs son APIs web: una biblioteca también expone una interfaz. Esta unidad se concentra en servicios accesibles por red. En ellos, además de las entradas y salidas, importan la latencia, los permisos, los errores y la evolución del contrato.

**Contrato** no significa únicamente un archivo de documentación. Incluye significados: si `precio` representa dólares, si `fecha` usa una zona horaria, si un pedido puede cancelarse y qué sucede cuando una petición se repite. Dos sistemas pueden intercambiar JSON válido y aun así interpretar datos de manera incompatible.

## 2. Objetivos del diseño: las cinco dimensiones

Las pp. 5-6 relacionan la API con necesidades técnicas y de negocio:

| Dimensión | Explicación | Ejemplo didáctico |
|---|---|---|
| Interoperabilidad | Sistemas distintos pueden intercambiar e interpretar información. | Un sistema Java consulta un catálogo implementado en Python. |
| Modularidad | Las responsabilidades tienen límites que reducen dependencias innecesarias. | La pantalla cambia sin alterar cómo se calcula una matrícula. |
| Escalabilidad | El servicio puede atender una demanda creciente con recursos adecuados. | Más usuarios consultan cupos al inicio del semestre. |
| Estandarización | Se comparten convenciones sobre operaciones, mensajes y errores. | Las consultas devuelven siempre identificadores y fechas con formatos definidos. |
| Seguridad | El acceso y las acciones se limitan a consumidores autorizados. | Un estudiante consulta sus datos, pero no modifica los de otro. |

La modularidad no garantiza que nunca cambie el frontend: si se elimina un dato que utiliza la pantalla, la integración se rompe. La ventaja es poder cambiar detalles internos sin obligar a todos los consumidores a adaptarse.

La escalabilidad tampoco equivale simplemente a incorporar inteligencia artificial. Es la capacidad de sostener carga; habilitar nuevos productos es una consecuencia posible de disponer de capacidades accesibles y datos bien definidos.

## 3. Anatomía de una petición y una respuesta

La p. 7 representa el recorrido cliente-servidor. Para entenderlo, conviene separar sus piezas:

- **Endpoint:** dirección expuesta para una operación. En HTTP, el método y la ruta ayudan a distinguirla.
- **Recurso:** entidad o colección con significado para el consumidor, como estudiantes o facturas.
- **Método:** expresa la intención de la solicitud, por ejemplo consultar con `GET`.
- **Parámetros:** datos en la ruta, consulta, encabezados o cuerpo.
- **Representación:** formato del mensaje, frecuentemente JSON.
- **Autenticación y autorización:** comprueban identidad y permisos.
- **Código de estado:** comunica el resultado a nivel HTTP.
- **Documentación:** explica condiciones de uso, mensajes y errores.

### Ejemplo paso a paso — elaboración didáctica

Una aplicación consulta las matrículas activas de un estudiante:

```http
GET /api/v1/estudiantes/42/matriculas?estado=activa HTTP/1.1
Host: universidad.example
Accept: application/json
Authorization: Bearer TOKEN_DE_EJEMPLO
```

1. `GET` solicita información.
2. `42` es un parámetro de ruta que identifica al estudiante.
3. `estado=activa` filtra una colección mediante la consulta.
4. `Accept` expresa el formato que el consumidor espera recibir.
5. El encabezado de autorización presenta una credencial ficticia.
6. El servidor comprueba que el solicitante puede consultar ese estudiante.
7. La respuesta devuelve una representación, no acceso directo a tablas internas.

```json
{
  "estudianteId": "42",
  "matriculas": [
    {"id": "MAT-12", "periodo": "2026-B", "estado": "activa"}
  ]
}
```

La respuesta podría tener estado `200`. Si faltan credenciales válidas, puede corresponder `401`; si el consumidor está autenticado pero carece de permiso, `403`. Un recurso inexistente puede producir `404`. Un arreglo vacío puede ser una respuesta exitosa cuando la colección existe pero no tiene coincidencias.

## 4. Naturaleza de las APIs

La p. 8 propone cuatro características que ayudan a estudiar el contrato:

**Intermediarias:** conectan sistemas heterogéneos. El consumidor no tiene que conocer el lenguaje de implementación ni la distribución física de los componentes.

**Abstractas:** exponen capacidades y ocultan complejidad interna. Solicitar una factura puede involucrar validación, almacenamiento y un proveedor externo, sin que el cliente tenga que ejecutar cada paso por separado.

**Contractuales:** definen entradas, salidas y reglas previsibles. La abstracción necesita precisión para resultar útil: esconder el funcionamiento interno no justifica ocultar los errores o las restricciones.

**Evolutivas:** deben adaptarse a nuevos requisitos preservando integraciones cuando sea posible. Una nueva versión necesita documentación, transición y retiro planificado.

## 5. Ecosistemas: interno, de socios y público

Las pp. 9-13 clasifican las APIs según su audiencia:

| Ecosistema | Consumidores | Propósito habitual | Necesidad de gestión |
|---|---|---|---|
| Interno | Equipos de una organización. | Reutilizar capacidades y mejorar coordinación. | Propiedad clara, permisos y contratos entre equipos. |
| De socios | Empresas o aliados identificados. | Integración B2B, ventas y operaciones compartidas. | Acuerdos, incorporación controlada y soporte. |
| Público | Desarrolladores externos. | Crear un ecosistema de productos y servicios. | Documentación accesible, cuotas y autoservicio. |

El PDF utiliza Netflix, Expedia, PayPal, NASA, Google Maps y un servicio de facturación como ejemplos de ecosistemas. Son referencias pedagógicas del material, no verificaciones de sus condiciones comerciales actuales. Una empresa puede exponer capacidades internas, públicas y de socios al mismo tiempo: clasificar toda la organización en una sola casilla simplifica demasiado.

**Pública** no significa sin autenticación ni gratuita. **Interna** no significa confiable por defecto. La red de acceso ayuda a delimitar consumidores, pero los permisos se deben definir sobre recursos y acciones.

## 6. Monetización y valor indirecto

La p. 14 distingue pago por uso, freemium, suscripciones por niveles, participación en ingresos, monetización indirecta y modelos híbridos.

En **pago por uso**, se cobra una unidad consumida, como una consulta o un documento procesado. En **freemium**, una oferta limitada facilita probar el producto y una oferta de pago amplía capacidades. Una **suscripción** ofrece un conjunto de condiciones durante un período. La **participación en ingresos** vincula el cobro con una operación comercial. El **valor indirecto** aparece cuando la API aumenta ventas, retención o eficiencia de otro producto.

Ejemplo didáctico: una API de consultas de cupos puede no cobrar por petición, pero reducir llamadas telefónicas y facilitar matrículas. Su valor se mide en tiempo ahorrado y procesos completados. No hace falta convertir toda API en un producto de cobro directo.

## 7. Cómo leer los casos del material

En Open Banking, pp. 15-16, el esquema conecta banco, gateway y aplicaciones financieras. El punto de estudio es la exposición controlada de capacidades mediante consentimiento y permisos. Este resumen explica la arquitectura conceptual del caso; no establece obligaciones regulatorias de una jurisdicción.

En facturación, p. 17, la API traduce entre aplicaciones empresariales y procesos externos. Una respuesta que indica que se creó un recurso no demuestra por sí sola que una autoridad externa lo haya autorizado: son estados distintos del negocio.

En Netflix, p. 18, la heterogeneidad de dispositivos ilustra por qué conviene ofrecer capacidades comunes sin obligar a cada interfaz a conocer toda la infraestructura.

## 8. Ciclo de vida y errores habituales

La p. 19 divide el ciclo en diseño, evolución y retiro. Diseñar significa acordar el contrato; evolucionar, introducir mejoras y gestionar compatibilidad; retirar, comunicar fechas, migrar consumidores y cerrar acceso de manera controlada.

Un `301` no reemplaza un plan de migración: una ruta nueva puede requerir datos o reglas diferentes. Un `410` expresa que el recurso dejó de estar disponible de forma deliberada; `404` no comunica por sí mismo toda esa historia.

Los errores más comunes son pensar que API equivale a base de datos expuesta, confundir una URL con el contrato completo, considerar que seguridad solo significa enviar un token y cambiar campos sin revisar consumidores.

## Síntesis

Una API convierte capacidades del sistema en un contrato utilizable. Su calidad depende de la claridad técnica, la audiencia, la seguridad, la evolución y el valor que produce. El diseño empieza por quién necesita qué capacidad y termina en decisiones concretas sobre mensajes, permisos y comportamiento.

## Preguntas de repaso

1. **¿Por qué una API es un contrato?** Porque establece operaciones, datos, reglas y resultados que ambos participantes deben interpretar igual.
2. **¿Qué diferencia hay entre recurso y endpoint?** El recurso tiene significado de dominio; el endpoint es una forma de acceder a una operación sobre él.
3. **¿Una API pública debe ser gratuita?** No; audiencia y modelo de cobro son dimensiones diferentes.
4. **¿Por qué la modularidad facilita cambios?** Porque permite sustituir detalles internos manteniendo las responsabilidades y el contrato.
5. **¿Qué distingue autenticación de autorización?** La primera verifica identidad; la segunda determina qué acciones y recursos están permitidos.
6. **¿Cuándo genera valor una API interna?** Cuando reduce esfuerzo, reutiliza capacidades o mejora procesos internos, aunque no cobre directamente.
7. **¿Basta con publicar una versión nueva?** No; hacen falta compatibilidad, comunicación y una estrategia de migración.

[Volver al índice](README.md) · [Continuar con arquitectura y patrones](02-01-arquitectura-capas-patrones.md)
