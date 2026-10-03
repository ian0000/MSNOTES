# Documento complementario · Seguridad, autenticación y autorización de APIs

**Fuente principal:** [SeguridadApis.pdf](<../DOCS/SeguridadApis.pdf>) · 11 páginas.

**Ruta de lectura:** seguridad y anatomía, pp. 1-4; AuthN y AuthZ, p. 5; encabezados, p. 6; API keys, p. 7; OAuth, p. 8; JWT, p. 9; PayPal y comparación, pp. 10-11.

## 1. Idea central: conocer identidad no concede todos los permisos

La seguridad de una API controla quién puede acceder, qué puede hacer y sobre qué recursos. El documento relaciona esta necesidad con interoperabilidad, modularidad y escalabilidad: abrir capacidades aumenta su utilidad, pero exige reglas de acceso claras.

Una credencial válida no sustituye una comprobación de permisos. Por ejemplo, un estudiante autenticado puede consultar su matrícula, pero no la de cualquier otro identificador. La autorización debe considerar la acción y el recurso concreto, además del rol general.

## 2. La anatomía de ocho niveles del material

La p. 3 representa endpoints, métodos, recursos, parámetros, formato, estados, autenticación/autorización y documentación. La p. 4 ubica credenciales en encabezados y distingue mecanismos de acceso.

Estas capas son una organización didáctica de la API. El título «capa 7» no debe interpretarse como que JWT forma una nueva capa del modelo OSI, ni que el número de la capa de parámetros indique un nivel de transporte de red.

Los controles se relacionan con todas las partes: una ruta necesita autorización sobre el recurso; un cuerpo necesita validación; un error no debe revelar datos indebidos; la documentación debe explicar cómo obtener y utilizar credenciales.

## 3. AuthN frente a AuthZ

**Autenticación**, AuthN, comprueba la identidad del participante mediante un mecanismo confiable. **Autorización**, AuthZ, determina si ese participante puede realizar una operación.

| Situación didáctica | Resultado conceptual |
|---|---|
| No se presenta una credencial válida. | Falla autenticación. |
| El usuario es válido, pero intenta leer datos ajenos. | Falla autorización. |
| El usuario tiene permiso y el recurso no existe. | La consulta falla por disponibilidad del recurso. |
| La identidad y el permiso son válidos, pero el cuerpo es incorrecto. | Falla validación de datos. |

Estas comprobaciones no son equivalentes y no deben mezclarse en un único mensaje ambiguo. En HTTP, `401` suele indicar falta de autenticación válida y `403`, acceso denegado. El servicio también puede ocultar la existencia de un recurso según su política.

## 4. Encabezados: transportar una credencial

La p. 6 muestra `Authorization: <Tipo> <Credenciales>`. El tipo determina cómo interpretar lo enviado.

```http
Authorization: Bearer TOKEN_DE_EJEMPLO
```

**Bearer** significa que quien posee el token puede presentarlo para obtener el acceso que este concede. No garantiza que el token sea JWT: también puede ser opaco, es decir, no contener información legible para el cliente.

**Basic** transmite usuario y contraseña codificados en Base64. Base64 es una codificación reversible, no cifrado. En el caso de un cliente de aplicación pueden ser identificador y secreto del cliente. HTTPS protege el canal; ocultar visualmente una cadena no la hace segura.

Los encabezados no cifran datos por sí solos. Evitar credenciales en la URL reduce su exposición en registros y enlaces, pero también se deben controlar los logs de encabezados y proteger el transporte.

## 5. API keys: identificar consumidores de una aplicación

La p. 7 compara una API key con una tarjeta de identificación. El proveedor reconoce una clave y la relaciona con un consumidor, permisos o un plan de consumo.

**Elaboración didáctica:** un socio autorizado consulta un catálogo:

```http
GET /catalogo HTTP/1.1
Host: proveedor.example
X-API-Key: CLAVE_FICTICIA
```

El servidor valida que la clave exista y esté activa, aplica su política y decide qué puede devolver. El nombre del encabezado es parte del contrato, no un estándar único para toda API key.

Su utilidad incluye identificar aplicaciones, atribuir consumo y administrar cuotas. Su limitación es que una clave compartida no demuestra qué persona ejecutó cada operación. Una clave entregada a una aplicación pública tampoco puede tratarse como un secreto imposible de extraer.

