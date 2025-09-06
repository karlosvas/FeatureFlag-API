
# FeatureFlag API

Una API REST robusta para la gestión dinámica de feature flags en entornos de producción, desarrollada con Spring Boot y PostgreSQL.

## 📋 Descripción

FeatureFlag API permite crear, activar y desactivar funcionalidades de manera dinámica según entornos específicos (dev, staging, prod) o clientes particulares. Ideal para despliegues controlados, pruebas A/B y activación gradual de nuevas características.

## 🛠️ Tecnologías

- **Lenguaje:** Java
- **Framework:** Spring Boot (MVC)
- **Base de datos:** PostgreSQL
- **Seguridad:** Spring Security + JWT
- **Documentación:** Swagger UI
- **Testing:** Postman + JUnit/Mockito
- **Contenedores:** Docker + DevContainers
- **Control de versiones:** Git + GitHub

## 🚀 Características principales

- ✅ Gestión completa de feature flags
- 🔐 Autenticación JWT con Spring Security
- 🌍 Configuración por entornos (dev, staging, prod)
- 👥 Personalización por cliente específico
- 📚 Documentación automática con Swagger
- 🧪 Cobertura de testing con JUnit
- 🐳 Desarrollo con Docker y DevContainers

## 🔧 Instalación y configuración

### Prerrequisitos

- Docker Desktop
- Visual Studio Code con extensión Dev Containers
- Git

### Configuración del entorno

1. **Clonar el repositorio:**

```bash
git clone https://github.com/usuario/FeatureFlag-API.git
cd FeatureFlag-API
```

2. **Configurar variables de entorno:**

```bash
# Crear archivo de configuración
touch .env

# Editar variables de entorno
nano .env
```

Ejemplo de archivo `.env` (basado en configuración de desarrollo):

```env
# Database Configuration
POSTGRES_DB=featureflag
POSTGRES_USER=featureflag
POSTGRES_PASSWORD=password

DB_NAME=featureflag
DB_USER=featureflag
DB_PASSWORD=password

# Database URLs per Environment
DB_URL_DEV=jdbc:postgresql://db-featureflag:5432/featureflag
DB_URL_STAGING=jdbc:postgresql://db-featureflag:5432/featureflag
DB_URL_PROD=jdbc:postgresql://db-featureflag:5432/featureflag

# Security Configuration
JWT_SECRET_KEY=your_secret_key_here
ACCESS_TOKEN_EXPIRATION=86400000
```

### Ejecución con DevContainers

1. **Abrir en VS Code:**

```bash
code .
```

2. **Ejecutar en contenedor:**

- Presiona `Ctrl+Shift+P`
- Selecciona "Dev Containers: Reopen in Container"
- El entorno se configurará automáticamente

### Ejecución manual con Docker

```bash
# Construir y ejecutar con Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f app
```

## 📡 Endpoints principales

### Autenticación

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registro de usuario |
| POST | `/api/auth/login` | Login y obtención de JWT |

### Gestión de Features

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/features` | Crear nueva feature |
| GET | `/api/features` | Listar todas las features |
| GET | `/api/features/{id}` | Obtener detalle de feature |
| POST | `/api/features/{id}/enable` | Activar feature |
| POST | `/api/features/{id}/disable` | Desactivar feature |
| GET | `/api/features/check` | Verificar estado de feature |

## 💾 Modelo de datos

### Feature

```java
{
    "id": "uuid",
    "name": "string",
    "description": "string",
    "enabledByDefault": "boolean"
}
```

### FeatureConfig

```java
{
    "id": "uuid",
    "environment": "DEV|STAGING|PROD",
    "clientId": "string",
    "enabled": "boolean"
}
```

## 🔍 Ejemplos de uso

### Crear feature

```bash
POST /api/features
{
    "name": "dark_mode",
    "description": "Modo oscuro para la interfaz",
    "enabledByDefault": false
}
```

### Verificar feature

```bash
GET /api/features/check?feature=dark_mode&clientId=acme123&env=staging
```

## 🧠 Lógica de activación

El sistema verifica en orden de prioridad:

  1. Configuración específica para cliente
  2. Configuración por entorno
  3. Valor por defecto (`enabledByDefault`)

## 📖 Documentación

Accede a la documentación interactiva en: `http://localhost:8080/swagger-ui.html`

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
