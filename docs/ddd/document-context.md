# Document Context — Análisis DDD

## Ubiquitous Language
| Término | Definición |
|---------|-----------|
| **Document** | Definición/plantilla documental con bloques y metadatos |
| **Document Version** | Versión semántica de un documento |
| **Document Instance** | Documento concreto generado durante ejecución (con folio y snapshot) |
| **Block** | Componente visual dentro de un formulario: Section, Columns, Tabs, Grid |
| **Metadata** | Campo tipado dentro de un documento: texto, número, fecha, lista, usuario |
| **Grid Block** | Bloque especializado con columnas tipadas, fórmulas y agregaciones |
| **Catalog** | Lista de valores jerárquica con dependencias |
| **Folio** | Identificador secuencial configurable: DOC-{YYYY}-{000000} |

## Bounded Context: Document

```
┌─────────────────────────────────────────────────────────────┐
│                   DOCUMENT CONTEXT                            │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Aggregate: DocumentDefinition                          │  │
│  │  Root: DocumentDefinition (AggregateRoot)               │  │
│  │                                                         │  │
│  │  Entities:                                              │  │
│  │  ├── DocumentVersion (Entity)                           │  │
│  │  │   ├── version: SemanticVersion                       │  │
│  │  │   ├── status: DocumentStatus (DRAFT|ACTIVE|INACTIVE) │  │
│  │  │   └── blocks: List<Block>                            │  │
│  │  │                                                       │  │
│  │  Value Objects:                                         │  │
│  │  ├── DocumentId (UUID)                                  │  │
│  │  ├── DocumentCode (String) — código único por tenant    │  │
│  │  ├── Block (type: Section|Columns|Tabs|Accordion|Grid)  │  │
│  │  ├── Metadata (name, type, config, validations)         │  │
│  │  ├── MetadataConfig (visible, editable, required)       │  │
│  │  ├── TaskOverride (taskId, modifiedMetadata)            │  │
│  │  └── Expression (formula / validation / visibility)     │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── DocumentPublished (documentId, version)            │  │
│  │  ├── VersionDeprecated (documentId, version)            │  │
│  │  └── MetadataChanged (documentId, metadataName)         │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Aggregate: DocumentInstance                            │  │
│  │  Root: DocumentInstance (AggregateRoot)                 │  │
│  │                                                         │  │
│  │  Value Objects:                                         │  │
│  │  ├── InstanceId (UUID)                                  │  │
│  │  ├── Folio (String) — DOC-2026-000001                  │  │
│  │  ├── Snapshot (frozen blocks + metadata definitions)    │  │
│  │  ├── Values (Map<MetadataName, Object>)                 │  │
│  │  ├── Status (DRAFT|COMPLETED|SIGNED|LOCKED)             │  │
│  │  └── AuditTrail (List<AuditEntry>)                      │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── InstanceCreated (instanceId, folio)                │  │
│  │  ├── InstanceCompleted (instanceId)                     │  │
│  │  ├── DocumentSigned (instanceId, signerId)              │  │
│  │  └── InstanceLocked (instanceId, reason)                │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Aggregate: Catalog                                     │  │
│  │  Root: Catalog (AggregateRoot)                          │  │
│  │                                                         │  │
│  │  Value Objects:                                         │  │
│  │  ├── CatalogId (UUID)                                   │  │
│  │  ├── CatalogItem (code, label, parentId)                 │  │
│  │  ├── CatalogLevel (GLOBAL|TENANT|ORGANIZATION|DOCUMENT) │  │
│  │  └── Dependencies (Map<CatalogId, Filter>)              │  │
│  │                                                         │  │
│  │  Domain Events:                                         │  │
│  │  ├── CatalogUpdated (catalogId, version)                │  │
│  └─────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Repository Interfaces

```java
interface DocumentDefinitionRepository {
    DocumentDefinition findById(DocumentId id);
    DocumentDefinition findByCode(String code, TenantId tenantId);
    List<DocumentDefinition> findAllByTenant(TenantId tenantId);
    DocumentDefinition save(DocumentDefinition document);
}

interface DocumentInstanceRepository {
    DocumentInstance findById(InstanceId id);
    DocumentInstance findByFolio(String folio);
    List<DocumentInstance> findByTenant(TenantId tenantId, Pageable pageable);
    DocumentInstance save(DocumentInstance instance);
}

interface CatalogRepository {
    Catalog findById(CatalogId id);
    List<Catalog> findByLevel(CatalogLevel level, TenantId tenantId);
    Catalog save(Catalog catalog);
}
```

## Use Cases

| Use Case | Input | Output |
|----------|-------|--------|
| `CreateDocumentDefinition` | code, name, description | DocumentId |
| `PublishDocumentVersion` | documentId, blocks, metadata | SemanticVersion |
| `CreateDocumentInstance` | documentId, version, context | InstanceId (with Folio) |
| `UpdateInstanceValues` | instanceId, values, userId | void |
| `CompleteInstance` | instanceId | void |
| `CreateCatalog` | name, level, items | CatalogId |
| `GetCatalogTree` | catalogId | Tree<CatalogItem> |
| `ResolveFolio` | format (DOC-{YYYY}-{000000}) | String |

## Grid Block Rules

- Grid is a specialized Block, NOT metadata
- Supports typed columns (text, number, date, formula)
- Formulas per row and per column (total, average, sum)
- Virtual scrolling for large datasets (10k+ rows)
- Configurable limit (unlimited or capped)
- Pagination with efficient DB queries
