import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';
import { PopUpDialogComponentTwo } from '../popUp/popupTwo_dialog.component';
import { PopUpService } from '../../services/popup.service';
import { TicketTypeService } from '../../services/ticketType.service';
import { E } from '@angular/cdk/keycodes';



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
  public ticketType: any;
  public accounts: any[] = [];
  public transactions: Transaction[] = [];

  //edit
  public isEditing = false;
  public save = false;


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private ticketTypeService: TicketTypeService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.authService.getCurrentUser();
    
    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    this.save = false;
    this.getTeamAccounts();
    this.getTicket();  
  }


  ///////////////////
  // DELETE TICKET //
  ///////////////////

  deleteTicket(){
    this.popupService.openPopUpTwoDeleteTicket("Are you sure to delete the ticket?", this.ticketId, this.teamId);
  }


  /////////////////
  // EDIT TICKET //
  /////////////////

  saveTicket(){

    this.ticket.statusName = "Pending";

    this.ticketService.saveTicket(this.ticketId, this.ticket).subscribe(
      (response) => {

        //if the ticket type has any change
        if(this.ticketType){  
          switch (this.ticket.type) {
            case 'Bonoloto':
              this.saveTicketBonoloto();
              break;
            case 'Eurodreams':
              this.saveTicketEurodreams();
              break;
            case 'Euromillones':
              this.saveTicketEuromillones();
              break;
            case 'El Gordo':
              this.saveTicketGordo();
              break;
            case 'Lotería Nacional':
              this.saveTicketLoteria();
              break;
            case 'Lototurf':
              this.saveTicketLototurf();
              break;
            case 'La Primitiva':
              this.saveTicketPrimitiva();
              break;
            case 'Quíntuple plus':
              this.saveTicketQuintuple();
              break;
            case 'El Quinigol':
              this.saveTicketQuinigol();
              break;
            case 'La Quiniela':
              this.saveTicketQuiniela();
              break;
            default:
              break;
          }

        //if the ticket type has no changes
        }else{
          this.showIsSave();
        }
      },
    (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketBonoloto(){
    this.ticketTypeService.saveTicketBonoloto(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketEurodreams(){
    this.ticketTypeService.saveTicketEurodreams(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketEuromillones(){
    this.ticketTypeService.saveTicketEuromillones(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketGordo(){
    this.ticketTypeService.saveTicketGordo(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketLoteria(){
    this.ticketTypeService.saveTicketLoteria(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketLototurf(){
    this.ticketTypeService.saveTicketLototurf(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketPrimitiva(){
    this.ticketTypeService.saveTicketPrimitiva(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketQuintuple(){
    this.ticketTypeService.saveTicketQuintuple(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveTicketQuinigol(){
    this.ticketTypeService.saveTicketQuinigol(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    ); 
  }

  saveTicketQuiniela(){
    this.ticketTypeService.saveTicketQuiniela(this.ticketType.id, this.ticketType).subscribe(
      (response) => {
        this.showIsSave();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  showIsSave(){
    this.isEditing = false;
    this.save = true;
    this.popupService.openPopUp('Ticket successfully saved.');
  }

  editTicket(){
    this.isEditing = true;
  }

  discardTicket(){
    this.isEditing = false;
  }

  getDataTicketType(data: string) {
    this.ticketType = data;
    console.log('Recibido del hijo:', data);
  }




  /////////////////
  // VIEW TICKET //
  /////////////////

  /** Get Ticket **/
  getTicket() {
    this.ticketService.getTicket(this.ticketId).subscribe(
      (response) => {
        this.ticket = response;
        this.getTeamAccounts();
        this.save = false;
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
        this.calculateDebts();

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

    this.transactions = [];
    let num = participants.length;

    participants.forEach(participant => {

      if(participant!=buyer){
        this.transactions.push({
          from: participant,
          to: buyer,
          amount: ticketPrice / num,
        });
      }
      
    });

    console.log(this.transactions);
    
  }

  genereteWinningTransactions(participants: string[], buyer: string, ticketPrice: number, prizeAmount: number, claimer: string) {
    
    let n = participants.length;
    let perPersonCost = ticketPrice / n;
    let perPersonPrize = prizeAmount / n;
  
    let balance: Record<string, number> = {};
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
    let debtors = Object.entries(balance).filter(([_, amt]) => amt < -0.01).map(([p, amt]) => ({ person: p, amount: -amt }));
    let creditors = Object.entries(balance).filter(([_, amt]) => amt > 0.01).map(([p, amt]) => ({ person: p, amount: amt }));
  
    let rawTransactions: { from: string; to: string; amount: number }[] = [];

    let i = 0, j = 0;
    while (i < debtors.length && j < creditors.length) {
      let debtAmount = Math.min(debtors[i].amount, creditors[j].amount);
  
      rawTransactions.push({
        from: debtors[i].person,
        to: creditors[j].person,
        amount: (debtAmount * 100) / 100,
      });
  
      debtors[i].amount -= debtAmount;
      creditors[j].amount -= debtAmount;
  
      if (debtors[i].amount < 0.01) i++;
      if (creditors[j].amount < 0.01) j++;
    }
  
    //agroups debs 
    let grouped: Record<string, { from: string; to: string; amount: number }> = {};
  
    for (let t of rawTransactions) {
      let key = `${t.from}->${t.to}`;
      if (!grouped[key]) {
        grouped[key] = { ...t };
      } else {
        grouped[key].amount += t.amount;
      }
    }
  
    this.transactions = Object.values(grouped)
      .map(t => ({
        ...t,
        amount: (t.amount * 100) / 100,
      }))
      .filter(t => t.amount > 0);
  }



  /** Helper **/
  formatSignedAmount(amount: number): string {
    const formatted = amount
      .toFixed(2) 
      .replace('.', ',') 
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.'); 

    return amount > 0 ? `+ ${formatted}` : formatted;
  }

  formatSignedAmountString(amountString: string): string {
    let amount = Number(amountString);
    const formatted = amount
      .toFixed(2)
      .replace('.', ',')
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.'); 

    return amount > 0 ? `+ ${formatted}` : formatted;
  }

  formatSignedAmountStringNegative(amountString: string): string {
    let amount = Number(amountString);
    const formatted = amount 
      .toFixed(2)
      .replace('.', ',')
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    return `- ${formatted}`; 
  }


  formatSignedAmountNoSign(amount: number): string {
    const formatted = amount 
      .toFixed(2)
      .replace('.', ',')
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    return `${formatted}`; 
  }

  
}

interface Transaction {
  from: string;
  to: string;
  amount: number;
}