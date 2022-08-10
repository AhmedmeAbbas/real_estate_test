package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentCreateAccountBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    private val createAccountViewModel: CreateAccountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpCreateAccountButton()
        setUpEditTextColor()
        setUpToSText()
        setUpPasswordLengthListener()
        observeLoading()
        observeMessages()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAccountViewModel.uiState
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
        binding.btnCreateAccount.tvButton.visibility = hide
        binding.btnCreateAccount.progressBar.visibility = show
        binding.btnCreateAccount.root.isEnabled = !isLoading
    }

    private fun observeMessages() {
        val failureMessage = getString(R.string.create_account_failure)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAccountViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            AuthRepositoryImpl.CREATE_ACCOUNT_SUCCESS -> findNavController().navigate(
                                R.id.action_global_accountFragment
                            )
                            AuthRepositoryImpl.CREATE_ACCOUNT_FAILURE -> showMessage(failureMessage)
                            else -> return@collect
                        }
                        createAccountViewModel.clearMessages()
                    }
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbarCreateAccount.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpCreateAccountButton() {
        binding.btnCreateAccount.tvButton.text = getString(R.string.create_account_button)

        binding.btnCreateAccount.root.setOnClickListener {
            val email = binding.etEmailCreateAccount.text.toString()
            val password = binding.etPasswordCreateAccount.text.toString()
            val confirmPassword = binding.etConfirmPasswordCreateAccount.text.toString()
            if (!validateForm(email, password, confirmPassword)) return@setOnClickListener

            val view = binding.root.findFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            createAccountViewModel.createAccount(email, password)
        }
    }

    private fun validateForm(email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            binding.etEmailCreateAccount.error = getString(R.string.required)
            isValid = false
        }
        if (password.isEmpty()) {
            binding.etPasswordCreateAccount.error = getString(R.string.required)
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPasswordCreateAccount.error = getString(R.string.required)
            isValid = false
        }

        if (password != confirmPassword) {
            showMessage(getString(R.string.passwords_dont_match))
            isValid = false
        }

        if (password.length < 6) {
            binding.tvPasswordMinChars.apply {
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_error,
                        null
                    ), null, null, null
                )
                compoundDrawablePadding = 10
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
            isValid = false
        }
        return isValid
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpPasswordLengthListener() {
        binding.etPasswordCreateAccount.addTextChangedListener {
            if (it?.length!! >= 6) {
                binding.tvPasswordMinChars.apply {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.ic_check,
                            null
                        ), null, null, null
                    )
                    compoundDrawablePadding = 10
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
            } else {
                binding.tvPasswordMinChars.apply {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_color))
                }
            }
        }
    }

    private fun setUpToSText() {
        val text = resources.getString(R.string.create_account_tos)
        val textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
        var click1Start = 0
        var click1End = 0
        var click2Start = 0
        var click2End = 0
        if (Locale.getDefault().language.equals("en")) {
            click1Start = 40; click1End = 56; click2Start = 61; click2End = 75
        } else if (Locale.getDefault().language.equals("ar")) {
            click1Start = 33; click1End = 44; click2Start = 46; click2End = 60
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
        binding.tvToSCreateAccount.text = spannableString
        binding.tvToSCreateAccount.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setUpEditTextColor() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEmailCreateAccount.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.emailCreateAccountTIL.apply {
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

        binding.etPasswordCreateAccount.setOnFocusChangeListener { _, hasFocus ->
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
            binding.passwordCreateAccountTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etConfirmPasswordCreateAccount.setOnFocusChangeListener { _, hasFocus ->
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
            binding.confirmPasswordCreateAccountTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}