import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalMoviesComponent } from './external-movies.component';

describe('ExternalMoviesComponent', () => {
  let component: ExternalMoviesComponent;
  let fixture: ComponentFixture<ExternalMoviesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExternalMoviesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExternalMoviesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
