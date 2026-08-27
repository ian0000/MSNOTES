# Resumen — Clase 05: Seguridad en Aplicaciones Web

Fuente: [`presentaciones/05-seguridad.md`](../presentaciones/05-seguridad.md)

## Idea central

La seguridad empresarial se construye por capas: identidad, cifrado, autorización, gestión de secretos, diseño seguro, infraestructura y observabilidad. TLS protege el transporte, OAuth delega acceso, OIDC identifica usuarios y Zero Trust evita confiar automáticamente en la red interna.

## PKI y certificados

Una infraestructura de clave pública incluye:

- Una autoridad certificadora que firma certificados.
- Certificados X.509 que relacionan identidad y clave pública.
- Una cadena de confianza desde el certificado del servicio hasta una CA raíz.
- CRL u OCSP para revocar certificados comprometidos.

La CA raíz se protege y normalmente firma autoridades intermedias, limitando el impacto de una filtración. PEM y DER representan certificados; PKCS#12 puede contener certificado y clave privada; un truststore contiene las autoridades en las que el servicio confía.

En mTLS, servidor y cliente presentan certificados. El canal queda cifrado y ambas partes autentican la identidad técnica de la otra.

## OAuth 2.0 y OpenID Connect

OAuth 2.0 permite que un cliente obtenga acceso sin recibir directamente las credenciales del usuario.

Roles principales:

- Resource Owner: dueño de los datos.
- Client: aplicación que solicita acceso.
- Authorization Server: autentica y emite tokens.
- Resource Server: API que valida el token.

Flujos principales:

- **Client Credentials:** comunicación máquina a máquina.
- **Authorization Code + PKCE:** aplicaciones web o móviles con usuario.

OIDC añade identidad sobre OAuth. El scope `openid` permite recibir un ID Token; scopes como `profile` y `email` agregan claims de usuario.

## JWT e Identity Providers

Un JWT contiene header, payload y firma. El backend debe verificar firma, emisor, audiencia, expiración y permisos; decodificar el token no equivale a validarlo.

Un Identity Provider centraliza usuarios, autenticación, emisión de tokens y políticas. Keycloak ofrece control self-hosted; Auth0 y Okta reducen operación mediante SaaS; Entra ID se integra con el ecosistema Microsoft.

## OWASP Top 10

Entre los riesgos principales están control de acceso roto, fallos criptográficos, inyección, diseño inseguro, configuraciones incorrectas, dependencias vulnerables, fallos de autenticación, problemas de integridad, monitoreo insuficiente y SSRF.

La autorización siempre debe verificarse en el backend. Autenticar a un usuario no significa que pueda acceder a cualquier objeto o ejecutar cualquier función.

## Seguridad en WebSockets

- `wss://` cifra el canal, pero no autentica por sí solo al usuario.
- El servidor debe validar token, permisos y `Origin` durante el handshake.
- La sesión debe cerrarse o renovarse cuando el token expire.
- Validar `Origin` evita que sitios externos utilicen las cookies del usuario para abrir conexiones.

## Gestión de secretos

Los secretos no deben guardarse en código, repositorios ni imágenes. Herramientas como gitleaks o truffleHog pueden encontrarlos incluso en el historial de Git.

HashiCorp Vault centraliza secretos, políticas, auditoría y rotación. Los Kubernetes Secrets solo usan Base64 de forma predeterminada; para mayor seguridad pueden combinarse con cifrado KMS, Sealed Secrets o External Secrets Operator.

## Zero Trust

Sus principios son:

- Verificar identidad, dispositivo y contexto en cada solicitud.
- Aplicar privilegio mínimo y tokens de corta duración.
- Asumir que una brecha ocurrirá y limitar su alcance.

En la práctica combina mTLS, OIDC, RBAC, scopes, NetworkPolicy, Vault y auditoría de comunicaciones.

## Tendencias

OAuth 2.1 elimina flujos inseguros, exige PKCE para clientes públicos y refuerza la rotación de refresh tokens y las URI de redirección exactas.

Las passkeys utilizan criptografía de clave pública y están ligadas al dominio. El servidor conserva únicamente una clave pública, lo que reduce el impacto de filtraciones y la efectividad del phishing tradicional.

## Para recordar

> Cifrar, autenticar y autorizar son responsabilidades distintas. Una aplicación segura debe cumplir las tres y gestionar correctamente secretos, permisos y auditoría.
