import { Component, OnInit } from '@angular/core';
import {Vacancy} from '../../models/Vacancy';
import {UserService} from '../../service/user.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VacancyService} from '../../service/vacancy.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  public searchForm: FormGroup;
  vacancys: Vacancy [];
  isLoggedIn = false;

  constructor(private tokenService: TokenStorageService,
              private userService: UserService,
              private vacancyService: VacancyService,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {

    this.searchForm = this.createSearchForm();
    this.isLoggedIn = !!this.tokenService.getToken();

    if(this.isLoggedIn) {

    this.vacancyService.getVacancyList()
      .subscribe(data => {
        console.log(data);
        this.vacancys = data;
      });
    }
  }

  createSearchForm(): FormGroup {
    return this.fb.group({
      str: ['', Validators.compose([Validators.required])],
    });
  }

  submit(): void {
    console.log(this.searchForm.value);

    this.vacancyService.searchVacancy(
      this.searchForm.value.str,
      ).subscribe(data => {
        console.log(data);
        this.vacancys = data;
      });
  }
}
