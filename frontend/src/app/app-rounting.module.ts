import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { GroupsComponent } from './components/gruops/groups.component';
import { ErrorComponent } from './components/error/error.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/profile/editProfile.component';
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
    {path: '', component: LoginComponent},
    {path: 'group', component: GroupsComponent},

    {path: 'login', component: LoginComponent},
    {path: 'signup', component: SignupComponent},
    {path: 'error', component: ErrorComponent},

    {path: 'profile', component: ProfileComponent},
    {path: 'editProfile', component: EditProfileComponent},

    // {path: 'subjects', component: SubjectsUser},

    // {path: 'admin', component: AdminAllSubject},
    // {path: 'subject/:id/general', component: GeneralInformation},
    // {path: 'subject/:id/exams', component: Exams},
    // {path: 'subject/:id/marks', component: MarksStudent},
    // {path: 'subject/:id/forum', component: ForumComponent},
    // {path: 'subject/:subjectId/exam/:examId/examStudents', component: ExamsStudent},
    // {path: 'subject/:id/exams/add-exam', component: AddExam},
    // {path: 'admin/new-subject', component: AdminAddSubject},
    // {path: 'admin/:id/add-teacher', component: AddUserSubject}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }