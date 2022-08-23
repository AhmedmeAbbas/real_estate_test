package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.INVALID_CREDENTIALS
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.NETWORK_ERROR
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentEmailSignInBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EmailSignInFragment : Fragment() {

    private var _binding: FragmentEmailSignInBinding? = null
    private val binding get() = _binding!!
    private val emailViewModel: EmailSignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpSignInButton()
        setUpForgotPassword()
        setUpEditTextColor()
        observeErrorMessages()
        observeSignInState()
        observeLoadingState()
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                emailViewModel.uiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect { isLoading ->
                        updateLoadingButton(isLoading)
                    }
            }
        }
    }

    private fun updateLoadingButton(isLoading: Boolean) {
        val show = if (isLoading) View.VISIBLE else View.GONE
        val hide = if (isLoading) View.GONE else View.VISIBLE
        binding.btnEmailSignIn.tvButton.visibility = hide
        binding.btnEmailSignIn.progressBar.visibility = show
        binding.btnEmailSignIn.root.isEnabled = !isLoading
    }

    private fun observeSignInState() {
        emailViewModel.isUserSignedIn.observe(viewLifecycleOwner) { isSignedIn ->
            if (isSignedIn) {
                findNavController().navigate(R.id.action_global_searchFragment)
                emailViewModel.stopLoading()
            }
        }
    }

    private fun observeErrorMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                emailViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { errorMessage ->
                        if (errorMessage.isEmpty()) return@collect
                        when (errorMessage) {
                            INVALID_CREDENTIALS -> showMessage(getString(R.string.error_invalid_credentials))
                            NETWORK_ERROR -> showMessage(getString(R.string.error_network))
                            else -> showMessage(errorMessage)
                        }
                        emailViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun setUpToolbar() {
        binding.toolbarEmailSignIn.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpForgotPassword() {
        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_emailSignInFragment_to_resetPasswordDialog)
        }
    }

    private fun setUpSignInButton() {
        binding.btnEmailSignIn.tvButton.text = getString(R.string.sign_in_button)

        binding.btnEmailSignIn.root.setOnClickListener {
            val email = binding.etEmailSignIn.text.toString()
            val password = binding.etPasswordSignIn.text.toString()
            if (!validateForm(email, password)) return@setOnClickListener
            val view = binding.root.findFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            emailViewModel.signInWithEmailAndPassword(email, password)
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        var valid = true
        if (TextUtils.isEmpty(email)) {
            binding.emailSignInTIL.error = getString(R.string.required)
            valid = false
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordSignInTIL.error = getString(R.string.required)
            valid = false
        }
        return valid
    }


    private fun setUpEditTextColor() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEmailSignIn.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.emailSignInTIL.apply {
                val mailOutlined =
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_mail,
                        null
                    )
                val mailFilled =
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_mail_selected,
                        null
                    )
                val drawable = if (hasFocus) mailFilled else mailOutlined
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etPasswordSignIn.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val lockOutlined =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
            val lockFilled =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_lock_selected,
                    null
                )
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.passwordSignInTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "EmailSignInFragment"
    }
}