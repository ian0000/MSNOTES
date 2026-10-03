# MSNOTES

**Apuntes de maestría · Ejercicios y material de estudio**

Repositorio académico de Ian K. Reúne notas, resúmenes, ejercicios de programación y diagramas utilizados durante la maestría. Es material de aprendizaje, separado de mis proyectos personales.

## Por dónde empezar

| Área | Contenido | Acceso |
| --- | --- | --- |
| Desarrollo de aplicaciones empresariales | Arquitectura, frontend/backend, microservicios, DDD, seguridad, infraestructura y observabilidad | [Apuntes](DESARROLLO%20DE%20APLICACIONES%20EMPRESARIALES/) |
| Patrones de diseño de software | Material de clase, resúmenes y ejercicios | [Carpeta de la asignatura](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/) |
| Guías de estudio | Resúmenes organizados por tema y clase | [Índice de resúmenes](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Resumenes/README.md) |
| Implementaciones | Ejemplos, decisiones y pruebas | [Ejercicios](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/) |

## Ejercicios destacados

- [Tarea 2](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/Tarea2/README.md): escenarios documentados, UML e implementación.
- [Tarea 3](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/Tarea3/README.md): Chain of Responsibility y Visitor.
- [Factory Method](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/Escenario01-FactoryMethod-Vehiculos/README.md): creación de vehículos.
- [Builder](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/Escenario02-Builder-ActivosFijos/README.md): construcción de activos fijos.
- [Template Method](PATRONES%20DE%20DISE%C3%91O%20DE%20SOFTWARE/Ejercicios/TemplateMethod-Caso04-Importacion/README.md): importación de datos.

## Cómo usar el repositorio

Para leer las notas basta GitHub o un editor Markdown. Para ejecutar un ejercicio, revisa primero su README: los requisitos, decisiones y comandos se documentan por escenario.

Varios ejercicios de patrones usan Java 11 y scripts PowerShell `verificar.ps1`. Por ejemplo, desde la raíz:

```powershell
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea3\Escenario1-Chain-of-Responsibility\verificar.ps1'
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea3\Escenario2-Visitor\verificar.ps1'
```

No existe un único proceso de instalación o prueba para todas las asignaturas.

## Criterios de lectura

Los diagramas, decisiones y capturas conservan el contexto de cada entrega. Los resultados de pruebas que aparecen en esas entregas corresponden a sus verificaciones documentadas, no a una ejecución automática permanente.

Los materiales de clase mantienen su procedencia y los derechos de sus autores. Este repositorio organiza mi estudio y práctica.

[Perfil de Ian K.](https://github.com/ian0000)
