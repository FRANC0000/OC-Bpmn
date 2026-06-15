export interface DocumentDefinition {
  id: string;
  code: string;
  name: string;
  description?: string;
  status: string;
  versions: DocumentVersion[];
}

export interface DocumentVersion {
  id: string;
  version: string;
  status: string;
  blocksJson: string;
  metadataJson: string;
  createdAt: string;
  createdBy: string;
}

export interface DocumentInstance {
  id: string;
  documentId: string;
  version: string;
  folio: string;
  status: string;
  valuesJson: string;
  snapshotJson: string;
  processInstanceId?: string;
  createdBy?: string;
  createdAt: string;
  completedAt?: string;
}

export interface CreateDocumentDefinitionRequest {
  code: string;
  name: string;
  description?: string;
}

export interface CreateDocumentDefinitionResponse {
  id: string;
  code: string;
  name: string;
}

export interface SubmitDocumentRequest {
  documentCode: string;
  valuesJson: string;
  createdBy: string;
}

export interface SubmitDocumentResponse {
  instanceId: string;
  folio: string;
  status: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
