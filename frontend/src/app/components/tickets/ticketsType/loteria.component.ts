import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../services/account.service';
import { TeamService } from '../../../services/team.service';
import { TicketService } from '../../../services/ticket.service';
import { PopUpService } from '../../../services/popup.service';


@Component({
  selector: 'app-loteria',
  templateUrl: './loteria.component.html',
  standalone: false,
})

export class LoteriaComponent {

  @Input() isEditing: boolean = false;
  @Output() dataEmitter = new EventEmitter<any>();

  public isApply = true;

  public ticketType: any = {};
  public ticketId: number = 0;

  public number: number = 0;
  public euros: number = 0;
  public series: number = 0;
  public fraction: number = 0; 

  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private router: Router, private route:ActivatedRoute, private popupService:PopUpService) {
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

      this.ticketType.number = this.number;
      this.ticketType.euros = this.euros;
      this.ticketType.series = this.series;
      this.ticketType.fraction = this.fraction;
      
      this.dataEmitter.emit(this.ticketType);
    }else{
      this.popupService.openPopUp('Fill all the box with numbers in range.');
    }
    
  }

  isApplicable(): boolean{
    return (
      !isNaN(this.number) && this.number >= 0 && this.number <= 99999 &&         //number range: 00000-99999
      !isNaN(this.euros) && this.euros >= 1 && this.euros <= 200 &&              //euros range: 1-200
      !isNaN(this.series) && this.series >= 1 && this.series <= 160 &&           //series range: 1-160
      !isNaN(this.fraction) && this.fraction >= 1 && this.fraction <= 10         //fraction range: 1-10
    );
  }


  /** Get Ticket **/
  getTicketType() {
    this.ticketService.getTicketType(this.ticketId).subscribe(
      (response) => {
        this.ticketType = response;
        
        this.number = response.number;
        this.euros = response.euros;
        this.series = response.series;
        this.fraction = response.fraction;

      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  formatLotteryNumber(num: number | string): string {
    let numStr = num?.toString() || '0';
    return numStr.padStart(5, '0');
  }


  /** Set TicketType Changes **/
  setApply(){
    this.isApply = false;
  }
  
}
