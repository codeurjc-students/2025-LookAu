import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TeamService } from '../../services/team.service';
import { TicketService } from '../../services/ticket.service';
import { PopUpService } from '../../services/popup.service';


@Component({
  selector: 'app-bonoloto',
  templateUrl: './bonoloto.component.html',
  standalone: false,
})

export class BonolotoComponent {

  @Input() isEditing: boolean = false;
  @Output() dataEmitter = new EventEmitter<any>();

  public isApply = true;

  public ticketType: any = {};
  public ticketId: number = 0;

  public num1: string = '0';
  public num2: string = '0';
  public num3: string = '0';
  public num4: string = '0';
  public num5: string = '0';
  public num6: string = '0'; 

  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private router: Router, private route:ActivatedRoute, private popupService: PopUpService) {
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    if(this.ticketId!=0){
      this.getTicketType();
    }
  }

  /** Send To Ticket the New TycketsTypes Changes **/
  applyTicketTypesChanges(){

    if(this.isApplicable()){
      this.isApply = true;

      this.ticketType.num1 = this.num1;
      this.ticketType.num2 = this.num2;
      this.ticketType.num3 = this.num3;
      this.ticketType.num4 = this.num4;
      this.ticketType.num5 = this.num5;
      this.ticketType.num6 = this.num6;
      
      this.dataEmitter.emit(this.ticketType);
    }else{
      this.popupService.openPopUp('Fill all the box with numbers in range.');
    }
    
  }

  isApplicable(): boolean{
    let numbers = [
      Number(this.num1),
      Number(this.num2),
      Number(this.num3),
      Number(this.num4),
      Number(this.num5),
      Number(this.num6),
    ];

    return numbers.every(num => !isNaN(num) && num >= 1 && num <= 49);
  }


  /** Get Ticket **/
  getTicketType() {
    this.ticketService.getTicketType(this.ticketId).subscribe(
      (response) => {
        this.ticketType = response;

        this.num1 = response.num1;
        this.num2 = response.num2;
        this.num3 = response.num3;
        this.num4 = response.num4;
        this.num5 = response.num5;
        this.num6 = response.num6;

      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /** Set TicketType Changes **/
  setApply(){
    this.isApply = false;
  }
}
