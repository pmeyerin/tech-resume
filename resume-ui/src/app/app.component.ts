import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="whole-page">
      <app-root-nav ></app-root-nav>
    </div>
  `,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

}
