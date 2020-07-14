package ro.linca.flickrank.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.full_review_dialog.*
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.warning_dialog.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ro.linca.flickrank.R
import ro.linca.flickrank.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment()
{
	private val viewModel: SettingsViewModel by sharedViewModel()
	private lateinit var binding: SettingsFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		binding = SettingsFragmentBinding.inflate(inflater, container, false).apply {
			settingsViewModel = viewModel
			lifecycleOwner = this@SettingsFragment
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
		signOutButton.setOnClickListener{
			FirebaseAuth.getInstance().signOut()
			this.view?.let { Navigation.findNavController(it).navigate(R.id.action_settingsFragment_to_loginFragment) }
		}

		clearViewedHistory.setOnClickListener {
			val reviewDialog = MaterialDialog(context!!)
				.noAutoDismiss()
				.cornerRadius(10f)
				.customView(R.layout.warning_dialog)
			reviewDialog.warningTitleLabel.text = "Warning"
			reviewDialog.warningDescriptionLabel.text = "Are you sure you want to delete view history?"
			reviewDialog.cancelButton.setOnClickListener {
				reviewDialog.dismiss()
			}
			reviewDialog.okButton.setOnClickListener {
				viewModel.clearViewedHistory()
				reviewDialog.dismiss()
			}
			reviewDialog.show()
		}

		clearSearchHistory.setOnClickListener {
			val reviewDialog = MaterialDialog(context!!)
				.noAutoDismiss()
				.cornerRadius(10f)
				.customView(R.layout.warning_dialog)
			reviewDialog.warningTitleLabel.text = "Warning"
			reviewDialog.warningDescriptionLabel.text = "Are you sure you want to delete your search history?"
			reviewDialog.cancelButton.setOnClickListener {
				reviewDialog.dismiss()
			}
			reviewDialog.okButton.setOnClickListener {
				viewModel.clearSearchHistory()
				reviewDialog.dismiss()
			}
			reviewDialog.show()
		}
	}
}
