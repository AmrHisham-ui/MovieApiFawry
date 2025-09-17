import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MovieService } from '../services/movie.service';
import { Movie } from '../models/movie';

@Component({
  selector: 'app-external-movies',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, InputTextModule],
  templateUrl: './external-movies.component.html',
  styleUrls: ['./external-movies.component.scss']
})
export class ExternalMoviesComponent {
  searchTerm: string = '';
  externalMovies: Movie[] = [];
  loading = false;
  selectedMovies: Movie[] = [];

  constructor(private movieService: MovieService) {}

searchMovies() {
  if (!this.searchTerm.trim()) return;

  console.log('🔍 Component: About to call searchExternalMovies');
  console.log('🔍 MovieService:', this.movieService);
  console.log('🔍 SearchTerm:', this.searchTerm);

  this.movieService.searchExternalMovies(this.searchTerm).subscribe({
    next: (movies) => {
      console.log('✅ Success:', movies);
      this.externalMovies = movies;
      this.selectedMovies = []; // clear selected movies when i do new search 
    },
    error: (err) => {
      console.error("❌ Error fetching external movies", err);
      console.error("❌ Error URL:", err.url);
    }
  });
}


  addMovie(imdbId: string) {
    this.movieService.addMovieByImdbId(imdbId).subscribe({
      next: (movie) => {
        alert(`Movie "${movie.title}" added successfully!`);
      },
      error: (err) => {
        console.error('Add failed', err);
        alert('Failed to add movie.');
      }
    });
  }
  removeMovie(imdbId: string) {
    this.movieService.removeMovieByImdbId(imdbId).subscribe({
      next: () => {
        alert("Movie removed successfully!");
      },
      error: (err) => {
        console.error('Remove failed', err);
        alert('Failed to remove movie.');
      }
    });
  }
addSelectedMovies() {
  if (this.selectedMovies.length === 0) {
    alert('Please select movies to add.');
    return;
  }

  // Filter out movies without imdbId and ensure they're strings
  const imdbIds = this.selectedMovies
    .map(movie => movie.imdbId)
    .filter(imdbId => imdbId !== undefined && imdbId !== null) as string[];
  
  if (imdbIds.length === 0) {
    alert('Selected movies do not have valid IMDb IDs.');
    return;
  }

  this.movieService.addMoviesBatch(imdbIds).subscribe({
    next: (movies) => {
      alert(`${movies.length} movies added successfully!`);
      this.selectedMovies = [];
    },
    error: (err) => {
      console.error('Batch add failed', err);
      alert('Failed to add selected movies.');
    }
  });
}

removeSelectedMovies() {
  if (this.selectedMovies.length === 0) {
    alert('Please select movies to remove.');
    return;
  }

  // Filter out movies without imdbId and ensure they're strings
  const imdbIds = this.selectedMovies
    .map(movie => movie.imdbId)
    .filter(imdbId => imdbId !== undefined && imdbId !== null) as string[];
  
  if (imdbIds.length === 0) {
    alert('Selected movies do not have valid IMDb IDs.');
    return;
  }

  // Call each remove individually
  let completed = 0;
  let errors = 0;
  
  imdbIds.forEach(imdbId => {
    this.movieService.removeMovieByImdbId(imdbId).subscribe({
      next: () => {
        completed++;
        if (completed + errors === imdbIds.length) {
          alert(`${completed} movies removed successfully!`);
          this.selectedMovies = [];
        }
      },
      error: (err) => {
        errors++;
        console.error('Remove failed for', imdbId, err);
        if (completed + errors === imdbIds.length) {
          alert(`${completed} movies removed, ${errors} failed.`);
          this.selectedMovies = [];
        }
      }
    });
  });
}

}
