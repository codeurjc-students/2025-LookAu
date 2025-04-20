import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account.service';
import { Router } from '@angular/router';
import { PopUpService } from '../../services/popup.service';
import { first } from 'rxjs';
import { findStylesheetFiles } from '@angular/cdk/schematics';

@Component({
  selector: 'app-editprofile',
  templateUrl: './editProfile.component.html',
  standalone: false,
})

export class EditProfileComponent {

  public user: Account = {} as Account;

  //Edit profile
  public firstName: string = "";
  public lastName: string = "";
  public password: string = "";

  public imagePreview: string | ArrayBuffer | null = null;
  public selectedFileName: string = '';
  public selectedFile: File | null = null;


  //My Friends
  public myFriends: any[] = [];
  public loadingMyFriends: boolean = false;
  public isLastMyFriendsRequest: boolean = false; //ajax
  public indexMyFriends: number = 1; //ajax
  public moreMyFriends: boolean = false; //ajax

  constructor(public authService: AuthService, public accountService: AccountService, private router: Router, private popUpService: PopUpService) {
    this.authService.getCurrentUser();
  }


  ngOnInit() {
    this.getMyFriends();
  }



  /** EDIT PROFILE **/

  //change the preview file and the name file
  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];

    if (file) {
      this.selectedFile = file;
      this.selectedFileName = file.name;

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }


  /** MY FRIENDS **/
  getMyFriends() {
    this.accountService.getMyFriends(0).subscribe(
      (response) => {
        this.myFriends = response.content;
        this.isLastMyFriendsRequest = response.last;
      },
      (error) => {
        console.error('Error al obtener los sujetos:', error);
      }
    );
  }

  getMoreMyFriends() {
    this.loadingMyFriends = true; //show the spinner
    this.accountService.getMyFriends(this.indexMyFriends).subscribe(
      (response) => {
        this.myFriends = this.myFriends.concat(response.content);
        this.moreMyFriends = !response.last;
        this.indexMyFriends++; //next ajax buttom
        this.loadingMyFriends = false; //hide the spinner
        this.isLastMyFriendsRequest = response.last;
      }
    );
  }

  isMyFriendsEmpty(): boolean {
    return !this.myFriends?.length;
  }

  deleteMyFriends(nickName: string) {
    this.popUpService.openPopUpTwo("Are you sure to delete " + nickName + " from friends?", nickName);
  }

  saveData() {
    if (this.selectedFile != null || this.firstName != "" || this.lastName != "" || this.password != "") {


      if (this.firstName == "") {
        this.firstName = this.authService.getUserFirstName();
      }
      if (this.lastName == "") {
        this.lastName = this.authService.getUserLastName();
      }
      if (this.password == "") {
        this.password = this.authService.getUserPassword();
      }
      this.accountService.editProfile(this.firstName, this.lastName, this.password).subscribe(
        (response) => {

          if (this.selectedFile != null) {
            this.accountService.setProfileImage(this.selectedFile).subscribe(
              (response) => {
                this.router.navigate(['/profile']);
              }
            );
          }else{
            this.router.navigate(['/profile']);
          }


        },);

    } else {
      this.popUpService.openPopUp("Fill any fields or upload a photo.");
    }

  }
}