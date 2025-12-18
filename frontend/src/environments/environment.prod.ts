// src/environments/environment.prod.ts
export const environment = {
  production: true,
  services: {
    auth: 'http://localhost:8081/api',
    management: 'http://localhost:8082/api', 
    billing: 'http://localhost:8083/api',
    ai: 'http://localhost:8084/api'
  }
};