package ro.linca.flickrank.data.persistence.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SearchResult(
	@PrimaryKey
	@SerializedName("imdbId")
	val imdbID: String,
	val title: String,
	val year: String,
	val poster: String,
	val plot: String
)