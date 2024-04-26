import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-project-parent',
  template: `
    <div class="parent-card">
      <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.parentName}} - {{projectParent.parentType}}</p>
      <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.startDate | date:'yyyyMMM'}} - {{projectParent.endDate ? (projectParent.endDate | date:'yyyyMMM') : "Present"}}</p>
<!--      <app-skills-list [skills]="projectParent.projectParentSkills"></app-skills-list>-->
      <mat-chip-set>
        <mat-chip *ngFor="let skill of projectParent.projectParentSkills">{{skill.techSkillName}}</mat-chip>
      </mat-chip-set>
      <p>{{projectParent.description}}</p>
    </div>
  `,
  styleUrls: ['./project-parent.component.scss']
})
export class ProjectParentComponent {
  @Input() projectParent!: ProjectParent;
}
