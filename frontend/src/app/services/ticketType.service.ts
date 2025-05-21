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

  //save
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


  //new
  saveNewTicketBonoloto(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'bonoloto', ticketType) as Observable<any>;
  }

  saveNewTicketEurodreams(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'eurodreams', ticketType) as Observable<any>;
  }

  saveNewTicketEuromillones(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'/euromillones', ticketType) as Observable<any>;
  }

  saveNewTicketGordo(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'gordo', ticketType) as Observable<any>;
  }

  saveNewTicketLoteria(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'loteria', ticketType) as Observable<any>;
  }

  saveNewTicketLototurf(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'lototurf', ticketType) as Observable<any>;
  }

  saveNewTicketPrimitiva(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'primitiva', ticketType) as Observable<any>;
  }

  saveNewTicketQuiniela(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'quiniela', ticketType) as Observable<any>;
  }

  saveNewTicketQuinigol(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'quinigol', ticketType) as Observable<any>;
  }

  saveNewTicketQuintuple(ticketType: any): Observable<any>{
    return this.http.post<any>(BASE_URL+'quintuple', ticketType) as Observable<any>;
  }
}