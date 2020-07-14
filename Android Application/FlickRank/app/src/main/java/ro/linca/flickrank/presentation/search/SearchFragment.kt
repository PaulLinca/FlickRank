package ro.linca.flickrank.presentation.search

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ro.linca.flickrank.MainActivity
import ro.linca.flickrank.R
import ro.linca.flickrank.databinding.SearchFragmentBinding
import ro.linca.flickrank.presentation.presentationUtilities.listAdapters.SearchHistoryAdapter
import ro.linca.flickrank.utils.Constants.CAMERA_REQUEST_CODE
import ro.linca.flickrank.utils.Constants.PICK_IMAGE_REQUEST_CODE

class SearchFragment : Fragment()
{
    private val viewModel: SearchViewModel by sharedViewModel()
    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = SearchFragmentBinding.inflate(inflater, container, false).apply {
            searchViewModel = viewModel
            lifecycleOwner = this@SearchFragment
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        setupUI()
    }

    private fun setupUI()
    {
        //show bottom navigation bar
        (activity as MainActivity).setBottomBarVisibility(true)

        //setup button click listeners
        manualSearchButton.setOnClickListener{
            startManualSearch()
        }

        imageSearchButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        imageFromGalleryButton.setOnClickListener {
            dispatchSelectImageFromGalleryIntent()
        }

        closeManualSearchButton.setOnClickListener {
            closeManualSearch()
        }

        //setup search action
        searchBar.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                performSearch(v.text.toString())
            }

            false
        }

        //get search history
        viewModel.getSearchHistory()

        //recycler view
        viewModel.searchHistory.observe(viewLifecycleOwner, Observer { history ->
            searchHistoryRecyclerView.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter =
                    SearchHistoryAdapter(
                        history,
                        { searchQuery ->
                            searchBar.setText(searchQuery)
                            navigateToSearchResultsPage(searchQuery)
                        },
                        { searchQuery -> viewModel.deleteSearchItem(searchQuery) }
                    )
            }
        })

        viewModel.imageSearchResponse.observe(viewLifecycleOwner, Observer { title ->
            if(!title.equals(""))
            {
                performSearch(title)
                viewModel.imageSearchResponse.value = ""
            }
        })
    }

    private fun startManualSearch()
    {
        viewModel.isManualSearchEnabled.value = true

        //focus on the search bar
        showKeyboard()
    }

    private fun closeManualSearch()
    {
        hideKeyboard()

        viewModel.isManualSearchEnabled.value = false
        //searchBar.text.clear()
    }

    private fun showKeyboard()
    {
        searchBar.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard()
    {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
        searchBar.clearFocus()
    }

    private fun dispatchTakePictureIntent()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this.activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun dispatchSelectImageFromGalleryIntent()
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(this.activity!!.packageManager) != null)
        {
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE)
        {
            val takenPicture = data?.extras?.get("data") ?: return
            val takenPictureAsBmp = takenPicture as Bitmap
            viewModel.imageSearch(takenPictureAsBmp)
//            val bmpDrawable = BitmapDrawable(resources, takenPictureAsBmp)
//            viewModel.takenPhoto.value = bmpDrawable
        }

        if (requestCode == PICK_IMAGE_REQUEST_CODE)
        {
            val imageUri = data?.data ?: return
            val selectedImageAsBitmap = if(Build.VERSION.SDK_INT < 28)
            {
                MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
            }
            else
            {
                val source = ImageDecoder.createSource(context?.contentResolver!!, imageUri)
                ImageDecoder.decodeBitmap(source)
            }

            viewModel.imageSearch(selectedImageAsBitmap)
//            val bmpDrawable = BitmapDrawable(resources, bitmap)
//            viewModel.takenPhoto.value = bmpDrawable
        }
    }

    private fun navigateToSearchResultsPage(query: String)
    {
        findNavController().navigate(R.id.action_searchFragment_to_searchResultsFragment, bundleOf("searchQuery" to query))
    }

    private fun performSearch(titleToSearch: String)
    {
        val searchQuery = titleToSearch.trim()
        if(searchQuery.equals(""))
        {
            Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
        }
        else
        {
            navigateToSearchResultsPage(searchQuery)
        }
    }
}
