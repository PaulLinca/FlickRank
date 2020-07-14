package ro.linca.flickrank.presentation.presentationUtilities.listAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.full_review_dialog.*
import kotlinx.android.synthetic.main.preview_review_card.view.*
import ro.linca.flickrank.R
import ro.linca.flickrank.data.dataModels.Review
import ro.linca.flickrank.databinding.PreviewReviewCardBinding

class ReviewListAdapter(private val movieReviews: List<Review>): RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>()
{
	class ReviewViewHolder(var binding: PreviewReviewCardBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		ReviewViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.preview_review_card,
				parent,
				false
			)
		)

	override fun getItemCount() = movieReviews.count()

	override fun onBindViewHolder(holder: ReviewViewHolder, position: Int)
	{
		val currentReview = movieReviews.get(position)
		holder.binding.review = currentReview
		var givenRating = "No rating given"
		if(currentReview.rating != null)
		{
			givenRating = if(currentReview.source.equals("imdb"))
			{
				currentReview.rating + "/10"
			}
			else
			{
				currentReview.rating + "/100"
			}
		}
		holder.itemView.reviewRating.text = givenRating

		holder.itemView.setOnClickListener {
			val reviewDialog = MaterialDialog(holder.itemView.context)
				.noAutoDismiss()
				.cornerRadius(10f)
				.customView(R.layout.full_review_dialog)
			reviewDialog.dialog_reviewTitle.text = currentReview.title.trim()
			reviewDialog.dialog_reviewRating.text = givenRating
			reviewDialog.dialog_reviewText.text = currentReview.reviewText
			if(!currentReview.containsSpoilers)
			{
				reviewDialog.dialog_spoilerWarning.visibility = View.GONE
			}
			reviewDialog.dismissButton.setOnClickListener {
				reviewDialog.dismiss()
			}
			reviewDialog.show()
		}
	}
}