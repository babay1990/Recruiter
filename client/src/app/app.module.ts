import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from './material-module';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {authInterceptorProviders} from './helper/auth-interceptor.service';
import {authErrorInterceptorProviders} from './helper/error-interceptor.service';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { InfoComponent } from './userinfo/info/info.component';
import { HeaderComponent } from './layout/header/header.component';
import { EditUserComponent } from './userinfo/edit-user/edit-user.component';
import { MainComponent } from './layout/main/main.component';
import { AddVacancyComponent } from './userinfo/add-vacancy/add-vacancy.component';
import { VacancyComponent } from './userinfo/vacancy/vacancy.component';
import { FooterComponent } from './layout/footer/footer.component';
import { UserdetailsComponent } from './userinfo/userdetails/userdetails.component';
import { UpdateVacancyComponent } from './userinfo/update-vacancy/update-vacancy.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    InfoComponent,
    HeaderComponent,
    EditUserComponent,
    MainComponent,
    AddVacancyComponent,
    VacancyComponent,
    FooterComponent,
    UserdetailsComponent,
    UpdateVacancyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MaterialModule,
    HttpClientModule,
    FormsModule
  ],

  providers: [authInterceptorProviders, authErrorInterceptorProviders],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
