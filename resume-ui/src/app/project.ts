import {TechSkills} from "./tech-skills";

export interface Project {
  projectId: string;
  projectName: string;
  projectDescription: string;
  startDate: Date;
  endDate: Date;
  projectSkills: TechSkills[];
}
