package ro.linca.flickrank.data.persistence.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchQuery(
	@PrimaryKey
	val queryString: String
)