import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Account } from '../models/account.model';
import { Ticket } from '../models/ticket.model';

const BASE_URL = '/api/ticketTypes/';

@Injectable({ providedIn: 'root' })
export class TicketTypeService {

  constructor(private http: HttpClient) { }

  getTicketType(id: number): Observable<any>{
    return this.http.get<any>(BASE_URL+id) as Observable<any>;
  }


  saveTicketBonoloto(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/bonoloto', ticketType) as Observable<any>;
  }

  saveTicketEurodreams(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/eurodreams', ticketType) as Observable<any>;
  }

  saveTicketEuromillones(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/euromillones', ticketType) as Observable<any>;
  }

  saveTicketGordo(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/gordo', ticketType) as Observable<any>;
  }

  saveTicketLoteria(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/loteria', ticketType) as Observable<any>;
  }

  saveTicketLototurf(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/lototurf', ticketType) as Observable<any>;
  }

  saveTicketPrimitiva(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/primitiva', ticketType) as Observable<any>;
  }

  saveTicketQuiniela(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/quiniela', ticketType) as Observable<any>;
  }

  saveTicketQuinigol(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/quinigol', ticketType) as Observable<any>;
  }

  saveTicketQuintuple(id: number, ticketType: any): Observable<any>{
    return this.http.put<any>(BASE_URL+id+'/quintuple', ticketType) as Observable<any>;
  }
}