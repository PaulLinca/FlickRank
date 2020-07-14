package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ro.linca.flickrank.R
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.databinding.RatingItemBinding

class RatingsListAdapter(private val movie: Movie): RecyclerView.Adapter<RatingsListAdapter.RatingViewHolder>()
{
	class RatingViewHolder(var binding: RatingItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder =
		RatingViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.rating_item,
				parent,
				false
			)
		)

	override fun getItemCount() = movie.ratings?.count() ?: 0

	override fun onBindViewHolder(holder: RatingViewHolder, position: Int)
	{
		holder.binding.rating = movie.ratings?.get(position)
	}
}
