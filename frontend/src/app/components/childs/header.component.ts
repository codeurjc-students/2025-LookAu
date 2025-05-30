import { Component, EventEmitter, OnInit, OnDestroy, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Account } from '../../models/account.model';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  standalone: false,
})
export class HeaderComponent implements OnInit, OnDestroy {

  logoPath: string = 'assets/header/header-logo.png';

  isLogged: boolean = false;
  activeTab: string = '';
  currentUser: Account = {} as Account;

  @Output() tabChange = new EventEmitter<string>();

  private userLoadedSubscription: Subscription = new Subscription();

  constructor(
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.route.fragment.subscribe((fragment) => {
      this.activeTab = fragment || 'generalinformation';
    });
  }

  ngOnInit(): void {
    this.userLoadedSubscription = this.authService.userLoaded().subscribe((loaded) => {
      this.isLogged = loaded;
      if (loaded) {
        this.currentUser = this.authService.getUser();
      } else {
        this.currentUser = {} as Account;
      }
    });

    this.authService.getCurrentUser().subscribe();
  }

  ngOnDestroy(): void {
    this.userLoadedSubscription.unsubscribe();
  }

  logout() {
    this.authService.logout();
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
    this.tabChange.emit(tab);
  }

}
