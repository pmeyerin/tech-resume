import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-chron-card',
  template: `
    <div class="chron-card">
      <div class="chron-summary">
        <p>{{projectParent.parentName}} - {{projectParent.parentType}}</p>
        <p>{{projectParent.startDate | date:'yyyyMMM'}} - {{projectParent.endDate ? (projectParent.endDate | date:'yyyyMMM') : "Present"}}</p>
      </div>
      <p>{{projectParent.description}}</p>
      <app-skills-list [skills]="projectParent.projectParentSkills"></app-skills-list>
    </div>`,
  styleUrls: ['./chron-card.component.css']
})
export class ChronCardComponent {
  @Input() projectParent!: ProjectParent;
}
