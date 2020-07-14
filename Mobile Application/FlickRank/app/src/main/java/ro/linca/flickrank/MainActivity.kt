package ro.linca.flickrank

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		navController = Navigation.findNavController(this, R.id.navHostFragment)
		bottomNavigationView.setupWithNavController(navController)
	}

	override fun onSupportNavigateUp(): Boolean
	{
		return NavigationUI.navigateUp(navController, null)
	}

	fun setBottomBarVisibility(isVisible: Boolean)
	{
		bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
	}
}