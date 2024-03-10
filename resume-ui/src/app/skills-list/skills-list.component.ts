import {Component, Input} from '@angular/core';
import {SkillsDomain} from "../skills-domain";

@Component({
  selector: 'app-skills-list',
  template: `
  <div [ngClass]="skills && skills.length > 0 ? 'skills-list' : 'no-skills'">
    <p class="skill-block" *ngFor="let skill of skills">{{skill.techSkillName}}</p>
  </div>`,
  styleUrls: ['./skills-list.component.css']
})
export class SkillsListComponent {
  @Input() skills!: SkillsDomain[];

}
