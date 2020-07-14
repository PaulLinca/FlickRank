package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ro.linca.flickrank.R
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.databinding.SearchResultItemBinding

class SearchResultsAdapter (private val searchResultsList: List<Movie>) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>()
{
	class SearchResultViewHolder(var binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder =
		SearchResultViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.search_result_item,
				parent,
				false
			)
		)

	override fun getItemCount() = searchResultsList.size

	override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int)
	{
		holder.binding.searchResult = searchResultsList[position]
		holder.itemView.setOnClickListener {
			Navigation.findNavController(it).navigate(R.id.action_searchResultsFragment_to_moviePageFragment, bundleOf("movieImdbId" to searchResultsList[position].imdbID))
		}
	}
}