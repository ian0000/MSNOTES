# Resumen — Clase 06: Infraestructura de Aplicaciones Empresariales

Fuente: [`presentaciones/06-infraestructura.md`](../presentaciones/06-infraestructura.md)

## Idea central

La infraestructura empresarial debe distribuir tráfico, mantener disponibilidad, proteger los recursos y permitir despliegues reproducibles. En arquitecturas modernas estas responsabilidades se reparten entre CDN, balanceadores, API Gateway, Ingress, Service Mesh y Kubernetes.

## Servidores de aplicaciones

Un web server se concentra en HTTP y contenido estático. Un servidor de aplicaciones aporta un entorno de ejecución, pools de conexiones, mensajería, procesos batch y administración de recursos.

Spring Boot popularizó servidores embebidos como Tomcat, Jetty o Netty. La aplicación incluye el servidor y puede ejecutarse con `java -jar`. En Kubernetes, muchas funciones tradicionales de clusterización pasan al orquestador.

## Tuning y hardening

El rendimiento exige revisar configuraciones predeterminadas:

- Dimensionar pools de hilos y conexiones.
- Definir timeouts apropiados.
- Rotar y centralizar logs.
- Deshabilitar servicios no utilizados.
- Medir antes de aumentar recursos.

El hardening reduce la superficie de ataque mediante parches, eliminación de cuentas predeterminadas, cierre de puertos y credenciales administrativas seguras.

## Balanceo de carga

Un balanceador distribuye tráfico únicamente entre nodos saludables, usando health checks. Puede operar en TCP, capa 4, o HTTP, capa 7, y realizar terminación TLS.

Algoritmos frecuentes:

- Round Robin y Weighted Round Robin.
- Least Connections.
- Menor tiempo de respuesta.
- Distribución basada en recursos disponibles.

Las sticky sessions mantienen a un usuario en el mismo nodo, pero generan distribución desigual y pérdida de sesión si ese nodo falla. Es preferible externalizar el estado cuando sea posible.

## Componentes de infraestructura moderna

- **Load Balancer:** distribuye tráfico entre réplicas del mismo servicio.
- **API Gateway:** enruta APIs y aplica autenticación, rate limiting y transformaciones.
- **Service Mesh:** controla mTLS, observabilidad y tráfico entre microservicios.
- **CDN:** acerca contenido al usuario, absorbe DDoS y puede aplicar WAF en el edge.
- **Ingress Controller:** implementa la entrada HTTP hacia servicios de Kubernetes.

El recorrido real suele ser CDN, balanceador, API Gateway o Ingress, Service y finalmente Pod. El balanceo moderno es una cadena de capas.

## Infraestructura como código

IaC hace que la infraestructura sea reproducible, versionable y revisable:

- Terraform, Pulumi o CloudFormation declaran recursos cloud.
- Ansible, Chef o Puppet configuran servidores.
- Helm y Kustomize empaquetan manifiestos de Kubernetes.

GitOps utiliza el repositorio como fuente de verdad. Controladores como Argo CD o Flux detectan cambios y sincronizan automáticamente el clúster.

## Seguridad de infraestructura

Un WAF inspecciona HTTP y bloquea ataques como inyección, XSS o patrones maliciosos. En contenedores y Kubernetes se recomienda:

- Ejecutar sin root.
- Utilizar filesystem de solo lectura.
- Limitar CPU y memoria.
- Aplicar NetworkPolicy.
- Restringir llamadas al sistema con seccomp.
- Utilizar AppArmor o SELinux.
- Revisar CIS Benchmarks.

## Monitoreo

Las cuatro señales de oro son latencia, tráfico, errores y saturación. También deben vigilarse pools de hilos, conexiones a base de datos, memoria, CPU e I/O.

Prometheus recolecta métricas; Loki o Elasticsearch agregan logs; Grafana ofrece paneles y alertas. El monitoreo de infraestructura observa recursos, mientras APM analiza el comportamiento interno de la aplicación.

## Para recordar

> La alta disponibilidad no proviene de un único balanceador: surge de health checks, redundancia, estado externalizado, despliegues reproducibles, seguridad por capas y monitoreo continuo.
