# Render Deployment with Docker - Step by Step

Since Render doesn't show Java as an option, we'll use **Docker** instead. This is actually better and more reliable!

## Step-by-Step Instructions

### 1. Create New Web Service on Render

1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository
4. Select your repository

### 2. Configure the Service

#### Basic Settings:
- **Name**: `attendance-management-system` (or your choice)
- **Region**: Choose closest to you
- **Branch**: `main` (or your default branch)
- **Root Directory**: Leave empty (or `.`)

#### **IMPORTANT: Select Docker**
- **Environment**: Select **"Docker"** (not Java)
- Render will automatically detect the `Dockerfile`

#### Instance Type:
- **Free Tier**: 512 MB RAM (good for testing)
- **Starter**: $7/month (recommended for production)

### 3. Environment Variables

Click on **"Environment"** tab and add:

| Key | Value | Description |
|-----|-------|-------------|
| `JDBC_DATABASE_URL` | `jdbc:mysql://host:port/database` | Your database URL |
| `JDBC_DATABASE_USERNAME` | `your_username` | Database username |
| `JDBC_DATABASE_PASSWORD` | `your_password` | Database password |
| `PORT` | Leave **BLANK** | Render sets this automatically |

#### Database Setup:

**Option 1: Use Render's PostgreSQL (Recommended)**
1. Go to **"New +"** → **"PostgreSQL"**
2. Create database
3. Copy the **Internal Database URL**
4. Format: `jdbc:postgresql://host:port/database`

**Option 2: External MySQL/PostgreSQL**
- Use your own database connection URL
- Format: `jdbc:mysql://host:port/database` or `jdbc:postgresql://host:port/database`

### 4. Deploy

1. Click **"Create Web Service"**
2. Render will:
   - Build the Docker image
   - Start the container
   - Provide a URL (e.g., `https://attendance-management-system.onrender.com`)

### 5. Wait for Deployment

- First build takes 5-10 minutes
- Subsequent deployments are faster
- Watch the build logs for progress

### 6. Access Your Application

1. Once deployed, visit your URL
2. Login with: `admin` / `admin123`
3. Start using the system!

## What Happens Behind the Scenes

1. **Build Stage**: 
   - Maven builds your Java application
   - Creates JAR file with all dependencies
   - Copies webapp files

2. **Runtime Stage**:
   - Runs Java with embedded Tomcat
   - Serves your application
   - Connects to database

## Configuration Summary

### What You Need to Enter:

**Environment**: `Docker` ✅

**Build Command**: (Leave empty - Dockerfile handles this) ✅

**Start Command**: (Leave empty - Dockerfile handles this) ✅

**Dockerfile Path**: (Leave empty - uses `Dockerfile` in root) ✅

**That's it!** Render automatically detects and uses the Dockerfile.

## Troubleshooting

### Build Fails
- Check Dockerfile syntax
- Verify all files are in repository
- Check build logs for errors

### Application Won't Start
- Check environment variables
- Verify database connection
- Check application logs

### Database Connection Error
- Verify database URL format
- Check credentials
- Ensure database is accessible
- For Render databases, use Internal URL

### Port Issues
- Don't set PORT manually
- Render sets it automatically
- Application reads from `PORT` env variable

## Files Included

- ✅ `Dockerfile` - Docker configuration
- ✅ `.dockerignore` - Files to exclude from build
- ✅ All source code
- ✅ `pom.xml` - Maven configuration

## Advantages of Docker Deployment

1. ✅ Works on any platform
2. ✅ Consistent environment
3. ✅ Easy to reproduce
4. ✅ Better isolation
5. ✅ Works even if Java option isn't available

## Quick Checklist

- [ ] Dockerfile in repository
- [ ] Code pushed to GitHub
- [ ] Render account created
- [ ] Web service created with Docker
- [ ] Environment variables set
- [ ] Database created
- [ ] Deployment successful
- [ ] Application accessible

## Support

If you encounter issues:
1. Check Render build logs
2. Check application logs
3. Verify environment variables
4. Test database connection
5. Review Dockerfile

## Next Steps After Deployment

1. ✅ Test login (admin/admin123)
2. ✅ Create test users
3. ✅ Create classes and subjects
4. ✅ Mark test attendance
5. ✅ Export reports
6. ✅ Test XML upload

Your application is now live! 🎉

