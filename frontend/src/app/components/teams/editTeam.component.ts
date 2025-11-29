import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PopUpService } from '../../services/popup.service';
import { TeamService } from '../../services/team.service';

@Component({
  selector: 'app-editprofile',
  templateUrl: './editTeam.component.html',
  standalone: false,
})

export class EditTeamComponent {

  public user: Account = {} as Account;

  public teamId: number = 0;
  public team: any = {};
  public tickets: any[] = [];
  
  //Accounts
  public accounts: any[] = [];
  public isEmptySearchAccounts: boolean = false;
  public currentAccount: any = {};

  //Edit profile
  public name: string = "";
  public imagePreview: string | ArrayBuffer | null = null;
  public selectedFileName: string = '';
  public selectedFile: File | null = null;
  
  

  constructor(public authService: AuthService, public accountService: AccountService, public teamService: TeamService, private router: Router, private route:ActivatedRoute, private popUpService: PopUpService) {
    this.authService.getCurrentUser();

    this.teamId = Number(this.route.snapshot.paramMap.get('teamId') || 0);

    
  }


  ngOnInit() {
    this.getTeam();
    this.getTickets();
  }

  /* Get Tickets */
  getTickets(){
    this.teamService.getAllTickets(this.teamId).subscribe(
      (response) => {
        this.tickets = response;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  /** Get Team **/
  getTeam(){
    this.teamService.getTeam(this.teamId).subscribe(
      (response) => {
        this.team = response;
        this.name = response.name;
        this.getTeamAccounts();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  /** Get Accounts **/
  getTeamAccounts(){
    this.teamService.getAccountsTeam(String(this.teamId)).subscribe(
      (response) => {
        this.accounts = response;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  isMyFriendsEmpty(): boolean {
    return !this.accounts?.length;
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


  /* Leave Team */
  leaveTeam(){
    if(this.hasTicketDependence()){
      this.popUpService.openPopUp('You are included in one or more tickets. Please delete the ticket or change the paid or claim account.');
    }else{
      this.popUpService.openPopUpTwoLeaveTeam('Leaving the team means you will not be a member anymore. Are you sure?', this.authService.getUserNickName(), this.teamId);
    }
  }

  hasTicketDependence(): boolean{
    
    let nickName = this.authService.getUserNickName();

    for (let ticket of this.tickets) {
      
      if (ticket.paidByName === nickName || ticket.claimedBy === nickName) {
        return true;
      }
    }

    return false;
  }

  

  /** UPDATE TEAM **/
  saveData() {
    if (this.name != "") {
    
      this.teamService.updateTeam(this.name, this.teamId).subscribe(
        (response) => {

          if (this.selectedFile != null) {
            
            this.teamService.setProfileImage(this.selectedFile, this.teamId).subscribe(
              (response) => {
                this.router.navigate(['/teams']);
              },
              (error) => {
                this.router.navigate(['/error']);
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