# FeatureFlag API

A robust REST API for dynamic feature flag management in production environments, developed with Spring Boot and PostgreSQL.
This project is part of an educational and collaborative initiative to learn backend development best practices. Proposed by Bytes Colaborativos, a developer community.

## 📋 Description

FeatureFlag API allows creating, enabling and disabling functionalities dynamically according to specific environments (dev, staging, prod) or particular clients. Ideal for controlled deployments, A/B testing and gradual activation of new features.

## Development Team 🫂

@karlosvas  
@alexDAW-IOC  
@angelAntezana  
@JosCarRub  
@nescaruncho

## Creators of Bytes Colaborativos

@dev-tech @Jorexbp
Twitch Channel: <https://www.twitch.tv/bytescolaborativos>  
Discord Channel: <https://discord.com/invite/YGGcPMzSQk>

## 🛠️ Technologies

- **Language:** Java
- **Framework:** Spring Boot (MVC)
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT
- **Documentation:** Swagger UI
- **Testing:** Postman + JUnit/Mockito
- **Containers:** Docker + DevContainers
- **CI/CD:** GitHub Actions
- **Static Code Analysis:** SonarQube
- **Version Control:** Git + GitHub

## 🚀 Main Features

- ✅ Complete feature flag management
- 🔐 JWT authentication with Spring Security
- 🌍 Environment-based configuration (dev, staging, prod)
- 👥 Client-specific customization
- 📚 Automatic documentation with Swagger
- 🧪 Testing coverage with JUnit
- 🐳 Development with Docker and DevContainers
- 🔄 CI/CD with GitHub Actions
- 🔍 SonarQube quality gate validation

## 🔧 Installation and Setup

### Prerequisites

- Docker Desktop
- Visual Studio Code with Dev Containers extension
- Git

### Environment Setup

1. **Clone the repository:**

```bash
git clone https://github.com/usuario/FeatureFlag-API.git
cd FeatureFlag-API
```

2. **Configure environment variables:**

The example `.env` file (based on development configuration) is located at `.devcontainer/devcontainer.env.example`. Copy it and edit with your values:

```bash
# Create configuration file
cp .devcontainer/devcontainer.env.demo .devcontainer/devcontainer.env
cp .devcontainer/sonarqube.env.demo .devcontainer/sonarqube.env

# Edit environment variables
nano .devcontainer/devcontainer.env
nano .devcontainer/sonarqube.env
```

### Running with DevContainers

1. **Open in VS Code:**

```bash
code .
```

2. **Run in container:**

- Press `Ctrl+Shift+P`
- Select "Dev Containers: Reopen in Container"
- The environment will be configured automatically

### Manual execution with Docker

```bash
# Build and run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f app
```

## 📡 Main Endpoints

### Authentication

| Method | Route                       | Description             |
| ------ | --------------------------- | ----------------------- |
| GET    | `/api/auth/health`          | Check service status    |
| POST   | `/api/auth/register`        | User registration       |
| POST   | `/api/auth/login`           | Login and JWT retrieval |
| POST   | `/api/auth/register/admin`  | Create admin user       |
| GET    | `/api/auth/users`           | List all users          |
| GET    | `/api/auth/user/{email}`    | Get user by email       |
| DELETE | `/api/auth/user/{userId}`   | Delete user by ID       |
| GET    | `/api/auth/test`            | Verify admin            |

### Feature Management

| Method | Route                         | Description           |
| ------ | ----------------------------- | --------------------- |
| PUT    | `/api/features/{id}/{action}` | Change feature status |
| POST   | `/api/features`               | Create new feature    |
| GET    | `/api/features`               | List all features     |
| GET    | `/api/features/{featureId}`   | Get feature detail    |
| GET    | `/api/features/check`         | Check feature status  |
| PUT    | `/api/features/{id}`          | Update feature        |

### Feature Configuration
| Method | Route                                           | Description           |
| ------ | ---------------------------------------- | --------------------- |
| PUT    | `/api/features/enable-disable`           | Change feature status |
| POST   | `/api/features/config`                   | Create new config     |
| GET    | `/api/features/config/{id}`              | Get config by ID      |
| GET    | `/api/features/config`                   | List all configs      |
| GET    | `/api/features/config/{featureConfigId}` | Get config by feature config ID |


## 💾 Data Model

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
    "role": ["USER", "ADMIN", "GUEST"],
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

## 🔍 Usage Examples

### Create feature

```bash
POST /api/features
{
    "name": "dark_mode",
    "description": "Dark mode for the interface",
    "enabledByDefault": false
}
```

### Check feature

```bash
GET /api/features/check?feature=dark_mode&clientId=acme123&env=staging
```

## 🧠 Activation Logic

The system checks in order of priority:

1. Client-specific configuration
2. Environment configuration
3. Default value (`enabledByDefault`)

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
   ```

## 📖 Documentation

Access interactive Swagger documentation at: `http://localhost:8080/swagger-ui.html`
Or access JavaDoc documentation at: `target/site/apidocs/index.html`

## 🤝 Contributing

1. Fork the project
2. Create feature branch (`git checkout -b feature/new-functionality`)
3. Commit changes (`git commit -am 'Add new functionality'`)
4. Push to branch (`git push origin feature/new-functionality`)
5. Create Pull Request

### Optionally you can use the new branch nomenclature as

1. Fork the project
2. Create feature branch (`git branch feature/new-functionality`)
3. Switch to the new branch (`git switch feature/new-functionality`)
4. Make your changes and save them (`git add .` and `git commit -m "Add new functionality"`)
5. Push the branch to the remote repository (`git push --set-upstream origin feature/new-functionality`)
6. Create Pull Request

## 📄 License

This project is under the MIT License - see the [LICENSE](LICENSE) file for more details.
