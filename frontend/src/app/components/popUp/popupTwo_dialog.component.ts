import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-popuptwo-dialog',
  templateUrl: './popupTwo_dialog.component.html',
  styleUrl: '../../app.component.css',
  standalone: false,
})

export class PopUpDialogComponentTwo {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}
}
