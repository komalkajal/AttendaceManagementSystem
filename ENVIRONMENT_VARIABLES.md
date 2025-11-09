# Environment Variables Guide

## Quick Answer

**Environment variables are OPTIONAL for testing, but REQUIRED for production!**

## Why?

- **Without environment variables**: App uses H2 in-memory database
  - ✅ Works for testing
  - ❌ **Loses all data when container restarts**
  - ❌ Not suitable for production

- **With environment variables**: App uses persistent database (MySQL/PostgreSQL)
  - ✅ Data persists across restarts
  - ✅ Suitable for production
  - ✅ Reliable for real use

## Environment Variables

### Required for Production

| Variable | Required? | Default | Description |
|----------|-----------|---------|-------------|
| `JDBC_DATABASE_URL` | **YES** (production) | `jdbc:h2:mem:attendance` | Database connection URL |
| `JDBC_DATABASE_USERNAME` | **YES** (production) | `sa` | Database username |
| `JDBC_DATABASE_PASSWORD` | **YES** (production) | `` (empty) | Database password |
| `PORT` | **NO** | `8080` | Server port (Render sets automatically) |

## Setup Options

### Option 1: Quick Test (No Environment Variables)

**For testing only - data will be lost on restart!**

1. Deploy without any environment variables
2. App will use H2 in-memory database
3. Works immediately, no database setup needed
4. ⚠️ **Warning**: All data is lost when container restarts

**When to use:**
- Testing the application
- Learning/debugging
- Temporary demos

**When NOT to use:**
- Production deployments
- Real attendance data
- Long-term use

### Option 2: Production Setup (With Database)

**Recommended for real use - data persists!**

#### Step 1: Create Database on Render

1. Go to Render Dashboard
2. Click **"New +"** → **"PostgreSQL"** (or MySQL if available)
3. Name: `attendance-db` (or your choice)
4. Select same region as your web service
5. Click **"Create Database"**
6. Wait for database to be created

#### Step 2: Get Database Connection Details

1. Click on your database
2. Go to **"Connections"** or **"Info"** tab
3. Copy the connection details:
   - **Internal Database URL**: `postgresql://user:password@host:port/database`
   - **Host**: Database hostname
   - **Port**: Database port (usually 5432 for PostgreSQL)
   - **Database**: Database name
   - **User**: Database username
   - **Password**: Database password

#### Step 3: Set Environment Variables

In your Web Service settings, go to **"Environment"** tab and add:

**For PostgreSQL (Render's default):**
```
JDBC_DATABASE_URL = jdbc:postgresql://host:port/database
JDBC_DATABASE_USERNAME = your_username
JDBC_DATABASE_PASSWORD = your_password
```

**For MySQL:**
```
JDBC_DATABASE_URL = jdbc:mysql://host:port/database
JDBC_DATABASE_USERNAME = your_username
JDBC_DATABASE_PASSWORD = your_password
```

#### Step 4: Format the Database URL

**PostgreSQL format:**
```
jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/attendance_db
```

**MySQL format:**
```
jdbc:mysql://mysql-host:3306/attendance_db
```

**Important Notes:**
- Use **Internal Database URL** for Render databases (faster, free)
- Use **External Database URL** for databases outside Render
- Remove `postgresql://` or `mysql://` prefix, use `jdbc:postgresql://` or `jdbc:mysql://`
- Include port number (5432 for PostgreSQL, 3306 for MySQL)

## Examples

### Example 1: Render PostgreSQL (Recommended)

**Environment Variables:**
```
JDBC_DATABASE_URL = jdbc:postgresql://dpg-abc123-a.oregon-postgres.render.com:5432/attendance_db_abc1
JDBC_DATABASE_USERNAME = attendance_db_user
JDBC_DATABASE_PASSWORD = abc123xyz789password
PORT = (leave blank - Render sets automatically)
```

### Example 2: External MySQL

**Environment Variables:**
```
JDBC_DATABASE_URL = jdbc:mysql://mysql.example.com:3306/attendance_db
JDBC_DATABASE_USERNAME = admin
JDBC_DATABASE_PASSWORD = mySecurePassword
PORT = (leave blank)
```

### Example 3: Testing (No Variables)

**Environment Variables:**
```
(Leave all blank - uses H2 in-memory database)
```

## How to Add Environment Variables on Render

1. Go to your Web Service on Render
2. Click on **"Environment"** tab
3. Click **"Add Environment Variable"**
4. Enter:
   - **Key**: `JDBC_DATABASE_URL`
   - **Value**: Your database URL
5. Click **"Save Changes"**
6. Repeat for other variables
7. Render will automatically restart your service

## Verification

### Check if Database is Connected

1. Deploy your application
2. Visit your app URL
3. Try to login (admin/admin123)
4. If login works, database is connected!
5. Create a test user/class
6. Restart your service
7. If data persists, database is working correctly!

## Troubleshooting

### Problem: "Database connection failed"

**Solution:**
- Check database URL format
- Verify username and password
- Ensure database is running
- Check if using Internal URL (for Render databases)
- Verify database allows connections from Render's network

### Problem: "Table doesn't exist"

**Solution:**
- Application creates tables automatically on first run
- Check application logs for errors
- Verify database user has CREATE TABLE permissions
- Try redeploying the application

### Problem: "Data lost after restart"

**Solution:**
- You're using H2 in-memory database
- Set up a persistent database (PostgreSQL/MySQL)
- Add environment variables
- Redeploy application

## Summary

| Scenario | Environment Variables | Database | Data Persistence |
|----------|----------------------|----------|------------------|
| **Testing** | Optional (none) | H2 (in-memory) | ❌ Lost on restart |
| **Production** | **Required** | PostgreSQL/MySQL | ✅ Persistent |

## Recommendation

**For Production: ALWAYS set up a database and environment variables!**

1. ✅ Create PostgreSQL database on Render
2. ✅ Set environment variables
3. ✅ Deploy application
4. ✅ Verify data persists after restart

This ensures your attendance data is safe and persistent! 🎉

