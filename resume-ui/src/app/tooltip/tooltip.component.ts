import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-tooltip',
  template: `
    <ng-container *ngIf="showsTooltip">
      <div
        class="tooltip"
        [style.top]="topPosition + 'px'"
        [style.left]="leftPosition + 'px'"
      >
        {{ tooltipText }}
      </div>
    </ng-container>

  `,
  styleUrls: ['./tooltip.component.css']
})
export class TooltipComponent implements OnInit {
  @Input() showsTooltip = true;
  @Input() tooltipText = 'Default tooltip text';
  // Add these ⤵️
  @Input() topPosition = 215;
  @Input() leftPosition = 400;

  constructor() { }

  ngOnInit(): void { }
}
