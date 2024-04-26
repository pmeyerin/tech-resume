import {Component, Input} from '@angular/core';
import {WorkHistoryDomain} from "../work-history-domain";

@Component({
  selector: 'app-chron-history',
  template: `
    <app-chron-card></app-chron-card>
  `,
  styleUrls: ['./chron-history.component.scss']
})
export class ChronHistoryComponent {
  @Input() workHistory!: WorkHistoryDomain;

}
