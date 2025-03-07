import {Component, Input} from '@angular/core';
import {SkillEstimatesDomain} from "../skill-estimates-domain";
import {MatDialog} from "@angular/material/dialog";
import {SkillsDialogComponent} from "../skills-dialog/skills-dialog.component";

@Component({
  selector: 'app-skills-overview',
  template: `
    <div class="skills-list">
      <mat-chip-set>
        <mat-chip (click)="openDialog(skill.techSkillId, skill.techSkillName)"
                  *ngFor="let skill of skills"
                  matTooltip="Click to see all activities related to {{skill.techSkillName}}">{{skill.techSkillName}} | {{calculateSkillYears(skill.yearsSkillsEstimate)}}</mat-chip>
      </mat-chip-set>
    </div>
  `,
  styleUrls: ['./skills-overview.component.scss']
})
export class SkillsOverviewComponent {
  @Input() skills!: SkillEstimatesDomain[] | undefined;

  constructor(public dialog: MatDialog) {}

  openDialog(skillId:string, skillName:string) {
    this.dialog.open(SkillsDialogComponent, {
      data: {
        skillId: skillId,
        skillName: skillName
      },
      height: '80%',
      width: '60%',
    });
  }

  calculateSkillYears(estimate : number) {
    if (estimate < 365) {
      return "<1";
    }
    return Math.trunc(estimate/365);
  }
}
