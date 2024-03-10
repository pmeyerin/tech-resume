import {Component, inject, Input} from '@angular/core';
import {SkillEstimatesDomain} from "../skill-estimates-domain";
import {ModalService} from "../modal.service";
import {WorkHistoryDomain} from "../work-history-domain";
import {ResumeService} from "../resume.service";
import {WorkerSkillRelationDomain} from "../worker-skill-relation-domain";
import {ProjectParent} from "../project-parent";
import {TechSkills} from "../tech-skills";

@Component({
  selector: 'app-worker-skills-section',
  template: `
    <div class="skills-section-wrapper">
      <app-tooltip
        [showsTooltip]="showsTooltip"
        [tooltipText]="tooltipText"
        [topPosition]="topPosition"
        [leftPosition]="leftPosition">
      </app-tooltip>
      <div class="summary-header">
        <b>Technology and skills summary</b>
        <a href="#"  class="summary-help" (mouseover)="onHover('Estimated years of experience with each given skill or technology. The estimate is calculated by adding up non-overlapping times a skill or technology was used as part of an employment or project, or was being used in education.', $event)" (mouseout)="onMouseout()">?</a>
      </div>
      <div class="skills-section">
        <p class="skill-estimate-block" (mouseover)="onHover('Click to see all projects, employments, or trainings related to this skill', $event)" (mouseout)="onMouseout()"
           *ngFor="let skill of skills" (click)="openSkillDialog(skill)">{{skill.techSkillName}} | {{calculateSkillYears(skill.yearsSkillsEstimate)}}</p>
      </div>
    </div>
    <app-skill-dialog id="skill-drilldown-modal"></app-skill-dialog>
  `,
  styleUrls: ['./worker-skills-section.component.css']
})
export class WorkerSkillsSectionComponent {
  @Input() skills!: SkillEstimatesDomain[];
  @Input() workHistory!: WorkHistoryDomain;
  modalService: ModalService = inject(ModalService);
  resumeService:ResumeService = inject(ResumeService);
  showsTooltip = false;
  tooltipText = 'This is default parent component text';
  topPosition: any;
  leftPosition: any;

  calculateSkillYears(estimate : number) {
    if (estimate < 365) {
      return "<1";
    }
    return Math.trunc(estimate/365);
  }

  openSkillDialog(skill: SkillEstimatesDomain) {
    this.subscribeToSkillRelation({techSkillId: skill.techSkillId, techSkillName: skill.techSkillName});
  }

  subscribeToSkillRelation(skill: TechSkills) {
    this.resumeService.getSkillRelations(skill).subscribe(value => {
      this.modalService.open('skill-drilldown-modal', {skill: skill, skillRelations: this.transformRelations(value)});
    })
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

  onHover(tooltipText: string, e: MouseEvent) {
    this.showsTooltip = true;
    this.tooltipText = tooltipText;
    this.topPosition = e.clientY;
    this.leftPosition = e.clientX;
  }
  onMouseout() {
    this.showsTooltip = false;
    this.tooltipText = '';
    this.topPosition = null;
    this.leftPosition = null;
  }
}
