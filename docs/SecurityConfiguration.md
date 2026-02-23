# Security Configuration Guide

This document explains the security improvements implemented in the Multi-Vendor Delivery System and how to configure them properly.

## Overview of Security Improvements

### 1. Removed Hardcoded Secrets ✅
**Problem**: JWT secrets and database passwords were hardcoded in source code, visible to anyone with repository access.

**Solution**: All sensitive values are now injected via environment variables.

**Files Modified**:
- `UserAuthenticationService/src/main/java/.../service/JWTService.java`
- `ApiGateway/src/main/java/.../util/JwtUtils.java`
- All `application.properties` files

### 2. Fixed CORS Vulnerability ✅
**Problem**: WebSocket endpoint allowed connections from ANY origin (`*`), enabling potential CSRF attacks.

**Solution**: CORS is now restricted to specific allowed origins configured via environment variables.

**Files Modified**:
- `UserAuthenticationService/src/main/java/.../config/WebSocketConfig.java`

### 3. Environment-Based Configuration ✅
**Problem**: Same configuration used for development, staging, and production.

**Solution**: Configuration now uses environment variables with safe defaults for local development.

## Configuration Instructions

### Local Development Setup

For local development, the application will work with default values. No environment variables are required, but you can override them:

```powershell
# Optional: Set custom values for local development
$env:DB_PASSWORD="root"
$env:DB_PASSWORD_POSTGRES="123456"
$env:JWT_SECRET="TXlTdXBlclNlY3JldEtleUF0TGVhc3QzMkNoYXJzTG9uZzEyMyE="
$env:CORS_ALLOWED_ORIGINS="http://localhost:3000,http://localhost:4200"
```

### Production Setup (REQUIRED)

For production, you **MUST** set environment variables with secure values:

#### Step 1: Generate Secure JWT Secret

**Using OpenSSL (Git Bash/Linux/Mac)**:
```bash
openssl rand -base64 64
```

**Using PowerShell**:
```powershell
$bytes = New-Object byte[] 64
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

#### Step 2: Set Environment Variables

**Windows (System-wide)**:
1. Open "Environment Variables" in System Properties
2. Add new system variables:
   - `DB_PASSWORD`: Your MySQL password
   - `DB_PASSWORD_POSTGRES`: Your PostgreSQL password
   - `JWT_SECRET`: Generated secret from Step 1
   - `CORS_ALLOWED_ORIGINS`: Your frontend URLs (e.g., `https://yourdomain.com`)

**Linux/Mac (in ~/.bashrc or ~/.zshrc)**:
```bash
export DB_PASSWORD="your_secure_password"
export DB_PASSWORD_POSTGRES="your_postgres_password"
export JWT_SECRET="your_generated_secret"
export CORS_ALLOWED_ORIGINS="https://yourdomain.com,https://www.yourdomain.com"
```

**Docker/Docker Compose**:
Create a `.env` file (see `.env.example`) and Docker Compose will automatically load it.

### JWT Secret Synchronization

**CRITICAL**: The `JWT_SECRET` environment variable **MUST** be the same for:
- UserAuthenticationService (generates tokens)
- ApiGateway (validates tokens)

If they don't match, authentication will fail!

### CORS Configuration

The `CORS_ALLOWED_ORIGINS` variable accepts comma-separated URLs:

```
# Single origin
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Multiple origins
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,https://yourdomain.com

# Production example
CORS_ALLOWED_ORIGINS=https://app.yourdomain.com,https://www.yourdomain.com
```

**Important**: 
- Include protocol (`http://` or `https://`)
- No trailing slashes
- No spaces between URLs

## Secret Rotation Best Practices

### When to Rotate Secrets

Rotate secrets when:
- Every 90 days (recommended)
- When an employee with access leaves
- After a security incident
- When secrets may have been exposed

### How to Rotate JWT Secret

1. Generate a new JWT secret
2. Update environment variable on all services
3. Restart all services simultaneously
4. All existing tokens will be invalidated (users must re-login)

### How to Rotate Database Passwords

1. Change password in database server
2. Update environment variable
3. Restart services
4. Verify connectivity

## Secret Management Solutions

For production deployments, consider using dedicated secret management:

### AWS
- **AWS Secrets Manager**: Automatic rotation, encryption at rest
- **AWS Systems Manager Parameter Store**: Free tier available

### Azure
- **Azure Key Vault**: Centralized secret management

### Google Cloud
- **Google Secret Manager**: Integrated with GCP services

### HashiCorp Vault
- Enterprise-grade secret management
- Dynamic secrets, automatic rotation
- Works with any cloud or on-premise

### Kubernetes
- **Kubernetes Secrets**: Base64 encoded secrets
- **Sealed Secrets**: Encrypted secrets in Git
- **External Secrets Operator**: Sync from external secret managers

## Verification

### Verify Environment Variables are Loaded

Add this to your application startup logs to verify (remove in production):

```java
@PostConstruct
public void init() {
    log.info("JWT Secret configured: {}", secretKey != null && !secretKey.isEmpty());
}
```

### Test JWT Authentication

1. Start UserAuthenticationService and ApiGateway
2. Login via `/api/v1/auth/signin`
3. Use returned token to access protected endpoint
4. Verify authentication succeeds

### Test CORS Configuration

1. Start UserAuthenticationService
2. Attempt WebSocket connection from allowed origin → Should succeed
3. Attempt WebSocket connection from disallowed origin → Should fail

## Troubleshooting

### "Could not resolve placeholder 'jwt.secret'"

**Cause**: Environment variable `JWT_SECRET` is not set and no default is configured.

**Solution**: Set the environment variable or use the default in application.properties for local dev.

### Authentication fails after deployment

**Cause**: JWT_SECRET is different between UserAuthenticationService and ApiGateway.

**Solution**: Ensure both services use the exact same JWT_SECRET value.

### WebSocket connection refused

**Cause**: Your frontend origin is not in CORS_ALLOWED_ORIGINS.

**Solution**: Add your frontend URL to the CORS_ALLOWED_ORIGINS environment variable.

## Security Checklist

- [ ] JWT_SECRET is set to a secure, randomly generated value (not the default)
- [ ] JWT_SECRET is the same for UserAuthenticationService and ApiGateway
- [ ] Database passwords are set via environment variables
- [ ] CORS_ALLOWED_ORIGINS contains only your actual frontend URLs
- [ ] `.env` file is added to `.gitignore`
- [ ] Secrets are stored securely (not in code or documentation)
- [ ] Secret rotation schedule is established
- [ ] Production uses a proper secret management solution
