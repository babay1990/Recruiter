import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {UserService} from '../../service/user.service';
import {Vacancy} from '../../models/Vacancy';
import {User} from '../../models/User';
import {NotificationService} from '../../service/notification.service';
import {VacancyService} from '../../service/vacancy.service';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {UpdateVacancyComponent} from '../..//userinfo/update-vacancy/update-vacancy.component';

@Component({
  selector: 'app-vacancy',
  templateUrl: './vacancy.component.html',
  styleUrls: ['./vacancy.component.css']
})
export class VacancyComponent implements OnInit {

  vacancy: Vacancy;
  id: number;
  user: User;
  canApply = false;

  constructor(private route: ActivatedRoute,
              private notificationService: NotificationService,
              private vacancyService: VacancyService,
              private router: Router,
              private dialog: MatDialog,
              private userService: UserService) { }

  ngOnInit(): void {
   this.id = this.route.snapshot.params['id'];
   this.vacancyService.getVacancyById(this.id)
     .subscribe(data => {
     this.vacancy = data;
   });

   this.userService.getCurrentUser()
     .subscribe(data => {
     this.user = data;
   });

   this.vacancyService.canApplyVacancy(this.id)
   .subscribe(data => {
   this.canApply = data;
   });

 }

  applyForVacancy(): void {
      this.vacancyService.applyForVacancy(this.vacancy)
          .subscribe(() => {
          this.notificationService.showSnackBar('Вы успешно откликнулись на вакансию!');
          this.router.navigate(['info']);
      });
  }

  deleteVacancy(): void {
    this.vacancyService.deleteVacancy(this.vacancy.id)
      .subscribe(() => {
         this.notificationService.showSnackBar('Вакансия удалена!');
         this.router.navigate(['info']);
      });
  }

  updateVacancy(): void {
     const dialogVacancyEditConfig = new MatDialogConfig();
     dialogVacancyEditConfig.width = '1000px';
     dialogVacancyEditConfig.height = '1000px';
     dialogVacancyEditConfig.data = {
       vacancy: this.vacancy
     };
     this.dialog.open(UpdateVacancyComponent, dialogVacancyEditConfig);
  }

  injectCandidate(candidateId: number): void {
    let list: number[] = [this.vacancy.id, candidateId];
    this.vacancyService.injectCandidate(list)
      .subscribe(() => {
         this.notificationService.showSnackBar('Кандидат переведен в список отклоненных!');
         this.router.navigate(['info']);
      });

  }

  approveCandidate(candidateId: number): void {
      let list: number[] = [this.vacancy.id, candidateId];
      this.vacancyService.approveCandidate(list)
        .subscribe(() => {
           this.notificationService.showSnackBar('Кандидат переведен в список одобренных!');
           this.router.navigate(['info']);
        });

  }
}
