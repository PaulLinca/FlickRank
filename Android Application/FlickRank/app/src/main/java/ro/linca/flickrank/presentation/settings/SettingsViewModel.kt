package ro.linca.flickrank.presentation.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import ro.linca.flickrank.data.persistence.database.LocalDatabase
import ro.linca.flickrank.presentation.BaseViewModel

class SettingsViewModel(application: Application) : BaseViewModel(application)
{
	var username = MutableLiveData<String>().apply {
		value = FirebaseAuth.getInstance().currentUser?.displayName ?: "Not logged in"
	}

	fun clearViewedHistory()
	{
		launch {
			val dao = LocalDatabase(getApplication()).searchResultDao()
			dao.deleteHistory()
		}
	}

	fun clearSearchHistory()
	{
		launch {
			val dao = LocalDatabase(getApplication()).searchQueryDao()
			dao.deleteAllQueries()
		}
	}
}
