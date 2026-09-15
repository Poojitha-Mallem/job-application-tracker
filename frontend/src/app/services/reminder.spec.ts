import { TestBed } from '@angular/core/testing';

import { ReminderService } from './reminder';

describe('Reminder', () => {
  let service: ReminderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReminderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
