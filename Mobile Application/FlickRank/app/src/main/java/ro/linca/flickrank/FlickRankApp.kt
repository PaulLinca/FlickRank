package ro.linca.flickrank

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ro.linca.flickrank.dependencyInjection.presentationModule

class FlickRankApp : Application()
{
	override fun onCreate()
	{
		super.onCreate()

		//start Koin
		startKoin {
			androidLogger()
			androidContext(this@FlickRankApp)
			modules(presentationModule)
		}
	}
}