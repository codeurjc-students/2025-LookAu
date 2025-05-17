import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { TeamsComponent } from './components/teams/teams.component';
import { ErrorComponent } from './components/error/error.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/profile/editProfile.component';
import { NewTeamComponent } from './components/teams/newTeam.component';
import { CardTeamsComponent } from './components/teams/cardTeams.component';
import { CardTicketsTeamsComponent } from './components/tickets/cardTicketsTeams.component';
import { NewCardTicketsTeamsComponent } from './components/tickets/newCardTicketsTeams.component';


const routes: Routes = [
  {path: 'teams', component: TeamsComponent},
  {path: 'teams/newteam', component: NewTeamComponent},
  {path: 'teams/:teamId/tickets/new', component: NewCardTicketsTeamsComponent},
  {path: 'teams/:teamId/tickets/:ticketId', component: CardTicketsTeamsComponent},
  {path: 'teams/:id/tickets', component: CardTeamsComponent},

  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent},
  {path: 'error', component: ErrorComponent},

  {path: 'profile', component: ProfileComponent},
  {path: 'editProfile', component: EditProfileComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }