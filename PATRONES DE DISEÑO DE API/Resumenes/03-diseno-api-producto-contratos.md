# Unidad 3 · Diseñar una API como producto y contrato

**Fuente:** [_Unidad 3 Diseño de una APIs v_compressed (1).pdf](<../DOCS/_Unidad 3 Diseño de una APIs v_compressed (1).pdf>) · 15 páginas. Autora indicada en la portada: Ing. Patsy Malena Prieto MSc.

**Ruta de lectura:** cadena de valor, pp. 4-5; alineación y propuesta, pp. 6-8; contrato y datos, pp. 9-10; adopción, pp. 11-12; operación y cambios, pp. 13-15.

## 1. Idea central: empezar por la capacidad que necesita el consumidor

El diseño de una API conecta objetivos de negocio con una experiencia técnica utilizable. Tener endpoints no demuestra que un consumidor pueda completar su tarea. Hay que identificar quién la utiliza, qué problema resuelve, cómo empieza y qué condiciones permiten llevarla a producción.

La p. 4 dibuja una cadena desde sistemas de registro y datos heredados hasta aplicaciones y socios. La integración transforma capacidades internas en una oferta accesible. **Sistema de registro** es la fuente autoritativa de ciertos datos; **sistema de interacción** es la aplicación con la que trabaja el usuario. La API ayuda a conectarlos sin copiar su complejidad directamente a cada cliente.

## 2. Cadena de valor: conectividad, canales y ecosistemas

La p. 5 presenta una progresión desde integración básica hasta monetización y diferenciación. Conectar ERP y CRM puede mejorar productividad; ofrecer acceso móvil habilita nuevos canales; incorporar socios permite nuevos productos y procesos compartidos.

El valor no siempre consiste en cobrar por petición. Una API de logística puede disminuir consultas manuales, reducir errores de seguimiento y facilitar nuevas ventas. Para diseñarla conviene formular una hipótesis comprobable: qué tarea mejorará y cómo se medirá.

**Ejemplo didáctico:** una institución quiere reducir el tiempo de entrega de certificados. La capacidad útil es solicitar y consultar certificados, con estados comprensibles. Exponer las tablas internas de estudiantes no resuelve necesariamente esa tarea.

## 3. Alineación top-down: seis capas

La p. 6 conecta metas corporativas, arquitectura de negocio, arquitectura de aplicaciones, arquitectura técnica, calidad de servicio de API y definición del contrato.

| Nivel | Pregunta | Ejemplo didáctico |
|---|---|---|
| Metas | ¿Qué resultado busca la organización? | Reducir espera para certificados. |
| Negocio | ¿Qué proceso permite alcanzarlo? | Solicitud, validación y emisión. |
| Aplicaciones | ¿Qué sistemas participan? | Académico, pagos y generación documental. |
| Técnica | ¿Qué infraestructura los sostiene? | Almacenamiento y procesamiento de documentos. |
| API QoS | ¿Qué condiciones de servicio se necesitan? | Límites, seguridad y tiempos medibles. |
| Definición | ¿Qué mensajes y operaciones se exponen? | Crear solicitud y consultar estado. |

**QoS**, calidad de servicio, agrupa condiciones técnicas de atención. Un **SLA** es un acuerdo de nivel de servicio; no basta con escribir un porcentaje si nadie mide cómo se calcula o qué operaciones incluye.

La pirámide invita a conectar decisiones, no obliga a un proceso rígido sin retroalimentación. Si el costo de una condición técnica es excesivo, también conviene revisar la promesa empresarial.

## 4. Propuesta de valor y perfil del usuario

La p. 7 relaciona tareas, frustraciones y beneficios del consumidor con capacidades de la API. Una SDK, biblioteca que facilita consumir la API, puede aliviar trabajo de integración; un webhook puede eliminar consultas periódicas; una documentación clara puede reducir pruebas por ensayo y error.

El material utiliza Lingo24 para ilustrar ofertas distintas según perfiles de usuarios. La lección es reconocer necesidades diferentes, no suponer que un único contrato muy amplio resulta adecuado para todos.

**Elaboración didáctica:** un socio técnico necesita procesar miles de certificados; un estudiante consulta uno. El primero requiere operaciones de lote, estados y límites claros; el segundo, una interfaz sencilla. La capacidad común puede mantenerse mientras la experiencia se adapta.

## 5. Modelos de negocio

La p. 8 compara audiencia interna o externa con generación de valor directa o indirecta. Los cuadrantes permiten distinguir agilidad interna, retención y canales, cobro directo y crecimiento con socios.

El caso de Netflix se utiliza como ejemplo de realineación del programa de APIs. La guía conserva su función pedagógica sin presentar los detalles históricos del PDF como una cronología verificada. El criterio importante es decidir si la audiencia elegida aporta al objetivo central.

## 6. API-first y modelo de datos

**API-first** significa acordar el contrato antes de implementar sus detalles. La p. 9 menciona OpenAPI, archivos `.proto` y AsyncAPI como representaciones de contratos para diferentes interacciones.

**OpenAPI** describe operaciones HTTP, mensajes y seguridad. **Protocol Buffers** define mensajes y puede describir servicios gRPC. **AsyncAPI** describe contratos de comunicación orientada a mensajes. No son archivos intercambiables: cada uno corresponde a una forma de interacción.

Un contrato permite revisar ejemplos, generar simulaciones y coordinar equipos. También necesita pruebas que comprueben que la implementación lo cumple.

