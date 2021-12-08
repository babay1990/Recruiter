import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VacancyService} from '../../service/vacancy.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {NotificationService} from '../../service/notification.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-add-vacancy',
  templateUrl: './add-vacancy.component.html',
  styleUrls: ['./add-vacancy.component.css']
})
export class AddVacancyComponent implements OnInit {

  public vacancyForm: FormGroup;

  constructor(
     private vacancyService: VacancyService,
     private notificationService: NotificationService,
     private router: Router,
     private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.vacancyForm = this.createVacancyForm();
  }

  createVacancyForm(): FormGroup {
    return this.fb.group({
      company: ['', Validators.compose([Validators.required])],
      profession: ['', Validators.compose([Validators.required])],
      description: ['', Validators.compose([Validators.required])],
      adress: ['', Validators.compose([Validators.required])],
    });
  }

  submit(): void {
    console.log(this.vacancyForm.value);

    this.vacancyService.create({
      company: this.vacancyForm.value.company,
      profession: this.vacancyForm.value.profession,
      description: this.vacancyForm.value.description,
      adress: this.vacancyForm.value.adress,
    }).subscribe(data => {
      console.log(data);
      this.notificationService.showSnackBar('Successfully Create Vacancy!');
      this.router.navigate(['info']);
    }, error => {
      this.notificationService.showSnackBar('Something went wrong with create vacancy!');
    });
  }
}
