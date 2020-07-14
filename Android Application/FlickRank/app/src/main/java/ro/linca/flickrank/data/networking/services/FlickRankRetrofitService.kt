package ro.linca.flickrank.data.networking.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ro.linca.flickrank.data.networking.retrofitInterfaces.FlickRankApi
import ro.linca.flickrank.utils.Constants
import java.util.concurrent.TimeUnit

class FlickRankRetrofitService
{
	companion object
	{
		private val okHttpClient = OkHttpClient.Builder()
			.readTimeout(60, TimeUnit.SECONDS)
			.connectTimeout(60, TimeUnit.SECONDS)
			.build()

		private val retrofitWithSearchConverter = Retrofit.Builder()
			.baseUrl(Constants.flickRankApiBaseUrl)
			.addConverterFactory(ScalarsConverterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.client(okHttpClient)
			.build()

		fun createBaseService(): FlickRankApi
		{
			return retrofitWithSearchConverter
				.create(FlickRankApi::class.java)
		}
	}
}
