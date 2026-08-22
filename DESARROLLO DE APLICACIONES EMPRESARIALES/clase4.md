# Arquitectura hexagonal

### el problema que resuelve

esta acopla la logica de negocio a la infraestructura

el objetivo es permitir que la aplicacion sea probada facilmente y sea flexible al cambio separando
la logica de negocio de sus dependecias externas En una aplicación en capas suele existir algo así:

Controller → Service → Repository → Base de datos El problema aparece cuando el servicio de negocio
conoce directamente tecnologías como:

- Spring Data JPA.
- Anotaciones @Entity.
- SQL.
- Controladores REST.
- Servicios externos.

Esto genera acoplamiento. Por ejemplo, cambiar PostgreSQL por MongoDB podría obligarnos a modificar
la lógica del negocio. La arquitectura hexagonal propone: La lógica del negocio debe funcionar sin
saber qué base de datos, framework o interfaz de usuario se está utilizando.

2. ¿Qué es el hexágono? El hexágono representa la aplicación y contiene:

- Reglas del negocio.
- Entidades del dominio.
- Casos de uso.
- Servicios de dominio.
- Interfaces o puertos. El núcleo no debería saber si recibe solicitudes desde REST, una aplicación
  móvil, una terminal o una prueba automática. Tampoco debería saber si guarda información en
  PostgreSQL, MongoDB o memoria. REST ──┐ ┌── PostgreSQL CLI ──┼──► APLICACIÓN ───────┼── API
  externa Test ──┘ └── Cola de mensajes

3. Actores primarios y secundarios Los elementos externos se denominan actores. Actores primarios o
   drivers Son quienes inician una acción:

- Usuario.
- Aplicación web.
- Aplicación móvil.
- Controlador REST.
- Mensaje recibido.
- Prueba automática. Por ejemplo, un cliente solicita crear un pedido. Actores secundarios o driven
  Son servicios que la aplicación necesita para completar una acción:
- Base de datos.
- API externa.
- Servidor de correo.
- Impresora.
- Cola de mensajes. Por ejemplo, la aplicación guarda el pedido en PostgreSQL y envía una
  notificación.

4.  Puertos Los puertos son interfaces que definen cómo puede comunicarse el exterior con la
    aplicación. Puerto de entrada o driver port Declara lo que la aplicación permite hacer: public
    interface CrearPedido { Pedido crear(CrearPedidoCommand command); } Este puerto representa un
    caso de uso. No dice si será llamado desde REST, una terminal o una aplicación móvil. Puerto de
    salida o driven port Declara algo que la aplicación necesita del exterior: public interface
    PedidoRepository { void guardar(Pedido pedido); Optional<Pedido> buscarPorId(PedidoId id); } El
    dominio necesita guardar pedidos, pero no especifica en qué tecnología. La regla importante es:
    El puerto pertenece a la aplicación; la implementación tecnológica queda fuera.

