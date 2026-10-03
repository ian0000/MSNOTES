# Prompt reutilizable

Revisa el caso 4, «Subasta electrónica», de `Unidad_03_02_PDS.pdf` y mi UML adjunto. Corrige el diagrama y genera una implementación Java 11 o superior en una carpeta nueva dentro de `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios`, sin modificar los demás ejercicios ni la captura original.

Conserva Observer y la estructura general `CambioDeValor`/`Subasta`/`ValorObserver`, pero unifica la colección y las operaciones de suscripción con la interfaz correcta. Evita llamar `notify()` al método del patrón porque ese nombre entra en conflicto con `Object.notify()` en Java. Modela a cada participante como observador concreto: debe actualizar su información al recibir una oferta máxima y poder decidir posteriormente si realiza una nueva oferta.

Utiliza `BigDecimal` para el importe. Una oferta se acepta únicamente cuando es positiva y estrictamente superior a la actual; solo una oferta aceptada cambia el estado y notifica. Permite suscribir y retirar participantes durante la ejecución, evita suscripciones duplicadas y recorre una copia durante la notificación. No implementes contraofertas automáticas dentro del callback ni añadas reglas de cierre, incremento mínimo o concurrencia que el caso no especifica.

Entrega un UML corregido visible y editable, el código, una demostración, pruebas relevantes, un script PowerShell reproducible y un README en español. Conserva y muestra el UML original. Explica qué estaba bien, qué se corrigió y qué decisiones son elaboración didáctica. Deja al final del README recomendaciones concretas sobre mi diagrama actual y registra las decisiones sin inventar rechazos o motivos personales.
