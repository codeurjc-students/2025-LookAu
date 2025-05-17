import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Account } from '../models/account.model';
import { Ticket } from '../models/ticket.model';

const BASE_URL = '/api/tickets/';

@Injectable({ providedIn: 'root' })
export class TicketService {

  constructor(private http: HttpClient) { }

  getTicket(id: number): Observable<any>{
    return this.http.get<number>(BASE_URL+id) as Observable<any>;
  }

  getTicketType(id: number): Observable<any>{
    return this.http.get<number>(BASE_URL+id+'/ticketType') as Observable<any>;
  }

  saveTicket(id: number, ticket:Ticket): Observable<any>{
    return this.http.put<number>(BASE_URL+id, ticket) as Observable<any>;
  }

  deleteTicket(ticketId: number){
    return this.http.delete<number>(BASE_URL+ticketId) as Observable<any>;
  }


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

  // searchMyFriend(searchTerm: string): Observable<any>{
  //   return this.http.get(BASE_URL+'myFriends/'+searchTerm) as Observable<any>;
  // }

  // searchFriend(searchTerm: string): Observable<any>{
  //   return this.http.get(BASE_URL+searchTerm) as Observable<any>;
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
