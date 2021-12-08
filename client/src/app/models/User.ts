import {Vacancy} from './Vacancy';

export interface User {
  id: number;
  email: string;
  username: string;
  name: string;
  lastname: string;
  role: string;
  profession: string;
  phoneNumber: string;
  recruiterVacancyList?: Vacancy[];
  employeeVacancyList?: Vacancy[];
}
