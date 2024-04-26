import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-category-section',
  template: `
    <div fxLayout="column" fxLayoutAlign="space-between stretch" fxLayoutGap="10px">
      <div *ngFor="let projectParent of projectParents" style="padding-bottom: 5px">
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.parentName}} - {{projectParent.parentType}}</p>
            </mat-card-title>
            <mat-card-subtitle>
              <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.startDate | date:'yyyyMMM'}} - {{projectParent.endDate ? (projectParent.endDate | date:'yyyyMMM') : "Present"}}</p>
            </mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <mat-chip-set>
              <mat-chip *ngFor="let skill of projectParent.projectParentSkills">{{skill.techSkillName}}</mat-chip>
            </mat-chip-set>
            <p class="blog-para">{{projectParent.description}}</p>
            <div style="display: flex; flex-direction: column; align-items: end">
              <div *ngFor="let project of projectParent.projects" style="padding-bottom: 5px; align-content: end; width: 80%" >
                <mat-card class="project-card">
                  <mat-card-header>
                    <mat-card-title>
                      {{project.projectName}}
                    </mat-card-title>
                    <mat-card-subtitle>
                      {{project.startDate | date:'yyyyMMM'}} - {{project.endDate ? (project.endDate | date:'yyyyMMM') : "Present"}}
                    </mat-card-subtitle>
                  </mat-card-header>
                  <mat-card-content>
                    <p class="blog-para">{{project.projectDescription}}</p>
                    <mat-chip-set>
                      <mat-chip *ngFor="let projSkill of project.projectSkills">{{projSkill.techSkillName}}</mat-chip>
                    </mat-chip-set>
                  </mat-card-content>
                </mat-card>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styleUrls: ['./category-section.component.scss']
})
export class CategorySectionComponent {
  @Input() projectParents!: ProjectParent[] | undefined;
}
