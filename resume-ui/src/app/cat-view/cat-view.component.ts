import {Component, inject} from '@angular/core';
import {ResumeService} from "../resume.service";
import {WorkHistoryDomain} from "../work-history-domain";

@Component({
  selector: 'app-cat-view',
  template: `
    <div class="cat-list">
      <app-category-card [sectionTitle]="'Employment history'" [entries]="resumeService.getEmploymentHistory(workHistory)"></app-category-card>
      <app-category-card [sectionTitle]="'Education history'" [entries]="resumeService.getEducationHistory(workHistory)"></app-category-card>
      <app-category-card [sectionTitle]="'Hobby projects'" [entries]="resumeService.getHobbyProjects(workHistory)"></app-category-card>
    </div>
  `,
  styleUrls: ['./cat-view.component.css']
})
export class CatViewComponent {

  resumeService: ResumeService = inject(ResumeService)

  workHistory: WorkHistoryDomain | undefined =  undefined;
  //this.resumeService.getWorkHistory();

  subscribeToWorkHistory() {
    this.resumeService.getWorkHistory().subscribe(value => {
      this.workHistory = value
    })
  }

  constructor() {
    this.subscribeToWorkHistory()
  }
}
