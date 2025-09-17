import { Component, OnInit } from '@angular/core';
import { MovieService } from '../services/movie.service';
import { AuthService } from '../auth/auth.service';
import { Movie } from '../models/movie';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms'; // ✅ Import FormsModule for ngModel
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ChangeDetectorRef } from '@angular/core';
import { RatingModule } from 'primeng/rating';
import { CardModule } from 'primeng/card';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [MenubarModule, CommonModule, ButtonModule, FormsModule,TableModule,DialogModule,RatingModule,CardModule]
})
export class DashboardComponent implements OnInit {
  menuItems: MenuItem[] = [];
  currentUser: any = null;
  movies: Movie[] = [];
  filteredMovies: Movie[] = [];
  searchTerm: string = '';
  showAddMovieDialog = false;
  selectedMovies: Movie[] = []; // ✅ Ensure this is initialized
  
  newMovie: Movie = { title: '', year: '', genre: '', director: '' };
  

  constructor(private authService: AuthService, private movieService: MovieService,private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      console.log("Current User Data:", this.currentUser);
      this.cdr.detectChanges();
      this.currentUser = user ? user : { role: 'Guest' };
      this.loadMovies();
    });

    this.menuItems = [
      { label: 'Home', icon: 'pi pi-home', routerLink: ['/dashboard'] },
      { label: 'Movies', icon: 'pi pi-video' },
      { label: 'Profile', icon: 'pi pi-user' }
    ];
  }

 loadMovies() {
  this.movieService.getMovies().subscribe((movies) => {
    this.movies = movies;
    this.filteredMovies = movies;

    // fetch average rating for each movie
    this.movies.forEach(movie => {
      if (movie.id) {
        this.movieService.getAverageRating(movie.id).subscribe(avg => {
          movie.averageRating = avg;  // attach new property
        });
      }
    });
  });
}


  addMovie() {
    this.movieService.addMovie(this.newMovie).subscribe(() => {
      this.showAddMovieDialog = false;
      this.loadMovies();
    });
  }

  deleteMovie(movie: Movie) {
    if (movie.id && confirm(`Are you sure you want to delete "${movie.title}"?`)) {
      this.movieService.deleteMovie(movie.id).subscribe(() => {
        this.loadMovies();
      });
    }
  }

  deleteSelectedMovies() {
    if (this.selectedMovies.length > 0 && confirm(`Are you sure you want to delete selected movies?`)) {
      this.selectedMovies.forEach(movie => {
        if (movie.id) {
          this.movieService.deleteMovie(movie.id).subscribe(() => {
            this.loadMovies();
          });
        }
      });
      this.selectedMovies = [];
    }
  }

  filterMovies() {
    this.filteredMovies = this.searchTerm.trim() ? this.movies.filter(movie =>
      movie.title.toLowerCase().includes(this.searchTerm.toLowerCase())
    ) : this.movies;
  }
  logout() {
    this.authService.logout();
    window.location.href = '/auth/login';
  }




addExternalMovie(movie: Movie) {
  this.movieService.addMovie(movie).subscribe(() => {
    this.loadMovies(); // refresh local DB movies
  });
}
rateMovie(movie: Movie, rating: number) {
  this.movieService.rateMovie(movie.id!, rating).subscribe(() => {
    this.loadMovies(); // ✅ Refresh movies with new averages
  });
}

}
