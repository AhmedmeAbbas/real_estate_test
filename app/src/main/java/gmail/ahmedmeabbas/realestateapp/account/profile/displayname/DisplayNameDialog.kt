package gmail.ahmedmeabbas.realestateapp.account.profile.displayname

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
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.FAILURE
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.NETWORK_ERROR
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl.Companion.SUCCESS
import gmail.ahmedmeabbas.realestateapp.databinding.DialogDisplayNameBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DisplayNameDialog : BottomSheetDialogFragment() {

    private var _binding: DialogDisplayNameBinding? = null
    private val binding get() = _binding!!
    private val displayNameViewModel: DisplayNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDisplayNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditText()
        setUpSaveButton()
        setUpCancelButton()
        observeDisplayName()
        observeLoading()
        observeMessages()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayNameViewModel.uiState
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
        binding.btnDisplayNameSave.tvButton.visibility = hide
        binding.btnDisplayNameSave.progressBar.visibility = show
        binding.btnDisplayNameSave.root.isEnabled = !isLoading
    }

    private fun observeDisplayName() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayNameViewModel.uiState
                    .map { it.displayName }
                    .distinctUntilChanged()
                    .collect {
                        binding.tvDialogCurrentDisplayName.text =
                            getString(R.string.edit_display_name_current, it)
                    }
            }
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayNameViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        if (userMessage.isEmpty()) return@collect
                        when (userMessage) {
                            SUCCESS -> {
                                displayNameViewModel.fetchDisplayName()
                                showMessage(getString(R.string.display_name_success))
                            }
                            NETWORK_ERROR -> showMessage(getString(R.string.error_network))
                            FAILURE -> showMessage(getString(R.string.display_name_error))
                            else -> return@collect
                        }
                        displayNameViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpEditText() {
        val colorSecondary =
            requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEditDisplayName.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.editDisplayNameTIL.apply {
                val personOutlined =
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_person,
                        null
                    )
                val personFilled =
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_person_selected,
                        null
                    )
                val drawable = if (hasFocus) personFilled else personOutlined
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    private fun setUpSaveButton() {
        binding.btnDisplayNameSave.tvButton.text = getString(R.string.save)

        binding.btnDisplayNameSave.root.setOnClickListener {
            val newName = binding.etEditDisplayName.text.toString()
            if (!validateName(newName)) return@setOnClickListener
            binding.etEditDisplayName.clearFocus()
            displayNameViewModel.updateDisplayName(newName)
        }
    }

    private fun validateName(name: String): Boolean {
        return if (name.isEmpty()) {
            binding.etEditDisplayName.error = getString(R.string.required)
            false
        } else true
    }

    private fun setUpCancelButton() {
        binding.btnDisplayNameCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onStart() {
        super.onStart()
        displayNameViewModel.fetchDisplayName()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DisplayNameDialog"
    }
}