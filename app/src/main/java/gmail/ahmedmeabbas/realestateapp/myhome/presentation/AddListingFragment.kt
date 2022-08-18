package gmail.ahmedmeabbas.realestateapp.myhome.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAddListingBinding
import gmail.ahmedmeabbas.realestateapp.listings.Listing
import gmail.ahmedmeabbas.realestateapp.listings.Price
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

        setUpButton()
    }

    private fun setUpButton() {
        binding.btnAdd.text = "Get listing"
        binding.btnAdd.setOnClickListener {
            getListing()
            //addListing()
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