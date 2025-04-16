import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../popUp/popup_dialog.component';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  standalone: false,
})

export class FooterComponent {

  logoPath: string = 'assets/footer/footer-logo.png';

  constructor(private dialog: MatDialog){}
  
  openDialog(info: string) {
    const dialogRef = this.dialog.open(PopUpDialogComponent, {
      data: info
    });
  }

  info: string = `
    LookAu: The Ultimate Lottery and Betting Management App

    LoterySplit is an innovative application designed to bring the excitement of state lotteries and betting into a practical and user-friendly platform similar to Tricount. It is perfect for groups of friends, family, or coworkers who purchase lottery tickets together and need an easy way to manage shares and winnings.

    The app allows users to register lottery tickets by scanning the QR code or manually entering the numbers. Users can assign shares to each group member, ensuring everyone knows their percentage of the prize if the ticket wins. LoterySplit automatically connects to the official State Lotteries and Betting website to check if a ticket is a winner and displays the prize amount. It then calculates how much each participant is owed based on their share.

    Users can create groups to share tickets and winnings, making it ideal for collaborative betting. Each group has a history of purchased tickets and prizes won, ensuring complete transparency. The app sends real-time notifications when a ticket wins or when new tickets are added, eliminating the need for manual checks.

    One of the standout features is the automatic prize division. If a ticket wins, the app splits the prize among participants according to their share and even allows direct transfers via integrated services like Bizum or PayPal. The app also provides a detailed history and statistics, showing total amounts spent on tickets and total winnings, making it easy to track your betting journey.

    Security and privacy are top priorities, with end-to-end encryption protecting ticket data and shares. Only group members can access the information related to shared tickets, ensuring complete confidentiality.

    The user experience is seamless, thanks to its intuitive and clean design, similar to Tricount. The app saves time by automating result checks and prize calculations while promoting transparency and fairness within groups.

    LoterySplit is perfect for various use cases. Friends can use it for Christmas lottery pools, coworkers can manage office betting pools, and families can organize shared tickets for special draws. The app’s automation, collaboration features, and transparency make it a must-have for anyone who enjoys group betting.

    Potential improvements could include expanding the app to support international lotteries or sports betting, adding an offline mode for ticket registration and result checking, and introducing a rewards system for frequent users.
  `;

  resources: string = `
    https://www.loteriasyapuestas.es/es

  `;

  aboutus: string = `
    https://www.loteriasyapuestas.es/es
    
  `;

  whatsapp: string = `
    +36 678 90 87 23
  `;

  contact: string = `
    +36 932 00 90 72
  `;

  mail: string = `
    infoforhelp@lookau.com
  `;
  

}