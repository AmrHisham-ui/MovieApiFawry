// src/app/auth/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

import { ApiResponse, LoginResponse } from '../models/auth-response';
import { User, UserRole } from '../models/user';
import { Password } from 'primeng/password';
import { EmailValidator } from '@angular/forms';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth'; // Adjust to your backend URL
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {
    // Check if user is stored in sessionStorage
    const storedUser = sessionStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  register(user: User): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/register`, user);
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          if (response && response.token) {
            
            const user : User = {
              username: response.username,
              role: response.role as UserRole
            };
            
            // Store user and token
            sessionStorage.setItem('currentUser', JSON.stringify(user));
            sessionStorage.setItem('token', response.token);
            
            // Update current user subject
            this.currentUserSubject.next(user);
          }
        })
      );
  }
  getCurrentUser(): Observable<User> {
    return this.http.get<User>('YOUR_BACKEND_API_URL/current-user');
  }

  logout(): void {
    sessionStorage.removeItem('currentUser');
    sessionStorage.removeItem('token');
    this.currentUserSubject.next(null);
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }
  setCurrentUser(user: User) {
    this.currentUserSubject.next(user);
  }

  get isLoggedIn(): boolean {
    return !!this.currentUser;
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}