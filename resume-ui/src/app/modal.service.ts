import { Injectable } from '@angular/core';
import {ModalComponent} from "./modal/modal.component";

//Modal logic shamelessly stolen from JasonWatmore.com
@Injectable({
  providedIn: 'root'
})
export class ModalService {
  private modals: ModalComponent[] = [];

  private dataMap: Map<String, any> = new Map<String, any>();
  constructor() { }

  add(modal: ModalComponent) {
    if (!modal.id || this.modals.find(value => value.id === modal.id)) {
      throw Error('modal must have a unique id');
    }
    this.modals.push(modal);
  }

  remove(modal: ModalComponent) {
    this.modals = this.modals.filter(value => value.id == modal.id);
  }

  open(id: string, data: any = {}) {
    const modal = this.modals.find(value => value.id === id);

    if (!modal) {
      throw Error(`modal ${id} not found`);
    }
    this.dataMap.set(id, data);
    modal.open();

  }

  getData(id: string) {
    return this.dataMap.get(id);
  }

  close() {
    const modal = this.modals.find(value => value.isOpen);
    this.dataMap.clear();
    modal?.close();
  }
}
