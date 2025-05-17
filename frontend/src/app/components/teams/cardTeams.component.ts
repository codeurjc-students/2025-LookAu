import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';


@Component({
  selector: 'app-cardteams',
  templateUrl: './cardTeams.component.html',
  standalone: false,
})

export class CardTeamsComponent {

  public idTeam: number = 0;
  
  //search bar
  selectedDate: Date = new Date(NaN);
  selectedTicketType: string = '';
  ticketTypes: string[] = ['Bonoloto', 'Eurodreams', 'Euromillones', 'El Gordo' , 'Lotería Nacional', 'Lototurf', 'La Primitiva', 'La Quiniela', 'Quinigol', 'Quintuple plus'];
  showFilters: boolean = false;

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
  

  /** New Ticket **/
  newTicket(){
    this.router.navigate(['/teams/',this.idTeam,'tickets','new']);
  }


  /** Filters **/
  applyFilters() {
    this.getTickets();
  }

  deleteFilters() {
    this.selectedDate = new Date(NaN);
    this.selectedTicketType = '';
    this.getTickets();
  }

  toggleFilters(){
    this.showFilters = this.showFilters == true? false: true;
    this.deleteFilters();    
  }



  /** Get Team Tickets **/
  getTickets() {
    this.teamService.getTeamTickets(0, this.idTeam, this.selectedDate, this.selectedTicketType).subscribe(
      (response) => {
        this.tickets = response.content;
        this.isLastTicketsRequest = response.last;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getMoreTickets() {
    this.loadingTickets = true; //show the spinner
    this.teamService.getTeamTickets(this.indexTickets, this.idTeam, this.selectedDate, this.selectedTicketType).subscribe(
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


  /** Reimbursement **/
  deleteReimbursement(teamId: string){
  }

  formatSignedAmountReimbursement(amountSring: string): string {
    let amount = Number(amountSring);
    const formatted = Math.abs(amount).toFixed(2);
    return amount > 0 ? `+ ${formatted}` : formatted;
  }

  formatSignedAmount(amountSring: string): string {
    let amount = Number(amountSring);
    const formatted = amount.toFixed(2);
    return amount > 0 ? `+ ${formatted}` : formatted;
  }

}