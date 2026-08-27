# Resumen — Clase 02: Tecnologías Frontend y Backend

Fuente: [`presentaciones/02-frontend-backend.md`](../presentaciones/02-frontend-backend.md)

## Idea central

La web evolucionó desde páginas generadas completamente en el servidor hasta aplicaciones distribuidas entre navegador, servidor y edge. La clase compara modelos de renderizado, arquitecturas frontend y protocolos de comunicación para elegir cada tecnología según sus trade-offs.

## Evolución del frontend

1. **Web estática:** cada acción solicitaba al servidor una página HTML completa.
2. **AJAX y jQuery:** partes de la página podían actualizarse sin una recarga completa, pero el estado terminaba disperso en el DOM.
3. **HTML5 y Web APIs:** el navegador incorporó almacenamiento, WebSockets, gráficos, geolocalización, workers y capacidades offline.
4. **SPA:** React, Angular, Vue y Svelte trasladaron gran parte de la lógica al cliente.

Las SPA ofrecen interactividad fluida, pero pueden producir bundles grandes, peor SEO y mayor tiempo hasta que la página queda utilizable.

## Event Loop de JavaScript

JavaScript ejecuta una tarea a la vez en el hilo principal. La asincronía se coordina mediante:

- **Call Stack:** funciones que se están ejecutando.
- **Web APIs:** temporizadores, red y eventos manejados por el navegador.
- **Microtask Queue:** principalmente callbacks de promesas.
- **Callback Queue:** temporizadores y otras tareas.
- **Event Loop:** envía tareas a la pila cuando esta queda vacía.

El orden general es código síncrono, microtareas y luego callbacks. Un `setTimeout(..., 0)` no se ejecuta inmediatamente; espera a que la pila y las microtareas terminen. Los cálculos intensivos deben moverse a un Web Worker para no congelar la interfaz.

## Modelos de renderizado

- **CSR:** el navegador construye la interfaz. Conviene para dashboards autenticados sin necesidad de SEO.
- **SSR:** el servidor genera HTML en cada petición. Conviene para contenido público, actualizado o personalizado.
- **SSG:** el HTML se genera durante el build y se distribuye desde una CDN. Conviene para contenido estable.
- **ISR:** combina contenido estático con regeneración periódica.
- **RSC:** permite que ciertos componentes React se ejecuten únicamente en el servidor; no es lo mismo que SSR.

Frameworks como Next.js, Nuxt, SvelteKit, Astro y Remix combinan varias de estas estrategias.

## Arquitecturas frontend empresariales

Cuando muchos equipos trabajan en una SPA monolítica aparecen despliegues coordinados, bundles grandes y fallos compartidos. Los **micro-frontends** dividen la interfaz por capacidades del negocio y permiten que cada equipo mantenga y despliegue su módulo.

Module Federation carga componentes remotos en tiempo de ejecución. Esto permite despliegues independientes, pero exige controlar versiones, diseño compartido y fallos mediante mecanismos como `Suspense` y `ErrorBoundary`.

## Backend for Frontend

Una API genérica puede causar:

- **Over-fetching:** entrega muchos campos que el cliente no necesita.
- **Under-fetching:** obliga a realizar varias llamadas para construir una pantalla.

El patrón BFF crea un backend especializado para cada tipo de cliente, por ejemplo web, móvil o televisión. Cada BFF agrega, filtra y transforma la información requerida por su interfaz.

## REST, GraphQL y gRPC

- **REST:** sencillo, interoperable y apropiado para APIs públicas o recursos claros.
- **GraphQL:** el cliente solicita campos específicos; resulta útil con múltiples clientes y relaciones complejas.
- **gRPC:** comunicación binaria y tipada mediante Protocol Buffers; ofrece alto rendimiento y streaming, especialmente entre servicios internos.

No existe un protocolo ganador. Browser y móvil suelen utilizar REST o GraphQL; la comunicación interna de alto rendimiento puede utilizar gRPC.

## WebAssembly

WebAssembly es bytecode portable y aislado, compilado desde lenguajes como Rust, C++ o Go. Complementa a JavaScript en operaciones intensivas como edición multimedia, criptografía, CAD o juegos. Fuera del navegador puede utilizarse para plugins seguros, edge computing y runtimes de contenedores.

## Para recordar

> La selección correcta depende del contexto: CSR para aplicaciones privadas interactivas, SSR o SSG para contenido público, BFF para clientes distintos, gRPC para comunicación interna y WebAssembly para cómputo realmente pesado.
