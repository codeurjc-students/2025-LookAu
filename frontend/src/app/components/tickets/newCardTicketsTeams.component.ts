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
  public ticketTypes: string[] = ['Bonoloto', 'Eurodreams', 'Euromillones', 'El Gordo' , 'Lotería Nacional', 'Lototurf', 'La Primitiva', 'La Quiniela', 'Quinigol', 'Quintuple plus'];
  
  public date: string = '';


  //tickets
  public ticket: any;
  public ticketType: any;

  //edit
  public save = false;


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private ticketTypeService: TicketTypeService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.authService.getCurrentUser();
    
    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);
  }

  ngOnInit() {
    this.save = false;
    this.getTeamAccounts();
  }


  /////////////////
  // EDIT TICKET //
  /////////////////

  saveNewTicket(){

    /*this.ticketService.saveNewTicket(this.ticket).subscribe(
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
    );*/
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
    this.save = true;
    this.popupService.openPopUp('Ticket successfully saved.');
  }

  discardTicket(){
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
    console.log(type);
    this.selectedTicketType = type;
  }

  
}