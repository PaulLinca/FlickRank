package ro.linca.flickrank.data.networking.retrofitInterfaces

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ro.linca.flickrank.data.dataModels.MovieReviewData
import ro.linca.flickrank.data.dataModels.Review
import ro.linca.flickrank.data.dataModels.SerializedImage
import ro.linca.flickrank.data.persistence.models.SearchResult
import ro.linca.flickrank.utils.Constants

interface FlickRankApi
{
	@GET(Constants.flickRankReviewUrl + "{imdbId}")
	suspend fun getReviews(@Path("imdbId") imdbId: String) : List<Review>

	@GET(Constants.flickRankPopularUrl)
	suspend fun getPopularMovies(): List<SearchResult>

	@GET(Constants.flickRankRecommendedUrl + "{imdbId}")
	suspend fun getRecommendedMovies(@Path("imdbId") imdbId: String): List<SearchResult>

	@POST(Constants.flickRankImageSearchUrl)
	suspend fun imageSearch(@Body body: SerializedImage): String
}