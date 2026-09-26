# Prompt reutilizable

Revisa el caso 4, «Importación de archivos», de `Unidad_03_01_PDS.pdf` y mi UML adjunto. Corrige el diagrama y genera Java 11 o superior en una carpeta nueva de `PATRONES DE DISEÑO DE SOFTWARE/Ejercicios`, preservando la captura original y los demás ejercicios.

Mantén las clases `ImportacionInformacion`, `ImportCSV`, `ImportJSON` e `ImportXML`. Aplica Template Method: agrega una operación pública final `importar(archivo)` que controle siempre validar, leer, transformar y almacenar. La validación inicial y el almacenamiento serán comunes; lectura y transformación serán abstractas en la base e implementadas en las subclases. Explica la visibilidad protegida, el uso de `final`, la corrección de `bit[]` y cómo pasan los datos entre etapas.

Concreta un ejemplo didáctico con registros de código y nombre, archivos CSV/JSON/XML reales y salida CSV normalizada. Puedes usar tipos intermedios genéricos y una biblioteca JSON con versión fija. Identifica estas decisiones como elaboración del ejemplo, no como requisitos de la diapositiva. No sobrescribas la entrada ni resultados existentes; si falla una etapa, detén las posteriores.

Entrega UML corregido visible y editable, código, datos de ejemplo, script PowerShell reproducible, pruebas significativas y README en español. Ejecuta la compilación y las pruebas, y distingue los resultados comprobados de las limitaciones. Compara mi dibujo con la corrección y deja al final del README un apartado breve de errores y mejoras del UML actual. Registra las decisiones sin inventar rechazos o motivaciones personales. Los cambios necesarios anteriores están autorizados; consulta únicamente si surge un cambio de alcance diferente.
