package gmail.ahmedmeabbas.realestateapp.account.profile

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.DialogEditPasswordBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditPasswordDialog: BottomSheetDialogFragment() {

    private var _binding: DialogEditPasswordBinding? = null
    private val binding get() = _binding!!

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
        setUpSaveButton()
        setUpCancelButton()
    }

    private fun setUpEditTexts() {
        val colorSecondary = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        val lockOutlined =
            ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
        val lockFilled =
            ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock_selected, null)

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

    private fun setUpSaveButton() {
        hideEditTexts()
        binding.btnPasswordSave.tvButton.text = getString(R.string.button_continue)

        binding.btnPasswordSave.root.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                binding.btnPasswordSave.tvButton.visibility = View.GONE
                binding.btnPasswordSave.progressBar.visibility = View.VISIBLE
                delay(1000)
                binding.currentPasswordTIL.visibility = View.GONE
                showEditTexts()
                binding.btnPasswordSave.tvButton.visibility = View.VISIBLE
                binding.btnPasswordSave.progressBar.visibility = View.GONE
                binding.btnPasswordSave.tvButton.text = getString(R.string.save)
            }
        }
    }

    private fun hideEditTexts() {
        binding.newPasswordTIL.visibility = View.GONE
        binding.tvEditPasswordMinChars.visibility = View.GONE
        binding.confirmPasswordTIL.visibility = View.GONE
    }

    private fun showEditTexts() {
        binding.newPasswordTIL.visibility = View.VISIBLE
        binding.tvEditPasswordMinChars.visibility = View.VISIBLE
        binding.confirmPasswordTIL.visibility = View.VISIBLE
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