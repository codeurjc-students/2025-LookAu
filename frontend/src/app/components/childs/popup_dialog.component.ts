import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-popup-dialog',
  templateUrl: './popup_dialog.component.html',
  styleUrl: '../../app.component.css',
  standalone: false,
})

export class PopUpDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<PopUpDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string
  ) {}

  ngOnInit(){}
}
