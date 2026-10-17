# Documentación C4 — Sistema Marketplace nexMarket

Modelo C4 en 4 niveles + fichas arquitectónicas por cada capa del proyecto + matriz integral de flujos por endpoint. Los diagramas están redactados en **Mermaid** (renderizables en GitHub, editores Markdown y visores C4).

## Mapa de Documentos

| # | Archivo | Contenido |
|---|---------|-----------|
| — | `README.md` | Índice general y matriz de correspondencia arquitectónica |
| L1 | `01-nivel-1-contexto.md` | C4 Nivel 1: Contexto del marketplace, actores (6 roles) y sistemas externos |
| L2 | `02-nivel-2-contenedores.md` | C4 Nivel 2: Contenedores (`nexmarket-app`, MySQL, MongoDB, Frontend SPA) y red |
| L3 | `03-nivel-3-componentes.md` | C4 Nivel 3: Componentes internos del backend y ciclo de vida de una petición |
| L4 | `04-nivel-4-codigo.md` | C4 Nivel 4: Clases clave, colaboraciones a nivel de código y diseño estático |
| Capa | `05-capa-adapters-rest.md` | Adaptador primario REST: controladores por rol, DTOs validados, mappers y manejo de excepciones |
| Capa | `06-capa-adapters-usecases.md` | Adaptadores de casos de uso (implementación de puertos de entrada `domain/ports/in`) |
| Capa | `07-capa-adapters-persistence.md` | Adaptadores de persistencia JPA (MySQL) y MongoDB (auditoría documental NoSQL) |
| Capa | `08-capa-adapters-security.md` | Adaptador de seguridad: JWT, filtro stateless, `SecurityConfig`, BCrypt, `AuthenticatedUserPrincipal` |
| Capa | `09-capa-domain.md` | Dominio puro: modelos jerárquicos, value objects, 47 servicios de dominio, puertos y excepciones |
| Capa | `10-capa-infrastructure.md` | Infraestructura: `DatabaseSeeder`, configuración de Spring Beans y variables de entorno |
| Flujos | `11-endpoints-flujos.md` | Matriz de endpoints por rol + diagramas de secuencia Mermaid para flujos principales |

## Correspondencia Capas ↔ Código

```text
nexMarket/src/main/java/application/
├── adapters/
│   ├── rest/                   → 05 (Entrada HTTP: Controllers, DTOs, Mappers, ExceptionHandler)
│   ├── useCases/               → 06 (Implementan domain/ports/in orquestando servicios puros)
│   ├── persistence/            → 07 (Implementan domain/ports/out con JPA relacional y MongoDB)
│   │   ├── jpa/                → Entidades, repositorios Spring Data y mappers JPA (MySQL)
│   │   └── mongodb/            → Documentos, repositorios y mappers Mongo (Colección audit_logs)
│   ├── security/               → 08 (JWTProvider, claims mapper/DTOs, filtro, SecurityConfig, UserDetails)
│   ├── config/                 → Adaptador de configuración de negocio (BusinessConfigurationPort)
│   └── notification/           → Adaptador de notificación (NotificationPort)
├── domain/                     → 09 (Núcleo puro desacoplado de frameworks)
│   ├── models/                 → Modelos de entidad (User, Buyer, Seller, Product, Order, etc.)
│   ├── enums/                  → Enumeraciones operativas (ApprovalDecision, AuditSeverity, NotificationChannel)
│   ├── valueObjects/           → Tipos de valor inmutables y estados (UserRole, UserStatus, OrderStatus, etc.)
│   ├── exceptions/             → Excepciones de dominio tipadas
│   ├── ports/
│   │   ├── in/                 → Puertos primarios segregados por rol (PublicAccess, Buyer, etc.)
│   │   └── out/                → Puertos secundarios requeridos por el dominio
│   └── services/               → 47 Servicios de dominio agrupados en 13 subdominios
└── infrastructure/             → 10
    ├── config/                 → DomainServicesConfig (inyección de servicios como Beans)
    └── seed/                   → DatabaseSeeder (semillas de datos para todos los roles con Password123*)
```

Regla transversal de diseño: **las dependencias siempre apuntan hacia el dominio**.
`REST Controllers → Ports In → UseCaseImpl → Domain Services → Ports Out → Persistence / Security Adapters`.
El dominio nunca importa Spring Framework, JPA, Hibernate, MongoDB, servlets ni bibliotecas JSON.
