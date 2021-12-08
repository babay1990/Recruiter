import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {InfoComponent} from './userinfo/info/info.component';
import {MainComponent} from './layout/main/main.component';
import {AddVacancyComponent} from './userinfo/add-vacancy/add-vacancy.component';
import {VacancyComponent} from './userinfo/vacancy/vacancy.component';
import { UserdetailsComponent } from './userinfo/userdetails/userdetails.component';


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'info', component: InfoComponent},
  {path: 'main', component: MainComponent},
  {path: '', component: MainComponent},
  {path: 'addVacancy', component: AddVacancyComponent},
  {path: 'user/:id', component: UserdetailsComponent},
  {path: 'vacancy/:id', component: VacancyComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
