# Deber 3 · Patrones de comportamiento

Patrones seleccionados:

1. **Chain of Responsibility:** aprobación escalonada de compras institucionales.
2. **Visitor:** operaciones sobre envíos heterogéneos de una empresa de mensajería.

| Escenario | Patrón | Entrega | UML final | Decisiones |
|---|---|---|---|---|
| 1 | Chain of Responsibility | [Código, explicación y pruebas](Escenario1-Chain-of-Responsibility/README.md) | [Captura aceptada](Escenario1-Chain-of-Responsibility/uml-final.png) | [Registro](Escenario1-Chain-of-Responsibility/DECISIONES.md) |
| 2 | Visitor | [Código, explicación y pruebas](Escenario2-Visitor/README.md) | [Captura aceptada](Escenario2-Visitor/uml-final.png) | [Registro](Escenario2-Visitor/DECISIONES.md) |

## Estado actual

La tarea está implementada y verificada. Se conservaron las capturas V1 y finales, se documentaron las decisiones del usuario, se crearon las dos soluciones Java, ejemplos ejecutables, pruebas y scripts reproducibles. Chain of Responsibility obtuvo **12/12 pruebas aprobadas** y Visitor **12/12 pruebas aprobadas**.

## Flujo acordado

1. Se definieron dos problemas realistas y se justificó cada patrón.
2. Se conservaron y revisaron los UML V1.
3. El usuario aceptó eliminar la operación provisional de Chain y decidió trasladar los demás detalles técnicos al código.
4. El UML de Visitor se conservó como modelo conceptual; sus tipos y conexiones se completaron en Java.
5. Se generaron implementación, demostraciones, pruebas, decisiones justificadas, diagramas complementarios y prompts finales.

La documentación de cada escenario explica qué resolvió el patrón, qué mejoró respecto de una solución directa, qué complejidad añadió y cuándo no conviene utilizarlo.

## Uso crítico de inteligencia artificial

La IA se utilizó para proponer escenarios, cuestionar decisiones, revisar los primeros UML, generar una implementación coherente y proponer pruebas. El grupo mantuvo el control de las decisiones: aceptó una corrección visual en Chain, decidió mantener ambos diagramas en un nivel conceptual y trasladó las precisiones técnicas al código. Las capturas originales se conservaron para distinguir las propuestas de la decisión final.

## Verificación completa

```powershell
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea3\Escenario1-Chain-of-Responsibility\verificar.ps1'
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea3\Escenario2-Visitor\verificar.ps1'
```

## Criterio que separa los patrones

En Chain of Responsibility, una solicitud avanza por una secuencia de posibles receptores hasta que uno puede resolverla. En Visitor, una operación recorre objetos de tipos distintos y ejecuta lógica especializada mediante doble despacho. El primer escenario varía la persona responsable de una solicitud; el segundo permite añadir operaciones sobre una jerarquía estable.
