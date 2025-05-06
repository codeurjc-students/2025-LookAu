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

  getAccountsTeam(teamId: string): Observable<any>{
    return this.http.get(BASE_URL+teamId+'/accounts') as Observable<any>;
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

  getTeamTickets(index: number, idTeam:number, date:Date, type:string): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', index.toString());
    params = params.append('size', '5');
    
    if(typeof date === 'string'){
      params = params.append('date', date);
    }

    if(type != ''){
      params = params.append('type', type);
    }

    return this.http.get(BASE_URL+idTeam+'/tickets', {
      params: params,
    }) as Observable<any>;
  }

}
