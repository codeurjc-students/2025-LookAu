import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account.service';
import { Router } from '@angular/router';
import { PopUpService } from '../../services/popup.service';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-editprofile',
  templateUrl: './newTeam.component.html',
  standalone: false,
})

export class NewTeamComponent {

  public user: Account = {} as Account;

  //Edit profile
  public name: string = "";

  public imagePreview: string | ArrayBuffer | null = null;
  public selectedFileName: string = '';
  public selectedFile: File | null = null;


  //My Friends
  public friendsTeam: any[] = [];

  
  //Add a friend
  public searchTerm: string = "";
  public searching: boolean = false;
  public isEmptySearchAccounts: boolean = false;
  public searchAccounts: any[] = [];

  constructor(public authService: AuthService, public accountService: AccountService, public teamService: TeamService, private router: Router, private popUpService: PopUpService) {
    this.authService.getCurrentUser();
  }


  ngOnInit() {
    this.friendsTeam = [];
  }


  /** TEAM DETAILS **/
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


  /** SEARCH BAR **/
  searchFriend(searchTerm: string) {
    if (!searchTerm || searchTerm.trim() === '') {
      this.searchAccounts = [];
      this.isEmptySearchAccounts = false;
      this.searching = false;
      return;
    }
  
    this.searching = true;
    this.accountService.searchMyFriend(searchTerm).subscribe(
      (response: string[]) => {
        var res = response;
        this.searchAccounts = res.filter(item => !this.friendsTeam.includes(item));
        this.isEmptySearchAccounts = response.length === 0;
        this.searching = true;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }


  /** ACCOUNTS **/
  addFriendToTeam(nickName: string){
    this.friendsTeam = this.friendsTeam.concat(nickName);
    this.searchFriend(this.searchTerm);
  }

  isMyFriendsEmpty(): boolean {
    return !this.friendsTeam?.length;
  }

  deleteFriendTeam(nickName: string) {
    this.friendsTeam = this.friendsTeam.filter(item => item !== nickName);
    this.searchFriend(this.searchTerm);
  }

  saveData() {
    if (this.name != "") {

    
      this.teamService.createTeam(this.name, this.friendsTeam).subscribe(
        (response) => {

          if (this.selectedFile != null && response!=null) {
            this.teamService.setProfileImage(this.selectedFile, response.id).subscribe(
              (response) => {
                this.router.navigate(['/teams']);
              }
            );
          }else{
            this.router.navigate(['/teams']);
          }


        },);

    } else {
      this.popUpService.openPopUp("Fill all data or upload a photo.");
    }

  }
}