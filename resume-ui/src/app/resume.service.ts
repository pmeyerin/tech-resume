import {inject, Injectable} from '@angular/core';
import {WorkHistoryDomain} from "./work-history-domain";
import {ProjectParent} from "./project-parent";
import {Project} from "./project";
import {ProjectDomain} from "./project-domain";
import {SkillsDomain} from "./skills-domain";
import {TechSkills} from "./tech-skills";
import {HttpClient} from "@angular/common/http";
import {WorkerSkillRelationDomain} from "./worker-skill-relation-domain";
import {environmentJsonserver} from "../environments/environment.jsonserver";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ResumeService {
  private workerIdMe: string;

  constructor() {
    this.workerIdMe = "d40109cf-967c-45d8-a0eb-f3fbbd8f8471";
  }

  http: HttpClient = inject(HttpClient);
  url = '/api';
  // url = "http://localhost:8081/api";
  getSkillRelations(skill: TechSkills) {
    return this.http.get<WorkerSkillRelationDomain>(`${this.url}/tech-skills/related/${skill.techSkillId}/${this.workerIdMe}`) ?? {};
  }

  getWorkHistory() {
    return this.http.get<WorkHistoryDomain>(`${this.url}/worker/${this.workerIdMe}`) ?? {};
  }

  getEmploymentHistory(workHistory:WorkHistoryDomain|undefined):ProjectParent[] {
    if (!workHistory) {
      return [];
    }
    return workHistory.employmentHistory.map(value => {
      return {
        parentName: value.employmentName,
        parentType: "Employment",
        description: value.employmentDescription,
        startDate: new Date(value.employmentStart),
        endDate: new Date(value.employmentEnd),
        projects: value.projects.map(value => this.projectDomainToProject(value)),
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    })
  }

  parseWorkHistoryChronologically(workHistory:WorkHistoryDomain | undefined): ProjectParent[] {
    if (!workHistory) {
      return [];
    }
    console.log(workHistory);
    let employmentProjectParentList: ProjectParent[] = workHistory?.employmentHistory.map(value => {
      return {
        parentName: value.employmentName,
        parentType: "Employment",
        description: value.employmentDescription,
        startDate: new Date(value.employmentStart),
        endDate: new Date(value.employmentEnd),
        projects: value.projects.map(value => this.projectDomainToProject(value)),
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    });

    let employmentProjectsFlattened: ProjectParent[] = this.flattenProjects(employmentProjectParentList);

    let educationProjectParentList: ProjectParent[] = workHistory?.educationHistory.map(value => {
      return {
        parentName: value.institutionName,
        parentType: "Education - " + value.educationType.toLowerCase(),
        description: value.program,
        startDate: new Date(value.graduationPeriod),
        endDate: new Date(value.graduationPeriod),
        projects: value.projects.map(value => this.projectDomainToProject(value)),
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    });

    let educationProjectsFlattened = this.flattenProjects(educationProjectParentList);

    let hobbyProjectParentList: ProjectParent[] = workHistory?.hobbyProjects.map(value => {
      return {
        parentName: value.projectName,
        parentType: "Hobby",
        description: value.projectDescription,
        startDate: new Date(value.projectStart),
        endDate: value.projectEnd ? new Date(value.projectEnd) : undefined,
        projects: [],
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    })

    return [...employmentProjectParentList, ...employmentProjectsFlattened, ...educationProjectParentList,
      ...educationProjectsFlattened, ...hobbyProjectParentList]
      .sort((a, b) => a.startDate.getTime() - b.startDate.getTime());
  }

  private flattenProjects(employmentProjectParentList: ProjectParent[]): ProjectParent[] {
    let returnMe:ProjectParent[] = [];
    employmentProjectParentList.forEach(parent => {
      returnMe.push(...parent.projects.map(value => {
        return {
          parentName: value.projectName,
          parentType: parent.parentType + " project (" + parent.parentName + ")",
          description: value.projectDescription,
          startDate: value.startDate,
          endDate: value.endDate ? value.endDate : undefined,
          projects: [],
          projectParentSkills: value.projectSkills,
        }
      }));
    })
    return returnMe;
  }

  projectDomainToProject(projectDomain: ProjectDomain): Project {
    return {
      endDate: projectDomain.projectEnd ? new Date(projectDomain.projectEnd) : new Date(),
      projectSkills: projectDomain.techAndSkills.map(value => this.techSkillsDomainToTechSkills(value)),
      startDate: new Date(projectDomain.projectStart),
      projectId: projectDomain.projectId,
      projectName: projectDomain.projectName,
      projectDescription: projectDomain.projectDescription,
    };
  }

  techSkillsDomainToTechSkills(techSkillsDomain: SkillsDomain): TechSkills {
    return {
      techSkillId: techSkillsDomain.techSkillId,
      techSkillName: techSkillsDomain.techSkillName
    };
  }

  getEducationHistory(workHistory: WorkHistoryDomain | undefined): ProjectParent[] {
    if (!workHistory) {
      return [];
    }
    return workHistory.educationHistory.map(value => {
      return {
        parentName: value.institutionName,
        parentType: "Education",
        description: value.program,
        startDate: new Date(value.startDate),
        endDate: new Date(value.graduationPeriod),
        projects: value.projects.map(value => this.projectDomainToProject(value)),
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    })
  }

  getHobbyProjects(workHistory: WorkHistoryDomain | undefined):ProjectParent[] {
    if (!workHistory) {
      return [];
    }
    return [{
      parentName: "",
      parentType: "hobby",
      description: "",
      startDate: new Date(),
      endDate: new Date(),
      projects: workHistory.hobbyProjects.map(value => {
        return this.projectDomainToProject(value);
      }),
      projectParentSkills: [],
    }];
  }
}
