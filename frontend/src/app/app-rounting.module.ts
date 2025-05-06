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
// import { Main } from './components/main/main.component';
// import { SubjectInfo } from './components/main/subject_info.component';
// import { LoginComponent } from './components/auth/login.component';
// import { ErrorComponent } from './components/errors/error.component';
// import { SignUpComponent } from './components/signup/signup.component';
// import { ProfileComponent } from './components/profile/profile.component';
// import { AdminAllSubject } from './components/subjects/admin_all_subjects.component';
// import { EditProfileComponent } from './components/profile/editProfile.component';
// import { SubjectsUser } from './components/subjects/subjectsUser.component';
// import { AdminAddSubject } from './components/subjects/admin_add_subject.component';
// import { GeneralInformation } from './components/subjects/general_informationt.component';
// import { AddUserSubject } from './components/subjects/add_user_subject.component';
// import { Exams } from './components/subjects/exams.component';
// import { ExamsStudent } from './components/subjects/exam_student.component';
// import { MarksStudent } from './components/subjects/marks_student.component';
// import { ForumComponent } from './components/subjects/forum.component';
// import { AddExam } from './components/subjects/add_exam.component';


const routes: Routes = [
  {path: 'teams', component: TeamsComponent},
  {path: 'teams/newteam', component: NewTeamComponent},
  {path: 'teams/:id/tickets', component: CardTeamsComponent}, 
  {path: 'teams/:teamId/tickets/:ticketId', component: CardTicketsTeamsComponent},

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