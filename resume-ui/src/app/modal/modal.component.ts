import {Component, ElementRef, Input, OnDestroy, OnInit} from '@angular/core';
import {ModalService} from "../modal.service";

@Component({
  selector: 'app-modal',
  template: `
    <div id=triggerDiv class="custom-modal-hidden">
      <div class="custom-modal-body">
        <ng-content></ng-content>
      </div>
    </div>
  `,
  styleUrls: ['./modal.component.less'],
})
export class ModalComponent implements OnInit, OnDestroy {
  @Input() id?: string;
  isOpen = false;
  protected element: any;

  constructor(protected modalService: ModalService,
              private el:ElementRef,) {
    this.element = el.nativeElement;
  }

  ngOnInit() {
    this.modalService.add(this);
    document.body.appendChild(this.element);
    this.element.addEventListener('click', (el:any) => {
      if (el.target.className === 'custom-modal-visible') {
        this.close();
      }
    });
  }

  ngOnDestroy() {
    this.modalService.remove(this);
    this.element.remove();
  }

  open() {
    this.element.children[0].className = 'custom-modal-visible';
    document.body.classList.add('custom-modal-open');
    this.isOpen = true;
  }

  close() {
    // this.element.display = 'none';
    this.element.children[0].className = 'custom-modal-hidden';
    document.body.classList.remove('custom-modal-open');
    this.isOpen = false;
  }

}
