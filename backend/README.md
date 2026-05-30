# E-Commerce API

Api REST desarrollada con Spring Boot para gestionar una tienda en línea con Productos, Usuarios y Pedidos.

## Características

- ✅ CRUD completo para Productos
- ✅ CRUD completo para Usuarios
- ✅ CRUD completo para Pedidos
- ✅ Relaciones entre entidades (Pedido-Usuario, Pedido-Producto)
- ✅ Base de datos H2 (desarrollo) o MySQL (producción)
- ✅ API RESTful
- ✅ Autenticacion con JWT 
- ✅ Endpoints publicos para registro/login
- ✅ Endpoints protegidos para operaciones de negocio
- ✅ Manejo centralizado de errores con `GlobalExceptionHandler`
- ✅ Soporte de perfiles:
    - MySQL por defecto
    - H2 activando profile `h2`

## Tecnologías

- Java 17
- Spring Boot 4.0.3
- Spring Data JPA
- H2 Database (desarrollo)
- MySQL (opcional para producción)
- Lombok
- Maven

## Endpoints

### Productos
- `GET /api/productos` - Obtener todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### Usuarios
- `GET /api/usuarios` - Obtener todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/email/{email}` - Obtener usuario por email
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario

### Pedidos
- `GET /api/pedidos` - Obtener todos los pedidos
- `GET /api/pedidos/{id}` - Obtener pedido por ID
- `GET /api/pedidos/usuario/{usuarioId}` - Obtener pedidos de un usuario
- `POST /api/pedidos` - Crear nuevo pedido
- `PUT /api/pedidos/{id}` - Actualizar pedido
- `DELETE /api/pedidos/{id}` - Eliminar pedido

## Estructura del Proyecto

```
src/main/java/com/uade/tpo/e_commerce3/
├── controller/
│   ├── ProductoController.java
│   ├── UsuarioController.java
│   └── PedidoController.java
├── model/
│   ├── Producto.java
│   ├── Usuario.java
│   └── Pedido.java
├── repository/
│   ├── ProductoRepository.java
│   ├── UsuarioRepository.java
│   └── PedidoRepository.java
├── service/
│   ├── ProductoService.java
│   ├── UsuarioService.java
│   └── PedidoService.java
└── ECommerce3Application.java

src/main/resources/
└── application.properties
```

## Instalación y Ejecución

### Requisitos
- Java 17+
- Maven

### Pasos

1. Clonar el repositorio
```bash
git clone <repository-url>
cd e-commerce3
```

2. Compilar el proyecto
```bash
mvn clean compile
```

3. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`


#### Plus: Para usar H2 en lugar de MySQL, ejecutar con el perfil `h2`
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

## Ejemplos de Uso

### Crear un Producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop",
    "descripcion": "Laptop de última generación",
    "precio": 1200.00,
    "stock": 5,
    "categoria": "Electrónica"
  }'
```

### Crear un Usuario
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@example.com",
    "telefono": "1234567890",
    "direccion": "Calle 123, Ciudad"
  }'
```

### Crear un Pedido
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": {"id": 1},
    "producto": {"id": 1},
    "cantidad": 2,
    "precioTotal": 2400.00,
    "estado": "pendiente"
  }'
```

## Modelos

### Producto
```json
{
  "id": 1,
  "nombre": "Laptop",
  "descripcion": "Laptop de última generación",
  "precio": 1200.00,
  "stock": 5,
  "categoria": "Electrónica"
}
```

### Usuario
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "telefono": "1234567890",
  "direccion": "Calle 123, Ciudad"
}
```

### Pedido
```json
{
  "id": 1,
  "usuario": {...},
  "producto": {...},
  "cantidad": 2,
  "precioTotal": 2400.00,
  "fechaPedido": "2024-03-20T15:30:00",
  "fechaEntrega": null,
  "estado": "pendiente"
}
```

## Configuración de Base de Datos

### Desarrollo (H2)
La configuración ya está lista en `application-h2.properties`. Para acceder a la consola H2:
`http://localhost:8080/h2-console`

- URL: `jdbc:h2:mem:ecommerce`
- Usuario: `sa`
- Contraseña: (vacía)

### Producción (MySQL)
Para usar MySQL, se usa la configuracion en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

## Estados de Pedidos

- `pendiente` - Pedido recibido, en proceso
- `enviado` - Pedido enviado
- `entregado` - Pedido entregado
- `cancelado` - Pedido cancelado

## Licencia

Este proyecto está bajo la licencia MIT.
