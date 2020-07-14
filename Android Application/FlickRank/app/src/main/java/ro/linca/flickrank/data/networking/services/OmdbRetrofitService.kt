package ro.linca.flickrank.data.networking.services

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.SearchResults
import ro.linca.flickrank.data.networking.customDeserializers.MovieDeserializer
import ro.linca.flickrank.data.networking.customDeserializers.SearchResultsDeserializer
import ro.linca.flickrank.data.networking.retrofitInterfaces.OmdbApi
import ro.linca.flickrank.utils.Constants
import java.util.concurrent.TimeUnit

class OmdbRetrofitService
{
	companion object
	{
		private val okHttpClient = OkHttpClient.Builder()
			.readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS)
			.build()
		private val gsonSearchResultsConverter = GsonBuilder()
			.registerTypeAdapter(SearchResults::class.java, SearchResultsDeserializer())
			.create()
		private val gsonMovieConverter = GsonBuilder()
			.registerTypeAdapter(Movie::class.java, MovieDeserializer())
			.create()

		private val retrofitWithSearchConverter = Retrofit.Builder()
			.baseUrl(Constants.omdbApiBaseUrl)
			.addConverterFactory(GsonConverterFactory.create(gsonSearchResultsConverter))
			.client(okHttpClient)
			.build()
		private val retrofitWithMovieConverter = Retrofit.Builder()
			.baseUrl(Constants.omdbApiBaseUrl)
			.addConverterFactory(GsonConverterFactory.create(gsonMovieConverter))
			.client(okHttpClient)
			.build()

		fun createSearchService(): OmdbApi
		{
			return retrofitWithSearchConverter
				.create(OmdbApi::class.java)
		}

		fun createMovieService(): OmdbApi
		{
			return retrofitWithMovieConverter
				.create(OmdbApi::class.java)
		}
	}
}