El PDF presenta claves sin vencimiento automático. Es una configuración frecuente, pero no una propiedad obligatoria: pueden tener vencimiento y políticas de revocación. La rotación requiere una transición que permita reemplazarlas sin cortar consumidores legítimos.

## 6. OAuth 2.0: autorización delegada

OAuth es un marco de autorización. La p. 8 utiliza la metáfora de una llave que permite una acción limitada sin entregar la contraseña principal.

Sus participantes son:

- **Propietario del recurso:** persona o entidad que puede autorizar acceso.
- **Cliente:** aplicación que solicita utilizar capacidades.
- **Servidor de autorización:** evalúa la solicitud y emite credenciales de acceso.
- **Servidor de recursos:** expone la API y valida acceso.

Un **scope** delimita capacidades, como consultar documentos. No sustituye necesariamente una regla por recurso: un permiso de lectura debe combinarse con comprobar qué documentos pertenecen al usuario.

### Recorrido conceptual — elaboración didáctica

1. Una aplicación solicita autorización para consultar certificados.
2. El usuario autoriza mediante el proveedor de identidad y permisos.
3. La aplicación obtiene un token de acceso mediante el flujo acordado.
4. Presenta el token a la API.
5. La API valida el token y los permisos sobre el certificado.

Este recorrido resume la colaboración; no representa todos los mensajes del protocolo. En aplicaciones que usan autorización por código, PKCE vincula el intercambio del código con el cliente que inició el proceso. Los flujos reales requieren configuración y controles adicionales; las [prácticas de seguridad OAuth, RFC 9700](https://www.rfc-editor.org/rfc/rfc9700) desarrollan esos requisitos.

**Aclaración:** OAuth no es por sí solo un protocolo de inicio de sesión del usuario. OpenID Connect añade una capa de identidad sobre OAuth. También existen flujos entre aplicaciones sin una persona autorizando cada petición. La [definición de OAuth 2.0, RFC 6749](https://www.rfc-editor.org/rfc/rfc6749) distingue cliente, servidor de autorización y servidor de recursos.

## 7. JWT: un formato, no una política completa

**JWT**, JSON Web Token, es un formato para transportar afirmaciones o **claims**. La p. 9 muestra el caso habitual firmado, organizado en header, payload y firma.

- **Header:** declara información como algoritmo y tipo.
- **Payload:** contiene afirmaciones como emisor, destinatario, sujeto o expiración.
- **Firma:** permite comprobar integridad y procedencia bajo un mecanismo de confianza definido.

```text
HEADER_CODIFICADO.PAYLOAD_CODIFICADO.FIRMA
```

Este ejemplo es ficticio. En un JWT firmado, el payload se puede decodificar; no debe confundirse firma con cifrado. Un dato sensible no queda oculto solo por introducirlo en el token.

Validar exige comprobar firma con claves confiables, algoritmos permitidos, emisor, audiencia y condiciones temporales. Decodificar el contenido sin comprobarlo no autentica nada. La [RFC 8725 sobre seguridad de JWT](https://www.rfc-editor.org/rfc/rfc8725) respalda estas verificaciones.

Un token firmado puede reducir consultas para validar cada petición, pero no elimina todo estado. Revocación, rotación de claves, cambios de permisos y otras condiciones pueden requerir consulta o seguimiento adicional. Las afirmaciones pueden permanecer válidas hasta expirar aunque el usuario haya cambiado de rol, si no existe otra política.

## 8. Cómo se relacionan OAuth, JWT y Bearer

OAuth define cómo se concede acceso; JWT define una forma de representar información; Bearer define una forma de presentar el token. Pueden aparecer juntos, pero no son equivalentes.

Un servidor OAuth puede emitir tokens opacos. Un JWT puede existir fuera de OAuth. Una API key puede utilizar un encabezado diferente de `Authorization`. Clasificar estos elementos como cuatro reemplazos idénticos confunde funciones.

| Elemento | Pregunta principal |
|---|---|
| API key | ¿Qué consumidor de aplicación presenta esta clave? |
| Basic | ¿Cómo se presentan ciertas credenciales? |
| OAuth | ¿Cómo se obtiene autorización de acceso? |
| JWT | ¿Cómo se representan afirmaciones verificables? |
| Bearer | ¿Cómo se presenta un token de acceso? |

## 9. Aclaración del ejemplo PayPal

La p. 10 presenta Basic y Bearer como dos opciones para la API de pagos. Conviene distinguir etapas: las credenciales de cliente se utilizan para obtener un access token, y las llamadas posteriores a la API presentan ese token como Bearer. No son dos encabezados intercambiables para toda operación de pago. La [documentación oficial de autenticación de PayPal](https://developer.paypal.com/api/rest/authentication/) explica ese intercambio. Los ejemplos de esta guía no contienen credenciales reales ni ejecutan operaciones.

## 10. Stateful y stateless: dónde se conserva el contexto

La comparación de la p. 11 relaciona mecanismos de acceso con conservación de estado. Para interpretarla, hay que separar el estado del negocio del contexto de autenticación. Guardar usuarios o facturas no convierte por sí solo una interacción en una sesión stateful.

**Stateful** implica que el servidor conserva contexto de sesión que necesita recuperar para atender peticiones posteriores. **Stateless**, en este sentido, permite procesar cada petición con el contexto de autenticación que presenta y las reglas de confianza configuradas. Un token verificable localmente facilita este último enfoque, aunque el sistema mantenga otros datos.

| Ejemplo didáctico | Cómo se valida | Consecuencia |
|---|---|---|
| Identificador de sesión | Se recupera una sesión conservada en el servidor. | Revocar esa sesión es directo, pero sus datos deben estar disponibles para las instancias que atienden. |
| Token opaco | Se consulta su validez en un almacén o servicio de autorización. | Facilita control central, a costa de la consulta y su dependencia. |
| JWT firmado con validación local | Se verifican firma y claims con claves confiables. | Reduce consultas, pero necesita una política ante expiración, revocación y cambios de permisos. |

Estas son configuraciones ilustrativas, no categorías inevitables. OAuth puede emitir tokens opacos o JWT; una API key puede consultarse en un almacén; un JWT puede combinarse con una lista de revocación. El nombre del mecanismo no determina por completo dónde se conserva estado.

**Ejemplo didáctico:** si una persona pierde permisos, una sesión central puede invalidarse inmediatamente en ese almacén. Un JWT aceptado solo por su firma y expiración podría seguir siendo válido hasta vencer. La decisión debe considerar cuánto tiempo de desfase tolera la aplicación y qué controles adicionales necesita.

## 11. Errores que conviene reconocer

Confundir autenticación con autorización deja acceso a recursos ajenos. Confundir codificación con cifrado expone información. Considerar JWT como autorización completa evita revisar reglas por recurso. Registrar tokens completos facilita su reutilización por terceros. Considerar todo tráfico interno confiable permite que una identidad comprometida tenga más acceso del debido.

La seguridad también necesita validación de entradas, límites y observabilidad. Estos temas complementan el documento: los tres mecanismos de acceso no cubren por sí solos todos los riesgos del servicio.

## Síntesis

La identidad, los permisos, el transporte y el formato del token tienen responsabilidades diferentes. El contrato debe explicar cómo se obtiene acceso y la implementación debe verificar quién solicita qué recurso. Una API protegida es aquella que aplica esa decisión de forma consistente, no simplemente la que recibe un encabezado.

## Preguntas de repaso

1. **¿Qué diferencia AuthN de AuthZ?** Verificar identidad frente a comprobar permisos.
2. **¿Un JWT firmado está cifrado?** No necesariamente; la firma protege integridad, no confidencialidad.
3. **¿Todo token OAuth es JWT?** No; puede ser opaco.
4. **¿Una API key identifica siempre una persona?** No; normalmente identifica al consumidor de aplicación.
5. **¿Leer el payload valida el token?** No; faltan verificaciones de confianza y condiciones.
6. **¿OAuth equivale a login?** No; es autorización, y OpenID Connect agrega identidad.
7. **¿Basic y Bearer siempre se intercambian?** No; sus usos dependen de la operación y el contrato.
8. **¿Un scope elimina controles sobre el recurso?** No; aún deben aplicarse reglas de pertenencia y permisos concretos.
9. **¿OAuth o JWT determinan por sí solos si todo el sistema es stateless?** No; depende de cómo se validan las credenciales y se gestionan sesiones, revocación y permisos.

[Volver al índice](README.md) · [Aplicación práctica: FinCorp](05-caso-fincorp-sagas-cqrs.md)
