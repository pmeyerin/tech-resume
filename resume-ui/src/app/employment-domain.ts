import {ProjectDomain} from "./project-domain";
import {SkillsDomain} from "./skills-domain";

export interface EmploymentDomain {
  employmentId: string;
  employmentName: string;
  employmentDescription: string;
  employmentStart: string;
  employmentEnd: string;
  projects: ProjectDomain[];
  techAndSkills: SkillsDomain[];
}
