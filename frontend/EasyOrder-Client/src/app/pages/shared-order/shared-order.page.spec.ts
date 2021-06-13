import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { SharedOrderPage } from './shared-order.page';

describe('SharedOrderPage', () => {
  let component: SharedOrderPage;
  let fixture: ComponentFixture<SharedOrderPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedOrderPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(SharedOrderPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
