import { TestBed } from '@angular/core/testing';

import { ResumeService } from './resume.service';

describe('ResumeServiceService', () => {
  let service: ResumeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResumeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
