import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../services/account.service';
import { TeamService } from '../../../services/team.service';
import { TicketService } from '../../../services/ticket.service';


@Component({
  selector: 'app-loteria',
  templateUrl: './loteria.component.html',
  standalone: false,
})

export class LoteriaComponent {

  public ticketId: number = 0;

  public number: number = 0;
  public euros: number = 0;
  public series: number = 0;
  public fraction: number = 0; 

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
  
}
