package ro.linca.flickrank.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ro.linca.flickrank.data.persistence.models.SearchQuery

@Dao
interface SearchQueryDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertSearchQuery(vararg searchQuery: SearchQuery)

	@Query("SELECT * FROM searchquery")
	suspend fun getAllSearchQueries(): List<SearchQuery>

	@Query("DELETE FROM searchquery where queryString = :queryString")
	suspend fun deleteQuery(queryString: String)

	@Query("DELETE FROM searchquery")
	suspend fun deleteAllQueries()
}