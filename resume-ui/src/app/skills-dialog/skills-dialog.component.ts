import {Component, Inject, inject} from '@angular/core';
import {ResumeService} from "../resume-service.service";
import {ProjectParent} from "../project-parent";
import {WorkerSkillRelationDomain} from "../worker-skill-relation-domain";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-skills-dialog',
  template: `
    <div class="dialog">
      <h2 mat-dialog-title>Activities related to <b class="highlight-skill">{{this.skillName}}</b></h2>
      <mat-dialog-content>
        <div class="relations-list">
          <div class="card-container" *ngFor="let relation of skillRelations">
            <mat-card class="dialog-card">
              <mat-card-header>
                <mat-card-title>
                  {{relation.parentName}} - {{relation.parentType}}
                </mat-card-title>
                <mat-card-subtitle>
                  {{relation.startDate | date:'yyyyMMM'}} - {{relation.endDate ? (relation.endDate | date:'yyyyMMM') : "Present"}}
                </mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <mat-chip-set>
                  <mat-chip *ngFor="let skill of relation.projectParentSkills">{{skill.techSkillName}}</mat-chip>
                </mat-chip-set>
              </mat-card-content>
            </mat-card>
          </div>
        </div>
      </mat-dialog-content>
    </div>
  `,
  styleUrls: ['./skills-dialog.component.scss']
})
export class SkillsDialogComponent {
  resumeService: ResumeService = inject(ResumeService);
  skillRelations: ProjectParent[] = [];
  skillName: string;

  constructor(@Inject(MAT_DIALOG_DATA) data: any) {
    console.log(data);
    this.openSkillDialog(data.skillId);
    this.skillName = data.skillName;
  }

  openSkillDialog(skillId: string) {
    this.subscribeToSkillRelation(skillId);
  }

  subscribeToSkillRelation(skillId: string) {
    this.resumeService.getSkillRelations(skillId).subscribe(value => {
    this.skillRelations = this.transformRelations(value)});
  }


  transformRelations(relations: WorkerSkillRelationDomain | undefined): ProjectParent[] {
    let employmentParents = relations?.employments?.map(value => {
      return {
        parentName: value.employmentName,
        parentType: "Employment",
        description: value.employmentDescription,
        startDate: new Date(value.employmentStart),
        endDate: new Date(value.employmentEnd),
        projects: [],
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      };
    })

    let educationParents = relations?.educations?.map(value => {
      return {
        parentName: value.institutionName,
        parentType: "Education",
        description: value.program,
        startDate: new Date(value.startDate),
        endDate: new Date(value.graduationPeriod),
        projects: [],
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      }
    });

    let projectParents:ProjectParent[] | undefined = relations?.projects?.map(value => {
      return {
        parentName: value.projectName,
        parentType: "Project",
        description: value.projectDescription,
        startDate: new Date(value.projectStart),
        endDate: value.projectEnd ? new Date(value.projectEnd) : new Date(),
        projects: [],
        projectParentSkills: value.techAndSkills.map(value => {
          return {
            techSkillId: value.techSkillId,
            techSkillName: value.techSkillName
          }
        })
      }
    });

    return [...(employmentParents ? employmentParents : []), ...(educationParents ? educationParents : []), ...(projectParents ? projectParents : [])]
      .sort((a, b) => a.startDate.getTime() - b.startDate.getTime());
  }
}
