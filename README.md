
# FeatureFlag API

Una API REST robusta para la gestión dinámica de feature flags en entornos de producción, desarrollada con Spring Boot y PostgreSQL.
Este proyecto es parte de una iniciativa educativa y colaborativa para aprender buenas prácticas en desarrollo backend. Propuesto por Bytes Colaborativos una comunidad de desarrolladores.

## 📋 Descripción

FeatureFlag API permite crear, activar y desactivar funcionalidades de manera dinámica según entornos específicos (dev, staging, prod) o clientes particulares. Ideal para despliegues controlados, pruebas A/B y activación gradual de nuevas características.

## Equipo de Desarrollo 🫂
@karlosvas  
@alexDAW-IOC  
@angelAntezana  
@JosCarRub  
@nescaruncho  

Creadores de Bytes Colaborativos
@dev-tech @Jorexbp
Canal de Twitch: https://www.twitch.tv/bytescolaborativos  
Canal de Discord: https://discord.com/invite/YGGcPMzSQk

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

El archivo de ejemplo de archivo `.env` (basado en configuración de desarrollo), se encuentra en `.devcontainer/devcontainer.env.example`. Cópialo y edítalo con tus valores.:
```bash
# Crear archivo de configuración
cp .devcontainer/devcontainer.env.example .devcontainer/devcontainer.env

# Editar variables de entorno
nano .devcontainer/devcontainer.env
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
| GET  | `/api/auth/health` | Verificar estado del servicio |
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

## UserRequestDTO

```java
{
    "username": "string",
    "email": "string",
    "password": "string"
}
```

## UserDTO

```java
{
    "id": "uuid",
    "username": "string",
    "email": "string",
    "roles": ["USER", "ADMIN", "GUEST"],
    "active": "boolean"
}
```

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

## 🔍 SonarQube

SonarQube is a static analysis tool that checks your code and signals if something is wrong with it.

### Steps

1. Open a browser and go to [http://localhost:9000](http://localhost:9000).
2. Log in with the following credentials:
   - **Login**: `admin`
   - **Password**: `admin`
3. After logging in, you will be asked to change your password.  
   👉 Set a secure password (example: `Featureflag2025`).
4. Create a local project:
   - Click **Create a local project**.
   - Enter a name in **Project display name** (example: `FeatureflagApi`).
   - Enter the same name in **Project key**.
   - Enter `develop` or `main` in **Main branch name**.
   - Click **Next**.
5. Choose **Use the global setting** and click **Create project**.
6. A project will be generated. For **Analysis Method**, choose **Locally**.
7. In **Token generation**, set it to **Expires** and click **Generate**.  
   ⚠️ A token will be generated → save it (e.g., in a `.env` file), because it will be needed when running the analysis. Then click **Continue**.
8. Run analysis on your project:
   - Select **Maven**.
   - Copy the commands given by SonarQube.  
     ⚠️ To run locally using containers, the URL must be the **service name of the container**; in this case: `sonarqube`.

   ```bash
   mvn clean verify sonar:sonar \
     -Dsonar.projectKey=FeatureflagApi \
     -Dsonar.projectName='FeatureflagApi' \
     -Dsonar.host.url=http://sonarqube:9000 \
     -Dsonar.token=$MY_SONARQUBE_TOKEN

## 📖 Documentación

Accede a la documentación interactiva de Swagger en: `http://localhost:8080/swagger-ui.html`
O accede a la documentacion de JavaDoc en: `target/site/apidocs/index.html`

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`) 
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

### Opcionalmente puedes utilizar la nueva nomenclatura de ramas como 
1. Fork del proyecto
2. Crear rama feature (`git branch feature/nueva-funcionalidad`)
3. Cambiar a la nueva rama (`git switch feature/nueva-funcionalidad`)
4. Hacer tus cambios y guardarlos (`git add .` y `git commit -m "Agregar nueva funcionalidad"`)
5. Subir la rama al repositorio remoto (`git push --set-upstream origin feature/nueva-funcionalidad`)
6. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
