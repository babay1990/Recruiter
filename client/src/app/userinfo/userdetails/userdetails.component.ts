import { Component, OnInit } from '@angular/core';
import {User} from '../../models/User';
import {NotificationService} from '../../service/notification.service';
import { ActivatedRoute } from '@angular/router';
import {UserService} from '../../service/user.service';

import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-userdetails',
  templateUrl: './userdetails.component.html',
  styleUrls: ['./userdetails.component.css']
})
export class UserdetailsComponent implements OnInit {

user: User;
id: number;
userProfileImage: File;
previewImgURL: any;

  constructor(private route: ActivatedRoute,
              private notificationService: NotificationService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.userService.getUserById(this.id)
    .subscribe(data => {
       this.user = data;
       this.userProfileImage = data.imageBytes;
    });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  summary(): void {
   this.userService.upload(this.id)
   .subscribe(data => { //when you use stricter type checking
    	let blob:any = new Blob([data], { type: 'application/pdf' });
    	const url = window.URL.createObjectURL(blob);
    	window.open(url);
    			//window.location.href = data.url;
    			//fileSaver.saveAs(blob, 'employees.pdf');
   }), (error: any) =>
   this.notificationService.showSnackBar('Пользователь пока что не закрузил резюме');
  }
}
