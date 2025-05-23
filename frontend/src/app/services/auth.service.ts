import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, filter, map, of } from 'rxjs';
import { Account } from '../models/account.model';
import { Router } from '@angular/router';

const BASE_URL = '/api/auth/';

@Injectable({ providedIn: 'root' })
export class AuthService {
  
  private currentUser: Account = {} as Account;
  public logged: boolean = false;
  public notification: boolean = false;
  private userLoadedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private isLoaded = false;

  constructor(private http: HttpClient, public router: Router, /*private accountService: AccountService*/){
    this.getCurrentUser();
  }

  login(username: string, password: string): Observable<String> {
    return this.http.post<string>(
      '/api/auth/login',
      { username, password },
      { withCredentials: true }
    ).pipe(
      map((response) => {
        this.getCurrentUser();
        return response;
      }),
      catchError((err) => {
        throw err;
      })
    );
  }

  logout() {
    this.http
      .post<HttpResponse<any>>('/api/auth/logout', { withCredentials: true })
      .subscribe(() => {
        this.logged = false;
        this.currentUser = {} as Account;
        this.router.navigate(['']);
      });
  }

  getCurrentUser(): Account {
    this.http
      .get<ApiResponse>('/api/accounts/me', { withCredentials: true })
      .subscribe({
        next: (response: ApiResponse) => {
          this.currentUser = response.account;
          this.logged = true;
          this.notification = this.currentUser.pendingFriends?.length > 0 || false;
          this.userLoadedSubject.next(true); 
        },
        error: (err) => {
          this.userLoadedSubject.next(false);
          const currentUrl = this.router.url;

          if (!['/', '/login', '/register'].includes(currentUrl)) {
            this.router.navigate(['/error']);
          }
        }
      });

    return this.currentUser;
  }


  
  createUser(nickName: string, firstName: string, lastName: string, email: string, password: string): Observable<any> {

    return this.http.post<string>(
      '/api/accounts/',
      {
        nickName,
        firstName,
        lastName,
        email,
        password
      }
    );
  }


  getUser(): Account {
    return this.currentUser;
  }

  /////////////
  // PROFILE //
  /////////////

  getUserFirstName(): string{
    return this.currentUser.firstName;
  }
  getUserLastName(): string{
    return this.currentUser.lastName;
  }
  getUserNickName(): string{
    return this.currentUser.nickName;
  }
  getUserEmail(): string{
    return this.currentUser.email;
  }
  getUserPassword(): string{
    return this.currentUser.password;
  }
  getUserProfilePicture(): number[]{
    return this.currentUser.profilePicture;
  }

  getUserMyFriends(): string[]{
    return this.currentUser.myFriends;
  }
  getUserPendingFriends(): string[]{
    return this.currentUser.pendingFriends;
  }
  getUserRequestFriends(): string[]{
    return this.currentUser.requestFriends;
  }

  isUserMyFriendsEmpty(): boolean {
    return !this.currentUser?.myFriends?.length;
  }
  isUserPendingFriendsEmpty(): boolean {
    return !this.currentUser?.pendingFriends?.length;
  }
  isUserRequestFriendsEmpty(): boolean {
    return !this.currentUser?.requestFriends?.length;
  }


  ////////////
  // HEADER //
  ////////////

  isLogged(): boolean {
    return this.logged;
  }

  hasNotification(): boolean{
    return this.notification;
  }

  userLoaded(): Observable<boolean> {
    return this.userLoadedSubject.asObservable();
  }

 
}

export interface ApiResponse {
  account: Account;
  status: string;
}
