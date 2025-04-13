import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Account } from '../models/account.model';

const BASE_URL = '/api/accounts/';

@Injectable({ providedIn: 'root' })
export class AccountService {

  constructor(private http: HttpClient) { }

  getUserId(gmail: String): Observable<number>{
    return this.http.get<number>(BASE_URL+'userId?gmail='+gmail)
  }

  getMyFriends(index: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', index.toString());
    params = params.append('size', '3');

    return this.http.get(BASE_URL+'myFriends', {
      params: params,
    }) as Observable<any>;
  }

  getPendingFriends(index: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', index.toString());
    params = params.append('size', '3');

    return this.http.get(BASE_URL+'pendingFriends', {
      params: params,
    }) as Observable<any>;
  }

  getRequestFriends(index: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', index.toString());
    params = params.append('size', '3');

    return this.http.get(BASE_URL+'requestFriends', {
      params: params,
    }) as Observable<any>;
  }

  acceptFriend(nickName: string): Observable<any>{
    return this.http.put(BASE_URL+'pendingFriends/'+nickName, null) as Observable<any>;
  }

  denyFriend(nickName: string): Observable<any>{
    return this.http.delete(BASE_URL+'pendingFriends/'+nickName) as Observable<any>;
  }

  searchFriend(searchTerm: string): Observable<any>{
    console.log("Adios");
    console.log(this.http.get(BASE_URL+searchTerm) as Observable<any>);
    return this.http.get(BASE_URL+searchTerm) as Observable<any>;
  }

  // getUserSubjects(index: number): Observable<any> {
  //   let params = new HttpParams();
  //   params = params.append('page', index.toString());
  //   params = params.append('size', '3');

  //   return this.http.get(BASE_URL, {
  //     params: params,
  //   }) as Observable<any>;
  // }

  // isGmailTeacher(gmail: String): Observable<boolean>{
  //   return this.http.get<boolean>(BASE_URL+'teacherGmail?gmail='+gmail)
  // }

  // isGmailStudent(gmail: String): Observable<boolean>{
  //   return this.http.get<boolean>(BASE_URL+'studentGmail?gmail='+gmail)
  // }

  // enroll(id: BigInt, studentid: number) {
  //   return this.http.post("/api/users/enroll/" + id.toString() + "/" + studentid.toString(), {})
  // }

  // setProfileImage(image: File){
  //   const formData = new FormData();
  //   formData.append('file', image);
  //   return this.http.put('/api/users/image', formData, { withCredentials: true })
  // }

  // getUserSubjectsNP(): Observable<any>{
  //   return this.http.get("/api/users/userSubjects", {}) as Observable<any>;;
  // }

}
