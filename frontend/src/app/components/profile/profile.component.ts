import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Account } from '../../models/account.model';
import { AccountService } from '../../services/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: false,
})

export class ProfileComponent {

  public user: Account = {} as Account;

  //Add a friend
  public searchTerm: string = "";
  public searching: boolean = false;
  public isEmptySearchAccounts: boolean = false;
  public searchAccounts: any[] = [];

  
  //My Friends
  public myFriends: any[] = [];
  public loadingMyFriends: boolean = false;
  public isLastMyFriendsRequest: boolean = false; //ajax
  public indexMyFriends: number = 1; //ajax
  public moreMyFriends: boolean = false; //ajax

  //Pending Friends
  public pendingFriends: any[] = [];
  public loadingPendingFriends: boolean = false;
  public isLastPendingFriendsRequest: boolean = false; //ajax
  public indexPendingFriends: number = 1; //ajax
  public morePendingFriends: boolean = false; //ajax

  //Request Friends
  public requestFriends: any[] = [];
  public loadingRequestFriends: boolean = false;
  public isLastRequestFriendsRequest: boolean = false; //ajax
  public indexRequestFriends: number = 1; //ajax
  public moreRequestFriends: boolean = false; //ajax
  
  constructor(public authService: AuthService, public accountService: AccountService, private router: Router) {
    this.authService.getCurrentUser();
  }


  ngOnInit() {
    this.getMyFriends();  
    this.getPendingFriends(); 
    this.getRequestFriends(); 
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
        console.log(response.last)
      }
    );
  }

  isMyFriendsEmpty(): boolean {
    return !this.myFriends?.length;
  }



  /** PENDING FRIENDS **/
  getPendingFriends() {
    this.accountService.getPendingFriends(0).subscribe(
      (response) => {
        this.pendingFriends = response.content;
        this.isLastPendingFriendsRequest = response.last;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getMorePendingFriends() {
    this.loadingPendingFriends = true; //show the spinner
    this.accountService.getPendingFriends(this.indexPendingFriends).subscribe(
      (response) => {
        this.pendingFriends = this.pendingFriends.concat(response.content);
        this.morePendingFriends = !response.last;
        this.indexPendingFriends++; //next ajax buttom
        this.loadingPendingFriends = false; //hide the spinner
        this.isLastPendingFriendsRequest = response.last;
        console.log(response.last)
      }
    );
  }

  isPendingFriendsEmpty(): boolean {
    return !this.pendingFriends?.length;
  }


  acceptFriend(nickName: string){
    this.accountService.acceptFriend(nickName).subscribe(
      (response) => {
        this.getMyFriends();  
        this.getPendingFriends(); 
        this.getRequestFriends();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  denyFriend(nickName: string){
    this.accountService.denyFriend(nickName).subscribe(
      (response) => {
        this.getMyFriends();  
        this.getPendingFriends(); 
        this.getRequestFriends();
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  searchFriend(searchTerm: string) {
    if (!searchTerm || searchTerm.trim() === '') {
      this.searchAccounts = [];
      this.isEmptySearchAccounts = false;
      this.searching = false;
      return;
    }
  
    this.searching = true;
    this.accountService.searchFriend(searchTerm).subscribe(
      (response) => {
        console.log("Hola");
        this.searchAccounts = response;
        this.isEmptySearchAccounts = response.length === 0;
        this.searching = true;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }




  /** REQUEST FRIENDS **/
  getRequestFriends() {
    this.accountService.getRequestFriends(0).subscribe(
      (response) => {
        this.requestFriends = response.content;
        this.isLastRequestFriendsRequest = response.last;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getMoreRequestFriends() {
    this.loadingRequestFriends = true; //show the spinner
    this.accountService.getRequestFriends(this.indexRequestFriends).subscribe(
      (response) => {
        this.requestFriends = this.requestFriends.concat(response.content);
        this.moreRequestFriends = !response.last;
        this.indexRequestFriends++; //next ajax buttom
        this.loadingRequestFriends = false; //hide the spinner
        this.isLastRequestFriendsRequest = response.last;
        console.log(response.last)
      }
    );
  }

  isRequestFriendsEmpty(): boolean {
    return !this.requestFriends?.length;
  }

}