import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SigmaGraphComponent } from './sigma-graph.component';

describe('SigmaGraphComponent', () => {
  let component: SigmaGraphComponent;
  let fixture: ComponentFixture<SigmaGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SigmaGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SigmaGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
