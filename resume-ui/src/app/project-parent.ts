import {Project} from "./project";
import {TechSkills} from "./tech-skills";

export interface ProjectParent {
  parentName: string
  parentType: string
  description: string
  startDate: Date
  endDate?: Date
  projects: Project[]
  projectParentSkills: TechSkills[]
}
