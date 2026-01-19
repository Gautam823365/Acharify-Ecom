import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeDashBoardComponent } from './welcome-dash-board.component';

describe('WelcomeDashBoardComponent', () => {
  let component: WelcomeDashBoardComponent;
  let fixture: ComponentFixture<WelcomeDashBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WelcomeDashBoardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WelcomeDashBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
