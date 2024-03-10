import {Component, inject} from '@angular/core';
import { RouterOutlet} from "@angular/router";
import {ResumeService} from "../resume.service";
import {WorkHistoryDomain} from "../work-history-domain";

@Component({
  selector: 'app-home',
  template: `
    <app-user-display [user]="workHistory ? workHistory.workerName : ''"></app-user-display>
    <app-worker-skills-section [skills]="workHistory ? workHistory.workerSkillEstimates : []"></app-worker-skills-section>
    <app-work-history-section></app-work-history-section>
  `,
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  resumeService: ResumeService = inject(ResumeService);
  workHistory:WorkHistoryDomain | undefined;

  constructor() {
    this.subscribeToWorkHistory();
  }

  subscribeToWorkHistory() {
    this.resumeService.getWorkHistory().subscribe(value => {
      this.workHistory = value
    })
  }
}
