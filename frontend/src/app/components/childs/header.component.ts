import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  standalone: false,
})

export class HeaderComponent implements OnInit{

  logoPath: string = 'assets/header/header-logo.png';

  isLogged: boolean = false;
  activeTab: string = '';

  @Output() tabChange = new EventEmitter<string>();

  constructor(public authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.authService.getCurrentUser();
    this.route.fragment.subscribe((fragment) => {
      this.activeTab = fragment || 'generalinformation';
    });
  }

  ngOnInit(): void{
    this.isLogged = this.authService.isLogged();
  }

  logout() {
    this.authService.logout();
  }



  setActiveTab(tab: string) {
    this.activeTab = tab;
    this.tabChange.emit(tab);
  }


}