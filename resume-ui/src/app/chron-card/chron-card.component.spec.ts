import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChronCardComponent } from './chron-card.component';

describe('ChronCardComponent', () => {
  let component: ChronCardComponent;
  let fixture: ComponentFixture<ChronCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChronCardComponent]
    });
    fixture = TestBed.createComponent(ChronCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
