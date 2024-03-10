import {ProjectDomain} from "./project-domain";
import {EmploymentDomain} from "./employment-domain";
import {EducationDomain} from "./education-domain";

export interface WorkerSkillRelationDomain {
  projects: ProjectDomain[];
  employments: EmploymentDomain[];
  educations: EducationDomain[];
}
