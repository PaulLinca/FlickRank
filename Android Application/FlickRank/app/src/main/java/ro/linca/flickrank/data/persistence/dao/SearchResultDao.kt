package ro.linca.flickrank.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ro.linca.flickrank.data.persistence.models.SearchResult

@Dao
interface SearchResultDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertSearchResult(vararg searchResult: SearchResult)

	@Query("SELECT * FROM searchresult")
	suspend fun getAllSearchResults(): List<SearchResult>

	@Query("DELETE FROM searchresult")
	suspend fun deleteHistory()
}