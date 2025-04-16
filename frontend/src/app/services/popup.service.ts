import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../components/popUp/popup_dialog.component';
import { PopUpDialogComponentTwo } from '../components/popUp/popupTwo_dialog.component';
import { AccountService } from './account.service';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class PopUpService {

  constructor(private dialog: MatDialog, private accountService: AccountService, private router: Router) { }

  openPopUp(message: string): void {
    this.dialog.open(PopUpDialogComponent, {
      width: '250px',
      position: {},
      panelClass: 'custom-dialog-container',
      data: message
    });
  }


  openPopUpTwo(message: string, nickName: string): void {
    const dialogRef = this.dialog.open(PopUpDialogComponentTwo, {
      width: '300px',
      data: message
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {        
        this.accountService.deleteFriend(nickName).subscribe(
          (response) => {
            
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