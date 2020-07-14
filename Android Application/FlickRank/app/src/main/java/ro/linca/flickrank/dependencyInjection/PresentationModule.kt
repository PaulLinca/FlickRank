package ro.linca.flickrank.dependencyInjection

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ro.linca.flickrank.presentation.home.HomeViewModel
import ro.linca.flickrank.presentation.login.LoginViewModel
import ro.linca.flickrank.presentation.model.MoviePageViewModel
import ro.linca.flickrank.presentation.search.SearchResultsViewModel
import ro.linca.flickrank.presentation.search.SearchViewModel
import ro.linca.flickrank.presentation.settings.SettingsViewModel

val presentationModule = module {
	viewModel { LoginViewModel() }
	viewModel { SearchViewModel(get()) }
	viewModel { HomeViewModel(get()) }
	viewModel { SettingsViewModel(get()) }
	viewModel { SearchResultsViewModel(get()) }
	viewModel { MoviePageViewModel(get()) }
}