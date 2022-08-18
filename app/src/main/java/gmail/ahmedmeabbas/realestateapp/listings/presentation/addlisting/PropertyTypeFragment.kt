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
        setUpContinueButton()
        setUpToolbar()
    }

    private fun setUpCheckedChipListener() {
        binding.cgPropertyType.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                updateButton(false)
            } else {
                updateButton(true)
            }
        }
    }

    private fun updateButton(enabled: Boolean) {
        val alpha = if (enabled) 1.0F else 0.3F
        binding.btnContinue.root.isEnabled = enabled
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
                    Toast.makeText(requireContext(), "apartment", Toast.LENGTH_SHORT).show()
                }
                R.id.chipApartmentBuilding -> {
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