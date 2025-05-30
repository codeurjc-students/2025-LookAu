import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Account } from '../../models/account.model';
import { HttpErrorResponse } from '@angular/common/http';
import { PopUpService } from '../../services/popup.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: false,
})
export class LoginComponent {

  logoPath: string = 'assets/others/signout-logo.png';
  public mail: string = '';
  public password: string = '';
  public showError: boolean = false;
  public error: string = '';

  constructor(
    private popUpService: PopUpService,
    private authService: AuthService,
    private router: Router
  ) {}

  sendCredentials(event: Event) {
    event.preventDefault();

    this.authService.login(this.mail, this.password).subscribe({
      next: (_) => {
        this.authService.getCurrentUser().subscribe({
          next: (account) => {
            if (account && account.email) {
              this.router.navigate(['/teams']);
            } else {
              this.router.navigate(['/error']);
            }
          },
          error: (_) => {
            this.router.navigate(['/error']);
          }
        });
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 401) {
          this.popUpService.openPopUp("The password or the mail are wrong.");
        } else {
          this.router.navigate(['/error']);
        }
      }
    });
  }
}
