import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

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
import { CardTicketsTeamsComponent } from './components/tickets/cardTicketsTeams.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { BonolotoComponent } from './components/tickets/ticketsType/bonoloto.component';
import { EurodreamsComponent } from './components/tickets/ticketsType/eurodreams.component';
import { EuromillonesComponent } from './components/tickets/ticketsType/euromillones.component';
import { GordoComponent } from './components/tickets/ticketsType/gordo.component';
import { LoteriaComponent } from './components/tickets/ticketsType/loteria.component';
import { LototurfComponent } from './components/tickets/ticketsType/lototurf.component';
import { PrimitivaComponent } from './components/tickets/ticketsType/primitiva.component';
import { QuintupleComponent } from './components/tickets/ticketsType/quintuple.component';
import { QuinielaComponent } from './components/tickets/ticketsType/quiniela.component';
import { QuinigolComponent } from './components/tickets/ticketsType/quinigol.component';
import { TicketTypeService } from './services/ticketType.service';
import { NewCardTicketsTeamsComponent } from './components/tickets/newCardTicketsTeams.component';

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
    CardTeamsComponent,
    CardTicketsTeamsComponent,
    BonolotoComponent,
    EurodreamsComponent,
    EuromillonesComponent,
    GordoComponent,
    LoteriaComponent,
    LototurfComponent,
    PrimitivaComponent,
    QuintupleComponent,
    QuinielaComponent,
    QuinigolComponent,
    NewCardTicketsTeamsComponent,

  ], 
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    MatDialogModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatOptionModule,
  ],
  providers: [AuthService],
  bootstrap: [AppComponent],

})
export class AppModule { }



