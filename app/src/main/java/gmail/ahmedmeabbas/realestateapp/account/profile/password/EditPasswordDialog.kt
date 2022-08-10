package gmail.ahmedmeabbas.realestateapp.account.profile.password

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.databinding.DialogEditPasswordBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditPasswordDialog : BottomSheetDialogFragment() {

    private var _binding: DialogEditPasswordBinding? = null
    private val binding get() = _binding!!
    private val editPasswordViewModel: EditPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditTexts()
        setUpContinueButton()
        setUpSaveButton()
        setUpCancelButton()
        setUpPasswordLengthListener()
        observeLoading()
        observeMessages()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editPasswordViewModel.uiState
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
        binding.btnPasswordContinue.tvButton.visibility = hide
        binding.btnPasswordContinue.progressBar.visibility = show
        binding.btnPasswordContinue.root.isEnabled = !isLoading
        binding.btnPasswordSave.tvButton.visibility = hide
        binding.btnPasswordSave.progressBar.visibility = show
        binding.btnPasswordSave.root.isEnabled = !isLoading
    }

    private fun observeMessages() {
        val successMessage = getString(R.string.edit_password_success)
        val failureMessage = getString(R.string.edit_password_error)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editPasswordViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            AuthRepositoryImpl.RE_AUTHENTICATE_SUCCESS -> setUpNewPasswordViews()
                            AuthRepositoryImpl.RE_AUTHENTICATE_FAILURE -> showMessage(getString(R.string.error_invalid_credentials))
                            AuthRepositoryImpl.EDIT_PASSWORD_SUCCESS -> showMessage(successMessage)
                            AuthRepositoryImpl.EDIT_PASSWORD_FAILURE -> showMessage(failureMessage)
                            else -> return@collect
                        }
                        editPasswordViewModel.clearMessages()
                    }
            }
        }
    }

    private fun setUpPasswordLengthListener() {
        binding.etNewPassword.addTextChangedListener {
            if (it?.length!! >= 6) {
                binding.tvEditPasswordMinChars.apply {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(
                            requireContext().resources,
                            R.drawable.ic_check,
                            null
                        ), null, null, null
                    )
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                }
            } else {
                binding.tvEditPasswordMinChars.apply {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.hint_color))
                }
            }
        }
    }

    private fun setUpNewPasswordViews() {
        binding.tvEditPasswordSub.text = getString(R.string.edit_password_sub_new)
        binding.currentEmailTIL.visibility = View.GONE
        binding.currentPasswordTIL.visibility = View.GONE
        binding.btnPasswordContinue.root.visibility = View.GONE
        binding.newPasswordTIL.visibility = View.VISIBLE
        binding.tvEditPasswordMinChars.visibility = View.VISIBLE
        binding.confirmPasswordTIL.visibility = View.VISIBLE
        binding.btnPasswordSave.root.visibility = View.VISIBLE
    }

    private fun setUpContinueButton() {
        binding.btnPasswordContinue.tvButton.text = getString(R.string.button_continue)
        binding.btnPasswordContinue.root.setOnClickListener {
            val inputCurrentEmail = binding.etCurrentEmail.text.toString()
            val inputCurrentPassword = binding.etCurrentPassword.text.toString()
            if (!validateEmailAndPassword(
                    inputCurrentEmail,
                    inputCurrentPassword
                )
            ) return@setOnClickListener

            editPasswordViewModel.reAuthenticateUser(inputCurrentEmail, inputCurrentPassword)
        }
    }

    private fun validateEmailAndPassword(email: String, password: String): Boolean {
        var isValid = true
        if (email.isEmpty()) {
            binding.etCurrentEmail.error = getString(R.string.required)
            isValid = false
        }
        if (password.isEmpty()) {
            binding.etCurrentPassword.error = getString(R.string.required)
            isValid = false
        }
        return isValid
    }

    private fun setUpSaveButton() {
        binding.btnPasswordSave.tvButton.text = getString(R.string.save)
        binding.btnPasswordSave.root.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (!validatePasswords(newPassword, confirmPassword)) return@setOnClickListener

            editPasswordViewModel.updatePassword(newPassword)
        }
    }

    private fun validatePasswords(newPassword: String, confirmPassword: String): Boolean {
        var isValid = true
        if (newPassword.isEmpty()) {
            binding.etNewPassword.error = getString(R.string.required)
            isValid = false
        }
        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = getString(R.string.required)
            isValid = false
        }
        if (newPassword != confirmPassword) {
            showMessage(getString(R.string.passwords_dont_match))
            isValid = false
        }
        if (newPassword.isNotEmpty() && newPassword.length < 6) {
            showMessage(getString(R.string.password_length_error))
            isValid = false
        }
        return isValid
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpEditTexts() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        val lockOutlined =
            ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
        val lockFilled =
            ResourcesCompat.getDrawable(
                requireContext().resources,
                R.drawable.ic_lock_selected,
                null
            )

        binding.etCurrentEmail.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.currentEmailTIL.apply {
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

        binding.etCurrentPassword.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.currentPasswordTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etNewPassword.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.newPasswordTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.confirmPasswordTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    private fun setUpCancelButton() {
        binding.btnPasswordCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}