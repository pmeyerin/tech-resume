import {EmploymentDomain} from "./employment-domain";
import {EducationDomain} from "./education-domain";
import {ProjectDomain} from "./project-domain";
import {SkillEstimatesDomain} from "./skill-estimates-domain";

export interface WorkHistoryDomain {
  workerId: string;
  workerName: string;
  workerPhone: string;
  workerEmail: string;
  employmentHistory: EmploymentDomain[];
  educationHistory: EducationDomain[];
  hobbyProjects: ProjectDomain[];
  workerSkillEstimates: SkillEstimatesDomain[]
}
