package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.app.Activity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.FAILURE
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.NETWORK_ERROR
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.SUCCESS
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSignInBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpFacebookLogin()
        setUpGoogleLogin()
        setUpButtonListeners()
        setUpToSText()
        observeMessages()
        observeLoading()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect { isLoading ->
                        if (isLoading) binding.clLoading.visibility = View.VISIBLE
                    }
            }
        }
    }

    private fun setUpGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        setUpGoogleButtonListener(googleSignInClient)
    }

    private fun setUpGoogleButtonListener(googleSignInClient: GoogleSignInClient) {
        binding.btnSignInGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                handleResults(task)
            } else {
                showMessage(getString(R.string.error_sign_in))
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.result
            if (account != null) {
                signInViewModel.handleGoogleAccessToken(account.idToken)
            } else {
                showMessage(getString(R.string.error_sign_in))
            }
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState
                    .map { it.userMessage }
                    .collect { userMessage ->
                        when (userMessage) {
                            SUCCESS -> findNavController().navigate(R.id.action_global_accountFragment)
                            NETWORK_ERROR -> showMessage(getString(R.string.error_network))
                            FAILURE -> showMessage(getString(R.string.error_sign_in))
                            else -> return@collect
                        }
                        signInViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setUpFacebookLogin() {
        val callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    showMessage(getString(R.string.sign_in_cancelled))
                }
                override fun onError(error: FacebookException) {
                    showMessage(getString(R.string.error_sign_in))
                }

                override fun onSuccess(result: LoginResult) {
                    signInViewModel.handleFacebookAccessToken(result.accessToken)
                }
            }
        )
        binding.btnSignInFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                callbackManager,
                listOf("email", "public_profile")
            )
        }
    }

    private fun setUpToolbar() {
        binding.toolbarSignIn.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpButtonListeners() {
        binding.btnSignInEmail.setOnClickListener {
            navigateTo(R.id.action_signInFragment_to_emailSignInFragment)
        }

        binding.btnSignInCreateAccount.setOnClickListener {
            navigateTo(R.id.action_signInFragment_to_createAccountFragment)
        }
    }

    private fun setUpToSText() {
        val text = resources.getString(R.string.sign_in_tos)
        val textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
        var click1Start = 0
        var click1End = 0
        var click2Start = 0
        var click2End = 0
        if (Locale.getDefault().language.equals("en")) {
            click1Start = 31; click1End = 47; click2Start = 52; click2End = 66
        } else if (Locale.getDefault().language.equals("ar")) {
            click1Start = 35; click1End = 46; click2Start = 48; click2End = 62
        }
        val spannableString = SpannableString(text)
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_global_termsFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_global_privacyFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        spannableString.setSpan(
            clickableSpan1,
            click1Start,
            click1End,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan2,
            click2Start,
            click2End,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvToSSignIn.text = spannableString
        binding.tvToSSignIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}