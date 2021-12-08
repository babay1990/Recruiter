import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {NotificationService} from '../../service/notification.service';
import {VacancyService} from '../../service/vacancy.service';
import {Vacancy} from '../../models/Vacancy';

@Component({
  selector: 'app-update-vacancy',
  templateUrl: './update-vacancy.component.html',
  styleUrls: ['./update-vacancy.component.css']
})
export class UpdateVacancyComponent implements OnInit {

public vacancyEditForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<UpdateVacancyComponent>,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data,
              private vacancyService: VacancyService) {
  }

  ngOnInit(): void {
    this.vacancyEditForm = this.createVacancyUpdateForm();
  }

  createVacancyUpdateForm(): FormGroup {
    return this.fb.group({
      company: [
        this.data.vacancy.company,
        Validators.compose([Validators.required])
      ],

      profession: [
        this.data.vacancy.profession,
        Validators.compose([Validators.required])
      ],

      description: [
        this.data.vacancy.description,
        Validators.compose([Validators.required])
      ],

      adress: [
        this.data.vacancy.adress,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.vacancyService.updateVacancy(this.updateVacancy())
    .subscribe(() => {
      this.notificationService.showSnackBar('Вакансия обновлена!');
      this.dialogRef.close();
    });
  }

  private updateVacancy(): Vacancy {
    this.data.vacancy.company = this.vacancyEditForm.value.company;
    this.data.vacancy.profession = this.vacancyEditForm.value.profession;
    this.data.vacancy.description = this.vacancyEditForm.value.description;
    this.data.vacancy.adress = this.vacancyEditForm.value.adress;
    return this.data.vacancy;
  }

  closeDialog() {
    this.dialogRef.close();
  }
}

