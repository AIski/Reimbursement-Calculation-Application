import { TestBed } from '@angular/core/testing';

import { LimitsConfigurationService } from './limits-configuration.service';

describe('LimitsConfigurationService', () => {
  let service: LimitsConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LimitsConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
