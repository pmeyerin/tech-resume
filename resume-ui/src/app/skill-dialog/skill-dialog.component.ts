import {Component, ElementRef, inject, Input} from '@angular/core';
import {ProjectParent} from "../project-parent";
import {ModalComponent} from "../modal/modal.component";
import {ModalService} from "../modal.service";

@Component({
  selector: 'app-skill-dialog',
  template: `
    <div id=triggerDiv class="custom-modal-hidden">
      <div class="custom-modal-body list-by-skill">
        <app-project-parent-card *ngFor="let projectParent of skillRelations" [projectParent]="projectParent"></app-project-parent-card>
      </div>
    </div>
  `,
  styleUrls: ['./skill-dialog.component.css', '../modal/modal.component.less'],
})
export class SkillDialogComponent extends ModalComponent {

  skillRelations: ProjectParent[] = [];

  constructor(protected override modalService: ModalService,
              el:ElementRef,) {
    super(modalService, el);
  }
  override open() {
    this.skillRelations = this.modalService.getData("skill-drilldown-modal").skillRelations;
    this.element.children[0].className = 'custom-modal-visible';
    document.body.classList.add('custom-modal-open');
    this.isOpen = true;
  }
}
