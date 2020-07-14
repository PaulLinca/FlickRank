package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ro.linca.flickrank.R
import ro.linca.flickrank.data.persistence.models.SearchResult
import ro.linca.flickrank.databinding.MovieCardDetailItemBinding

class RecommendedMovieListAdapter(private val movies: List<SearchResult>): RecyclerView.Adapter<RecommendedMovieListAdapter.MovieViewHolder>()
{
	class MovieViewHolder(var binding: MovieCardDetailItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
		RecommendedMovieListAdapter.MovieViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.movie_card_detail_item,
				parent,
				false
			)
		)

	override fun getItemCount() = movies.count()

	override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
	{
		holder.binding.searchResult = movies.get(position)
		holder.itemView.setOnClickListener {
			Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_moviePageFragment, bundleOf("movieImdbId" to movies[position].imdbID))
		}
	}
}