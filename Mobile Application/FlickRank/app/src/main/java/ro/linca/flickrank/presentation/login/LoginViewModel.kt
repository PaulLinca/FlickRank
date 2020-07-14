package ro.linca.flickrank.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel()
{
	var isBusy = MutableLiveData(false)

	fun getDisplayGreeting(): String?
	{
		val user = FirebaseAuth.getInstance().currentUser
		return user?.displayName ?: "Sign in please"
	}
}
