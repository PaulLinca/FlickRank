package ro.linca.flickrank.data.networking.customDeserializers

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.Rating
import java.lang.reflect.Type

class MovieDeserializer : JsonDeserializer<Movie>
{
	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): Movie
	{
		val movieJsonObject = json?.asJsonObject
		val ratings = movieJsonObject?.get("Ratings")?.asJsonArray
		return Movie(
			movieJsonObject?.get("Title")?.asString,
			movieJsonObject?.get("Year")?.asString,
			movieJsonObject?.get("imdbID")?.asString,
			movieJsonObject?.get("Type")?.asString,
			movieJsonObject?.get("Poster")?.asString,
			movieJsonObject?.get("Rated")?.asString,
			movieJsonObject?.get("Runtime")?.asString,
			separateStringByComma(movieJsonObject?.get("Genre")?.asString),
			movieJsonObject?.get("Director")?.asString,
			separateStringByComma(movieJsonObject?.get("Writer")?.asString),
			separateStringByComma(movieJsonObject?.get("Actors")?.asString),
			movieJsonObject?.get("Plot")?.asString,
			movieJsonObject?.get("Awards")?.asString,
			movieJsonObject?.get("BoxOffice")?.asString,
			getRatings(ratings, context),
			movieJsonObject?.get("Response")?.asBoolean
		)
	}

	private fun separateStringByComma(stringToSeparate: String?): List<String>?
	{
		return stringToSeparate?.split(", ")
	}

	private fun getRatings(
		ratings: JsonArray?,
		context: JsonDeserializationContext?
	): ArrayList<Rating>?
	{
		if(ratings == null)
		{
			return null
		}

		val ratingsList = ArrayList<Rating>()
		for(item in ratings)
		{
			val actualRating = context?.deserialize<Rating>(item, Rating::class.java)!!

			ratingsList.add(actualRating)
		}

		return ratingsList
	}
}
