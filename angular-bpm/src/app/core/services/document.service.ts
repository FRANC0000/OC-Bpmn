import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import {
  ApiResponse,
  DocumentDefinition,
  DocumentInstance,
  CreateDocumentDefinitionRequest,
  CreateDocumentDefinitionResponse,
  SubmitDocumentRequest,
  SubmitDocumentResponse,
} from '../models/document.model';

@Injectable({ providedIn: 'root' })
export class DocumentService {
  private readonly api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createDefinition(req: CreateDocumentDefinitionRequest): Observable<ApiResponse<CreateDocumentDefinitionResponse>> {
    return this.http.post<ApiResponse<CreateDocumentDefinitionResponse>>(`${this.api}/documents/definitions`, req);
  }

  submit(req: SubmitDocumentRequest): Observable<ApiResponse<SubmitDocumentResponse>> {
    return this.http.post<ApiResponse<SubmitDocumentResponse>>(`${this.api}/documents/submit`, req);
  }

  getDefinitions(): Observable<ApiResponse<DocumentDefinition[]>> {
    return this.http.get<ApiResponse<DocumentDefinition[]>>(`${this.api}/documents/definitions`);
  }

  getInstances(): Observable<ApiResponse<DocumentInstance[]>> {
    return this.http.get<ApiResponse<DocumentInstance[]>>(`${this.api}/documents/instances`);
  }
}
