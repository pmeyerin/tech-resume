import {SkillsDomain} from "./skills-domain";

export interface ProjectDomain {
  projectId: string;
  projectName: string;
  projectDescription: string;
  projectStart: string;
  projectEnd?: string;
  techAndSkills: SkillsDomain[];
}