### Ejemplo de recurso — elaboración didáctica

```json
{
  "id": "SOL-42",
  "tipo": "certificado-estudios",
  "estado": "en-proceso",
  "creadaEn": "2026-10-03T14:00:00Z"
}
```

El identificador, el estado y el instante tienen significado para el consumidor. No hace falta exponer columnas auxiliares ni rutas internas de archivos. El contrato debe definir estados posibles y sus transiciones, además del formato.

Diseñar datos orientados al consumidor implica definir unidades, obligatoriedad, ausencia, errores y paginación. `null`, campo ausente y arreglo vacío no deberían usarse indistintamente si representan situaciones distintas.

## 7. Paginación y agregación

La p. 10 contrapone paginación por offset con paginación por cursor.

Con **offset**, el consumidor solicita una posición: `?limit=20&offset=40`. Es fácil de comprender, pero los datos nuevos pueden desplazar posiciones y offsets grandes pueden resultar costosos.

Con **cursor**, el cliente envía una referencia opaca para continuar: `?limit=20&cursor=CURSOR_DE_EJEMPLO`. El servidor mantiene una estrategia de orden estable y devuelve el siguiente cursor.

```json
{
  "items": [{"id": "SOL-42", "estado": "en-proceso"}],
  "nextCursor": "CURSOR_SIGUIENTE"
}
```

El cursor no garantiza automáticamente una fotografía inmutable de todo el conjunto. Hay que definir orden y comportamiento frente a cambios. Offset sigue siendo razonable para conjuntos pequeños y consultas que necesitan salto a páginas.

La agregación reúne datos útiles de varios servicios en una respuesta. BFF y API Composition ayudan, pero deben definir qué información falta cuando un componente no responde y evitar promesas de consistencia que no pueden sostener.

## 8. Experiencia del desarrollador y adopción

La p. 11 distingue **TTFHW**, tiempo hasta la primera llamada exitosa, y **TTFPA**, tiempo hasta una aplicación que produce valor. El primero mide incorporación; el segundo, éxito de integración.

Un consumidor puede ejecutar un ejemplo rápidamente y luego fallar al manejar errores, renovar credenciales o interpretar webhooks. Una buena experiencia requiere documentación, entorno de prueba, ejemplos completos, soporte y estabilidad.

La p. 12 muestra un programa que combina portal, comunidad, eventos, comunicación, pilotos y medición. El portal es una parte del programa; no reemplaza soporte ni gestión de cambios.

**Ejemplo didáctico:** medir primera consulta de certificado ayuda a evaluar el inicio; medir cuántas solicitudes llegan a emisión real permite evaluar si la integración funciona en todo su recorrido.

## 9. Operación: balance entre costo, velocidad y fiabilidad

La p. 13 presenta un radar de costo, velocidad, calidad, flexibilidad y fiabilidad. Más libertad para consultas puede trasladar costo de procesamiento al proveedor. Menor latencia puede requerir caché y tolerar información menos reciente.

Los controles tienen efectos distintos: caché reutiliza respuestas; throttling regula consumo; versionado organiza evolución; controles de acceso limitan acciones. Antes de añadirlos conviene definir qué problema y qué métrica pretenden mejorar.

## 10. Cambios compatibles e incompatibles

La p. 14 diferencia breaking changes de cambios aditivos. Eliminar una operación o cambiar el tipo de un campo puede romper consumidores. Añadir una operación suele ser compatible. Añadir un parámetro nuevo **obligatorio** también puede romper, aunque sea una adición.

Incluso un campo opcional o un nuevo valor de enumeración puede afectar a clientes demasiado estrictos. La compatibilidad se evalúa sobre consumidores y contrato, no solo sobre si se añadieron líneas.

**Ejemplo didáctico:** convertir `precio` de número a objeto con moneda cambia la forma de lectura. Puede justificar una versión nueva y una migración. Añadir `descripcionOpcional` sin alterar lo existente suele permitir continuidad, si los consumidores toleran campos desconocidos.

El PDF usa Stripe para destacar estabilidad por versión. La enseñanza es mantener compromisos y migraciones previsibles; no implica que una promesa de compatibilidad sea automática ni ilimitada.

## Síntesis

La p. 15 reúne alineación estratégica, experiencia del desarrollador y operación robusta. Diseñar bien significa que el consumidor puede completar una tarea valiosa con un contrato comprensible, estable y operable. El modelo de datos, los límites y el plan de cambios forman parte del producto.

## Preguntas de repaso

1. **¿API-first significa escribir todo el backend antes del frontend?** No; significa coordinar el contrato antes de sus implementaciones.
2. **¿Por qué una API no debería copiar automáticamente sus tablas?** Porque el consumidor necesita capacidades y representaciones estables, no detalles de persistencia.
3. **¿Qué mide TTFHW?** El tiempo hasta la primera interacción exitosa.
4. **¿Qué agrega TTFPA?** Una medida de integración que ya produce valor.
5. **¿Añadir un parámetro siempre conserva compatibilidad?** No; si es obligatorio puede romper clientes existentes.
6. **¿El cursor elimina todos los problemas de concurrencia?** No; exige reglas sobre orden y cambios.
7. **¿Qué diferencia valor directo de indirecto?** Cobro por la oferta frente a beneficios en otros productos o procesos.

[Volver al índice](README.md) · [Continuar con seguridad](04-seguridad-autenticacion-autorizacion.md)
