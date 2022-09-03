package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPropertyAddressBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class PropertyAddressFragment : Fragment() {

    private var _binding: FragmentPropertyAddressBinding? = null
    private val binding get() = _binding!!
    private val propertyAddressViewModel: PropertyAddressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
        setUpStateSpinner()
        setUpCitySpinner()
        setUpSelectRegion()
        setUpContinueButton()
    }

    private fun setUpStateSpinner() {
        val states =
            listOf(getString(R.string.add_listing_state_spinner), "Khartoum")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, states)
        binding.spinnerState.adapter = adapter
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) resetTextError(binding.tvState)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setUpProgressColor() {
        with(binding) {
            progressLayout.ivAdvertiserInfo.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line1.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPropertyAddress.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
        }

    }

    private fun setUpProgressLayout() {
        when (propertyAddressViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun setUpCitySpinner() {
        val cities =
            listOf(getString(R.string.add_listing_city_spinner), "Khartoum", "Omdurman", "Bahri")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        binding.spinnerCity.adapter = adapter
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) resetTextError(binding.tvCity)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setUpSelectRegion() {
        val regions = listOf(
            "Almansheya",
            "Altayef",
            "Alryiadh",
            "Almamoura",
            "Alblabel",
            "Aljref",
            "Arkweet",
            "Alamarat",
            "Aldem",
            "Asahafa",
            "Burri",
            "Nasir"
        )
        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._400sdp).toInt()
        binding.tvSelectRegion.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.apply {
                setContentView(R.layout.dialog_select_region)
                window?.setLayout(dialogWidth, dialogHeight)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
            val etEnterRegion = dialog.findViewById<EditText>(R.id.etEnterRegion)
            val lvSelectRegion = dialog.findViewById<ListView>(R.id.lvSelectRegion)
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, regions)
            lvSelectRegion.adapter = adapter
            etEnterRegion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            lvSelectRegion.setOnItemClickListener { _, _, position, _ ->
                binding.tvSelectRegion.text = adapter.getItem(position)
                dialog.dismiss()
            }
        }
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            val state = binding.spinnerState.selectedItem.toString()
            val city = binding.spinnerCity.selectedItem.toString()
            val region = binding.tvSelectRegion.text.toString()
            val blockInput = binding.etBlock.text.toString()
            val propertyNumberInput = binding.etPropertyNumber.text.toString()

            if (!validateForm(
                    state,
                    city,
                    region,
                    blockInput,
                    propertyNumberInput
                )
            ) return@setOnClickListener
            propertyAddressViewModel.addPropertyAddress(
                state,
                city,
                region,
                blockInput.toInt(),
                propertyNumberInput.toInt()
            )
            navigateToNextScreen()
        }
    }

    private fun validateForm(
        state: String,
        city: String,
        region: String,
        blockInput: String,
        propertyNumberInput: String
    ): Boolean {
        var isValid = true

        if (state == getString(R.string.add_listing_state_spinner)) {
            showTextError(binding.tvState)
            isValid = false
        }
        if (city == getString(R.string.add_listing_city_spinner)) {
            showTextError(binding.tvCity)
            isValid = false
        }
        if (region == getString(R.string.add_listing_select_region)) {
            showTextError(binding.tvRegion)
            setUpRegionTextWatcher()
            isValid = false
        }
        if (blockInput.isEmpty()) {
            binding.blockTIL.error = getString(R.string.required)
            setUpBlockErrorListener()
            // Returning false here instead of isValid to skip the format check
            isValid = false
        } else {
            try {
                blockInput.toInt()
            } catch (e: Exception) {
                binding.blockTIL.error = getString(R.string.error_invalid_number)
                setUpBlockErrorListener()
                isValid = false
            }
        }
        if (propertyNumberInput.isEmpty()) {
            binding.propertyNumberTIL.error = getString(R.string.required)
            setUpPropertyNumberErrorListener()
            // Returning false here instead of isValid to skip the format check
            isValid = false
        } else {
            try {
                propertyNumberInput.toInt()
            } catch (e: Exception) {
                binding.propertyNumberTIL.error = getString(R.string.error_invalid_number)
                setUpPropertyNumberErrorListener()
                isValid = false
            }
        }
        return isValid
    }

    private fun setUpPropertyNumberErrorListener() {
        binding.etPropertyNumber.setOnClickListener {
            binding.propertyNumberTIL.error = null
        }
        binding.etPropertyNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.propertyNumberTIL.error = null
        }
    }

    private fun setUpBlockErrorListener() {
        binding.etBlock.setOnClickListener {
            binding.blockTIL.error = null
        }
        binding.etBlock.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.blockTIL.error = null
        }
    }

    private fun setUpRegionTextWatcher() {
        binding.tvSelectRegion.doOnTextChanged { text, _, _, _ ->
            if (text != getString(R.string.add_listing_select_region)) {
                resetTextError(binding.tvRegion)
            }
        }
    }

    private fun showTextError(textView: TextView) {
        textView.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_error,
                    requireActivity().theme
                ),
                null, null, null
            )
            setTextColor(ResourcesCompat.getColor(resources, R.color.red, requireActivity().theme))
            compoundDrawablePadding = 15
        }
    }

    private fun resetTextError(textView: TextView) {
        textView.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            setTextColor(requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary))
        }
    }

    private fun navigateToNextScreen() {
        when (propertyAddressViewModel.getPropertyType()) {
            PropertyType.HOUSE -> navigateTo(R.id.action_propertyAddressFragment_to_constructionDetailsFragment)
            PropertyType.APARTMENT -> navigateTo(R.id.action_propertyAddressFragment_to_apartmentDetailsFragment)
            else -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_occurred),
                    Snackbar.LENGTH_SHORT
                ).show()
                navigateTo(R.id.action_propertyAddressFragment_to_propertyTypeFragment)
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbarPropertyAddress.setNavigationOnClickListener {
            findNavController().navigateUp()
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