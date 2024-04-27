import {Component, Inject, inject} from '@angular/core';
import {ResumeService} from "../resume-service.service";
import {WorkHistoryDomain} from "../work-history-domain";

@Component({
  selector: 'app-root-nav',
  templateUrl: './root-nav.component.html',
  styleUrls: ['./root-nav.component.scss']
})
export class RootNavComponent {
  resumeService: ResumeService = inject(ResumeService);
  workHistory: WorkHistoryDomain | undefined =  undefined;

  constructor() {
    this.subscribeToWorkHistory();
  }

  subscribeToWorkHistory() {
    this.resumeService.getWorkHistory().subscribe(value => {
      this.workHistory = value
    })
  }
}
