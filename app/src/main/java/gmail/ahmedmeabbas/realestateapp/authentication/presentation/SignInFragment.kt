package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSignInBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
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

        setUpFacebookLoginButton()
        setUpToolbar()
        setUpButtonListeners()
        setUpToSText()
        observeMessages()
    }

    private fun observeMessages() {
        val failureMessage = getString(R.string.error_sign_in)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState
                    .map { it.userMessage }
                    .collect { userMessage ->
                        when (userMessage) {
                            AuthRepositoryImpl.SUCCESS -> findNavController().navigate(R.id.action_global_accountFragment)
                            AuthRepositoryImpl.FAILURE -> showMessage(failureMessage)
                            else -> return@collect
                        }
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setUpFacebookLoginButton() {
        val callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {}

                override fun onError(error: FacebookException) {}

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