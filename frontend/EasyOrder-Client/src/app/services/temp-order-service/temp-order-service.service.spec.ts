import { TestBed } from '@angular/core/testing';

import { TempOrderService } from './temp-order-service.service';

describe('TempOrderService', () => {
  let service: TempOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TempOrderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
