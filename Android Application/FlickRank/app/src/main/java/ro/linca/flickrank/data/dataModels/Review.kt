package ro.linca.flickrank.data.dataModels

data class Review(
	val source: String,
	val rating: String,
	val title: String,
	val reviewText: String,
	val containsSpoilers: Boolean
)