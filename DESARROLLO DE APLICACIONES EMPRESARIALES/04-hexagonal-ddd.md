# Resumen — Clase 04: Arquitectura Hexagonal y DDD Táctico

Fuente: [`presentaciones/04-hexagonal-ddd.md`](../presentaciones/04-hexagonal-ddd.md)

## Idea central

La arquitectura hexagonal separa las reglas del negocio de tecnologías como Spring, REST, JPA o PostgreSQL. DDD táctico ofrece bloques para modelar el negocio dentro de ese núcleo protegido.

## El problema del acoplamiento

En una arquitectura en capas mal aplicada, el servicio de negocio depende directamente de JPA, SQL o controladores web. Como consecuencia, cambiar de base de datos afecta el dominio y probar una regla puede requerir levantar todo el framework.

El hexágono representa la aplicación y contiene reglas, casos de uso y modelo de dominio. No sabe si es llamado mediante REST, CLI, mensajería o pruebas; tampoco sabe si persiste en una base de datos o en memoria.

## Actores, puertos y adaptadores

- **Actores primarios o drivers:** inician la interacción, como un usuario, una SPA, un controlador o una prueba.
- **Actores secundarios o driven:** proporcionan una capacidad externa, como una base de datos, correo o cola de mensajes.
- **Puertos de entrada:** interfaces que expresan los casos de uso ofrecidos por la aplicación.
- **Puertos de salida:** interfaces que expresan lo que el dominio necesita del exterior.
- **Adaptadores de entrada:** convierten REST, CLI o eventos en llamadas a casos de uso.
- **Adaptadores de salida:** implementan persistencia, correo o integraciones concretas.

Los puertos pertenecen al hexágono; los adaptadores son tecnología reemplazable alrededor de él. La dirección de las dependencias apunta hacia el dominio.

## Dependency Configurator

El configurador de dependencias es el único componente que conoce todas las implementaciones concretas. Primero crea los adaptadores de salida, luego los inyecta en la aplicación y finalmente conecta los adaptadores de entrada. El dominio recibe sus dependencias y nunca las construye directamente.

## Ventajas y límites

Ventajas:

- Pruebas unitarias sin framework ni base de datos.
- Sustitución de adaptadores reales por implementaciones en memoria.
- Cambio de REST a gRPC o de JPA a otra tecnología sin modificar las reglas.
- Fronteras claras y mayor mantenibilidad.

No suele justificarse para CRUD puro, scripts, prototipos rápidos o proyectos de vida corta, porque agrega interfaces, clases e indirección.

## Bloques de DDD táctico

- **Entity:** objeto con identidad que permanece aunque cambien sus atributos.
- **Value Object:** objeto inmutable definido por sus valores, como `Money` o `Email`.
- **Aggregate:** conjunto de entidades y value objects protegido por una raíz.
- **Domain Event:** hecho relevante del negocio expresado en pasado, como `OrderPlaced`.
- **Repository:** abstracción para guardar y recuperar raíces de agregados.
- **Domain Service:** regla que no pertenece naturalmente a una entidad concreta.

La raíz del agregado controla sus invariantes. Los objetos internos no deberían modificarse desde fuera sin pasar por ella.

## Bounded Context y casos de uso

Un bounded context define el límite dentro del cual un modelo conserva un significado específico. El concepto `Customer` puede tener atributos y reglas diferentes en ventas, facturación y soporte; no es necesario compartir una única clase global.

El Application Service o caso de uso:

- Coordina el flujo de la operación.
- Traduce DTOs y comandos a objetos de dominio.
- Delega las reglas a aggregates y Domain Services.
- Define el límite transaccional, normalmente un agregado por transacción.

En un monolito modular, cada módulo puede ser un minihexágono con su propio bounded context. Los módulos se comunican mediante contratos o eventos, no compartiendo sus agregados internos.

## Relación con microservicios y CQRS

Los microservicios organizan la arquitectura entre aplicaciones; hexagonal organiza el interior de cada una. CQRS puede separar comandos que atraviesan el dominio de consultas que leen una vista optimizada y devuelven un DTO directamente.

Vertical Slice es una alternativa de organización por funcionalidad. Mantiene juntos los archivos de un caso de uso, mientras que hexagonal suele agruparlos por dominio, aplicación y adaptadores. Ambos enfoques pueden combinarse.

## Para recordar

> Las reglas del negocio deben permanecer en el centro. Frameworks, bases de datos y protocolos son detalles externos que se conectan mediante puertos y adaptadores.
