package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import android.content.res.ColorStateList
import android.os.Bundle
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
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.databinding.DialogResetPasswordBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ResetPasswordDialog: BottomSheetDialogFragment() {

    private var _binding: DialogResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val resetPasswordViewModel: ResetPasswordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCancelButton()
        setUpEditText()
        setUpSaveButton()
        observeLoading()
        observeMessages()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                resetPasswordViewModel.uiState
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
        binding.btnResetPasswordSave.tvButton.visibility = hide
        binding.btnResetPasswordSave.progressBar.visibility = show
        binding.btnResetPasswordSave.root.isEnabled = !isLoading
    }

    private fun observeMessages() {
        val successMessage = getString(R.string.reset_password_success)
        val failureMessage = getString(R.string.reset_password_error)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                resetPasswordViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            AuthRepositoryImpl.RESET_PASSWORD_SUCCESS -> showMessage(successMessage)
                            AuthRepositoryImpl.RESET_PASSWORD_FAILURE -> showMessage(failureMessage)
                            else -> return@collect
                        }
                        resetPasswordViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpSaveButton() {
        binding.btnResetPasswordSave.tvButton.text = getString(R.string.send)
        binding.btnResetPasswordSave.root.setOnClickListener {
            val email = binding.etResetPassword.text.toString()
            if (email.isEmpty()) {
                binding.etResetPassword.error = getString(R.string.required)
                return@setOnClickListener
            }
            resetPasswordViewModel.sendPasswordResetEmail(email)
        }
    }

    private fun setUpEditText() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etResetPassword.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.resetPasswordTIL.apply {
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

    private fun setUpCancelButton() {
        binding.btnResetPasswordCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}