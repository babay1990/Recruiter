import {Component, OnInit} from '@angular/core';
import {User} from '../../models/User';
import {TokenStorageService} from '../../service/token-storage.service';
import {UserService} from '../../service/user.service';
import {NotificationService} from '../../service/notification.service';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {EditUserComponent} from '../edit-user/edit-user.component';
import {Vacancy} from '../../models/Vacancy';
import {VacancyService} from '../../service/vacancy.service';

import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

 isLoggedIn = false;
 isUserDataLoaded = false;
 user: User;
 userProfileImage: File;
 summary: File;
 previewImgURL: any;
 summaryURL: any;
 selectedFile: File;
 vacancys: Vacancy [];

 summarys: File;

 constructor(private tokenService: TokenStorageService,
             private notificationService: NotificationService,
             private dialog: MatDialog,
             private vacancyService: VacancyService,
             private userService: UserService) {
 }

 ngOnInit(): void {

   this.isLoggedIn = !!this.tokenService.getToken();

   if(this.isLoggedIn) {
       this.userService.getCurrentUser()
       .subscribe(data => {
         this.user = data;
         this.summary = data.summaryBytes;
         this.userProfileImage = data.imageBytes;
         this.isUserDataLoaded = true;
       });

       this.vacancyService.getVacancyForCurrentUser()
       .subscribe(data => {
         console.log(data);
         this.vacancys = data;
       });
   }
 }

 onFileSelected(event): void {
   this.selectedFile = event.target.files[0];

   const reader = new FileReader();
   reader.readAsDataURL(this.selectedFile);
   reader.onload = () => {
     this.previewImgURL = reader.result;
   };
 }

 formatImage(img: any): any {
   if (img == null) {
     return null;
   }
   return 'data:image/jpeg;base64,' + img;
 }

 onUpload(): void {
   if (this.selectedFile != null) {
     this.userService.uploadImageToUser(this.selectedFile)
     .subscribe(() => {
       this.notificationService.showSnackBar('Profile Image updated successfully');
     });
   }
 }

 openEditDialog(): void {
   const dialogUserEditConfig = new MatDialogConfig();
   dialogUserEditConfig.width = '400px';
   dialogUserEditConfig.data = {
     user: this.user
   };
   this.dialog.open(EditUserComponent, dialogUserEditConfig);
 }

 onSummarySelected(event): void {
   this.selectedFile = event.target.files[0];

   const reader = new FileReader();
   reader.readAsDataURL(this.selectedFile);
   reader.onload = () => {
     this.summaryURL = reader.result;
   };
 }

 uploadSummary(): void {
   if (this.selectedFile != null) {
     this.userService.uploadSummaryToUser(this.selectedFile)
     .subscribe(() => {
       this.notificationService.showSnackBar('Summary updated successfully');
     });
   }
 }

 formatSummary(summary: any): any {
   if (summary == null) {
     return null;
   }
   return 'data:image/jpeg;base64,' + summary;
 }

 upload(): void {
  this.userService.upload(this.user.id)
    .subscribe(data => { //when you use stricter type checking
    	let blob:any = new Blob([data], { type: 'application/pdf' });
    	const url = window.URL.createObjectURL(blob);
    	window.open(url);
    			//window.location.href = data.url;
    			//fileSaver.saveAs(blob, 'employees.pdf');
    		//}), error => console.log('Error downloading the file'),
    }), (error: any) => console.log('Error downloading the file'), //when you use stricter type checking
    () => console.info('File downloaded successfully');
 }

 injectedVacancys(): void {
    this.vacancyService.getInjectedVacancyForCurrentUser()
    .subscribe(data => {
       console.log(data);
       this.vacancys = data;
    });
 }

 acceptedVacancys(): void {
   this.vacancyService.getAcceptedVacancyForCurrentUser()
   .subscribe(data => {
      console.log(data);
      this.vacancys = data;
   });
 }
}



