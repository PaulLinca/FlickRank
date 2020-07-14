package ro.linca.flickrank.presentation.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.SearchResults
import ro.linca.flickrank.data.networking.services.OmdbRetrofitService
import ro.linca.flickrank.data.persistence.database.LocalDatabase
import ro.linca.flickrank.data.persistence.models.SearchQuery
import ro.linca.flickrank.presentation.BaseViewModel
import ro.linca.flickrank.presentation.presentationUtilities.AlertDialogEventInterface
import ro.linca.flickrank.presentation.presentationUtilities.LiveMessageEvent

class SearchResultsViewModel(application: Application) : BaseViewModel(application)
{
	companion object
	{
		const val TAG = "Search Results Page ViewModel"
	}

	val errorAlertEvent = LiveMessageEvent<AlertDialogEventInterface>()
	val isSearchSuccessful = MutableLiveData<Boolean>()
	val searchQuery = MutableLiveData<String>()
	val searchResultsAmount = MutableLiveData<String>()
	private val _searchResultsList = MutableLiveData<List<Movie>>()
	val searchResults: LiveData<List<Movie>>
		get() = _searchResultsList

	fun storeSearchQueryLocally()
	{
		launch {
			val query = searchQuery.value
			if(query != null)
			{
				val dao = LocalDatabase(getApplication()).searchQueryDao()
				dao.insertSearchQuery(SearchQuery(query))
				Log.d(TAG, "Persisted query")
			}
			else
			{
				Log.d(TAG, "Could not persist query.")
			}
		}
	}

	fun search()
	{
		launch {
			val omdbApi = OmdbRetrofitService.createSearchService()
			val searchResults = omdbApi.getSearchResults(searchQuery.value.toString())

			interpretRequestResult(searchResults)
		}
	}

	private fun interpretRequestResult(searchResults: SearchResults)
	{
		isSearchSuccessful.value = searchResults.response
		if(isSearchSuccessful.value!!)
		{
			_searchResultsList.value = searchResults.movies
			searchResultsAmount.value = searchResults.totalResults
		}
		else
		{
			_searchResultsList.value = arrayListOf()
			searchResultsAmount.value = "0"
			errorAlertEvent.sendEvent { showAlert(searchResults.errorMessage!!, "Try being more specific") }
		}
	}
}
