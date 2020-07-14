package ro.linca.flickrank.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ro.linca.flickrank.MainActivity
import ro.linca.flickrank.R
import ro.linca.flickrank.databinding.LoginFragmentBinding
import ro.linca.flickrank.utils.Constants

class LoginFragment : Fragment()
{
	companion object
	{
		const val TAG = "LOGIN"
	}

	private val viewModel: LoginViewModel by sharedViewModel()
	private lateinit var binding: LoginFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		binding = LoginFragmentBinding.inflate(inflater, container, false).apply {
			loginViewModel = viewModel
			lifecycleOwner = this@LoginFragment
		}

		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)

		(activity as MainActivity).setBottomBarVisibility(false)

		googleSignInButton.setOnClickListener {
			viewModel.isBusy.value = true
			googleSignIn()
		}

		if(FirebaseAuth.getInstance().currentUser != null)
		{
			this.view?.let { Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_searchFragment) }
		}
	}

	private fun googleSignIn()
	{
		val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.build()
		val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

		val signInIntent = googleSignInClient.signInIntent
		startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		super.onActivityResult(requestCode, resultCode, data)

		if(requestCode != Constants.RC_SIGN_IN)
		{
			viewModel.isBusy.value = false
			return
		}

		val task = GoogleSignIn.getSignedInAccountFromIntent(data)
		try
		{
			val account: GoogleSignInAccount? = task.getResult((ApiException::class.java))
			if(account != null)
			{
				firebaseAuthWithGoogle(account)
			}
		}
		catch (exception: Exception)
		{
			Log.d(TAG, exception.toString())

			viewModel.isBusy.value = false
		}
	}

	private fun firebaseAuthWithGoogle(googleAccount: GoogleSignInAccount)
	{
		val credential: AuthCredential = GoogleAuthProvider
			.getCredential(googleAccount.idToken, null)
		FirebaseAuth.getInstance()
			.signInWithCredential(credential)
			.addOnCompleteListener {
				this.view?.let {
					Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_searchFragment)
					viewModel.isBusy.value = false
				}
			}
	}
}
