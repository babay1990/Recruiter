import {User} from './User';

export interface Vacancy {
  id?: number;
  recruiter?: User;
  company: string;
  profession: string;
  description: string;
  adress: string;
  candidateList?: User[];
  injectedCandidateList?: User[];
  approvedCandidateList?: User[];
}
