import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeHistoComponent } from './liste-histo.component';

describe('ListeHistoComponent', () => {
  let component: ListeHistoComponent;
  let fixture: ComponentFixture<ListeHistoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListeHistoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListeHistoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
