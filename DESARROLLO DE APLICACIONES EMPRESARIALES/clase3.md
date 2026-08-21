# Evolucion arquitecturas

## el monolito

Un solo bloque que contiene la logica

- es simple para desarrollar debuggear y desplegar al inicio
- se debe escalar todo o nada, se daña algo tumba todo

## modular monolith

- sigue siendo un gran bloque pero internamente tiene una separacion clara de que y para que es cada
  parte
- si se requiere refactorizar a microservicios se puede hacer cuando sea necesario y mas "facil"
- se puede comenzar desde aqui antes de separar los servicios

## soa -> microservicios

- enterprise service base era un punto central de acoplamiento para el soa al ser un cuello de
  botella
- service oriented architecture
- la idea principal es ocnstruir un sistema dividiendolo en servicios independientes donde cada
  serivcio representa una capacidad del negocio y se cominca con los demas mediante interfaces bien
  definidas

| SOA                     | Microservicios                 |
| ----------------------- | ------------------------------ |
| servicios grandes       | servicios pequeños y enfocados |
| bd compartida comun     | cada servicio con su propia bd |
| su deploy es coordinado | es independiente por servicio  |

### principios de microservicios

- responsabilidad unica -> cada servicio es una isla la comunicacion entre cada una debe estar bien
  marcada para cada dominio
- base de datos por servicio
- deploy independiente, un cambio en uno no afecta a otros
- falla ailada
- organizado por negocio -> un equipo = un servicio

## sincrono vs asincrono

| Sincrono                          | Asyncrono                       |
| --------------------------------- | ------------------------------- |
| espera a respuesta para continuar | no la espera se puede continuar |

## Orquestacion vs coreografia

### orquestacion

- un director central coordina los paso
- mas facil de rastrear pero crea acoplamienti

### coreografia

- cada servicio reacciona a eventos sin director
- mas desacoplada pero el flujo completo es dificil de visualizar
## event-driven architecture
es un sistema de microservicion con llamadas sincronas encadenadas
### eda y el frontend
que es lo que ve el usuario? el backend responde con un estado pending  el frontend maneja la espera 