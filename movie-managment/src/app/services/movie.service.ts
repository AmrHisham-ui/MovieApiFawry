import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  private apiUrl = 'http://localhost:8080/api/movies';

  constructor(private http: HttpClient) {}

  getMovies(): Observable<Movie[]> {
    const token = sessionStorage.getItem('token');

    let httpOptions = {
      
      headers:new HttpHeaders({
        'Authorization': 'Bearer '+ token,
        'Content-Type': 'application/json'
      }),withCredentials: true
    };
    return this.http.get<Movie[]>(this.apiUrl,httpOptions);
  }

  addMovie(movie: Movie): Observable<Movie> {
    return this.http.post<Movie>(`${this.apiUrl}/add`, movie);
  }

  deleteMovie(movieId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/remove/${movieId}`);
  }


searchExternalMovies(title: string): Observable<any[]> {
  const token = sessionStorage.getItem('token');
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + token
    })
  };

  const url = `http://localhost:8080/api/movies/search/external?title=${title}`;
  console.log('🔍 MovieService: Making request to:', url);
  console.log('🔍 Token:', token);
  
  return this.http.get<any[]>(url, httpOptions);
}
addMovieByImdbId(imdbId: string): Observable<Movie> {
  const token = sessionStorage.getItem('token');
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + token,
      'Content-Type': 'application/json'
    })
  };

  return this.http.post<Movie>(`${this.apiUrl}/add`, null, {
    ...httpOptions,
    params: { imdbId }
  });
}

removeMovieByImdbId(imdbId: string): Observable<void> {
  const token = sessionStorage.getItem('token');
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + token
    })
  };

  return this.http.delete<void>(`${this.apiUrl}/remove`, {
    ...httpOptions,
    params: { imdbId }
  });
}
// Add these to your MovieService if they don't exist
addMoviesBatch(imdbIds: string[]): Observable<Movie[]> {
  const token = sessionStorage.getItem('token');
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + token,
      'Content-Type': 'application/json'
    })
  };

  return this.http.post<Movie[]>(`${this.apiUrl}/add/batch`, imdbIds, httpOptions);
}

deleteMoviesBatch(ids: number[]): Observable<void> {
  const token = sessionStorage.getItem('token');
  const httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Bearer ' + token,
      'Content-Type': 'application/json'
    })
  };

  return this.http.delete<void>(`${this.apiUrl}/remove/batch`, {
    ...httpOptions,
    body: ids
  });
}
rateMovie(id: number, rating: number) {
  const ratingDTO = {
    movieId: id,
    rating: rating
  };
  return this.http.post(`${this.apiUrl}/addrating`, ratingDTO);
}
getAverageRating(movieId: number) {
  return this.http.get<number>(`${this.apiUrl}/average/${movieId}`);
}

}




