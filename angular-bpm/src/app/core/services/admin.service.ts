import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {
  TenantSummary, AdminUserSummary, AdminCreateUserRequest, AdminCreateUserResponse,
  UpdateTenantRequest, UpdateUserRequest, StatusRequest, RoleRequest,
  RoleSummary, CreateRoleRequest, UpdateRoleRequest, UpdatePermissionsRequest
} from '../models/admin.model';
import { ApiResponse } from '../models/document.model';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getTenants(): Observable<TenantSummary[]> {
    return this.http.get<ApiResponse<TenantSummary[]>>(`${this.api}/admin/tenants`).pipe(map(r => r.data));
  }

  createTenant(req: { name: string; slug: string; planCode: string }): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.api}/admin/tenants`, req).pipe(map(r => r.data));
  }

  updateTenant(id: string, req: UpdateTenantRequest): Observable<TenantSummary> {
    return this.http.put<ApiResponse<TenantSummary>>(`${this.api}/admin/tenants/${id}`, req).pipe(map(r => r.data));
  }

  updateTenantStatus(id: string, req: StatusRequest): Observable<TenantSummary> {
    return this.http.patch<ApiResponse<TenantSummary>>(`${this.api}/admin/tenants/${id}/status`, req).pipe(map(r => r.data));
  }

  getTenantUsers(tenantId: string): Observable<AdminUserSummary[]> {
    return this.http.get<ApiResponse<AdminUserSummary[]>>(`${this.api}/admin/tenants/${tenantId}/users`).pipe(map(r => r.data));
  }

  createUser(req: AdminCreateUserRequest): Observable<AdminCreateUserResponse> {
    return this.http.post<ApiResponse<AdminCreateUserResponse>>(`${this.api}/admin/users`, req).pipe(map(r => r.data));
  }

  updateUser(id: string, tenantId: string, req: UpdateUserRequest): Observable<AdminUserSummary> {
    return this.http.put<ApiResponse<AdminUserSummary>>(`${this.api}/admin/users/${id}?tenantId=${tenantId}`, req).pipe(map(r => r.data));
  }

  updateUserStatus(id: string, tenantId: string, req: StatusRequest): Observable<AdminUserSummary> {
    return this.http.patch<ApiResponse<AdminUserSummary>>(`${this.api}/admin/users/${id}/status?tenantId=${tenantId}`, req).pipe(map(r => r.data));
  }

  updateUserRole(id: string, tenantId: string, req: RoleRequest): Observable<AdminUserSummary> {
    return this.http.patch<ApiResponse<AdminUserSummary>>(`${this.api}/admin/users/${id}/role?tenantId=${tenantId}`, req).pipe(map(r => r.data));
  }

  deleteUser(id: string, tenantId: string): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.api}/admin/users/${id}?tenantId=${tenantId}`).pipe(map(r => r.data));
  }

  getRoles(): Observable<RoleSummary[]> {
    return this.http.get<ApiResponse<RoleSummary[]>>(`${this.api}/admin/roles`).pipe(map(r => r.data));
  }

  createRole(req: CreateRoleRequest): Observable<RoleSummary> {
    return this.http.post<ApiResponse<RoleSummary>>(`${this.api}/admin/roles`, req).pipe(map(r => r.data));
  }

  updateRole(id: string, req: UpdateRoleRequest): Observable<RoleSummary> {
    return this.http.put<ApiResponse<RoleSummary>>(`${this.api}/admin/roles/${id}`, req).pipe(map(r => r.data));
  }

  deleteRole(id: string): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.api}/admin/roles/${id}`).pipe(map(r => r.data));
  }
}
