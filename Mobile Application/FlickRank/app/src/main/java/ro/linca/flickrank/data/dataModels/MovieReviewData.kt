package ro.linca.flickrank.data.dataModels

data class MovieReviewData(
	val movieTitle: String,
	val overallRating: String,
	val numberOfReviews: String,
	val allReviews: List<Review>
)