package saschpe.gameon.mobile.profile.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.TransitionInflater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile_sign_in.*
import saschpe.gameon.common.app.hideSoftInput
import saschpe.gameon.common.isValidEmail
import saschpe.gameon.common.isValidPassword
import saschpe.gameon.data.core.Result
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.text.TextInputLayoutDisableErrorTextWatcher

class ProfileSignInFragment : Fragment(R.layout.fragment_profile_sign_in) {
    private val viewModel: ProfileSignInViewModel by viewModels()

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
        passwordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        password.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(passwordLayout))

        signInWithEmail.setOnClickListener {
            if (validateEmailAndPasswordForm()) {
                viewModel.signInWithEmail(email.text.toString(), password.text.toString())
            }
        }
        signUp.setOnClickListener {
            findNavController().navigate(
                R.id.action_signIn_to_signUp,
                bundleOf(
                    ProfileSignUpFragment.ARG_EMAIL to email.text.toString(),
                    ProfileSignUpFragment.ARG_PASSWORD to password.text.toString()
                ),
                null,
                FragmentNavigatorExtras(
                    emailLayout to getString(R.string.shared_element_email),
                    passwordLayout to getString(R.string.shared_element_password)
                )
            )
        }
        signInWithGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result is Result.Success<FirebaseUser> && !result.data.isAnonymous) {
                findNavController().popBackStack() // Already logged in
            }
        })
        viewModel.signInLiveData.observe(viewLifecycleOwner, Observer
        { result ->
            when (result) {
                is Result.Success<AuthResult> -> findNavController().popBackStack() // Success
                is Result.Error -> Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.unable_to_sign_in_template, result.throwable.message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(
            requireActivity(), "Profile Sign-In", "ProfileSignInFragment"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.unable_to_sign_in_with_google_template, e.message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateEmailAndPasswordForm(): Boolean {
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
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 9001
    }
}