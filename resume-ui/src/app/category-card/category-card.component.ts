import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-category-card',
  template: `
    <div class="category-card-wrapper">
      <b>{{sectionTitle}}</b>
      <br/>
      <app-project-parent-card *ngFor="let projectParent of entries" [projectParent]="projectParent"></app-project-parent-card>
    </div>
  `,
  styleUrls: ['./category-card.component.css']
})
export class CategoryCardComponent {
  @Input() sectionTitle!: string;
  @Input() entries!: ProjectParent[];
}
