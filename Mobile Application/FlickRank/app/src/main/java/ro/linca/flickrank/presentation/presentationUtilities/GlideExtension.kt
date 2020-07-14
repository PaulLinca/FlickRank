package ro.linca.flickrank.presentation.presentationUtilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ro.linca.flickrank.R

fun getProgressDrawable(context: Context): CircularProgressDrawable
{
	return CircularProgressDrawable(context).apply {
		strokeWidth = 10f
		setColorSchemeColors(context.getColor(R.color.primaryRed))
		centerRadius = 50f
		start()
	}
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable)
{
	val options = RequestOptions()
		.placeholder(progressDrawable)
		.error(R.drawable.movie_poster_default)
	Glide.with(context)
		.setDefaultRequestOptions(options)
		.load(uri)
		.into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?)
{
	view.loadImage(url, getProgressDrawable(view.context))
}

@BindingAdapter("android:ratingSourceIcon")
fun loadSourceIcon(view: ImageView, source: String?)
{
	when(source)
	{
		"Internet Movie Database" -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.imdb_icon))
		"imdb" -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.imdb_icon))
		"Rotten Tomatoes" -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.rotten_tomatoes_icon))
		"Metacritic" -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.metacritic_icon))
		"metacritic" -> view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.metacritic_icon))
	}
}

@BindingAdapter("android:visibleWhileBusy")
fun isVisible(view: View, isBusy: Boolean)
{
	if(isBusy)
	{
		view.visibility = View.VISIBLE
	}
	else
	{
		view.visibility = View.GONE
	}
}