import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';
import { PopUpService } from '../../services/popup.service';
import { TicketTypeService } from '../../services/ticketType.service';


@Component({
  selector: 'app-quiniela',
  templateUrl: './quiniela.component.html',
  standalone: false,
})

export class QuinielaComponent {

  @Input() isEditing: boolean = false;
  @Output() dataEmitter = new EventEmitter<any>();

  public isApply = true;

  public ticketId: number = 0;
  public ticketType: any = {};

  public num1: number = 0;
  public num2: number = 0;
  public num3: number = 0;
  public num4: number = 0;
  public num5: number = 0;
  public num6: number = 0; 

  public range = Array.from({ length: 8 }, (_, i) => i + 1);
  public buttonMatrix: boolean[][][] = []; //columna, fila, valor = 1, X, 2  o  0, 1, 2, M


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, public ticketTypeService: TicketTypeService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    if(this.ticketId!=0){
      this.getTicketType();
    }

    //inicialize bet matrix
    for (let i = 0; i < 16; i++) {
      this.buttonMatrix[i] = [];
      for (let j = 0; j < 9; j++) {
        this.buttonMatrix[i][j] = [false, false, false, false]; // 
      }
    }
  }


  /** Send To Ticket the New TycketsTypes Changes **/
  applyTicketTypesChanges(){

    if(this.isApplicable()){
      this.isApply = true;
      let buttonIndex = -1;
      for (let i = 1; i < 9; i++) {
        let bet: string[] = [];

        for (let j = 0; j < 16; j++) {
          buttonIndex = this.buttonMatrix[j][i].findIndex(val => val === true);

          if (buttonIndex !== -1) {
            bet[j] = (j < 14) ? ['1', 'X', '2'][buttonIndex] : ['0', '1', '2', 'M'][buttonIndex];
          }
        }

        this.ticketType['bet' + i] = bet;      
      }
      this.dataEmitter.emit(this.ticketType);
    }else{
      this.popupService.openPopUp('Fill any bet or all the box form a bet.');
    }
    
  }

  isApplicable(): boolean {

    //run all the bets
    for (let i = 1; i < 9; i++) {
      let filledCount = 0;

      //run all the box bet
      for (let j = 0; j < 16; j++) {
        if (this.buttonMatrix[j][i].some(val => val)) {
          filledCount++;
        }
      }

      //the bet is incomplete if it had been filled
      if (filledCount > 0 && filledCount < 16) {
        return false;
      }
    }

    //al the bets are complete if they are fill
    for (let i = 1; i < 9; i++) {
      let isComplete = true;
      for (let j = 0; j < 16; j++) {
        if (!this.buttonMatrix[j][i].some(val => val)) {
          isComplete = false;
          break;
        }
      }

      //if almost one bet is complete 
      if (isComplete) return true;
    }

    //no one is complete
    return false;
  }


  sendToParent() {
    this.dataEmitter.emit(this.ticketType);
  }
  

  /** Get Ticket **/
  getTicketType() {
    this.ticketService.getTicketType(this.ticketId).subscribe(
      (response) => {
        this.ticketType = response;

        let bet = [];
        let buttonIndex = -1;

        for(let i=1; i<9; i++){

          bet = this.ticketType['bet'+i];
          if(bet){
            
            for(let j=0; j<16; j++){

              buttonIndex = (j < 14) ? ['1', 'X', '2'].indexOf(bet[j]) : ['0', '1', '2', 'M'].indexOf(bet[j]);
              if (buttonIndex !== -1) {
                this.buttonMatrix[j][i][buttonIndex] = true;  //for show the matrix, it need be order by row and before col, the opsotive from the
              }
            }

          }

        }
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /** Set TicketType Changes **/
  toggleButton(i: number, j: number, k: number) {
    this.isApply = false;
    this.buttonMatrix[i][j] = this.buttonMatrix[i][j].map((_, index) => index === k);
  }

  deleteRow(j: number){
    this.isApply = false;
    for (let i = 0; i < 16; i++) {
      this.buttonMatrix[i][j] = [false, false, false, false];
    }
  }
  
}
