import {Component, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-cat-card',
  template: `
    <div>
      vdasdfa
    </div>
      <mat-card class="dashboard-card">
<!--        <mat-card-header>-->
<!--          <mat-card-title>-->
<!--            <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.parentName}} - {{projectParent.parentType}}</p>-->
<!--          </mat-card-title>-->
<!--          <mat-card-subtitle>-->
<!--            <p *ngIf="projectParent.parentType.toLowerCase() != 'hobby'">{{projectParent.startDate | date:'yyyyMMM'}} - {{projectParent.endDate ? (projectParent.endDate | date:'yyyyMMM') : "Present"}}</p>-->
<!--          </mat-card-subtitle>-->
<!--        </mat-card-header>-->
<!--        <mat-card-content class="dashboard-card-content">-->
<!--          <mat-chip-set>-->
<!--            <mat-chip *ngFor="let techSkill of projectParent.projectParentSkills">{{techSkill.techSkillName}}</mat-chip>-->
<!--          </mat-chip-set>-->
<!--          <p class="blog-para">{{projectParent.description}}</p>-->
<!--          <div class='internalMatGrid' >-->
<!--            <mat-grid-list cols="1" rowHeight="2:1">-->
<!--              <mat-grid-tile *ngFor="let project of projectParent.projects">-->
<!--                {{console.log(project)}}-->
<!--          <div>-->
<!--            df;aklsjf;laksj;falkj-->
<!--          </div>-->
<!--            <div class="projects-div">-->
<!--              <mat-card class="project-card" *ngFor="let project of projectParent.projects">-->
<!--                <mat-card-content>;laskdjf;lasdf</mat-card-content>-->
<!--              </mat-card>-->
<!--            </div>-->
<!--                <mat-card class="project-card" *ngFor="let project of projectParent.projects">-->
<!--                  <mat-card-header>-->
<!--                    <mat-card-title>-->
<!--                      {{project.projectName}}-->
<!--                    </mat-card-title>-->
<!--                    <mat-card-subtitle>-->
<!--                      {{project.startDate | date:'yyyyMMM'}} - {{project.endDate | date:'yyyyMMM'}}-->
<!--                    </mat-card-subtitle>-->
<!--                  </mat-card-header>-->
<!--                  <mat-chip-set>-->
<!--                    <mat-chip *ngFor="let projSkill of project.projectSkills">{{projSkill.techSkillName}}</mat-chip>-->
<!--                  </mat-chip-set>-->
<!--                  <mat-card-content>-->
<!--                    <p class="blog-para">{{project.projectDescription}}</p>-->
<!--                  </mat-card-content>-->
<!--                </mat-card>-->
<!--              </mat-grid-tile>-->
<!--            </mat-grid-list>-->
<!--          </div>-->
<!--        </mat-card-content>-->
      </mat-card>
  `,
  styleUrls: ['./cat-card.component.scss']
})
export class CatCardComponent {
  @Input() projectParent!: ProjectParent;
  protected readonly console = console;
}
