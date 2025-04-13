import { Component } from '@angular/core';
import { Account } from '../../models/account.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { PopUpService } from '../../services/popup.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  standalone: false,
})

export class SignupComponent {
  logoPath: string = 'assets/others/signout-logo.png';

  public firstName: string = '';
  public lastName: string = '';
  public nickName: string = '';
  public mail: string = '';
  public password: string = '';
  public repeatPassword: string = '';

  public showError: boolean = false;
  public error: string = '';

  //private account: Account = {};


  constructor(private popUpService: PopUpService, private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.firstName = '';
    this.lastName = '';
    this.mail = '';
    this.password = '';
    this.repeatPassword = '';
  }



  createUser() {

    if (!this.nickName || !this.firstName || !this.lastName || !this.mail || !this.password || !this.repeatPassword) {
      this.popUpService.openPopUp('Please fill all the fields');
      return; 
    }
    
    if (this.password !== this.repeatPassword) {
      this.popUpService.openPopUp('Passwords do not match');
      this.router.navigate(['signup']);
      return;
    }
  
    this.authService.createUser(this.nickName, this.firstName, this.lastName, this.mail, this.password).subscribe({
      next: _ => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
  
        switch (err.status) {
          case 400:
            this.popUpService.openPopUp('Please fill all fields correctly');
            break;
  
          case 403:
            this.popUpService.openPopUp('Email is already registered');
            break;
  
          case 409:
            this.popUpService.openPopUp('Nickname is already taken');
            break;
  
          default:
            this.popUpService.openPopUp('An unexpected error occurred');
            break;
        }
  
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/signup']);
        });
      }
    });
  }
  

}