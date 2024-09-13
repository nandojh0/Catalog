## Proyecto de Gestión de Órdenes
## Descripción
Este proyecto es una aplicación de gestión de órdenes que permite a los usuarios realizar pedidos de productos, clasificados en categorías. El sistema almacena información sobre productos, categorías, órdenes y los elementos dentro de cada orden.

## Estructura de la Base de Datos
Modelo Relacional
Tablas:

Category

Descripción: Almacena las categorías de productos.
Columnas: id, name
Product

Descripción: Almacena la información de los productos.
Columnas: id, name, description, price, category_id, stock
Order

Descripción: Representa las órdenes realizadas por los usuarios.
Columnas: id, userId, orderDate, status, total
OrderItem

Descripción: Almacena los elementos de cada orden.
Columnas: id, productId, quantity, price, order_id
Relaciones:

Category a Product: Muchos a uno (ManyToOne)
Order a OrderItem: Uno a muchos (OneToMany)
OrderItem a Product: Muchos a uno (ManyToOne)

Modelo No Relacional (MongoDB)

Colecciones:

categories

Descripción: Almacena las categorías de productos.
Documentos: { "id": "Long", "name": "String" }
products

Descripción: Almacena la información de los productos.
Documentos: { "id": "Long", "name": "String", "description": "String", "price": "Double", "category": { "id": "Long", "name": "String" }, "stock": "Integer" }
orders

Descripción: Almacena las órdenes realizadas.
Documentos: { "id": "Long", "userId": "Long", "orderDate": "ISODate", "status": "String", "total": "Double", "items": [ { "productId": "Long", "quantity": "Integer", "price": "Double" } ] }


## Requisitos
- JDK 17
- Maven
- MySQL
MongoDB (si usas la versión no relacional)
Dependencias necesarias (ver archivo pom.xml)


## Implementación
## Configuración
Para que la aplicación funcione correctamente, debes configurar las siguientes variables de entorno en Railway.app:

- **Crea una Cuenta en Railway.app:

- **Regístrate en Railway.app si aún no lo has hecho.
Crea un Nuevo Proyecto: 

- **Inicia sesión en Railway.app y crea un nuevo proyecto.
Conecta tu Repositorio de GitHub: https://github.com/nandojh0/Catalog.git

- **Dentro del proyecto en Railway.app, selecciona la opción para conectar un repositorio de GitHub. Esto permitirá que Railway.app construya y despliegue tu proyecto directamente desde GitHub.

- **MYSQL_HOSTNAME**: El nombre del host de tu base de datos.


- **MYSQL_PORT**: El puerto en el que tu base de datos está escuchando.


- **MYSQL_DATABASE**: El nombre de la base de datos a la que te conectarás.


- **MYSQL_USERNAME**: El nombre de usuario para la base de datos.


- **MYSQL_PASSWORD**: La contraseña para el nombre de usuario de la base de datos.


Configuración en Railway.app
Accede a tu proyecto en Railway.app.
Navega a la sección de "Settings" o "Environment Variables".
Agrega cada una de las variables de entorno mencionadas anteriormente.
Guarda los cambios.
Reinicia la aplicación para que los cambios tomen efecto.

## Archivo application.properties
Asegúrate de que el archivo application.properties de tu aplicación esté configurado de la siguiente manera:
spring.datasource.url=jdbc:mysql://${MYSQL_HOSTNAME}:${MYSQL_PORT}/${MYSQL_DATABASE}
spring.datasource.username=${MYSQL_USERNAME}
spring.datasource.password=${MYSQL_PASSWORD}
Esto permitirá que la aplicación se conecte a la base de datos utilizando las variables de entorno configuradas en Railway.app.

Uso
Inicio de sesión: Navega a https://catalog-production-886a.up.railway.app  para acceder a la página de inicio.
Gestión de productos,Ordenes y categorías: Accede a las secciones correspondientes en la aplicación para añadir, actualizar o eliminar..