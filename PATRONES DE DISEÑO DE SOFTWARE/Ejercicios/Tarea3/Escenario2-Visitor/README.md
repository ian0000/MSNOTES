# Tarea 3 · Escenario 2: operaciones sobre envíos

**Visitor es adecuado para este escenario.** Los tipos de envío permanecen estables mientras costo, inspección y futuras operaciones pueden añadirse como visitantes. Cada elemento dirige la llamada hacia la sobrecarga que corresponde a su tipo, sin `instanceof` ni `switch`.

Recursos: [escenario y justificación](ESCENARIO.md) · [UML final aceptado](uml-final.png) · [correspondencia con Java](UML-IMPLEMENTADO.md) · [decisiones](DECISIONES.md) · [prompt final](PROMPT-FINAL.md).

## Participantes e implementación

| Rol del patrón | Clase | Responsabilidad |
|---|---|---|
| Element | [`Envio`](src/ejemplo/visitor/Envio.java) | Declara `aceptar(VisitanteEnvio)`. |
| ConcreteElement | [`Documento`](src/ejemplo/visitor/Documento.java) | Conserva peso, destino y confidencialidad. |
| ConcreteElement | [`PaqueteFragil`](src/ejemplo/visitor/PaqueteFragil.java) | Conserva peso, destino, valor y tipo de embalaje. |
| ConcreteElement | [`CargaRefrigerada`](src/ejemplo/visitor/CargaRefrigerada.java) | Conserva peso, destino, volumen, distancia y temperatura. |
| Visitor | [`VisitanteEnvio`](src/ejemplo/visitor/VisitanteEnvio.java) | Declara una visita para cada elemento concreto. |
| ConcreteVisitor | [`VisitanteCalculoCosto`](src/ejemplo/visitor/VisitanteCalculoCosto.java) | Calcula y acumula costos. |
| ConcreteVisitor | [`VisitanteInspeccion`](src/ejemplo/visitor/VisitanteInspeccion.java) | Produce resultados de inspección. |
| ObjectStructure | [`LoteEnvios`](src/ejemplo/visitor/LoteEnvios.java) | Conserva envíos y aplica un visitante a todos. |

Cada implementación de `aceptar` ejecuta `visitante.visitar(this)`. El tipo concreto de `this` selecciona la sobrecarga correspondiente; este es el doble despacho que demuestra Visitor. `LoteEnvios` solo recorre `List<Envio>` y no conoce las reglas de cada operación.

Los resultados se mantuvieron dentro de los visitantes: costo conserva un total `BigDecimal` e inspección conserva una lista de mensajes. Estos detalles no se añadieron al UML final por decisión del usuario; pueden consultarse en el código y en el diagrama complementario.

## Reglas del ejemplo

| Envío | Costo |
|---|---|
| Documento | 2,00 + 0,01 por gramo. |
| Paquete frágil | 3,00 por kilogramo + 2 % del valor declarado. |
| Carga refrigerada | 0,80 por kilómetro + 0,15 por litro + 20,00 fijos. |

La inspección advierte sobre documentos confidenciales, paquetes sin embalaje reforzado y temperaturas menores que -20 °C o mayores que 8 °C. Los demás casos se registran como conformes.

## Ejecutar y probar

Desde la raíz de MSNOTES:

```powershell
& '.\PATRONES DE DISEÑO DE SOFTWARE\Ejercicios\Tarea3\Escenario2-Visitor\verificar.ps1'
```

El [script](verificar.ps1) compila `src` y `test` con `--release 11 -encoding UTF-8 -Xlint:all`, ejecuta el ejemplo y después las pruebas, sin dependencias externas.

Salida del ejemplo:

```text
Costo total: $104.00
Inspección:
- Documento a Cuenca: requiere custodia especial
- Paquete frágil a Quito: requiere reforzar embalaje
- Carga refrigerada a Guayaquil: conforme
```

Resultado comprobado: **12/12 pruebas aprobadas**, sin advertencias, con OpenJDK 21.0.2 y destino Java 11. Las pruebas cubren los tres cálculos, acumulación del lote, reglas de inspección, límites de temperatura, lote vacío, doble despacho, inmutabilidad de resultados y validaciones.

## Validación crítica

Visitor separó costo e inspección de las entidades y permite agregar una nueva operación creando otro visitante. Frente a servicios con condiciones por tipo, el contrato obliga a contemplar explícitamente `Documento`, `PaqueteFragil` y `CargaRefrigerada`.

El costo es que añadir un nuevo tipo de envío obliga a modificar `VisitanteEnvio` y todos los visitantes. No lo recomendaría si los tipos cambian continuamente, si existe una única operación sencilla o si la operación pertenece naturalmente a cada entidad.

## Comparación V1 → UML final → Java

| Aspecto | V1 | UML final aceptado | Java |
|---|---|---|---|
| Estructura Visitor | Elemento, tres elementos, Visitor y dos visitantes. | Se conserva sin redibujar. | Se implementa con los mismos nombres. |
| Firmas y tipos | Varios tipos y retornos se omiten. | Se conservan conceptuales. | Todas las firmas están tipadas. |
| Datos para fórmulas | No aparecen todos los datos acordados. | Se conserva el dibujo. | Se añaden valor declarado, volumen y distancia. |
| Resultados | No se muestra dónde quedan. | Se conserva el dibujo. | Los visitantes acumulan total y mensajes. |
| Colección | `LoteEnvios` muestra `List<Envio>` y una caja duplicada. | Se conserva el dibujo. | Existe una sola interfaz y una lista tipada. |
| Tipo de `ejecutar` | Utiliza `Visitante`. | Se conserva conceptual. | Utiliza `VisitanteEnvio`. |

La captura V1 se conservó y se copió sin modificaciones como UML final aceptado:

![UML final aceptado](uml-final.png)

El [registro de decisiones](DECISIONES.md) explica qué recomendaciones se reservaron como aprendizaje para próximos UML y cómo se resolvieron en código.

[Volver al índice de Tarea 3](../README.md)
