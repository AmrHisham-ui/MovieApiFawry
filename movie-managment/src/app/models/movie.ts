export interface Movie {
    id?: number;  // Add this line (optional to handle new movies)
    title: string;
    year: string;
    genre: string;
    director: string;
   imdbId?: string
    rating?: number;
    averageRating?: number;
    poster?: string;
  }
  