package ro.linca.flickrank.presentation.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.Review
import ro.linca.flickrank.data.networking.services.FlickRankRetrofitService
import ro.linca.flickrank.data.networking.services.OmdbRetrofitService
import ro.linca.flickrank.data.persistence.database.LocalDatabase
import ro.linca.flickrank.data.persistence.models.SearchResult
import ro.linca.flickrank.presentation.BaseViewModel

class MoviePageViewModel(application: Application) : BaseViewModel(application)
{
	companion object
	{
		const val TAG = "Movie Page ViewModel"
	}
	var movieImdbId = MutableLiveData<String>()
	val movie = MutableLiveData<Movie>()
	val movieReviews = MutableLiveData<List<Review>>()
	var isMovieFetched = MutableLiveData(false)
	var areReviewsFetched = MutableLiveData(false)

	fun getMovie()
	{
		launch {
			isMovieFetched.value = true

			val omdbApi = OmdbRetrofitService.createMovieService()
			val movieResponse = omdbApi.getMovie(movieImdbId.value.toString())
			movie.postValue(movieResponse)

			isMovieFetched.value = false
		}
	}
	
	fun getMovieReviews()
	{
		launch {
			areReviewsFetched.value = true

			val flickRankApi = FlickRankRetrofitService.createBaseService()
			val movieReviewsResponse = flickRankApi.getReviews(movieImdbId.value.toString())
			movieReviews.postValue(movieReviewsResponse)

			areReviewsFetched.value = false
		}
	}

	fun storeMovieLocally()
	{
		launch {
			val movie = movie.value
			if(movie != null)
			{
				val dao = LocalDatabase(getApplication()).searchResultDao()
				dao.insertSearchResult(SearchResult(movie.imdbID!!, movie.title!!, movie.year!!, movie.poster!!, movie.plot!!))
				Log.d(TAG, "Persisted movie")
			}
			else
			{
				Log.d(TAG, "Could not persist movie.")
			}
		}
	}
}
