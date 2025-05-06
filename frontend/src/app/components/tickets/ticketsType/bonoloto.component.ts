import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../services/account.service';
import { TeamService } from '../../../services/team.service';
import { TicketService } from '../../../services/ticket.service';


@Component({
  selector: 'app-bonoloto',
  templateUrl: './bonoloto.component.html',
  standalone: false,
})

export class BonolotoComponent {

  public ticketId: number = 0;

  public num1: number = 0;
  public num2: number = 0;
  public num3: number = 0;
  public num4: number = 0;
  public num5: number = 0;
  public num6: number = 0; 

  constructor(public authService: AuthService, public accountService: AccountService,public teamService: TeamService, public ticketService: TicketService, private router: Router, private route:ActivatedRoute) {
    this.ticketId = Number(this.route.snapshot.paramMap.get('ticketId') || 0);
  }

  ngOnInit() {
    this.getTicketType();
  }


  /** Get Ticket **/
  getTicketType() {
    this.ticketService.getTicketType(this.ticketId).subscribe(
      (response) => {
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
  
}
