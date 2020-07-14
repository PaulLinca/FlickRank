package ro.linca.flickrank.data.dataModels

data class Movie(
	val title: String?,
	val year: String?,
	val imdbID: String?,
	val type: String?,
	val poster: String?,
	val mpaRating: String?,
	val runtime: String?,
	val genres: List<String>?,
	val director: String?,
	val writers: List<String>?,
	val actors: List<String>?,
	val plot: String?,
	val awards: String?,
	val boxOfficeEarnings: String?,
	val ratings: List<Rating>?,
	val response: Boolean?
)