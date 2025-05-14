import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaPatientComponent } from './crea-patient.component';

describe('CreaPatientComponent', () => {
  let component: CreaPatientComponent;
  let fixture: ComponentFixture<CreaPatientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreaPatientComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreaPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