5.  Adaptadores Los adaptadores conectan una tecnología específica con un puerto. Adaptadores de
    entrada Transforman una solicitud tecnológica en una llamada al caso de uso:

    @RestController

    public class PedidoController {

        private final CrearPedido crearPedido;
        @PostMapping("/pedidos")
        public PedidoResponse crear(@RequestBody PedidoRequest request) {
            return convertir(crearPedido.crear(convertir(request)));
        }

    }

    El controlador REST es un adaptador. También podría existir un adaptador CLI que utilice el
    mismo caso de uso. Adaptadores de salida Implementan las necesidades externas de la aplicación:

    @Repository

    public class PedidoJpaAdapter implements PedidoRepository {

        @Override
        public void guardar(Pedido pedido) {
            // Conversión del modelo de dominio a una entidad JPA
        }

    }

    Es posible reemplazar este adaptador por uno en memoria: public class PedidoEnMemoriaAdapter
    implements PedidoRepository { // Guarda los pedidos en una colección } El caso de uso no cambia
    porque ambos implementan el mismo puerto.

6.  Dirección de las dependencias La regla esencial es que las dependencias apuntan hacia el núcleo:
    Tecnología → Adaptador → Puerto → Aplicación La aplicación no debería importar clases de
    infraestructura. Incorrecto: Servicio de dominio → JpaRepository Correcto: Servicio de dominio →
    PedidoRepository ▲ │ implementa PedidoJpaAdapter El dominio define la interfaz y la
    infraestructura la implementa. A esto se lo relaciona con la inversión de dependencias.
7.  Dependency Configurator Alguien debe crear los objetos y conectarlos. Esa responsabilidad
    pertenece al Dependency Configurator, también conocido como raíz de composición. En Spring
    podría ser: @Configuration public class PedidoConfiguration {

        @Bean
        CrearPedido crearPedido(PedidoRepository repository) {
            return new CrearPedidoService(repository);
        }

    } El configurador:

8.  Crea el adaptador de salida.
9.  Lo inyecta en la aplicación.
10. Inyecta la aplicación en el adaptador de entrada. El dominio recibe sus dependencias; no las
    construye directamente.
11. Ventajas y desventajas Las principales ventajas son:

- La lógica del negocio se prueba sin levantar Spring.
- Se puede utilizar un repositorio en memoria durante las pruebas.
- Cambiar REST por gRPC afecta al adaptador, no al dominio.
- Cambiar JPA por otra tecnología afecta al adaptador de persistencia.
- Las reglas del negocio quedan más claras.
- Las decisiones tecnológicas pueden postergarse. Las desventajas son:
- Aparecen más interfaces y clases.
- Existe más indirección.
- La estructura requiere aprendizaje.
- Puede ser excesiva para sistemas sencillos. No suele ser conveniente para:
- CRUD sin reglas importantes.
- Scripts pequeños.
- Prototipos.
- Aplicaciones de corta duración.
- Proyectos donde la estructura cuesta más que el problema resuelto.

9. ¿Qué es DDD Táctico?

Domain-Driven Design busca que el software represente correctamente el negocio. La arquitectura
hexagonal protege el núcleo; DDD ayuda a organizar lo que existe dentro de ese núcleo. Sus
principales bloques son: Entity Objeto que posee identidad propia:

public class Cliente { private ClienteId id; private String nombre; }

Aunque el cliente cambie de nombre, sigue siendo el mismo porque conserva su identificador. Una
entidad de DDD no significa necesariamente una clase JPA con @Entity. Value Object Objeto definido
por sus valores, sin identidad e idealmente inmutable:

public record Dinero( BigDecimal cantidad, Moneda moneda ) {}

Dos objetos Dinero son iguales cuando tienen la misma cantidad y moneda. Otros ejemplos:

- Dirección.
- Correo electrónico.
- Número de teléfono.
- Rango de fechas. Aggregate Conjunto de objetos del dominio que se modifica como una unidad. Por
  ejemplo: Pedido — raíz del agregado ├── Línea de pedido ├── Línea de pedido └── Total El Pedido
  controla sus líneas: pedido.agregarProducto(producto, cantidad); No debería permitirse que
  cualquier componente modifique directamente la lista de líneas, porque la raíz debe proteger
  reglas como:
- La cantidad debe ser positiva.
- El pedido no puede modificarse después de enviarse.
- El total debe recalcularse.
- No pueden agregarse productos inexistentes. Domain Event Representa algo importante que ya
  ocurrió: public record PedidoCreado( PedidoId pedidoId, ClienteId clienteId, Instant ocurridoEn )
  {} Se nombra en pasado:
- PedidoCreado.
- PagoAprobado.
- ProductoAgotado.
- ClienteRegistrado. Otros componentes pueden reaccionar al evento sin acoplarse directamente al
  agregado. Repository Permite recuperar y guardar agregados: public interface PedidoRepository {
  void guardar(Pedido pedido); Optional<Pedido> buscarPorId(PedidoId id); } En DDD, el repositorio
  trabaja principalmente con raíces de agregados, no con cada objeto interno por separado. Domain
  Service Contiene una regla del negocio que no pertenece naturalmente a una entidad: public class
  CalculoPrecioService {

      public Dinero calcular(Pedido pedido, TipoCliente cliente) {
          // Aplicación de descuentos y reglas comerciales
      }

  }

  No debe convertirse en un contenedor de toda la lógica. Primero se intenta colocar el
  comportamiento en las entidades y value objects.

10. Cómo se combinan Hexagonal y DDD Las dos ideas cumplen funciones diferentes: Arquitectura
    hexagonal └── Define fronteras y dependencias └── Núcleo de la aplicación ├── Casos de uso └──
    Dominio organizado con DDD ├── Entities ├── Value Objects ├── Aggregates ├── Domain Services └──
    Domain Events Un flujo completo sería: POST /pedidos │ ▼ PedidoController Adaptador de entrada │
    ▼ CrearPedido Puerto de entrada │ ▼ CrearPedidoService Caso de uso │ ▼ Pedido.crear() Aggregate
    de dominio │ ▼ PedidoRepository Puerto de salida │ ▼ PedidoJpaAdapter Adaptador de salida │ ▼
    PostgreSQL
11. Hexagonal y microservicios Los microservicios y la arquitectura hexagonal resuelven problemas en
    dimensiones distintas:

- Microservicios: organización entre aplicaciones.
- Hexagonal: organización dentro de cada aplicación. Por ejemplo, pedido-service puede comunicarse
  con inventario-service mediante eventos. Internamente, cada microservicio puede estar organizado
  con puertos y adaptadores. No todo microservicio necesita arquitectura hexagonal. Resulta útil
  cuando el servicio tiene reglas de negocio relevantes.

12. CQRS dentro del hexágono CQRS separa:

- Commands: modifican información.
- Queries: solamente consultan. Ejemplo: Crear pedido → pasa por el dominio → valida reglas → guarda
  agregado Consultar resumen → lee una vista optimizada → devuelve DTO Las operaciones que modifican
  estado deben proteger las reglas del dominio. Una consulta sencilla puede leer directamente una
  proyección optimizada sin reconstruir todo el agregado.

13. Vertical Slice como alternativa La arquitectura hexagonal suele organizar por capas: domain/
    application/ adapter/ Vertical Slice organiza por funcionalidad: features/ crear-pedido/
    consultar-pedido/ cancelar-pedido/ Hexagonal ofrece fronteras muy claras, mientras que Vertical
    Slice mantiene juntos los archivos de cada caso de uso. También pueden combinarse: cada feature
    puede respetar puertos y mantener el dominio separado de infraestructura. Idea final La clase 4
    puede resumirse con esta frase: Las reglas del negocio deben ser el centro del sistema, mientras
    que Spring, REST, JPA, PostgreSQL y las demás tecnologías deben ser piezas reemplazables
    alrededor de ese centro.

La arquitectura hexagonal define las fronteras y DDD táctico permite modelar correctamente el
negocio dentro de ellas.
