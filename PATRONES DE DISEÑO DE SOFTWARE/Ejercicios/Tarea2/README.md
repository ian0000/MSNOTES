# Tarea 2 · Composite y Adapter en Java

Ambos escenarios están implementados a partir de las propuestas aceptadas por el usuario. Cada carpeta es independiente, requiere JDK 11 o superior y no usa dependencias externas.

| Escenario | Patrón | Guía | Modelo aceptado | Decisiones | Prompt reutilizable |
|---|---|---|---|---|---|
| 1: presupuesto jerárquico | Composite | [README](Escenario1/README.md) | [UML](Escenario1/UML-PROPUESTO.md) | [Registro](Escenario1/DECISIONES.md) | [Prompt](Escenario1/PROMPT-FINAL.md) |
| 2: asistencia institucional | Adapter | [README](Escenario2/README.md) | [UML](Escenario2/UML-PROPUESTO.md) | [Registro](Escenario2/DECISIONES.md) | [Prompt](Escenario2/PROMPT-FINAL.md) |

## Ejecutar desde la raíz de MSNOTES

```powershell
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea2\Escenario1\verificar.ps1'
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea2\Escenario2\verificar.ps1'
```

Cada script compila, muestra el ejemplo y ejecuta las pruebas; se detiene si algo falla. Resultados verificados: **11/11 pruebas de Composite y 12/12 de Adapter**, usando OpenJDK 21.0.2 con compilación para Java 11 y advertencias activadas. No se probó sobre una instalación de JDK 11 separada ni sobre dispositivos reales.

Se preservan V1, V2, V3 y la captura posterior como `uml-captura-4.png`. Los README comparan lo visible en las imágenes con las correcciones implementadas; no se presentan las capturas como si ya estuvieran corregidas. El archivo `UML-PROPUESTO.md` conserva su nombre para mantener los enlaces y ahora contiene el modelo aceptado.
