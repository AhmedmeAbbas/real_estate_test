package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentApartmentDetailsBinding

class ApartmentDetailsFragment : Fragment() {

    private var _binding: FragmentApartmentDetailsBinding? = null
    private val binding get() = _binding!!

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
        setUpViews()
        setUpBedrooms()
        setUpBathrooms()
        setUpFloor()
        setUpContinueButton()
    }

    private fun setUpFloor() {
        binding.floor.apply {
            tvHeader.text = getString(R.string.add_apartment_floor)
            textInputLayout.hint = getString(R.string.add_apartment_floor_hint)
            chip0.text = getString(R.string.add_apartment_floor_ground)
        }
        binding.floor.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.floor.textInputLayout, true)
            } else {
                enableTIL(binding.floor.textInputLayout, false)
            }
        }
    }

    private fun setUpBedrooms() {
        binding.bedrooms.apply {
            tvHeader.text = getString(R.string.add_apartment_bedrooms)
            textInputLayout.hint = getString(R.string.add_apartment_bedrooms_hint)
        }
        binding.bedrooms.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.bedrooms.textInputLayout, true)
            } else {
                enableTIL(binding.bedrooms.textInputLayout, false)
            }
        }
    }

    private fun setUpBathrooms() {
        binding.bathrooms.apply {
            tvHeader.text = getString(R.string.add_apartment_bathrooms)
            textInputLayout.hint = getString(R.string.add_apartment_bathrooms_hint)
        }
        binding.bathrooms.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipOther) {
                enableTIL(binding.bathrooms.textInputLayout, true)
            } else {
                enableTIL(binding.bathrooms.textInputLayout, false)
            }
        }
    }

    private fun enableTIL(view: View, enable: Boolean) {
        val alpha = if (enable) 1.0F else 0.6F
        view.alpha = alpha
        view.isEnabled = enable
    }

    private fun setUpViews() {
        binding.kitchen.tvHeader.text = getString(R.string.add_apartment_kitchen)
        binding.livingRoom.tvHeader.text = getString(R.string.add_apartment_living_rooms)
        binding.balcony.tvHeader.text = getString(R.string.add_apartment_balcony)
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            findNavController().navigate(R.id.action_apartmentDetailsFragment_to_utilitiesApartmentFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarAddApartment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}