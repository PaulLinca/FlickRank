package ro.linca.flickrank.utils

object Constants
{
	const val RC_SIGN_IN = 1998
	const val CAMERA_REQUEST_CODE = 1337
	const val PICK_IMAGE_REQUEST_CODE = 1338

	const val omdbApiBaseUrl = "https://www.omdbapi.com/"
	const val omdbApiKey = "21de0614"
	const val omdbApiKeyHttpParameter = "?apikey=$omdbApiKey"

	const val flickRankApiBaseUrl = "https://flickrank-backend.herokuapp.com/"
	const val flickRankReviewUrl = "reviews/"
	const val flickRankPopularUrl = "popular/"
	const val flickRankRecommendedUrl = "recommended/"
	const val flickRankImageSearchUrl = "imageSearch/"
}