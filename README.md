# MediaConnect

## App Description

Media Connect is a JavaFX application that allows users to search for movies and TV shows, then view detailed information about them including cast, streaming availability, and related details. The app combines information from two APIs in a meaningful way - first searching for basic movie/show information and then enriching it with additional details about where to watch it and who's in it.

When users enter a movie title, the app searches for matches and displays them in a searchable list. When a user selects a movie/show from the list, the app automatically fetches and displays extended information including the overview, cast details, and streaming availability. This integration saves users from having to look up this information separately across multiple services.

## APIs

### API 1: OMDB API

http://www.omdbapi.com/?apikey=[key]&s=[searchterm]

Rate limit: 30 requests per minute
Provides basic movie/show information including titles, years, and posters.

### API 2: TMDB API

https://api.themoviedb.org/3/find/{imdb_id}?api_key=[key]&external_source=imdb_id

Rate limit: 40 requests per 10 seconds
Provides detailed show information including cast, streaming providers, and extended metadata.

## Retrospect

Working on this project, I learned several new concepts. The most significant was implementing proper rate limiting for API requests. I created a RateLimiter class that uses a queue to track request timestamps and ensures the application stays within API usage limits. I also learned about using CompletableFuture for handling multiple concurrent API requests efficiently, which significantly improved the app's performance when fetching detailed information.

If starting over, I would approach the TV show handling differently from the beginning. Currently, the application was initially built with movies in mind and later adapted for TV shows, which led to some complications with handling different poster orientations and metadata fields. I would design the data models from the start to handle both content types equally well.

I would also implement a caching system for frequently accessed data to reduce API calls and improve response times. Additionally, I would create a more robust error handling system specifically designed for different types of API failures rather than adapting it as issues were discovered.
