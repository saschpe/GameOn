package saschpe.gameon.mobile.profile.account

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.fragment_profile_sign_up.*
import saschpe.gameon.common.base.app.disableScreenshots
import saschpe.gameon.common.base.app.enableScreenshots
import saschpe.gameon.common.base.app.hideSoftInput
import saschpe.gameon.common.base.isValidEmail
import saschpe.gameon.common.base.isValidPassword
import saschpe.gameon.data.core.Result
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.Application
import saschpe.gameon.mobile.base.text.TextInputLayoutDisableErrorTextWatcher

class ProfileSignUpFragment : Fragment(R.layout.fragment_profile_sign_up) {
    private val viewModel: ProfileSignUpViewModel by viewModels()
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

        email.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(emailLayout))
        paramEmail?.let { email.setText(it) }
        password.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(passwordLayout))
        paramPassword?.let { password.setText(it) }

        signUp.setOnClickListener {
            if (validateForm()) {
                viewModel.signUpWithEmail(this.email.text.toString(), password.text.toString())
            }
        }
        signUpTerms.movementMethod = LinkMovementMethod()
        signUpTerms.text = HtmlCompat.fromHtml(
            getString(R.string.sign_up_terms_notice_template, Application.INTENT_SCHEME),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        viewModel.signUpLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success<AuthResult> -> findNavController().popBackStack() // Success
                is Result.Error -> Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.unable_to_sign_up_template, result.throwable.message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        disableScreenshots()
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Profile Sign-Up")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ProfileSignUpFragment")
        }
    }

    override fun onStop() {
        super.onStop()
        enableScreenshots()
    }

    private fun validateForm(): Boolean {
        requireActivity().hideSoftInput()
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

    companion object {
        const val ARG_EMAIL = "email"
        const val ARG_PASSWORD = "password"
    }
}
