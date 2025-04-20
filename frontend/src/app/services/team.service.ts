import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

const BASE_URL = '/api/teams/';

@Injectable({ providedIn: 'root' })
export class TeamService {

  constructor(private http: HttpClient) { }

  searchTeam(searchTerm: string): Observable<any>{
    return this.http.get(BASE_URL+searchTerm) as Observable<any>;
  }

  createTeam(name: string, friendsTeam: string[]): Observable<any> {
    return this.http.post<string>(
      BASE_URL,
      {
        name,
        friendsTeam
      }
    );
  }

  setProfileImage(image: File, id: number){
    const formData = new FormData();
    formData.append('file', image);
    return this.http.put(BASE_URL+id+'/image', formData, { withCredentials: true })
  }

  // getUserId(gmail: string): Observable<number>{
  //   return this.http.get<number>(BASE_URL+'userId?gmail='+gmail)
  // }

  // getMyFriends(index: number): Observable<any> {
  //   let params = new HttpParams();
  //   params = params.append('page', index.toString());
  //   params = params.append('size', '3');

  //   return this.http.get(BASE_URL+'myFriends', {
  //     params: params,
  //   }) as Observable<any>;
  // }

  // getPendingFriends(index: number): Observable<any> {
  //   let params = new HttpParams();
  //   params = params.append('page', index.toString());
  //   params = params.append('size', '3');

  //   return this.http.get(BASE_URL+'pendingFriends', {
  //     params: params,
  //   }) as Observable<any>;
  // }

  // getRequestFriends(index: number): Observable<any> {
  //   let params = new HttpParams();
  //   params = params.append('page', index.toString());
  //   params = params.append('size', '3');

  //   return this.http.get(BASE_URL+'requestFriends', {
  //     params: params,
  //   }) as Observable<any>;
  // }

  // getTeams(index: number): Observable<any> {
  //   let params = new HttpParams();
  //   params = params.append('page', index.toString());
  //   params = params.append('size', '3');

  //   return this.http.get(BASE_URL+'teams', {
  //     params: params,
  //   }) as Observable<any>;
  // }

  // acceptFriend(nickName: string): Observable<any>{
  //   return this.http.put(BASE_URL+'pendingFriends/'+nickName, null) as Observable<any>;
  // }

  // denyFriend(nickName: string): Observable<any>{
  //   return this.http.delete(BASE_URL+'pendingFriends/'+nickName) as Observable<any>;
  // }

  // deleteFriend(nickName: string): Observable<any>{
  //   return this.http.delete(BASE_URL+'myFriends/'+nickName) as Observable<any>;
  // }

  

  // sendRequestFriend(nickName: string): Observable<any>{
  //   return this.http.put(BASE_URL+nickName, null) as Observable<any>;
  // }

  // setProfileImage(image: File){
  //   const formData = new FormData();
  //   formData.append('file', image);
  //   return this.http.put(BASE_URL+'image', formData, { withCredentials: true })
  // }

  // editProfile(firstName: string, lastName: string, password: string){
  //   return this.http.put(BASE_URL, {firstName, lastName, password})
  // }

}
