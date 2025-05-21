import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';
import { PopUpService } from '../../services/popup.service';


@Component({
  selector: 'app-quinigol',
  templateUrl: './quinigol.component.html',
  standalone: false,
})

export class QuinigolComponent {

  @Input() isEditing: boolean = false;
  @Output() dataEmitter = new EventEmitter<any>();

  public isApply = true;

  public ticketType: any = {};
  public ticketId: number = 0;

  public num1: number = 0;
  public num2: number = 0;
  public num3: number = 0;
  public num4: number = 0;
  public num5: number = 0;
  public num6: number = 0; 

  buttonMatrix: boolean[][][] = [];
  mainRows = Array(6);


  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private router: Router, private route:ActivatedRoute, private popupService:PopUpService) {
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    if(this.ticketId!=0){
      this.getTicketType();
    }
    this.deleteRow();  //inizialite the aux tickettype
  }

  /** Send To Ticket the New TycketsTypes Changes **/
  applyTicketTypesChanges(){

    if(this.isApplicable()){
      this.isApply = true;
      let buttonIndex = -1;
      for (let i = 0; i < 6; i++) {
        let bet: string[] = [];

        for (let j = 0; j < 2; j++) {
          buttonIndex = this.buttonMatrix[i][j].findIndex(val => val === true);

          if (buttonIndex !== -1) {
            bet[j] = ['0', '1', '2', 'M'][buttonIndex];
          }
        }

        this.ticketType['bet' + i] = bet;      
      }
      this.dataEmitter.emit(this.ticketType);
    }else{
      this.popupService.openPopUp('Fill any bet or all the box form a bet.');
    }
  }

  isApplicable(): boolean{
    let eachBetWellFilled: boolean[] = new Array(6).fill(true);  //comprueba si todas están rellenadas

    for (let i = 0; i < 6; i++) {
      for (let j = 0; j < 2; j++) {
        if(!this.buttonMatrix[i][j].some(val => val)){  //if the bet has any box no filled 
          eachBetWellFilled[i] = false;     
        }
      }
    }

    return eachBetWellFilled.every(val => val);  //if any bet team is bad fille = false, if all bet are empty = false
  }

  sendToParent() {
    this.dataEmitter.emit(this.ticketType);
  }


  /** Get Ticket **/
  getTicketType() {
    this.ticketService.getTicketType(this.ticketId).subscribe(
      (response) => {
        this.ticketType = response;
        this.isApply = true;
        
        let bet = [];
        let buttonIndex = -1;

        for(let i=1; i<7; i++){

          bet = response['bet'+i];
          if(bet){
            
            for(let j=0; j<2; j++){

              buttonIndex = ['0', '1', '2', 'M'].indexOf(bet[j]);
              if (buttonIndex !== -1) {
                this.buttonMatrix[i-1][j][buttonIndex] = true;
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

  deleteRow(){
    this.isApply = false;
    for (let i = 0; i < 6; i++) {
      this.buttonMatrix[i] = [];
      for (let j = 0; j < 2; j++) {
        this.buttonMatrix[i][j] = [false, false, false, false]; //0, 1, 2, M
      }
    }
  }
  
}
