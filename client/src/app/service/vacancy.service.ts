import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

const VAC_API = 'http://localhost:8080/api/vacancy/';

@Injectable({
  providedIn: 'root'
})
export class VacancyService {

  constructor(private http: HttpClient) { }

  public create(vacancy): Observable<any> {
    return this.http.post(VAC_API + 'create', {
      company: vacancy.company,
      profession: vacancy.profession,
      description: vacancy.description,
      adress: vacancy.adress
    });
  }

  public searchVacancy(str): Observable<any> {
    return this.http.post(VAC_API + 'search', str);
  }

  public canApplyVacancy(id: number): Observable<any> {
    return this.http.get(VAC_API + 'canApplyVacancy/' + id);
  }

  public deleteVacancy(id: number): Observable<any> {
    return this.http.post(VAC_API + 'deleteVacancy/', id);
  }

  updateVacancy(vacancy: any): Observable<any> {
    return this.http.post(VAC_API + 'update', vacancy);
  }

  injectCandidate(list: number[]): Observable<any> {
    return this.http.post(VAC_API + 'injectCandidate', list);
  }

  approveCandidate(list: number[]): Observable<any> {
    return this.http.post(VAC_API + 'approveCandidate', list);
  }

  getVacancyForCurrentUser(): Observable<any> {
    return this.http.get(VAC_API + 'vacancys');
  }

  getInjectedVacancyForCurrentUser(): Observable<any> {
    return this.http.get(VAC_API + 'injectedVacancyForUser');
  }

  getAcceptedVacancyForCurrentUser(): Observable<any> {
    return this.http.get(VAC_API + 'acceptedVacancyForUser');
  }

  getVacancyById(id: number): Observable<any> {
    return this.http.get(VAC_API + 'vacancy/' + id);
  }

  applyForVacancy(vacancy: any): Observable<any> {
    return this.http.post(VAC_API + 'vacancy/apply', vacancy);
  }

  getVacancyList(): Observable<any> {
    return this.http.get(VAC_API + 'vacancyList');
  }

}
