package gmail.ahmedmeabbas.realestateapp.account.profile.email

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.FAILURE
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.NETWORK_ERROR
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.RE_AUTHENTICATE_FAILURE
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.RE_AUTHENTICATE_SUCCESS
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.SUCCESS
import gmail.ahmedmeabbas.realestateapp.databinding.DialogEditEmailBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditEmailDialog : BottomSheetDialogFragment() {

    private var _binding: DialogEditEmailBinding? = null
    private val binding get() = _binding!!
    private val editEmailViewModel: EditEmailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditTexts()
        setUpContinueButton()
        setUpSaveButton()
        setUpCancelButton()
        observeLoading()
        observeMessages()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editEmailViewModel.uiState
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
        binding.btnEmailContinue.tvButton.visibility = hide
        binding.btnEmailContinue.progressBar.visibility = show
        binding.btnEmailContinue.root.isEnabled = !isLoading
        binding.btnEmailSave.tvButton.visibility = hide
        binding.btnEmailSave.progressBar.visibility = show
        binding.btnEmailSave.root.isEnabled = !isLoading
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editEmailViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            RE_AUTHENTICATE_SUCCESS -> setUpNewEmailViews()
                            RE_AUTHENTICATE_FAILURE -> showMessage(getString(R.string.error_invalid_credentials))
                            SUCCESS -> showMessage(getString(R.string.edit_email_success))
                            NETWORK_ERROR -> showMessage(getString(R.string.error_network))
                            FAILURE -> showMessage(getString(R.string.edit_email_error))
                            else -> return@collect
                        }
                        editEmailViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpEditTexts() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
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
            val lockOutlined =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
            val lockFilled =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_lock_selected,
                    null
                )
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.currentPasswordTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etNewEmail.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.newEmailTIL.apply {
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
    }

    private fun setUpContinueButton() {
        binding.btnEmailContinue.tvButton.text = getString(R.string.button_continue)
        binding.btnEmailContinue.root.setOnClickListener {
            val inputCurrentEmail = binding.etCurrentEmail.text.toString()
            val inputCurrentPassword = binding.etCurrentPassword.text.toString()
            if (!validateEmailAndPassword(
                    inputCurrentEmail,
                    inputCurrentPassword
                )
            ) return@setOnClickListener
            Log.d(TAG, "setUpContinueButton: valid passed")
            editEmailViewModel.reAuthenticateUser(inputCurrentEmail, inputCurrentPassword)
        }
    }

    private fun setUpNewEmailViews() {
        binding.tvEditEmailSub.text = getString(R.string.edit_email_sub_new)
        binding.currentEmailTIL.visibility = View.GONE
        binding.currentPasswordTIL.visibility = View.GONE
        binding.btnEmailContinue.root.visibility = View.GONE
        binding.tvEditEmailSub.visibility = View.VISIBLE
        binding.newEmailTIL.visibility = View.VISIBLE
        binding.btnEmailSave.root.visibility = View.VISIBLE
    }

    private fun setUpSaveButton() {
        binding.btnEmailSave.tvButton.text = getString(R.string.save)
        binding.btnEmailSave.root.setOnClickListener {
            val newEmail = binding.etNewEmail.text.toString()
            if (newEmail.isEmpty()) {
                binding.etNewEmail.error = getString(R.string.required)
                return@setOnClickListener
            }
            editEmailViewModel.updateEmail(newEmail)
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
        Log.d(TAG, "validateEmailAndPassword: is valid: $isValid")
        return isValid
    }

    private fun setUpCancelButton() {
        binding.btnEmailCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "EditEmailDialog"
    }
}