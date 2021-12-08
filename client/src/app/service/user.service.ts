import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

const USER_API = 'http://localhost:8080/api/user/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserById(id: number): Observable<any> {
    return this.http.get(USER_API + 'details/' + id);
  }

  getCurrentUser(): Observable<any> {
    return this.http.get(USER_API);
  }

  updateUser(user: any): Observable<any> {
    return this.http.post(USER_API + 'update', user);
  }

  uploadImageToUser(file: File): Observable<any> {
    const uploadData = new FormData();
    uploadData.append('file', file);
    return this.http.post(USER_API + 'upload', uploadData);
  }

  uploadSummaryToUser(file: File): Observable<any> {
    const uploadData = new FormData();
    uploadData.append('file', file);
    return this.http.post(USER_API + 'uploadSummary', uploadData);
  }

  upload(id: number): Observable<any> {
    return this.http.get(USER_API + 'summary/' + id, {responseType: 'blob'});
  }
}
