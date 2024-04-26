import {Component, inject, Input} from '@angular/core';
import {WorkHistoryDomain} from "../work-history-domain";
import {ResumeService} from "../resume-service.service";

@Component({
  selector: 'app-cat-dash',
  template: `
    <div class="grid-container">
      <h1 class="mat-h1">Employment History</h1>
      <app-category-section [projectParents]="resumeService.getEmploymentHistory(workHistory)"></app-category-section>
      <br/>
      <h1 class="mat-h1">Education History</h1>
      <app-category-section [projectParents]="resumeService.getEducationHistory(workHistory)"></app-category-section>
      <br/>
      <h1 class="mat-h1">Hobby Projects</h1>
      <app-category-section [projectParents]="resumeService.getHobbyProjects(workHistory)"></app-category-section>
    </div>
  `,
  styleUrls: ['./cat-dash.component.scss']
})
export class CatDashComponent {
  @Input() workHistory!: WorkHistoryDomain | undefined;
  resumeService: ResumeService = inject(ResumeService);
}
