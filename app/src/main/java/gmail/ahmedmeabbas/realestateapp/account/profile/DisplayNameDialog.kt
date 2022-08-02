package gmail.ahmedmeabbas.realestateapp.account.profile

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.DialogDisplayNameBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class DisplayNameDialog: BottomSheetDialogFragment() {

    private var _binding: DialogDisplayNameBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setUpEditText() {
        val colorSecondary = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEditDisplayName.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.editDisplayNameTIL.apply {
                val personOutlined =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_person, null)
                val personFilled =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_person_selected, null)
                val drawable = if (hasFocus) personFilled else personOutlined
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    private fun setUpSaveButton() {
        binding.btnDisplayNameSave.tvButton.text = getString(R.string.save)

        binding.btnDisplayNameSave.root.setOnClickListener {
            binding.btnDisplayNameSave.apply {
                root.isEnabled = false
                tvButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpCancelButton() {
        binding.btnDisplayNameCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}