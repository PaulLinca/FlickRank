package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_history_item.view.*
import ro.linca.flickrank.R
import ro.linca.flickrank.data.persistence.models.SearchQuery
import ro.linca.flickrank.databinding.SearchHistoryItemBinding

class SearchHistoryAdapter(
	private val searchHistory: List<SearchQuery>,
	val clickListener: (String) -> Unit,
	val deleteListener: (String) -> Unit
): RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>()
{
	class SearchHistoryViewHolder(var binding: SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		SearchHistoryViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.search_history_item,
				parent,
				false
			)
		)
	override fun getItemCount() = searchHistory.count()

	override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int)
	{
		val searchItem = searchHistory.get(position)
		holder.binding.searchQuery = searchItem
		holder.itemView.setOnClickListener {
			clickListener(searchItem.queryString)
		}
		holder.itemView.deleteSearchQueryButton.setOnClickListener {
			deleteListener(searchItem.queryString)
		}
	}
}