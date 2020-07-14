package ro.linca.flickrank.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ro.linca.flickrank.databinding.HomeFragmentBinding
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.MovieListAdapter
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.RecommendedMovieListAdapter

class HomeFragment : Fragment()
{
	private val viewModel: HomeViewModel by sharedViewModel()
	private lateinit var binding: HomeFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
			homeViewModel = viewModel
			lifecycleOwner = this@HomeFragment
		}

		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)

		setupUI()
		viewModel.getRecent()

	}

	private fun setupUI()
	{
		refreshRecommendationButton.setOnClickListener {
			loadRecommendations()
		}

		viewModel.recentlyViewed.observe(viewLifecycleOwner, Observer { movieList ->
			recentlyViewedMoviesList.also {
				it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
				it.adapter = MovieListAdapter(movieList)
			}

			if(!viewModel.areRecommendedLoaded)
			{
				loadRecommendations()
			}
		})

		viewModel.popularMovies.observe(viewLifecycleOwner, Observer { movieList ->
			popularMoviesList.also {
				it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
				it.adapter = MovieListAdapter(movieList)
			}
		})

		viewModel.recommendedMovies.observe(viewLifecycleOwner, Observer { movieList ->
			recommendedMoviesList.also {
				it.layoutManager = LinearLayoutManager(context)
				it.adapter = RecommendedMovieListAdapter(movieList)
			}
		})
	}

	private fun loadRecommendations()
	{
		val recentlyViewedMovies = viewModel.recentlyViewed.value
		if(recentlyViewedMovies != null && recentlyViewedMovies.count() != 0)
		{
			val randomMovie = recentlyViewedMovies.random()
			viewModel.getRecommendedMovies(randomMovie.imdbID, randomMovie.title, randomMovie.year)
		}
	}
}
