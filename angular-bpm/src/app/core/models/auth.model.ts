export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: string;
  email: string;
  displayName: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  displayName: string;
  tenantId?: string;
}

export interface RegisterResponse {
  userId: string;
  email: string;
  displayName: string;
}

export interface User {
  id: string;
  email: string;
  displayName: string;
  status: string;
  tenantId: string;
}
