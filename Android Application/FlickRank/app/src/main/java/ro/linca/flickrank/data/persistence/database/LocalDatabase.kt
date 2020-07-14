package ro.linca.flickrank.data.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ro.linca.flickrank.data.persistence.dao.SearchQueryDao
import ro.linca.flickrank.data.persistence.dao.SearchResultDao
import ro.linca.flickrank.data.persistence.models.SearchQuery
import ro.linca.flickrank.data.persistence.models.SearchResult

@Database(entities = arrayOf(SearchResult::class, SearchQuery::class), version = 2)
abstract class LocalDatabase : RoomDatabase()
{
	abstract fun searchResultDao(): SearchResultDao
	abstract fun searchQueryDao(): SearchQueryDao

	companion object
	{
		@Volatile private var instance: LocalDatabase? = null
		private val LOCK = Any()

		operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
			instance ?: buildDatabase(context).also{
				instance = it
			}
		}

		private fun buildDatabase(context: Context) = Room.databaseBuilder(
			context.applicationContext,
			LocalDatabase::class.java,
			"localdatabase"
		).fallbackToDestructiveMigration().build()
	}
}