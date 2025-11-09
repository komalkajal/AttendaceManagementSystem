# Deployment Guide

## Quick Start for Render

### Prerequisites
1. GitHub repository with your code
2. Render account (free tier available)
3. Database (MySQL or PostgreSQL)

### Step-by-Step Deployment

1. **Push your code to GitHub**
   ```bash
   git add .
   git commit -m "Initial commit"
   git push origin main
   ```

2. **Create a new Web Service on Render**
   - Go to https://dashboard.render.com
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Select your repository

3. **Configure the Service**
   - **Name**: attendance-management-system (or your preferred name)
   - **Environment**: Java
   - **Build Command**: `mvn clean package && mkdir -p target/webapp && cp -r src/main/webapp/* target/webapp/`
   - **Start Command**: `java -jar target/attendance-management-system.jar`

4. **Set Environment Variables**
   Click on "Environment" tab and add:
   - `JDBC_DATABASE_URL`: `jdbc:mysql://your-host:3306/your-database` or `jdbc:postgresql://your-host:5432/your-database`
   - `JDBC_DATABASE_USERNAME`: Your database username
   - `JDBC_DATABASE_PASSWORD`: Your database password
   - `PORT`: Leave blank (Render sets this automatically)

5. **Create Database (if using Render's database)**
   - Go to "New +" → "PostgreSQL" (or MySQL if available)
   - Create a new database
   - Copy the connection details to environment variables

6. **Deploy**
   - Click "Create Web Service"
   - Render will build and deploy your application
   - Wait for the deployment to complete (usually 5-10 minutes)

7. **Access your application**
   - Your app will be available at `https://your-app-name.onrender.com`
   - Default admin login: `admin` / `admin123`

## Local Testing

### Using Maven and Embedded Tomcat

1. **Build the project**
   ```bash
   mvn clean package
   mkdir -p target/webapp
   cp -r src/main/webapp/* target/webapp/
   ```

2. **Run the application**
   ```bash
   java -jar target/attendance-management-system.jar
   ```

3. **Access the application**
   - Open http://localhost:8080
   - Login with admin/admin123

### Using External Tomcat

1. **Build the WAR file**
   ```bash
   mvn clean package
   ```

2. **Deploy to Tomcat**
   ```bash
   cp target/attendance-management-system.war $TOMCAT_HOME/webapps/
   ```

3. **Start Tomcat**
   ```bash
   $TOMCAT_HOME/bin/startup.sh
   ```

4. **Access the application**
   - Open http://localhost:8080/attendance-management-system

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Check your database credentials
   - Ensure database is accessible from Render's servers
   - For Render databases, use the internal connection URL

2. **Port Already in Use**
   - Render sets the PORT environment variable automatically
   - Don't hardcode port numbers

3. **Webapp Directory Not Found**
   - Ensure build command includes copying webapp files
   - Check that `target/webapp` directory exists after build

4. **Class Not Found Errors**
   - Ensure maven-shade-plugin includes all dependencies
   - Check that all required JARs are in the classpath

### Database Setup

#### For MySQL:
```sql
CREATE DATABASE attendance_db;
CREATE USER 'attendance_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON attendance_db.* TO 'attendance_user'@'%';
FLUSH PRIVILEGES;
```

#### For PostgreSQL:
```sql
CREATE DATABASE attendance_db;
CREATE USER attendance_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE attendance_db TO attendance_user;
```

The application will automatically create tables on first run.

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `JDBC_DATABASE_URL` | Database connection URL | `jdbc:mysql://host:3306/db` |
| `JDBC_DATABASE_USERNAME` | Database username | `myuser` |
| `JDBC_DATABASE_PASSWORD` | Database password | `mypassword` |
| `PORT` | Server port (auto-set by Render) | `8080` |

## Support

For issues, check:
1. Render logs in the dashboard
2. Application logs for errors
3. Database connection status
4. Environment variables configuration

