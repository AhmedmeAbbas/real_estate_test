package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPropertyTypeBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType

class PropertyTypeFragment : Fragment() {

    private var _binding: FragmentPropertyTypeBinding? = null
    private val binding get() = _binding!!
    private val propertyTypeViewModel: PropertyTypeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListeners()
        setUpToolbar()
    }

    private fun setUpClickListeners() {
        binding.tvPropertyTypeHouse.setOnClickListener {
            setPropertyType(PropertyType.HOUSE)
            navigateToAdvertiserInfo()
        }
        binding.tvPropertyTypeApartment.setOnClickListener {
            setPropertyType(PropertyType.APARTMENT)
            navigateToAdvertiserInfo()
        }
        binding.tvPropertyTypeBuilding.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        binding.tvPropertyTypeStore.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        binding.tvPropertyTypeLand.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        binding.tvPropertyTypeWarehouse.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        binding.tvPropertyTypeFarm.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPropertyType(propertyType: String) {
        propertyTypeViewModel.setPropertyType(propertyType)
    }

    private fun navigateToAdvertiserInfo() {
        findNavController().navigate(R.id.action_propertyTypeFragment_to_advertiserInfoFragment)
    }

    private fun setUpToolbar() {
        binding.toolbarPropertyType.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PropertyTypeFragment"
    }
}