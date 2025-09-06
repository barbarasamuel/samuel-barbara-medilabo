export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  username: string;
  authorities: string[];
}
