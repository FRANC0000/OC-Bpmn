export interface TenantSummary {
  id: string;
  name: string;
  slug: string;
  schemaName: string;
  plan: string;
  status: string;
}

export interface UpdateTenantRequest {
  name: string;
  slug: string;
}

export interface AdminUserSummary {
  id: string;
  email: string;
  displayName: string;
  status: string;
  tenantId: string;
  lastLoginAt: string | null;
  role: string | null;
}

export interface UpdateUserRequest {
  displayName: string;
  email: string;
}

export interface StatusRequest {
  status: string;
}

export interface RoleRequest {
  role: string;
}

export interface AdminCreateUserRequest {
  email: string;
  password: string;
  displayName: string;
  tenantId: string;
}

export interface AdminCreateUserResponse {
  userId: string;
  tenantId: string;
  email: string;
  displayName: string;
}

export interface RoleSummary {
  id: string;
  name: string;
  description: string | null;
  permissions: string;
  roleType: string;
  isSystem: boolean;
}

export interface CreateRoleRequest {
  name: string;
  description?: string;
  permissions?: string;
  roleType?: string;
}

export interface UpdateRoleRequest {
  name: string;
  description?: string;
  permissions?: string;
  roleType?: string;
}

export interface UpdatePermissionsRequest {
  permissions: string;
}
