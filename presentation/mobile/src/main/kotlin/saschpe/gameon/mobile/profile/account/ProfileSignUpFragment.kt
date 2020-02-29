package saschpe.gameon.mobile.profile.account

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_profile_sign_up.*
import saschpe.gameon.common.app.hideSoftInputFromWindow
import saschpe.gameon.common.isValidEmail
import saschpe.gameon.common.isValidPassword
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Application
import saschpe.gameon.mobile.base.text.TextInputLayoutDisableErrorTextWatcher
import saschpe.log4k.Log

class ProfileSignUpFragment : Fragment(R.layout.fragment_profile_sign_up) {
    // private val accountViewModel: AccountViewModel by activityViewModels()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var paramEmail: String? = null
    private var paramPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paramEmail = arguments?.getString(ARG_EMAIL)
        paramPassword = arguments?.getString(ARG_PASSWORD)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val transition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupWithNavController(toolbar, findNavController())

        /*accountViewModel.accountLiveData.observe(this, Observer { account ->
            email.setText(account.email)
        })*/

        email.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(emailLayout))
        paramEmail?.let { email.setText(it) }
        password.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(passwordLayout))
        paramPassword?.let { password.setText(it) }

        signUp.setOnClickListener {
            signUp(email.text.toString(), password.text.toString())
        }
        signUpTerms.text = HtmlCompat.fromHtml(
            getString(R.string.sign_up_terms_notice_template, Application.INTENT_SCHEME),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        signUpTerms.movementMethod = LinkMovementMethod()
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(
            requireActivity(), "Profile Sign-Up", "ProfileSignUpFragment"
        )
    }

    private fun validateForm(): Boolean {
        requireActivity().hideSoftInputFromWindow()
        var isValidForm = true
        if (!email.text.toString().isValidEmail()) {
            emailLayout.error = getString(R.string.invalid_email_address)
            isValidForm = false
        } else {
            emailLayout.isErrorEnabled = false
        }
        if (!password.text.toString().isValidPassword()) {
            passwordLayout.error = getString(R.string.invalid_password)
            isValidForm = false
        } else {
            passwordLayout.isErrorEnabled = false
        }
        return isValidForm
    }

    private fun signUp(email: String, password: String) {
        Log.debug("email=$email")
        if (!validateForm()) {
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.user
                    if (user?.email != null) {
                        user.updateProfile(
                            UserProfileChangeRequest.Builder()
                                // .setDisplayName(displayName)
                                .build()
                        )
                    }
                    findNavController().popBackStack()
                } else {
                    Log.debug("exception=${it.exception}")
                    Snackbar.make(
                        coordinator_layout,
                        it.exception?.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    companion object {
        const val ARG_EMAIL = "email"
        const val ARG_PASSWORD = "password"
    }
}
