import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Account } from '../models/account.model';
import { Ticket } from '../models/ticket.model';

const BASE_URL = '/api/loteria/';

@Injectable({ providedIn: 'root' })
export class LoteriaService {

  constructor(private http: HttpClient) { }

  checkAndUpdateTickets(teamId: number): Observable<any>{
    return this.http.get<any>(BASE_URL+'teams/'+teamId) as Observable<any>;
  }

  checkAndUpdateTicketsPersonal(): Observable<any>{
    return this.http.get<any>(BASE_URL+'personal') as Observable<any>;
  }
}