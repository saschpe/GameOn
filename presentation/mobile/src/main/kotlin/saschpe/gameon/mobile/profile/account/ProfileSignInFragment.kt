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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.TransitionInflater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import saschpe.gameon.common.base.app.disableScreenshots
import saschpe.gameon.common.base.app.enableScreenshots
import saschpe.gameon.common.base.app.hideSoftInput
import saschpe.gameon.common.base.isValidEmail
import saschpe.gameon.common.base.isValidPassword
import saschpe.gameon.data.core.Result
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.base.text.TextInputLayoutDisableErrorTextWatcher
import saschpe.gameon.mobile.databinding.FragmentProfileSignInBinding

class ProfileSignInFragment : Fragment() {
    private val viewModel: ProfileSignInViewModel by viewModels()
    private lateinit var binding: FragmentProfileSignInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        binding = FragmentProfileSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setupWithNavController(binding.toolbar, findNavController())

        binding.email.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(binding.emailLayout))
        binding.passwordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.password.addTextChangedListener(TextInputLayoutDisableErrorTextWatcher(binding.passwordLayout))

        binding.signInWithEmail.setOnClickListener {
            if (validateEmailAndPasswordForm()) {
                viewModel.signInWithEmail(binding.email.text.toString(), binding.password.text.toString())
            }
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(
                R.id.action_signIn_to_signUp,
                bundleOf(
                    ProfileSignUpFragment.ARG_EMAIL to binding.email.text.toString(),
                    ProfileSignUpFragment.ARG_PASSWORD to binding.password.text.toString()
                ),
                null,
                FragmentNavigatorExtras(
                    binding.emailLayout to getString(R.string.shared_element_email),
                    binding.passwordLayout to getString(R.string.shared_element_password)
                )
            )
        }
        binding.signInWithGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, { result ->
            if (result is Result.Success<FirebaseUser> && !result.data.isAnonymous) {
                findNavController().popBackStack() // Already logged in
            }
        })
        viewModel.signInLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success<AuthResult> -> findNavController().popBackStack() // Success
                is Result.Error -> Snackbar.make(
                    binding.coordinatorLayout,
                    getString(R.string.unable_to_sign_in_template, result.throwable.message),
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
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Profile Sign-In")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ProfileSignInFragment")
        }
    }

    override fun onStop() {
        super.onStop()
        enableScreenshots()
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
                    binding.coordinatorLayout,
                    getString(R.string.unable_to_sign_in_with_google_template, e.message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateEmailAndPasswordForm(): Boolean {
        requireActivity().hideSoftInput()
        var isValidForm = true
        if (!binding.email.text.toString().isValidEmail()) {
            binding.emailLayout.error = getString(R.string.invalid_email_address)
            isValidForm = false
        } else {
            binding.emailLayout.isErrorEnabled = false
        }
        if (!binding.password.text.toString().isValidPassword()) {
            binding.passwordLayout.error = getString(R.string.invalid_password)
            isValidForm = false
        } else {
            binding.passwordLayout.isErrorEnabled = false
        }
        return isValidForm
    }

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 9001
    }
}