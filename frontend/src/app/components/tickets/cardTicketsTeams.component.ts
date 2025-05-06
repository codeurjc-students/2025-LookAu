import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';



@Component({
  selector: 'app-cardticketsteams',
  templateUrl: './cardTicketsTeams.component.html',
  standalone: false,
})

export class CardTicketsTeamsComponent {

  public teamId: number = 0;
  public ticketId: number = 0;
  
  //tickets
  public ticket: any;
  public accounts: any[] = [];
  public transactions: Transaction[] = [];


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private router: Router, private route:ActivatedRoute) {
    this.authService.getCurrentUser();
    
    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    this.getTicket();  
    this.getTeamAccounts();
  }



  /** Get Ticket **/
  getTicket() {
    this.ticketService.getTicket(this.ticketId).subscribe(
      (response) => {
        this.ticket = response;
        console.log(this.ticket.type);

        this.calculateDebts();
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
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /** Calculate de bets **/
  calculateDebts(){
    if(this.ticket.statusName==='Winning'){
      this.genereteWinningTransactions(this.accounts, this.ticket.paidByName, Number(this.ticket.paidByPice), Number(this.ticket.statusPrice), this.ticket.claimedBy);
    }else if(this.ticket.statusName==='Pending'){
      this.transactions = [];
    }else{
      this.genereteNotWinningTransactions(this.accounts, this.ticket.paidByName, Number(this.ticket.paidByPice));
    }
  }

  genereteNotWinningTransactions(participants: string[], buyer: string, ticketPrice: number){
    
    participants.forEach(participant => {

      if(participant!=buyer){
        this.transactions.push({
          from: participant,
          to: buyer,
          amount: Math.round(ticketPrice) / participants.length,
        });
      }
      
    });
    
  }

  genereteWinningTransactions(participants: string[], buyer: string, ticketPrice: number, prizeAmount: number, claimer: string) {
    const n = participants.length;
    const perPersonCost = ticketPrice / n;
    const perPersonPrize = prizeAmount / n;
  
    const balance: Record<string, number> = {};
    this.transactions = [];
  
    //initialice balances
    participants.forEach(p => balance[p] = 0);
  
    participants.forEach(p => {
      balance[p] -= perPersonCost;
    });
    balance[buyer] += ticketPrice;
  
    participants.forEach(p => {
      balance[p] += perPersonPrize;
    });
    balance[claimer] -= prizeAmount;
  
    //generet debts
    const debtors = Object.entries(balance).filter(([_, amt]) => amt < -0.01).map(([p, amt]) => ({ person: p, amount: -amt }));
    const creditors = Object.entries(balance).filter(([_, amt]) => amt > 0.01).map(([p, amt]) => ({ person: p, amount: amt }));
  
    const rawTransactions: { from: string; to: string; amount: number }[] = [];

    let i = 0, j = 0;
    while (i < debtors.length && j < creditors.length) {
      const debtAmount = Math.min(debtors[i].amount, creditors[j].amount);
  
      rawTransactions.push({
        from: debtors[i].person,
        to: creditors[j].person,
        amount: Math.round(debtAmount * 100) / 100,
      });
  
      debtors[i].amount -= debtAmount;
      creditors[j].amount -= debtAmount;
  
      if (debtors[i].amount < 0.01) i++;
      if (creditors[j].amount < 0.01) j++;
    }
  
    //agroups debs 
    const grouped: Record<string, { from: string; to: string; amount: number }> = {};
  
    for (const t of rawTransactions) {
      const key = `${t.from}->${t.to}`;
      if (!grouped[key]) {
        grouped[key] = { ...t };
      } else {
        grouped[key].amount += t.amount;
      }
    }
  
    this.transactions = Object.values(grouped)
      .map(t => ({
        ...t,
        amount: Math.round(t.amount * 100) / 100,
      }))
      .filter(t => t.amount > 0);
  }



  /** Helper **/
  formatSignedAmount(amount: number): string {
    const formatted = amount.toFixed(2);
    return amount > 0 ? `+ ${formatted}` : formatted;
  }
  
}

interface Transaction {
  from: string;
  to: string;
  amount: number;
}