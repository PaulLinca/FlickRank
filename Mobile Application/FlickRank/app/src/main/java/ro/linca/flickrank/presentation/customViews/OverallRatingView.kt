package ro.linca.flickrank.presentation.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.overall_rating_view.view.*
import ro.linca.flickrank.R

class OverallRatingView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs)
{
	init
	{
		View.inflate(context, R.layout.overall_rating_view, this)

		val icon: ImageView = this.sourceIcon
		val rating: TextView = this.ratingText

		val attributes = context.obtainStyledAttributes(attrs, R.styleable.OverallRatingView)
		icon.setImageDrawable(attributes.getDrawable(R.styleable.OverallRatingView_iconSource))
		rating.text = attributes.getString(R.styleable.OverallRatingView_ratingText)
		attributes.recycle()
	}
}