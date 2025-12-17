// src/environments/environment.ts
export const environment = {
  production: false,
  services: {
    auth: 'http://localhost:8079/api',
    management: 'http://localhost:8081/api', 
    billing: 'http://localhost:8080/api',
    ai: 'http://localhost:8082/api'
  }
};
