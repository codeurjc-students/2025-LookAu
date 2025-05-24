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
  
  saveNewTicket(ticket:Ticket, teamId:number): Observable<any>{
    let params = new HttpParams();
    params = params.append('teamId', teamId);

    return this.http.post<number>(BASE_URL, ticket, {params: params,}) as Observable<any>;
  }

  deleteTicket(ticketId: number){
    return this.http.delete<number>(BASE_URL+ticketId) as Observable<any>;
  }
}
