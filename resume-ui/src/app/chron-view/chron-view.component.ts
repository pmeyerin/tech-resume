import {Component, inject} from '@angular/core';
import {ResumeService} from "../resume.service";
import {ProjectParent} from "../project-parent";

@Component({
  selector: 'app-chron-view',
  template: `
    <div class="chron-list">
      <app-chron-card *ngFor="let projectParent of workHistory" [projectParent]="projectParent"></app-chron-card>
    </div>
  `,
  styleUrls: ['./chron-view.component.css']
})
export class ChronViewComponent {
  resumeService: ResumeService = inject(ResumeService);
  workHistory:ProjectParent[] = [];

  constructor() {
    this.subscribeToWorkHistory();
  }

  subscribeToWorkHistory() {
    this.resumeService.getWorkHistory().subscribe(value => {
      this.workHistory = this.resumeService.parseWorkHistoryChronologically(value)
    })
  }
}
