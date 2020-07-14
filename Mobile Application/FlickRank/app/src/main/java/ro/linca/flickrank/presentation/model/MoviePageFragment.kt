package ro.linca.flickrank.presentation.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.movie_page_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.linca.flickrank.databinding.MoviePageFragmentBinding
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.GenreListAdapter
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.RatingsListAdapter
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.ReviewListAdapter

class MoviePageFragment : Fragment()
{
	private val viewModel: MoviePageViewModel by viewModel()
	private lateinit var binding: MoviePageFragmentBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = MoviePageFragmentBinding.inflate(inflater, container, false).apply {
			moviePageViewModel = viewModel
			lifecycleOwner = this@MoviePageFragment
		}

		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)

		setupUI()
		setupRequests()
	}

	private fun setupUI()
	{
		viewModel.movieImdbId.value = arguments?.getString("movieImdbId")

		//recycler views
		viewModel.movie.observe(viewLifecycleOwner, Observer { movie ->
			//update genre list
			movieGenreList.also {
				it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
				it.adapter =
					GenreListAdapter(
						movie
					)
			}
			//update ratings list
			movieRatingsList.also {
				it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
				it.adapter =
					RatingsListAdapter(
						movie
					)
			}
			//store movie locally
			viewModel.storeMovieLocally()
		})

		viewModel.movieReviews.observe(viewLifecycleOwner, Observer { allReviews ->
			movieReviewList.also {
				it.layoutManager = LinearLayoutManager(context)
				it.adapter =
					ReviewListAdapter(
						allReviews
					)
			}
		})

		backButton.setOnClickListener {
			activity!!.onBackPressed()
		}
	}

	private fun setupRequests()
	{
		//retrieve movie from API
		viewModel.getMovie()
		//retrieve reviews from backed
		viewModel.getMovieReviews()
	}
}
