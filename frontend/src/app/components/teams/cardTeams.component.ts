import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { LoteriaService } from '../../services/loteria.service';


@Component({
  selector: 'app-cardteams',
  templateUrl: './cardTeams.component.html',
  standalone: false,
})

export class CardTeamsComponent {

  public teamId: number = 0;
  public show: boolean = false;
  public isLoding = false;
  
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


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, private router: Router, private route:ActivatedRoute, private loteriaService: LoteriaService) {
    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);

    this.authService.getCurrentUser();
  }

  ngOnInit() {

    this.isLoding = true;
    this.getTickets();

    /*this.loteriaService.checkAndUpdateTickets(this.teamId).subscribe(
      (response) => {
        this.getTickets();  
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );*/
  }
  

  /** New Ticket **/
  newTicket(){
    this.router.navigate(['/teams/',this.teamId,'tickets','new']);
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
    this.teamService.getTeamTickets(0, this.teamId, this.selectedDate, this.selectedTicketType).subscribe(
      (response) => {
        this.tickets = response.content;
        this.isLastTicketsRequest = response.last;
        this.isLoding = false;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getMoreTickets() {
    this.loadingTickets = true; //show the spinner
    this.teamService.getTeamTickets(this.indexTickets, this.teamId, this.selectedDate, this.selectedTicketType).subscribe(
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

  formatSignedAmount(amountString: string): string {
    let amount = Number(amountString);
    const formatted = amount
      .toFixed(2) // dos decimales
      .replace('.', ',') // cambia el punto decimal por coma
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // añade puntos como separadores de miles

    return amount > 0 ? `+ ${formatted}` : formatted;
  }


}