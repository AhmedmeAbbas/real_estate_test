package gmail.ahmedmeabbas.realestateapp.listings.presentation.addlisting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAddListingBinding
import gmail.ahmedmeabbas.realestateapp.listings.data.Listing
import gmail.ahmedmeabbas.realestateapp.listings.data.Price
import java.util.*

class AddListingFragment : Fragment() {

    private var _binding: FragmentAddListingBinding? = null
    private val binding get() = _binding!!
    private val addListingViewModel: AddListingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpCheckedChipListener()
        setUpContinueButton()
    }

    private fun setUpToolbar() {
        binding.toolbarAddListingMain.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpCheckedChipListener() {
        binding.cgListingOwner.setOnCheckedStateChangeListener { _, checkedIds ->
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
            when (binding.cgListingOwner.checkedChipId) {
                R.id.chipOwner -> {
                    Toast.makeText(requireContext(), "owner", Toast.LENGTH_SHORT).show()
                }
                R.id.chipBroker -> {
                    Toast.makeText(requireContext(), "broker", Toast.LENGTH_SHORT).show()
                }
                else -> binding.cgListingOwner.requestFocus()
            }
        }
    }

    private fun getListing() {
        addListingViewModel.getListing("K")
    }

    private fun addListing() {
        val currentTime = Calendar.getInstance()
        currentTime.timeZone = TimeZone.getTimeZone("GMT")
        val date = currentTime.time
        Log.d(TAG, "addListing: $date")
        val listing = Listing(
            "1",
            "KHARTOUM",
            "KHARTOUM",
            "Jabra",
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            "house",
            30000.00,
            "401, Block. 41, Jabra, Khartoum",
            2,
            6,
            4,
            478.21,
            date,
            null,
            null,
            "Beautiful house...",
            1990,
            "Corner",
            null,
            "Next to abc market",
            false,
            true,
            2,
            "Outside",
            "Owner",
            true,
            true,
            "Main line",
            "Partially finished",
            "RC structure",
            true,
            false,
            "Septic tank",
            listOf(Price(date, 30000.00)),
            "For sale"
        )
        addListingViewModel.addListing(listing)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AddListingFragment"
    }
}