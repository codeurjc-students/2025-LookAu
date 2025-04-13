import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../components/childs/popup_dialog.component';

@Injectable({
  providedIn: 'root'
})
export class PopUpService {

  constructor(private dialog: MatDialog) { }

    openPopUp(message: string): void {
        this.dialog.open(PopUpDialogComponent, {
            width: '250px',
            position: {},
            panelClass: 'custom-dialog-container',
            data: message
        });
    }

}