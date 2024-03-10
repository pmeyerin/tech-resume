import {Component, Input} from '@angular/core';
import {Project} from "../project";

@Component({
  selector: 'app-project-card',
  template: `
    <div class="project-card-wrapper">
      <div class="project-card-spacer"></div>
      <div class="project-card">
        <div class="project-summary">
          <p>{{project.projectName}}</p>
          <p>{{project.startDate | date:'yyyyMMM'}} - {{project.endDate | date:'yyyyMMM'}}</p>
        </div>

        <p>{{project.projectDescription}}</p>
        <app-skills-list [skills]="project.projectSkills"></app-skills-list>
      </div>
    </div>
  `,
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent {
  @Input() project!: Project;
}
