import requests
import json
from movies.movie import Movie

def getPopularMovies():
    popularMoviesRequest = requests.get("https://api.themoviedb.org/3/movie/popular?api_key=74552cbf6cd1766b39290c6a54fc2441&language=en-US&page=1")
    if popularMoviesRequest.status_code != 200:
        return

    responseJson = popularMoviesRequest.json()
    popularMovies = []
    for item in responseJson['results']:
        # Get imdb ID
        imdbId = requests.get("https://api.themoviedb.org/3/movie/" + str(item['id']) + "/external_ids?api_key=74552cbf6cd1766b39290c6a54fc2441")
        imdbId = imdbId.json()['imdb_id']

        # Get the rest of the data
        title = item['title']
        year = item['release_date'].partition('-')[0]
        poster = "https://image.tmdb.org/t/p/w500" + str(item['poster_path'])
        plot = item['overview']
        movie = Movie(imdbId, title, year, poster, plot)
        popularMovies.append(movie)

    popularMoviesAsJson = json.dumps(popularMovies, default=lambda o: o.__dict__, sort_keys=False, indent=4)
    return popularMoviesAsJson

def findMovieByImdbId(imdbId):
    findMovieRequest = requests.get("https://api.themoviedb.org/3/find/" + str(imdbId) + "?api_key=74552cbf6cd1766b39290c6a54fc2441&language=en-US&external_source=imdb_id")
    if findMovieRequest.status_code != 200:
        return

    responseJson = findMovieRequest.json()

    if not responseJson["movie_results"]:
        if not responseJson['tv_results']:
            return -1
        else:
            tmdbId = responseJson['tv_results'][0]['id']
    else:
        tmdbId = responseJson['movie_results'][0]['id']

    return tmdbId

def findRecommendedMovies(imdbId):
    tmdbId = findMovieByImdbId(imdbId)
    recommendedMovies = []
    recommendedMoviesAsJson = json.dumps(recommendedMovies, default=lambda o: o.__dict__, sort_keys=False, indent=4)

    if tmdbId != -1:
        findRecommendedRequest = requests.get("https://api.themoviedb.org/3/movie/" + str(tmdbId) + "/recommendations?api_key=74552cbf6cd1766b39290c6a54fc2441&language=en-US&page=1")

        if findRecommendedRequest.status_code != 200:
            return recommendedMoviesAsJson

        responseJson = findRecommendedRequest.json()
        for item in responseJson['results']:
            # Get imdb ID
            newImdbId = requests.get("https://api.themoviedb.org/3/movie/" + str(
                item['id']) + "/external_ids?api_key=74552cbf6cd1766b39290c6a54fc2441")
            newImdbId = newImdbId.json()['imdb_id']

            # Get the rest of the data
            title = item['title']
            year = item['release_date'].partition('-')[0]
            poster = "https://image.tmdb.org/t/p/w500" + str(item['poster_path'])
            plot = item['overview']
            movie = Movie(newImdbId, title, year, poster,plot)
            recommendedMovies.append(movie)

    recommendedMoviesAsJson = json.dumps(recommendedMovies, default=lambda o: o.__dict__, sort_keys=False, indent=4)
    return recommendedMoviesAsJson