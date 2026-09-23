# Prompt final · Escenario 1

Revisa mis UML V1, V2, V3 y la última captura del escenario de presupuesto jerárquico y genera una implementación Java 11 o superior en `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios/Tarea2/Escenario1`, sin modificar los demás ejercicios ni las imágenes originales.

Elige Composite y conserva estos nombres: `PartidaPresupuestaria` como base abstracta con código y descripción; `PartidaIndividual` como hoja con `valorAsignado`; y `GrupoPresupuestario` como compuesto que también hereda de la base. Todos comparten `obtenerCodigo()`, `obtenerDescripcion()`, `calcularValorTotal(): float` y `mostrarEstructura(): String`. Mantén `float` para corresponder al modelo académico y explica su limitación para dinero exacto.

Acepto que el grupo tenga una colección de componentes con multiplicidad `0..*` y operaciones públicas `agregar(elemento)` y `quitar(elemento)`. El total del grupo suma recursivamente sus hijos; un grupo vacío suma cero. El cliente debe consultar hojas y grupos mediante la misma abstracción, sin distinguir clases concretas. Evita ciclos y duplicar un mismo hijo directo; no impongas propiedad exclusiva ni deduplicación global sin indicarlo.

Incluye un ejemplo de TI con Licencias por 500 e Infraestructura con Servidores por 2000 y Almacenamiento por 1000; muestra el total de 3500 y el cambio al retirar un componente. Añade pruebas significativas y un script PowerShell que compile, ejecute el ejemplo y las pruebas sin dependencias externas.

Entrega un README explicativo con comandos reproducibles, resultados comprobados, UML editable y comparación entre versiones. Al final del README deja correcciones concretas de mis diagramas, no una lista de trabajo futuro. Registra por separado decisiones aceptadas, modificadas y alternativas no elegidas, sin inventar rechazos ni motivos. Los cambios anteriores ya están autorizados; consulta únicamente si resulta necesario otro cambio importante de lógica.
