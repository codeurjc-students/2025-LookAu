import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Account } from '../../models/account.model';
import { Subscription } from 'rxjs';
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

  private account: Account;
  private userLoadedSubscription: Subscription;

  constructor(private popUpService: PopUpService, private authService: AuthService, private router: Router,) {
    this.mail = '';
    this.password = '';
    this.account = {} as Account;
    this.userLoadedSubscription = new Subscription();

    this.showError = false;
    this.error = '';
  }


  ngOnDestroy() {
    this.userLoadedSubscription.unsubscribe();
  }


  sendCredentials(event: Event) {
    event.preventDefault();

    this.authService.login(this.mail, this.password).subscribe({
      next: (_) => {

        this.userLoadedSubscription = this.authService.userLoaded().subscribe((loaded: boolean) => {
          if (loaded) {
            this.router.navigate(['/group']);
          } else {
            this.router.navigate(['/error']);
          }
        });

        this.authService.getCurrentUser();
      },
      error: (err: HttpErrorResponse) => {

        if (err.status === 400) {
          this.router.navigate(['/error']);
          
        } else if (err.status === 401) {
          this.popUpService.openPopUp("The password or the mail are wrong.");

        } else if (err.status === 403) {
          this.router.navigate(['/error']);

        } else {
          this.router.navigate(['/error']);
        }
      },

    });
  }

}