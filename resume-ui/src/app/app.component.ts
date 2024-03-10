import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <main class="root-class">
      <app-home></app-home>
    </main>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'resume-ui';
}
