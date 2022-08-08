package gmail.ahmedmeabbas.realestateapp.account.profile.email

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
import gmail.ahmedmeabbas.realestateapp.databinding.DialogEditEmailBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditEmailDialog : BottomSheetDialogFragment() {

    private var _binding: DialogEditEmailBinding? = null
    private val binding get() = _binding!!

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
        setUpSaveButton()
        setUpCancelButton()
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

    private fun setUpSaveButton() {
        binding.btnEmailSave.tvButton.text = getString(R.string.button_continue)

        binding.btnEmailSave.root.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                binding.btnEmailSave.tvButton.visibility = View.GONE
                binding.btnEmailSave.progressBar.visibility = View.VISIBLE
                delay(1000)
                binding.currentEmailTIL.visibility = View.GONE
                binding.newEmailTIL.visibility = View.VISIBLE
                binding.btnEmailSave.tvButton.visibility = View.VISIBLE
                binding.btnEmailSave.progressBar.visibility = View.GONE
                binding.btnEmailSave.tvButton.text = getString(R.string.save)
            }
        }
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
}