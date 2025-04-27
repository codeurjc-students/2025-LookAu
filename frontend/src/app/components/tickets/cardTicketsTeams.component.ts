import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';


@Component({
  selector: 'app-cardticketsteams',
  templateUrl: './cardTicketsTeams.component.html',
  standalone: false,
})

export class CardTicketsTeamsComponent {

  public idTeam: number = 0;
  
  //search bar
  public searchTerm: string = "";
  public searchTickets: any[] = [];
  public isEmptySearchTickets: boolean = false;
  public searching: boolean = false;

  //tickets
  public tickets: any[] = [];
  public isLastTicketsRequest: boolean = false; //ajax
  public loadingTickets: boolean = false; //ajax
  public indexTickets: number = 1; //ajax
  public moreTickets: boolean = false; //ajax


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, private router: Router, private route:ActivatedRoute) {
    this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.idTeam = params['id']; 
    });
    
    this.getTickets();  
  }


  /** Search Bar **/
  searchTeam(searchTerm: string){
    if (!searchTerm || searchTerm.trim() === '') {
      this.searchTickets = [];
      this.isEmptySearchTickets = false;
      this.searching = false;
      return;
    }
  
    this.searching = true;
    this.teamService.searchTeam(searchTerm).subscribe(
      (response) => {
        this.searchTickets = response;
        this.isEmptySearchTickets = response.length === 0;
        this.searching = true;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /** Get Team Tickets **/
  getTickets() {
    this.teamService.getTeamTickets(0, this.idTeam, new Date, '').subscribe(
      (response) => {
        this.tickets = response.content;
        this.isLastTicketsRequest = response.last;
      },
      (error) => {
        console.error('Error al obtener los sujetos:', error);
      }
    );
  }

  getMoreTickets() {
    this.loadingTickets = true; //show the spinner
    this.teamService.getTeamTickets(this.indexTickets, this.idTeam, new Date, '').subscribe(
      (response) => {
        this.tickets = this.tickets.concat(response.content);
        this.moreTickets = !response.last;
        this.indexTickets++; //next ajax buttom
        this.loadingTickets = false; //hide the spinner
        this.isLastTicketsRequest = response.last;
      }
    );
  }

  isTicketsEmpty(): boolean {
    return !this.tickets?.length;
  }

}