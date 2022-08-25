package gmail.ahmedmeabbas.realestateapp.listings.presentation.addlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPropertyTypeBinding

class PropertyTypeFragment: Fragment() {

    private var _binding: FragmentPropertyTypeBinding? = null
    private val binding get() = _binding!!

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

        setUpCheckedChipListener()
        setUpToolbar()
    }

    private fun setUpCheckedChipListener() {
        binding.cgPropertyType.setOnCheckedStateChangeListener { _, _ ->
            when (binding.cgPropertyType.checkedChipId) {
                R.id.chipHouse -> {
                    navigateTo(R.id.action_propertyTypeFragment_to_addHouseFragment)
                }
                R.id.chipApartment -> {
                    navigateTo(R.id.action_propertyTypeFragment_to_addApartmentFragment)
                }
                R.id.chipBuilding -> {
                    Toast.makeText(requireContext(), "building", Toast.LENGTH_SHORT).show()
                }
                R.id.chipStore -> {
                    Toast.makeText(requireContext(), "store", Toast.LENGTH_SHORT).show()
                }
                R.id.chipLand -> {
                    Toast.makeText(requireContext(), "land", Toast.LENGTH_SHORT).show()
                }
                R.id.chipWarehouse -> {
                    Toast.makeText(requireContext(), "warehouse", Toast.LENGTH_SHORT).show()
                }
                R.id.chipFarm -> {
                    Toast.makeText(requireContext(), "farm", Toast.LENGTH_SHORT).show()
                }
                else -> return@setOnCheckedStateChangeListener
            }
            binding.cgPropertyType.clearCheck()
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
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