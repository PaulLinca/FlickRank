package ro.linca.flickrank.data.dataModels

data class SearchResults(
	val movies: List<Movie>?,
	val totalResults: String?,
	val errorMessage: String?,
	val response: Boolean
)