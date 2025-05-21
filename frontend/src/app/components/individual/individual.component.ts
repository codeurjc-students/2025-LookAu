import { Component, Input } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';
import { TicketTypeService } from '../../services/ticketType.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PopUpService } from '../../services/popup.service';
import { Ticket } from '../../models/ticket.model';
import { Team } from '../../models/team.model';

@Component({
  selector: 'app-individual',
  templateUrl: './individual.component.html',
  standalone: false,
})

export class IndividualComponent {
 
  public teamId: number;

  public lTickets: Ticket[] = [];
  public accounts: string[] = [];
  public team: Team = {
    id: 0,
    name: ''
  };
  public total = {
    cost: 0,
    income: 0,
    winning: 0,
    nowinning: 0,
    pending: 0,
  };

  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private ticketTypeService: TicketTypeService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.authService.getCurrentUser();
    
    this.teamId = Number(this.route.snapshot.paramMap.get('id') || this.route.snapshot.paramMap.get('teamId') || 0);
  }

  ngOnInit() {
    this.getTickets();
  }

  

  /** Get Ticket **/
  getTickets(){
    if(this.teamId>0){
      this.getTeamTickets();
    }else{
      this.getPersonalTickets();
    }
    console.log(this.lTickets)
  }

  getPersonalTickets(){
    this.accountService.getAllAccountTickets().subscribe(
      (response) => {
        this.lTickets = response;
        this.calculateResume();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getTeamTickets() {
    this.teamService.getAllTickets(this.teamId).subscribe(
      (response) => {
        this.lTickets = response;
        this.getTeamAccounts();
        this.calculateResume();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }
  

  /** Get Accounts **/
  getTeamAccounts(){
    this.teamService.getAccountsTeam(String(this.teamId)).subscribe(
      (response) => {
        this.accounts = response;
        this.getTeam(); 
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  /* Get Team */
  getTeam(){
    this.teamService.getTeam(this.teamId).subscribe(
      (response) => {
        console.log(response);
        this.team = response;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /* CACULATE RESUME */
  calculateResume(){
  
    this.lTickets.forEach((ticket) => {
      this.total.cost -= Number(ticket.paidByPice || 0);
      this.total.income += Number(ticket.statusPrice || 0);

      ticket.statusName==='Winning'? this.total.winning += 1 : (ticket.statusName==='Pending'? this.total.pending += 1: this.total.nowinning += 1);
    });
  }

  /** Helper **/
  formatSignedAmount(amount: number): string {
    let formatted = amount.toFixed(2);
    return amount > 0 ? `+ ${formatted}` : formatted;
  }

}