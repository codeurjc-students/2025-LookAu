import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  standalone: false,
})

export class GroupsComponent {
  
  //search bar
  public searchTerm: string = "";
  public searchGroups: any[] = [];
  public isEmptySearchGroups: boolean = false;
  public searching: boolean = false;

  //groups
  public groups: any[] = [];
  public isLastGroupsRequest: boolean = false; //ajax
  public loadingGroups: boolean = false; //ajax
  public indexGroups: number = 0; //ajax
  public moreGroups: boolean = false; //ajax


  constructor(public authService: AuthService, public accountService: AccountService, private router: Router) {
    this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.getMyFriends();  
  }

  /** Search Bar **/
  searchGroup(searchTerm: string){
    if (!searchTerm || searchTerm.trim() === '') {
      this.searchGroups = [];
      this.isEmptySearchGroups = false;
      this.searching = false;
      return;
    }
  
    this.searching = true;
    this.accountService.searchFriend(searchTerm).subscribe(
      (response) => {
        this.searchGroups = response;
        this.isEmptySearchGroups = response.length === 0;
        this.searching = true;
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  /** Get Account Groups **/
  getMyFriends() {
    this.accountService.getMyFriends(0).subscribe(
      (response) => {
        this.groups = response.content;
        this.isLastGroupsRequest = response.last;
      },
      (error) => {
        console.error('Error al obtener los sujetos:', error);
      }
    );
  }

  getMoreMyFriends() {
    this.loadingGroups = true; //show the spinner
    this.accountService.getMyFriends(this.indexGroups).subscribe(
      (response) => {
        this.groups = this.groups.concat(response.content);
        this.moreGroups = !response.last;
        this.indexGroups++; //next ajax buttom
        this.loadingGroups = false; //hide the spinner
        this.isLastGroupsRequest = response.last;
        console.log(response.last)
      }
    );
  }

  isMyFriendsEmpty(): boolean {
    return !this.groups?.length;
  }

}