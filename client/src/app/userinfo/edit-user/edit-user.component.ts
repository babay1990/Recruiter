import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {NotificationService} from '../../service/notification.service';
import {UserService} from '../../service/user.service';
import {User} from '../../models/User';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {

  public profileEditForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<EditUserComponent>,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.profileEditForm = this.createProfileForm();
  }

  createProfileForm(): FormGroup {
    return this.fb.group({
      name: [
        this.data.user.name,
        Validators.compose([Validators.required])
      ],

      lastName: [
        this.data.user.lastname,
        Validators.compose([Validators.required])
      ],

      profession: [
        this.data.user.profession,
        Validators.compose([Validators.required])
      ],

      phoneNumber: [
        this.data.user.phoneNumber,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.userService.updateUser(this.updateUser())
      .subscribe(() => {
      this.notificationService.showSnackBar('User updated successfully');
      this.dialogRef.close();
    });
  }

  private updateUser(): User {
    this.data.user.name = this.profileEditForm.value.name;
    this.data.user.lastname = this.profileEditForm.value.lastName;
    this.data.user.profession = this.profileEditForm.value.profession;
    this.data.user.phoneNumber = this.profileEditForm.value.phoneNumber;
    return this.data.user;
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
