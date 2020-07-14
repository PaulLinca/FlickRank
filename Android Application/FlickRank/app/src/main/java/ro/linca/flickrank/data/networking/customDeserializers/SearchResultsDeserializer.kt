package ro.linca.flickrank.data.networking.customDeserializers

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.data.dataModels.SearchResults
import java.lang.reflect.Type

class SearchResultsDeserializer : JsonDeserializer<SearchResults>
{
	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): SearchResults
	{
		val searchResultsJsonObject = json?.asJsonObject
		val searchResults =  searchResultsJsonObject?.get("Search")?.asJsonArray
		return SearchResults(
			getMovies(searchResults, context),
			searchResultsJsonObject?.get("totalResults")?.asString,
			searchResultsJsonObject?.get("Error")?.asString,
			searchResultsJsonObject?.get("Response")!!.asBoolean
			)
	}

	private fun getMovies(
		searchResults: JsonArray?,
		context: JsonDeserializationContext?
	): List<Movie>?
	{
		if(searchResults == null)
		{
			return null
		}

		val movieResults = ArrayList<Movie>()
		for(item in searchResults)
		{
			movieResults.add(MovieDeserializer().deserialize(item, Movie::class.java, context))
		}

		return movieResults
	}
}