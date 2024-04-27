import {Component, Inject, inject} from '@angular/core';
import {ResumeService} from "../resume-service.service";
import {WorkHistoryDomain} from "../work-history-domain";
import {MatDialog} from "@angular/material/dialog";
import {SkillsDialogComponent} from "../skills-dialog/skills-dialog.component";
import {SpinnerDialogComponent} from "../spinner-dialog/spinner-dialog.component";

@Component({
  selector: 'app-root-nav',
  templateUrl: './root-nav.component.html',
  styleUrls: ['./root-nav.component.scss']
})
export class RootNavComponent {
  resumeService: ResumeService = inject(ResumeService);
  workHistory: WorkHistoryDomain | undefined =  undefined;

  constructor(public dialog: MatDialog)  {
    this.openDialog();
    this.subscribeToWorkHistory();
  }

  openDialog() {
    this.dialog.open(SpinnerDialogComponent, {
      data: { },
      height: '30%',
      width: '30%',
      disableClose: true,
      backdropClass: 'opacity',
    });
  }

  subscribeToWorkHistory() {
    this.resumeService.getWorkHistory().subscribe(value => {
      this.workHistory = value
      this.dialog.closeAll();
    })
  }
}
