import { TestBed } from '@angular/core/testing';

import { InterviewRound } from './interview-round';

describe('InterviewRound', () => {
  let service: InterviewRound;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterviewRound);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
