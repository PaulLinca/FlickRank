package ro.linca.flickrank.presentation.search

import android.app.Application
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ro.linca.flickrank.data.dataModels.SerializedImage
import ro.linca.flickrank.data.networking.services.FlickRankRetrofitService
import ro.linca.flickrank.data.persistence.database.LocalDatabase
import ro.linca.flickrank.data.persistence.models.SearchQuery
import ro.linca.flickrank.presentation.BaseViewModel
import java.io.ByteArrayOutputStream

class SearchViewModel(application: Application) : BaseViewModel(application)
{
	var imageSearchResponse = MutableLiveData<String>()
	var isManualSearchEnabled = MutableLiveData(false)
	private val _searchHistoryList = MutableLiveData<List<SearchQuery>>()
	val searchHistory: LiveData<List<SearchQuery>>
		get() = _searchHistoryList

	fun getSearchHistory()
	{
		launch {
			val dao = LocalDatabase(getApplication()).searchQueryDao()
			var localSearchHistory = dao.getAllSearchQueries()
			localSearchHistory = localSearchHistory.reversed()
			_searchHistoryList.value = localSearchHistory
		}
	}

	fun deleteSearchItem(queryString: String)
	{
		launch {
			val dao = LocalDatabase(getApplication()).searchQueryDao()
			dao.deleteQuery(queryString)
			var localSearchHistory = dao.getAllSearchQueries()
			localSearchHistory = localSearchHistory.reversed()
			_searchHistoryList.value = localSearchHistory
		}
	}

	fun imageSearch(bmp: Bitmap)
	{
		launch {
			var byteArrayOutputStream = ByteArrayOutputStream()
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
			val bmpString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)

			val flickRankApi = FlickRankRetrofitService.createBaseService()
			val response = flickRankApi.imageSearch(SerializedImage(bmpString))
			imageSearchResponse.value = response
		}
	}
}
