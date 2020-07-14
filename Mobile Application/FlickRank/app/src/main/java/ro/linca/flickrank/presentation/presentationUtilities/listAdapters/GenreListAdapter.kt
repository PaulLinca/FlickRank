package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ro.linca.flickrank.R
import ro.linca.flickrank.data.dataModels.Movie
import ro.linca.flickrank.databinding.GenreItemBinding

class GenreListAdapter(private val movie: Movie): RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>()
{
	class GenreViewHolder(var binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder =
		GenreViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.genre_item,
				parent,
				false
			)
		)

	override fun getItemCount() = movie.genres?.count() ?: 0

	override fun onBindViewHolder(holder: GenreViewHolder, position: Int)
	{
		holder.binding.genre = movie.genres?.get(position)
	}
}