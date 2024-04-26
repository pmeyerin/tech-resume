import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-user-display',
  template: `
    <header>
      <div class="user-display">
        <div class="field">
          <div>Name:</div>
          <div>Contact phone:</div>
          <div>Contact email:</div>
        </div>
        <div class="values">
          <div>{{user}}</div>
          <div>{{phone}}</div>
          <div>{{email}}</div>
        </div>
      </div>
    </header>
  `,
  styleUrls: ['./user-display.component.scss']
})
export class UserDisplayComponent {
  @Input() user!: string | undefined;
  @Input() phone!: string |  undefined;
  @Input() email!: string | undefined;
}
