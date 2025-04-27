import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';


@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  standalone: false,
})

export class TeamsComponent {
  
  //search bar
  public searchTerm: string = "";
  public searchTeams: any[] = [];
  public isEmptySearchTeams: boolean = false;
  public searching: boolean = false;

  //teams
  public teams: any[] = [];
  public isLastTeamsRequest: boolean = false; //ajax
  public loadingTeams: boolean = false; //ajax
  public indexTeams: number = 1; //ajax
  public moreTeams: boolean = false; //ajax


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, private router: Router) {
    this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.getTeams();  
  }


  /** Search Bar **/
  searchTeam(searchTerm: string){
    if (!searchTerm || searchTerm.trim() === '') {
      this.searchTeams = [];
      this.isEmptySearchTeams = false;
      this.searching = false;
      return;
    }
  
    this.searching = true;
    this.teamService.searchTeam(searchTerm).subscribe(
      (response) => {
        this.searchTeams = response;
        this.isEmptySearchTeams = response.length === 0;
        this.searching = true;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  /** Get Account Teams **/
  getTeams() {
    this.accountService.getTeams(0).subscribe(
      (response) => {
        this.teams = response.content;
        this.isLastTeamsRequest = response.last;
      },
      (error) => {
        console.error('Error al obtener los sujetos:', error);
      }
    );
  }

  getMoreTeams() {
    this.loadingTeams = true; //show the spinner
    this.accountService.getTeams(this.indexTeams).subscribe(
      (response) => {
        this.teams = this.teams.concat(response.content);
        this.moreTeams = !response.last;
        this.indexTeams++; //next ajax buttom
        this.loadingTeams = false; //hide the spinner
        this.isLastTeamsRequest = response.last;
      }
    );
  }

  isTeamsEmpty(): boolean {
    return !this.teams?.length;
  }

}