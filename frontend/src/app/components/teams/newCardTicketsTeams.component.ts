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
import { Ticket } from '../../models/ticket.model';



@Component({
  selector: 'app-newcardticketsteams',
  templateUrl: './newCardTicketsTeams.component.html',
  standalone: false
})

export class NewCardTicketsTeamsComponent {

  public teamId: number = 0;

  //ticketType
  public paidByName: string = '';
  public paidByPice: string = '';
  public accounts: string[] = [];

  public selectedTicketType: string = '';
  public ticketTypes: string[] = ['Bonoloto', 'Eurodreams', 'Euromillones', 'El Gordo' , 'Lotería Nacional', 'Lototurf', 'La Primitiva', 'La Quiniela', 'El Quinigol', 'Quíntuple plus'];
  
  public date: string = '';


  //tickets
  public ticket: Ticket = {
    id: 0,
    type: '',
    date: '',
    statusName: '',
    statusPrice: '',
    claimedBy: '',
    paidByName: '',
    paidByPice: '',
    ticketTypeId: '0',
    idAccountsAreBeingPaid: [],
    isBalanced: false,
    idReimbursementAreReferenced: []
  };
  public ticketType: any;


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private ticketTypeService: TicketTypeService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.authService.getCurrentUser();
    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);
  }

  ngOnInit() {
    this.getTeamAccounts();
  }


  /////////////////
  // EDIT TICKET //
  /////////////////
  //save the ticketType, later de ticket with the ticketType id


  //SWITCH SAVE TICKET TYPES
  saveNewTicket(){

    this.ticket.paidByName = this.paidByName;
    this.ticket.paidByPice = this.paidByPice;
    this.ticket.date = this.date;
    this.ticket.type = this.selectedTicketType;

    if(this.paidByName && this.paidByPice && this.selectedTicketType && this.date){
     
      switch (this.ticket.type) {
        case 'Bonoloto':
          this.saveNewTicketBonoloto();
          break;
        case 'Eurodreams':
          this.saveNewTicketEurodreams();
          break;
        case 'Euromillones':
          this.saveNewTicketEuromillones();
          break;
        case 'El Gordo':
          this.saveNewTicketGordo();
          break;
        case 'Lotería Nacional':
          this.saveNewTicketLoteria();
          break;
        case 'Lototurf':
          this.saveNewTicketLototurf();
          break;
        case 'La Primitiva':
          this.saveNewTicketPrimitiva();
          break;
        case 'Quíntuple plus':
          this.saveNewTicketQuintuple();
          break;
        case 'El Quinigol':
          this.saveNewTicketQuinigol();
          break;
        case 'La Quiniela':
          this.saveNewTicketQuiniela();
          break;
        default:
          break;
      }
        
    }else{
      this.popupService.openPopUp('Fill all the boxes.');
    }
  }

  
  //SAVE THE TICKET TYPES
  saveNewTicketBonoloto(){
    this.ticketTypeService.saveNewTicketBonoloto(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketEurodreams(){
    this.ticketTypeService.saveNewTicketEurodreams(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketEuromillones(){
    this.ticketTypeService.saveNewTicketEuromillones(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketGordo(){
    this.ticketTypeService.saveNewTicketGordo(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketLoteria(){
    this.ticketTypeService.saveNewTicketLoteria(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketLototurf(){
    this.ticketTypeService.saveNewTicketLototurf(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketPrimitiva(){
    this.ticketTypeService.saveNewTicketPrimitiva(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketQuintuple(){
    this.ticketTypeService.saveNewTicketQuintuple(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  saveNewTicketQuinigol(){
    this.ticketTypeService.saveNewTicketQuinigol(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    ); 
  }

  saveNewTicketQuiniela(){
    this.ticketTypeService.saveNewTicketQuiniela(this.ticketType).subscribe(
      (response) => {
        this.saveTicketWhitTicketType(response);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  //SAVE THE TICKET
  saveTicketWhitTicketType(ticketTypeId: string){

    this.ticket.ticketTypeId = ticketTypeId;

    this.ticketService.saveNewTicket(this.ticket, this.teamId).subscribe(
      (response) => {
        this.showIsSave();
        this.router.navigate(['/teams',this.teamId, 'tickets']);
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  //helpers save ticket
  showIsSave(){
    this.popupService.openPopUp('Ticket successfully saved.');
  }

  discardTicket(){
    this.popupService.openPopUpTwoDiscardTicket("Are you sure to discard the ticket?", this.teamId);
  }

  getDataTicketType(data: string) {
    this.ticketType = data;
    console.log('Recibido del hijo:', data);
  }




  /////////////////
  // VIEW TICKET //
  /////////////////

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


  /** Helper **/
  formatSignedAmount(amount: number): string {
    let formatted = amount.toFixed(2);
    return amount > 0 ? `+ ${formatted}` : formatted;
  }

  onTicketTypeChange(type: string) {
    this.selectedTicketType = type;
  }

  
}