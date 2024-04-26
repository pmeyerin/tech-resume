import {Component, Input} from '@angular/core';
import {WorkHistoryDomain} from "../work-history-domain";

@Component({
  selector: 'app-work-history',
  template: `
    <app-cat-dash [workHistory]="workHistory"></app-cat-dash>
  `,
  styleUrls: ['./work-history.component.scss']
})
export class WorkHistoryComponent {
  @Input() workHistory!: WorkHistoryDomain | undefined;
}
