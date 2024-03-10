import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-user-display',
  template: `
    <header>
      <div class="user-display">
        {{user}}
      </div>
    </header>
  `,
  styleUrls: ['./user-display.component.css']
})
export class UserDisplayComponent {
  @Input() user!: string;

}
