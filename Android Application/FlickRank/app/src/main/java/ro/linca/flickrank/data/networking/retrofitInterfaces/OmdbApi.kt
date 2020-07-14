package ro.linca.flickrank.data.networking.retrofitInterfaces

import retrofit2.http.GET
import retrofit2.http.Query
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.SearchResults
import ro.linca.flickrank.utils.Constants

interface OmdbApi
{
	@GET(Constants.omdbApiKeyHttpParameter)
	suspend fun getSearchResults(@Query("s") titleToSearch: String) : SearchResults
	@GET(Constants.omdbApiKeyHttpParameter)
	suspend fun getMovie(@Query("i") imdbId: String) : Movie
}

