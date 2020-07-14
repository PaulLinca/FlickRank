package ro.linca.flickrank.presentation.home

import android.app.Application
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.networking.services.FlickRankRetrofitService
import ro.linca.flickrank.data.persistence.database.LocalDatabase
import ro.linca.flickrank.data.persistence.models.SearchResult
import ro.linca.flickrank.presentation.BaseViewModel

class HomeViewModel(application: Application) : BaseViewModel(application)
{
	private val _recentlyViewed = MutableLiveData<List<SearchResult>>()
	val recentlyViewed: LiveData<List<SearchResult>>
		get() = _recentlyViewed

	private val _popularMovies = MutableLiveData<List<SearchResult>>()
	val popularMovies: LiveData<List<SearchResult>>
		get() = _popularMovies

	private val _recommendedMovies = MutableLiveData<List<SearchResult>>()
	val recommendedMovies: LiveData<List<SearchResult>>
		get() = _recommendedMovies

	val recommendationsBasedOn = MutableLiveData<String>()
	var areRecommendedLoaded = false

	var isBusy = MutableLiveData(false)
	var areRecommendationsLoading = MutableLiveData(false)

	init
	{
		getPopularMovies()
	}

	fun getRecent()
	{
		launch {
			val dao = LocalDatabase(getApplication()).searchResultDao()
			var recentList = dao.getAllSearchResults()
			recentList = recentList.reversed()
			_recentlyViewed.postValue(recentList)
		}
	}

	private fun getPopularMovies()
	{
		launch {
			isBusy.value = true

			val flickRankApi = FlickRankRetrofitService.createBaseService()
			val popularMoviesResponse = flickRankApi.getPopularMovies()
			_popularMovies.postValue(popularMoviesResponse)

			isBusy.value = false
		}
	}
	
	fun getRecommendedMovies(imdbId: String, title: String, year: String)
	{
		launch {
			areRecommendationsLoading.value = true

			val flickRankApi = FlickRankRetrofitService.createBaseService()
			val recommendedMoviesResponse = flickRankApi.getRecommendedMovies(imdbId)

			_recommendedMovies.postValue(recommendedMoviesResponse)
			recommendationsBasedOn.postValue(title + " (" + year + ")")

			areRecommendationsLoading.value = false
		}
		areRecommendedLoaded = true
	}
}
