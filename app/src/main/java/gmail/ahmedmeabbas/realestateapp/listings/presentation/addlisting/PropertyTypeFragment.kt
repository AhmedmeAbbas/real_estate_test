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

        setUpContinueButton()
        updateButton(binding.cgPropertyType.checkedChipIds.isEmpty())
        setUpCheckedChipListener()
        setUpToolbar()
    }

    private fun setUpCheckedChipListener() {
        binding.cgPropertyType.setOnCheckedStateChangeListener { _, checkedIds ->
            updateButton(checkedIds.isEmpty())
        }
    }

    private fun updateButton(isIdsEmpty: Boolean) {
        val alpha = if (isIdsEmpty) 0.3F else 1.0F
        binding.btnContinue.root.isEnabled = !isIdsEmpty
        binding.btnContinue.root.alpha = alpha
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.button_continue)

        binding.btnContinue.root.setOnClickListener {
            when (binding.cgPropertyType.checkedChipId) {
                R.id.chipHouse -> {
                    Toast.makeText(requireContext(), "house", Toast.LENGTH_SHORT).show()
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
                else -> binding.cgPropertyType.requestFocus()
            }
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
}