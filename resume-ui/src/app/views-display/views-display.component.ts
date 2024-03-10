import { Component } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-views-display',
  template: `
    <div class="views-section">
      <div class="views-display">
        View:
        <div class="tabs">
          <div [ngClass]="location.path() == '/chronological' || location.path() == '' ? 'tab-selected' : 'tab'" >
            <a class="nav-link" routerLink="/chronological">Chronological</a>
          </div>
          <div [ngClass]="location.path() == '/categorical' ? 'tab-selected' : 'tab'" >
            <a class="nav-link" routerLink="/categorical">Categorical</a>
          </div>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./views-display.component.css']
})
export class ViewsDisplayComponent {

  location: Location;
  constructor(location: Location) {
    this.location = location;
  }
}
