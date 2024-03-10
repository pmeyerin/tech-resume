import { Component } from '@angular/core';

@Component({
  selector: 'app-work-history-section',
  template: `
    <div class="work-history-wrapper">
      <b>Work history</b>
      <app-views-display></app-views-display>
      <router-outlet class="router-wrapper"></router-outlet>
    </div>
  `,
  styleUrls: ['./work-history-section.component.css'],
})
export class WorkHistorySectionComponent {

}
