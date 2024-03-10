import {ProjectDomain} from "./project-domain";
import {SkillsDomain} from "./skills-domain";

export interface EducationDomain {
  educationId: string;
  institutionName: string;
  program: string;
  startDate: string;
  graduationPeriod: string;
  educationType: string;
  projects: ProjectDomain[];
  techAndSkills: SkillsDomain[];
}
