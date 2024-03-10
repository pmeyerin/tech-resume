import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-project-parent-card',
  template: `
    <div class="parent-card">
      <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.parentName}} - {{projectParent.parentType}}</p>
      <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.startDate | date:'yyyyMMM'}} - {{projectParent.endDate ? (projectParent.endDate | date:'yyyyMMM') : "Present"}}</p>
      <app-skills-list [skills]="projectParent.projectParentSkills"></app-skills-list>
      <p>{{projectParent.description}}</p>
      <app-project-card *ngFor="let project of projectParent.projects" [project]="project"></app-project-card>
    </div>
  `,
  styleUrls: ['./project-parent-card.component.css']
})
export class ProjectParentCardComponent {
  @Input() projectParent!: ProjectParent;
}
