import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';



import { AppComponent } from './app.component';


import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/childs/header.component';

import { LoginComponent } from './components/auth/login.component';
import { AppRoutingModule } from './app-rounting.module';
import { FooterComponent } from './components/childs/footer.component';
import { BrowserModule } from '@angular/platform-browser';
import { AuthService } from './services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { PopUpDialogComponent } from './components/popUp/popup_dialog.component';
import { ProfileComponent } from './components/profile/profile.component';
import { IndividualComponent } from './components/individual/individual.component';
import { SignupComponent } from './components/signup/signup.component';
import { TeamsComponent } from './components/teams/teams.component';
import { EditProfileComponent } from './components/profile/editProfile.component';
import { MatButtonModule } from '@angular/material/button';
import { PopUpDialogComponentTwo } from './components/popUp/popupTwo_dialog.component';
import { NewTeamComponent } from './components/teams/newTeam.component';
import { CardTeamsComponent } from './components/teams/cardTeams.component';



@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    SignupComponent,
    PopUpDialogComponent,
    ProfileComponent,
    IndividualComponent,
    TeamsComponent,
    EditProfileComponent,
    PopUpDialogComponentTwo,
    NewTeamComponent,
    CardTeamsComponent

  ], 
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    MatDialogModule,
    FormsModule,
    MatButtonModule,
  ],
  providers: [AuthService],
  bootstrap: [AppComponent],

})
export class AppModule { }



