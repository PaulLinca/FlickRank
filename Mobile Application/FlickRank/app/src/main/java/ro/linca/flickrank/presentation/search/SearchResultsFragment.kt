package ro.linca.flickrank.presentation.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.search_results_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.linca.flickrank.databinding.SearchResultsFragmentBinding
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.SearchResultsAdapter
import ro.linca.flickrank.presentation.presentationUtilities.AlertDialogEventInterface

class SearchResultsFragment : Fragment(), AlertDialogEventInterface
{
	private val viewModel: SearchResultsViewModel by viewModel()
	private lateinit var binding: SearchResultsFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		binding = SearchResultsFragmentBinding.inflate(inflater, container, false).apply {
			searchResultsViewModel = viewModel
			lifecycleOwner = this@SearchResultsFragment
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
		//setup alert event
		viewModel.errorAlertEvent.setEventReceiver(this, this)

		//get search parameter from navigation bundle
		viewModel.searchQuery.value = arguments?.getString("searchQuery")

		//recycler view
		viewModel.searchResults.observe(viewLifecycleOwner, Observer { movies ->
			searchResultsRecyclerView.also {
				it.layoutManager = LinearLayoutManager(context)
				it.adapter =
					SearchResultsAdapter(
						movies
					)
			}
		})
		//add divider
		searchResultsRecyclerView.addItemDecoration(
			DividerItemDecoration(searchResultsRecyclerView.context, DividerItemDecoration.HORIZONTAL)
		)

		//setup search button listener
		searchButton.setOnClickListener{
			activity!!.onBackPressed()
		}
		backToSearchButton.setOnClickListener{
			activity!!.onBackPressed()
		}
	}

	private fun setupRequests()
	{
		//perform search
		viewModel.search()
		//store search query
		viewModel.storeSearchQueryLocally()
	}

	override fun showAlert(title: String, message: String)
	{
		AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(message)
			.setNeutralButton("Ok"){_,_ ->  activity!!.onBackPressed()} // TODO: Revisit this
			.create()
			.show()
	}
}
