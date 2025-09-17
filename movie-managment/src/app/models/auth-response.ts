export interface ApiResponse {
    success: boolean;
    message: string;
  }
  
  export interface LoginResponse {
    token: string;
    username: string;
    role: string;
  }
