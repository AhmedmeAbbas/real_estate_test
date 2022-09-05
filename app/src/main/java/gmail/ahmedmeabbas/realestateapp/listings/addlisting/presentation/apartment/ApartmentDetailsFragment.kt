package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentApartmentDetailsBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class ApartmentDetailsFragment : Fragment() {

    private var _binding: FragmentApartmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val apartmentDetailsViewModel: ApartmentDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApartmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
        setUpViews()
        setUpChipGroups()
        setUpContinueButton()
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
            progressLayout.line2.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.line3.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivHouseDetails.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
        }
    }

    private fun setUpProgressLayout() {
        when (apartmentDetailsViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun setUpChipGroups() {
        binding.bedrooms.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.bedrooms.textInputLayout, true)
            } else {
                enableTIL(binding.bedrooms.textInputLayout, false)
            }
        }
        binding.bathrooms.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.bathrooms.textInputLayout, true)
            } else {
                enableTIL(binding.bathrooms.textInputLayout, false)
            }
        }
        binding.floor.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.floor.textInputLayout, true)
            } else {
                enableTIL(binding.floor.textInputLayout, false)
            }
        }
    }

    private fun setUpTILHints() {
        binding.bedrooms.apply {
            tvHeader.text = getString(R.string.add_apartment_bedrooms)
            textInputLayout.hint = getString(R.string.add_apartment_bedrooms_hint)
        }
        binding.bathrooms.apply {
            tvHeader.text = getString(R.string.add_apartment_bathrooms)
            textInputLayout.hint = getString(R.string.add_apartment_bathrooms_hint)
        }
        binding.floor.apply {
            tvHeader.text = getString(R.string.add_apartment_floor)
            textInputLayout.hint = getString(R.string.add_apartment_floor_hint)
            chip0.text = getString(R.string.add_apartment_floor_ground)
        }
    }

    private fun enableTIL(view: View, enable: Boolean) {
        val alpha = if (enable) 1.0F else 0.6F
        view.alpha = alpha
        view.isEnabled = enable
    }

    private fun setUpViews() {
        binding.kitchen.tvHeader.text = getString(R.string.add_apartment_kitchen)
        binding.livingRoom.tvHeader.text = getString(R.string.add_apartment_living_room)
        binding.balcony.tvHeader.text = getString(R.string.add_apartment_balcony)
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            val area = binding.etArea.text.toString().toDoubleOrNull()
            val yearBuilt = binding.etYearBuilt.text.toString().toIntOrNull()
            val finished = getUserChoice(binding.cgFinishing)
            val furnished = getUserChoice(binding.cgFurniture)
            val bedrooms = getNumberFromChipGroup(binding.bedrooms.chipGroup, binding.bedrooms.editText)
            val bathrooms =
                getNumberFromChipGroup(binding.bathrooms.chipGroup, binding.bathrooms.editText)
            val kitchen = getUserChoice(binding.kitchen.chipGroup)
            val livingRoom = getUserChoice(binding.livingRoom.chipGroup)
            val balcony = getUserChoice(binding.balcony.chipGroup)
            val floorNumber = getNumberFromChipGroup(binding.floor.chipGroup, binding.floor.editText)
            val moreInfo = binding.etApartmentDetailsMore.text.toString().ifEmpty { null }
            apartmentDetailsViewModel.addApartmentDetails(
                area,
                yearBuilt,
                finished,
                furnished,
                bedrooms,
                bathrooms,
                kitchen,
                livingRoom,
                balcony,
                floorNumber,
                moreInfo
            )
            findNavController().navigate(R.id.action_apartmentDetailsFragment_to_utilitiesApartmentFragment)
        }
    }

    private fun getNumberFromChipGroup(chipGroup: ChipGroup, editText: TextInputEditText): Int? {
        return when (chipGroup.checkedChipId) {
            R.id.chip0 -> 0
            R.id.chip1 -> 1
            R.id.chip2 -> 2
            R.id.chip3 -> 3
            R.id.chip4 -> 4
            R.id.chipOther -> editText.text.toString().toIntOrNull()
            else -> null
        }
    }

    private fun getUserChoice(chipGroup: ChipGroup): Boolean? {
        return when (chipGroup.checkedChipId) {
            R.id.chipFinished -> true
            R.id.chipUnfinished -> false
            R.id.chipFurnished -> true
            R.id.chipUnfurnished -> false
            R.id.chipYes -> true
            R.id.chipNo -> false
            else -> null
        }
    }

    private fun setUpToolbar() {
        binding.toolbarAddApartment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onStart() {
        super.onStart()
        setUpTILHints()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ApartmentDetailsFragment"
    }
}