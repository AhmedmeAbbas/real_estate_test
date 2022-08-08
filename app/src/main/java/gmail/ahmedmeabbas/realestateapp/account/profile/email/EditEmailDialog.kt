package gmail.ahmedmeabbas.realestateapp.account.profile.email

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
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.EDIT_EMAIL_FAILURE
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.EDIT_EMAIL_SUCCESS
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.LOGIN_REQUIRED
import gmail.ahmedmeabbas.realestateapp.databinding.DialogEditEmailBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditEmailDialog : BottomSheetDialogFragment() {

    private var _binding: DialogEditEmailBinding? = null
    private val binding get() = _binding!!
    private val editEmailViewModel: EditEmailViewModel by activityViewModels()
    private var currentEmail: String? = null

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
        observeCurrentEmail()
        observeLoading()
        observeMessages()
    }

    private fun observeCurrentEmail() {
        viewLifecycleOwner.lifecycleScope.launch {
            editEmailViewModel.currentEmailLiveData.observe(viewLifecycleOwner) {
                currentEmail = it
            }
        }
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editEmailViewModel.uiState
                    .map { it.isLoading }
                    .collect { isLoading ->
                        updateViews(isLoading)
                    }
            }
        }
    }

    private fun updateViews(isLoading: Boolean) {
        val show = if (isLoading) View.VISIBLE else View.GONE
        val hide = if (isLoading) View.GONE else View.VISIBLE
        binding.btnEmailSave.tvButton.visibility = hide
        binding.btnEmailSave.progressBar.visibility = show
        binding.btnEmailSave.root.isEnabled = !isLoading
    }

    private fun observeMessages() {
        val failureMessage = getString(R.string.edit_email_error)
        val successMessage = getString(R.string.edit_email_success)
        val loginRequired = getString(R.string.error_recent_login_required)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                editEmailViewModel.uiState
                    .map { it.userMessage }
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            EDIT_EMAIL_SUCCESS -> showMessage(successMessage)
                            LOGIN_REQUIRED -> showMessage(loginRequired)
                            EDIT_EMAIL_FAILURE -> showMessage(failureMessage)
                            else -> return@collect
                        }
                        editEmailViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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
            if (!validateEmail(inputCurrentEmail)) return@setOnClickListener
            if (inputCurrentEmail != currentEmail) {
                showMessage(getString(R.string.error_invalid_email))
                return@setOnClickListener
            }
            setUpNewEmailViews()
        }
    }

    private fun setUpNewEmailViews() {
        binding.currentEmailTIL.visibility = View.GONE
        binding.btnEmailContinue.root.visibility = View.GONE
        binding.tvEditEmailSub.visibility = View.VISIBLE
        binding.newEmailTIL.visibility = View.VISIBLE
        binding.btnEmailSave.root.visibility = View.VISIBLE
    }

    private fun setUpSaveButton() {
        binding.btnEmailSave.tvButton.text = getString(R.string.save)
        binding.btnEmailSave.root.setOnClickListener {
            val newEmail = binding.etNewEmail.text.toString()
            if (!validateEmail(newEmail)) return@setOnClickListener
            binding.etNewEmail.clearFocus()
            editEmailViewModel.updateEmail(newEmail)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.etCurrentEmail.error = getString(R.string.required)
            false
        } else true
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