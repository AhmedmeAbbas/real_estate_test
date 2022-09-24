package gmail.ahmedmeabbas.realestateapp.search.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSearchBinding
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.domain.mapToApartmentItem
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.domain.mapToHouseItem
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.search.data.ListingItem
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var listingAdapter: ListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeListings()
        binding.tvMap.setOnClickListener {
            binding.tvMap.visibility = View.INVISIBLE
            binding.tvList.visibility = View.VISIBLE
        }

        binding.tvList.setOnClickListener {
            binding.tvList.visibility = View.INVISIBLE
            binding.tvMap.visibility = View.VISIBLE
        }
    }

    private fun observeListings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState
                    .map { it.listings }
                    .collect { listings ->
                        if (listings.filterNotNull().isNotEmpty()) {
                            val listingItems = getListingItems(listings)
                            listingAdapter.submitList(listingItems)
                        }
                    }
            }
        }
    }

    private fun getListingItems(listings: List<Listing?>): List<ListingItem> {
        val listingItems = mutableListOf<ListingItem>()
        for (listing in listings) {
            val address = getFullAddress(listing!!)
            Log.d(TAG, "getListingItems: price: ${listing.price}")
            val listingItem = when (listing.type) {
                PropertyType.APARTMENT -> listing.mapToApartmentItem(address)
                PropertyType.HOUSE -> listing.mapToHouseItem(address)
                else -> ListingItem()
            }
            listingItems.add(listingItem)
        }
        return listingItems
    }

    private fun setUpRecyclerView() {
        listingAdapter = ListingAdapter()
        binding.rvSearchListings.adapter = listingAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFullAddress(listing: Listing): String {
        val address: String
        val city = listing.city
        val region = listing.region
        val block = getString(
            R.string.double_string,
            getString(R.string.listing_preview_block),
            listing.block.toString()
        )
        val propertyNumber = listing.propertyNumber.toString()
        address = if (Locale.getDefault().language == "en") {
            "$propertyNumber, $block, $region, $city"
        } else {
            "$propertyNumber، $block، $region، $city"
        }
        return address
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}