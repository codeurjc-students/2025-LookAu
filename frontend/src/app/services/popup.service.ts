import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../components/popUp/popup_dialog.component';
import { PopUpDialogComponentTwo } from '../components/popUp/popupTwo_dialog.component';
import { AccountService } from './account.service';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { TicketService } from './ticket.service';
import { TeamService } from './team.service';

@Injectable({
  providedIn: 'root'
})

export class PopUpService {

  constructor(private dialog: MatDialog, private accountService: AccountService, private router: Router, private ticketService:TicketService, private teamService:TeamService) { }

  openPopUp(message: string): void {
    this.dialog.open(PopUpDialogComponent, {
      width: '250px',
      position: {},
      panelClass: 'custom-dialog-container',
      data: message
    });
  }


  openPopUpTwoDeletefriend(message: string, nickName: string): void {
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {        
        this.accountService.deleteFriend(nickName).subscribe(
          (response) => {
            this.router.navigate(['/profile']);
            this.openPopUp(nickName+' is no longer your friend :(')
          },
          (error) => {
            this.router.navigate(['/error']);
          }
        );

      } else {
        
      }
    });
  }


  openPopUpTwoDeleteTicket(message:string, ticketId: number, teamId:number): void {
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {        
        this.ticketService.deleteTicket(ticketId).subscribe(
          (response) => {
            this.openPopUp('Ticket successfully deleted.');
            if(teamId>0)  {
              this.router.navigate(['/teams',teamId,'tickets']);
            } else{
              this.router.navigate(['/personal','tickets']);
            }    
          },
          (error) => {
            this.router.navigate(['/error']);
          }
        );

      } else {
        
      }
    });
  }


  
  openPopUpTwoDiscardTicket(message:string, teamId:number): void {
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') { 
        if(teamId>0)  {
          this.router.navigate(['/teams',teamId,'tickets']);
        } else{
          this.router.navigate(['/personal','tickets']);
        }    
      } else {
        
      }
    });
  }


  openPopUpTwoTickettype(message: string): any {
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {        
        return true;
      } else {
        return false;
      }
    });
  }

  openPopUpTwoLeaveTeam(message: string, nickName:string, teamId:number): void{
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {        
        
        this.teamService.deleteAccountTeam(teamId, nickName).subscribe(
          (response) => {
            this.openPopUp('You left the team successfully.');
            this.router.navigate(['/teams']);
          },
          (error) => {
            this.router.navigate(['/error']);
          }
        );

      } else {
        
      }
    });
  }

}