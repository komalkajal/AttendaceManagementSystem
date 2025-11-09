# Quick Start Guide for Render Deployment

## Step-by-Step: First Time Deployment

### Step 1: Create Database First (Recommended)

**Before creating your web service, create a database:**

1. Go to Render Dashboard
2. Click **"New +"** → **"PostgreSQL"**
3. Fill in:
   - **Name**: `attendance-db` (or any name)
   - **Region**: Choose closest to you
   - **Database**: Leave default or name it
   - **User**: Leave default
   - **PostgreSQL Version**: Latest (default)
4. Click **"Create Database"**
5. **Wait 2-3 minutes** for database to be created
6. Once created, click on the database
7. Go to **"Connections"** or **"Info"** tab
8. **Copy these values** (you'll need them):
   - Internal Database URL (or individual host, port, database, user, password)

### Step 2: Create Web Service

1. Click **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. Select your repository
4. Fill in:
   - **Name**: `attendance-management-system`
   - **Environment**: **Docker**
   - **Region**: Same as database
   - **Branch**: `main` (or your branch)

### Step 3: Environment Variables (When Render Asks)

**If Render asks for environment variables during setup:**

#### Option A: Skip for Testing (Quick Test)

- Click **"Skip"** or **"Continue"** without adding variables
- App will deploy with H2 in-memory database
- ⚠️ Data will be lost on restart
- Good for initial testing

#### Option B: Add Database Variables (Production)

If you created a database in Step 1, add these:

**Click "Add Environment Variable" for each:**

1. **JDBC_DATABASE_URL**
   - **Key**: `JDBC_DATABASE_URL`
   - **Value**: Convert your database URL
     - If Render shows: `postgresql://user:pass@host:5432/dbname`
     - Use: `jdbc:postgresql://host:5432/dbname`
     - Example: `jdbc:postgresql://dpg-abc123-a.oregon-postgres.render.com:5432/attendance_db_abc1`

2. **JDBC_DATABASE_USERNAME**
   - **Key**: `JDBC_DATABASE_USERNAME`
   - **Value**: Your database username (from database info)

3. **JDBC_DATABASE_PASSWORD**
   - **Key**: `JDBC_DATABASE_PASSWORD`
   - **Value**: Your database password (from database info)

4. **PORT** (Optional)
   - **Key**: `PORT`
   - **Value**: Leave **BLANK** (Render sets automatically)

### Step 4: Build & Deploy Settings

**Build Command**: (Leave empty - Dockerfile handles it)

**Start Command**: (Leave empty - Dockerfile handles it)

### Step 5: Deploy

1. Click **"Create Web Service"**
2. Wait for build (5-10 minutes first time)
3. Watch build logs
4. Once deployed, visit your URL

## If You Skipped Environment Variables

**You can add them later:**

1. Go to your Web Service
2. Click **"Environment"** tab
3. Click **"Add Environment Variable"**
4. Add the three database variables
5. Render will automatically restart with new settings

## Converting Database URL Format

**Render shows:**
```
postgresql://user:password@host:5432/database
```

**You need:**
```
jdbc:postgresql://host:5432/database
```

**How to convert:**
1. Remove `postgresql://` prefix
2. Remove `user:password@` part
3. Add `jdbc:postgresql://` prefix
4. Keep `host:port/database` part

**Example:**
- Render shows: `postgresql://attendance_user:abc123@dpg-xyz-a.render.com:5432/attendance_db`
- You enter: `jdbc:postgresql://dpg-xyz-a.render.com:5432/attendance_db`
- Username: `attendance_user`
- Password: `abc123`

## Quick Decision Guide

**Choose based on your needs:**

| Scenario | Action | Environment Variables |
|----------|--------|---------------------|
| **Just testing** | Skip variables | None needed |
| **Real project** | Add database | All 3 required |
| **Not sure yet** | Skip now, add later | Can add after deployment |

## After Deployment

1. Visit your app URL
2. Login with: `admin` / `admin123`
3. Test the application
4. If you skipped variables, add them later for persistence

## Troubleshooting

**"Can't connect to database"**
- Check URL format (must start with `jdbc:postgresql://`)
- Verify username and password
- Ensure database is running

**"Data lost after restart"**
- You're using H2 (no variables set)
- Add database environment variables
- Redeploy

**"Build failed"**
- Check Dockerfile exists
- Verify code is pushed to GitHub
- Check build logs for errors

## Summary

✅ **For Testing**: Skip environment variables, deploy, test
✅ **For Production**: Create database first, then add variables during setup
✅ **Can Add Later**: You can always add environment variables after deployment

**Most Common Path:**
1. Skip variables for first deployment (quick test)
2. Create database on Render
3. Add environment variables in service settings
4. Service restarts automatically with database

