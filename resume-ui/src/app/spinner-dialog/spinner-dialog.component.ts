import { Component } from '@angular/core';

@Component({
  selector: 'app-spinner-dialog',
  template: `
    <div class="dialog">
      <span>
        Please wait. The web service is initializing. This may take up to a minute...
      </span>
      <mat-spinner></mat-spinner>
    </div>
  `,
  styleUrls: ['./spinner-dialog.component.scss']
})
export class SpinnerDialogComponent {

}
