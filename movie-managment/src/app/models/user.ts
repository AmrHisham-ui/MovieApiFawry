export enum UserRole {
    ADMIN = 'ROLE_ADMIN',
    USER = 'ROLE_USER'
  }
  
  export interface User {
    username: string;
    role: UserRole; // ✅ Ensure this expects a UserRole enum
    password?:string;
    email?:string;
  }
  