package gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentApartmentListingBinding
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation.adapters.ItemSliderAdapter
import gmail.ahmedmeabbas.realestateapp.listings.models.*
import gmail.ahmedmeabbas.realestateapp.listings.models.Currency
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class ApartmentListingFragment : Fragment() {

    private var _binding: FragmentApartmentListingBinding? = null
    private val binding get() = _binding!!
    private val apartmentListingViewModel: ApartmentListingViewModel by activityViewModels()
    private val args: ApartmentListingFragmentArgs by navArgs()
    private var listing = Listing()
    private var property = Property.Apartment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apartmentListingViewModel.getListingById(args.listingId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApartmentListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hidePreviewViews()
        setUpToolbar()
        observeListing()
        observeLoading()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apartmentListingViewModel.uiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect {
                        Log.d(TAG, "observeLoading: loading: $it")
                        updateViews(it)
                    }
            }
        }
    }

    private fun updateViews(isLoading: Boolean) {
        val pbVisibility = if (isLoading) View.VISIBLE else View.GONE
        val viewsVisibility = if (isLoading) View.GONE else View.VISIBLE

        with(binding) {
            pbLoadingListing.visibility = pbVisibility
            apartmentHeader.root.visibility = viewsVisibility
            apartmentInstallments.root.visibility = viewsVisibility
            apartmentAbout.root.visibility = viewsVisibility
            apartmentDetails.root.visibility = viewsVisibility
            apartmentUtilities.root.visibility = viewsVisibility
            contactAdvertiser.root.visibility = viewsVisibility
        }
    }

    private fun setUpContact() {
        setUpContactHeader()
        setUpName()
        setUpPhoneNumber()
        setUpEmail()
    }

    private fun setUpName() {
        if (listing.name == null) {
            binding.contactAdvertiser.tvName.visibility = View.GONE
            return
        }
        binding.contactAdvertiser.tvName.text = getString(R.string.single_string, listing.name)
    }

    private fun setUpEmail() {
        setUpStringValues(
            binding.contactAdvertiser.tvEmail,
            binding.contactAdvertiser.tvEmailValue,
            listing.email
        )
        binding.contactAdvertiser.tvEmailValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.add_listing_email), listing.email)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpPhoneNumber() {
        var phoneNumber = listing.phoneNumber.toString()
        if (phoneNumber.length == 9) phoneNumber = "0$phoneNumber"
        if (phoneNumber.length == 12) phoneNumber = "+$phoneNumber"
        setUpStringValues(
            binding.contactAdvertiser.tvPhoneNumber,
            binding.contactAdvertiser.tvPhoneNumberValue,
            phoneNumber
        )
        binding.contactAdvertiser.tvPhoneNumberValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText(getString(R.string.add_listing_phone_number), phoneNumber)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpContactHeader() {
        val owner = getString(R.string.listing_preview_contact_owner)
        val broker = getString(R.string.listing_preview_contact_broker)
        if (listing.advertiser == Advertiser.OWNER) {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, owner)
        } else {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, broker)
        }
    }

    private fun setUpUtilities() {
        setUpBooleanValues(
            binding.apartmentUtilities.tvElectricity,
            binding.apartmentUtilities.tvElectricityValue,
            property.electricity
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvWater,
            binding.apartmentUtilities.tvWaterValue,
            property.water
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvElevator,
            binding.apartmentUtilities.tvElevatorValue,
            property.elevator
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvParking,
            binding.apartmentUtilities.tvParkingValue,
            property.parking
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvBackupGenerator,
            binding.apartmentUtilities.tvBackupGeneratorValue,
            property.backupGenerator
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvSecurity,
            binding.apartmentUtilities.tvSecurityValue,
            property.security
        )
        if (property.utilitiesMoreInfo == null) {
            binding.apartmentUtilities.tvMoreInfo.visibility = View.GONE
            binding.apartmentUtilities.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.apartmentUtilities.tvMoreInfoValue.text = property.utilitiesMoreInfo
    }

    private fun setUpApartmentDetails() {
        setUpArea()
        setUpLongValues(
            binding.apartmentDetails.tvYearBuilt,
            binding.apartmentDetails.tvYearBuiltValue,
            property.yearBuilt
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvFinishing,
            binding.apartmentDetails.tvFinishingValue,
            property.finished
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvFurniture,
            binding.apartmentDetails.tvFurnitureValue,
            property.furnished
        )
        setUpLongValues(
            binding.apartmentDetails.tvBedrooms,
            binding.apartmentDetails.tvBedroomsValue,
            property.bedrooms
        )
        setUpLongValues(
            binding.apartmentDetails.tvBathrooms,
            binding.apartmentDetails.tvBathroomsValue,
            property.bathrooms
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvKitchen,
            binding.apartmentDetails.tvKitchenValue,
            property.kitchen
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvLivingRoom,
            binding.apartmentDetails.tvLivingRoomValue,
            property.livingRoom
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvBalcony,
            binding.apartmentDetails.tvBalconyValue,
            property.balcony
        )
        if (property.floorNumber == 0L) {
            setUpStringValues(
                binding.apartmentDetails.tvFloor,
                binding.apartmentDetails.tvFloorValue,
                getString(R.string.add_apartment_floor_ground)
            )
        } else {
            setUpLongValues(
                binding.apartmentDetails.tvFloor,
                binding.apartmentDetails.tvFloorValue,
                property.floorNumber
            )
        }
        if (property.detailsMoreInfo == null) {
            binding.apartmentDetails.tvMoreInfo.visibility = View.GONE
            binding.apartmentDetails.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.apartmentDetails.tvMoreInfoValue.text = property.detailsMoreInfo
    }

    private fun setUpBooleanValues(textView: TextView, value: TextView, choice: Boolean?) {
        if (choice == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = if (choice) getString(R.string.yes) else getString(R.string.no)
    }

    private fun setUpLongValues(textView: TextView, value: TextView, detail: Long?) {
        if (detail == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = getString(R.string.single_string, detail.toString())
    }

    private fun setUpStringValues(textView: TextView, value: TextView, detail: String?) {
        if (detail == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = getString(R.string.single_string, detail)
    }

    private fun setUpArea() {
        if (property.area == null) {
            binding.apartmentDetails.tvArea.visibility = View.GONE
            binding.apartmentDetails.tvAreaValue.visibility = View.GONE
            return
        }
        binding.apartmentDetails.tvAreaValue.text =
            getString(R.string.listing_item_area_m2, formatDouble(property.area!!))
    }

    private fun setUpAbout() {
        if (listing.additionalInfo.isNullOrEmpty()) {
            binding.apartmentAbout.root.visibility = View.GONE
            return
        }
        binding.apartmentAbout.tvAboutHeader.text =
            getString(R.string.listing_preview_about_apartment)
        binding.apartmentAbout.tvAboutBody.text = listing.additionalInfo
    }

    private fun setUpInstallments() {
        if (listing.installments == null || listing.installments == false) {
            binding.apartmentInstallments.root.visibility = View.GONE
            Log.d(TAG, "setUpInstallments: ${binding.apartmentInstallments.root.isVisible}")
            return
        }
        val downPayment = formatDouble(listing.downPayment!!)
        setUpMoneyText(binding.apartmentInstallments.tvDownPaymentValue, downPayment)

        val monthlyInstallment = formatDouble(listing.monthlyInstallment!!)
        setUpMoneyText(binding.apartmentInstallments.tvMonthlyInstallmentValue, monthlyInstallment)

        binding.apartmentInstallments.tvInstallmentPeriodValue.text = getString(
            R.string.listing_preview_installment_period_value,
            listing.installmentPeriod.toString()
        )
    }

    private fun setUpHeader() {
        setUpPhotos()
        setUpPrice()
        setUpAddress()
        setUpType()
        setUpAdvertiser()
        setUpDateAdded()
        setUpStatus()
    }

    private fun setUpStatus() {
        binding.apartmentHeader.tvStatusValue.text =
            if (listing.listingStatus == ListingStatus.FOR_SALE) getString(
                R.string.listing_preview_status_for_sale
            ) else getString(R.string.listing_preview_status_sold)
    }

    private fun setUpDateAdded() {
        binding.apartmentHeader.tvDateAddedValue.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(listing.dateAdded?.toDate()?.time)
    }


    private fun setUpAdvertiser() {
        val owner = getString(R.string.listing_preview_for_sale_by_owner)
        val broker = getString(R.string.listing_preview_for_sale_by_broker)
        if (listing.advertiser == Advertiser.OWNER) {
            binding.apartmentHeader.tvAdvertiser.text = owner
        } else {
            binding.apartmentHeader.tvAdvertiser.text = broker
        }
    }

    private fun setUpType() {
        binding.apartmentHeader.tvType.text = getString(R.string.property_type_apartment)
    }

    private fun setUpAddress() {
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
        binding.apartmentHeader.tvAddress.text = address
    }

    private fun setUpPhotos() {
        setUpEmptyPhotos()
        binding.apartmentHeader.previewViewPager.adapter =
            ItemSliderAdapter(listing.photos)
        TabLayoutMediator(
            binding.apartmentHeader.previewTabLayout,
            binding.apartmentHeader.previewViewPager
        ) { _, _ -> }.attach()
    }

    private fun setUpEmptyPhotos() {
        if (listing.photos.isEmpty()) {
            binding.apartmentHeader.previewTabLayout.visibility = View.GONE
            binding.apartmentHeader.previewViewPager.visibility = View.GONE
            binding.apartmentHeader.ivPlaceholderPhoto.visibility = View.VISIBLE
        }
    }

    private fun setUpPrice() {
        val price = formatDouble(listing.price!!)
        setUpMoneyText(binding.apartmentHeader.tvPrice, price)
    }

    private fun observeListing() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apartmentListingViewModel.uiState
                    .map { it.listing }
                    .distinctUntilChanged()
                    .collect {
                        Log.d(TAG, "observeListing: $it")
                        it?.let {
                            listing = it
                            property = it.property as Property.Apartment
                            setUpViews()
                        }
                    }
            }
        }
    }

    private fun setUpMoneyText(textView: TextView, input: String) {
        if (Locale.getDefault().language == "en" && listing.currency == getString(R.string.add_listing_currency_usd)) {
            textView.text =
                getString(R.string.double_string, getString(R.string.add_listing_usd_prefix), input)
        } else {
            textView.text = getString(R.string.double_string, input, " ${getCurrencySuffix()}")
        }
    }

    private fun getCurrencySuffix(): String {
        return when (listing.currency) {
            Currency.USD -> getString(R.string.add_listing_currency_usd)
            Currency.SDG -> getString(R.string.add_listing_currency_sdg)
            else -> ""
        }
    }

    private fun formatDouble(d: Double): String {
        return DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(d)
    }

    private fun setUpViews() {
        setUpHeader()
        setUpInstallments()
        setUpAbout()
        setUpApartmentDetails()
        setUpUtilities()
        setUpContact()
    }

    private fun hidePreviewViews() {
        binding.apartmentHeader.tvPreviewSub.visibility = View.GONE
    }

    private fun setUpToolbar() {
        binding.toolbarApartmentListing.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: triggered")
        apartmentListingViewModel.clearListing()
    }

    companion object {
        private const val TAG = "ApartmentListingFragment"
    }
}