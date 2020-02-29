package saschpe.gameon.mobile.profile.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.TransitionInflater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_profile_sign_in.*
import saschpe.gameon.common.app.hideSoftInputFromWindow
import saschpe.gameon.common.isValidEmail
import saschpe.gameon.common.isValidPassword
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.text.TextInputLayoutDisableErrorTextWatcher
import saschpe.log4k.Log

class ProfileSignInFragment : Fragment(R.layout.fragment_profile_sign_in) {
    private var firebaseAuth = FirebaseAuth.getInstance()

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
            signInWithEmailAndPassword(email.text.toString(), password.text.toString())
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
    }

    override fun onResume() {
        super.onResume()
        // Check if userLiveData is signed in (non-null)
        if (firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous != true) {
            findNavController().popBackStack() // Already logged in
        }
        firebaseAnalytics.setCurrentScreen(
            requireActivity(), "Profile Sign-In", "ProfileSignInFragment"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                firebaseAuthWithGoogle(task.getResult(ApiException::class.java))
            } catch (e: ApiException) {
                Log.warn("failed $e")
                Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.unable_to_sign_in_with_google_template, e.message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateEmailAndPasswordForm(): Boolean {
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

    private fun signInWithEmailAndPassword(email: String, password: String) {
        Log.debug("email=$email")
        if (!validateEmailAndPasswordForm()) {
            return
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().popBackStack()
                } else {
                    Log.debug("error=${task.exception}")
                    Snackbar.make(
                        coordinatorLayout, task.exception?.message.toString(), Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.debug("account id=${account?.id}")
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().popBackStack()
                } else {
                    Log.debug("error=${task.exception}")
                    Snackbar.make(
                        coordinatorLayout, task.exception?.message.toString(), Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 9001
    }
}