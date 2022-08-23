package gmail.ahmedmeabbas.realestateapp.listings.presentation.addlisting

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAddApartmentBinding

class AddApartmentFragment : Fragment() {

    private var _binding: FragmentAddApartmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val chosenPhotoUris: MutableList<Uri?> = MutableList(MAX_NUMBER_OF_PHOTOS) { null }
    private val tempPhotoUris: MutableList<Uri?> = MutableList(MAX_NUMBER_OF_PHOTOS) { null }
    private var clickedPhotoIndex: Int = -1
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                hidePermissionDeniedMessage()
                launchIntentForPhotos()
            } else {
                showPermissionDeniedMessage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddApartmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog(requireContext())

        initResultLauncher()
        setUpToolbar()
        setUpAdvertiserChipListener()
        setUpCitySpinner()
        setUpSelectRegion()
        setUpAddPhotos()
        setUpCurrencySpinner()
        setUpInstallmentsChipListener()
        setUpSubmitButton()
    }

    private fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedUri = result.data?.data
                    val clipData = result.data?.clipData
                    val photoIndex = result.data?.getIntExtra(PHOTO_INDEX, clickedPhotoIndex)
                    if (clipData != null) {
                        tempPhotoUris[photoIndex!!] = clipData.getItemAt(0).uri
                    } else if (selectedUri != null) {
                        tempPhotoUris[photoIndex!!] = selectedUri
                    } else return@registerForActivityResult
                    setImages()
                    showSaveIcon()
                }
            }
    }

    private fun showSaveIcon() {
        dialog.findViewById<ImageView>(R.id.ivAddPhotosCheck).visibility = View.VISIBLE
    }

    private fun setImages() {
        dialog.findViewById<ImageView>(R.id.ivPhoto1).setImageURI(tempPhotoUris[0])
        dialog.findViewById<ImageView>(R.id.ivPhoto2).setImageURI(tempPhotoUris[1])
        dialog.findViewById<ImageView>(R.id.ivPhoto3).setImageURI(tempPhotoUris[2])
        dialog.findViewById<ImageView>(R.id.ivPhoto4).setImageURI(tempPhotoUris[3])
        dialog.findViewById<ImageView>(R.id.ivPhoto5).setImageURI(tempPhotoUris[4])
    }

    private fun setUpAddPhotos() {
        binding.tvAddPhotos.setOnClickListener {
            val dialogWidth = binding.root.width
            val dialogHeight = binding.root.height
            dialog.apply {
                setContentView(R.layout.dialog_add_photos)
                window?.setLayout(dialogWidth, dialogHeight)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setUpDismissAddPhotos()
                setUpSavePhotos()
                show()
            }
            for (i in tempPhotoUris.indices) {
                tempPhotoUris[i] = chosenPhotoUris[i]
            }
            setImages()
            setUpPhotoClickListeners(dialog)
        }
    }

    private fun setUpDismissAddPhotos() {
        dialog.findViewById<Toolbar>(R.id.toolbarAddPhotos).setNavigationOnClickListener {
            for (i in tempPhotoUris.indices) {
                tempPhotoUris[i] = null
            }
            dialog.dismiss()
        }
    }

    private fun setUpSavePhotos() {
        dialog.findViewById<ImageView>(R.id.ivAddPhotosCheck).setOnClickListener {
            for (i in chosenPhotoUris.indices) {
                chosenPhotoUris[i] = tempPhotoUris[i]
            }
            dialog.dismiss()
        }
    }

    private fun setUpPhotoClickListeners(dialog: Dialog) {
        dialog.findViewById<ImageView>(R.id.ivPhoto1).setOnClickListener {
            clickedPhotoIndex = 0
            onPhotoClicked()
        }
        dialog.findViewById<ImageView>(R.id.ivPhoto2).setOnClickListener {
            clickedPhotoIndex = 1
            onPhotoClicked()
        }
        dialog.findViewById<ImageView>(R.id.ivPhoto3).setOnClickListener {
            clickedPhotoIndex = 2
            onPhotoClicked()
        }
        dialog.findViewById<ImageView>(R.id.ivPhoto4).setOnClickListener {
            clickedPhotoIndex = 3
            onPhotoClicked()
        }
        dialog.findViewById<ImageView>(R.id.ivPhoto5).setOnClickListener {
            clickedPhotoIndex = 4
            onPhotoClicked()
        }
    }

    private fun onPhotoClicked() {
        if (isReadPermissionGranted()) {
            launchIntentForPhotos()
        } else {
            requestReadPhotosPermission()
        }
    }

    private fun isReadPermissionGranted(): Boolean {
        val readPhotosPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        return readPhotosPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadPhotosPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun launchIntentForPhotos() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(PHOTO_INDEX, clickedPhotoIndex)
        resultLauncher.launch(intent)
    }

    private fun setUpSubmitButton() {
        binding.btnSubmitApartment.tvButton.text = getString(R.string.add_listing_submit)
    }

    private fun setUpInstallmentsChipListener() {
        binding.cgInstallments.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                enableInstallmentViews(false)
                return@setOnCheckedStateChangeListener
            }
            if (checkedIds[0] == R.id.chipInstallmentsYes) {
                enableInstallmentViews(true)
            } else {
                enableInstallmentViews(false)
            }
        }
    }

    private fun enableInstallmentViews(enable: Boolean) {
        val alpha = if (enable) 1.0F else 0.4F
        with(binding) {
            tvDownPayment.alpha = alpha
            etDownPayment.isEnabled = enable
            etDownPayment.alpha = alpha
            tvMonthlyInstallment.alpha = alpha
            etMonthlyInstallment.isEnabled = enable
            etMonthlyInstallment.alpha = alpha
            tvInstallmentPeriod.alpha = alpha
            etInstallmentPeriod.isEnabled = enable
            etInstallmentPeriod.alpha = alpha
        }
    }

    private fun setUpCurrencySpinner() {
        val currencies = listOf(
            getString(R.string.add_listing_select_currency),
            getString(R.string.add_listing_currency_usd),
            getString(R.string.add_listing_currency_sdg)
        )
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, currencies)
        binding.spinnerCurrency.adapter = adapter
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) {
                        binding.tvPrice.text = getString(R.string.add_listing_price, "")
                        binding.tvDownPayment.text =
                            getString(R.string.add_listing_down_payment, "")
                        binding.tvMonthlyInstallment.text =
                            getString(R.string.add_listing_monthly_installment, "")
                    } else {
                        binding.tvPrice.text = getString(
                            R.string.add_listing_price,
                            "\n(${adapter.getItem(position)})"
                        )
                        binding.tvDownPayment.text = getString(
                            R.string.add_listing_down_payment,
                            "\n(${adapter.getItem(position)})"
                        )
                        binding.tvMonthlyInstallment.text = getString(
                            R.string.add_listing_monthly_installment,
                            "\n(${adapter.getItem(position)})"
                        )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

    private fun setUpAdvertiserChipListener() {
        binding.cgAdvertiser.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                binding.tvAdvertiserAcknowledgement.visibility = View.GONE
                return@setOnCheckedStateChangeListener
            }
            when (checkedIds[0]) {
                R.id.chipOwner -> {
                    binding.tvAdvertiserAcknowledgement.text =
                        getString(R.string.add_listing_owner_acknowledgement)
                    binding.tvAdvertiserAcknowledgement.visibility = View.VISIBLE
                }
                R.id.chipBroker -> {
                    binding.tvAdvertiserAcknowledgement.text =
                        getString(R.string.add_listing_broker_acknowledgement)
                    binding.tvAdvertiserAcknowledgement.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpSelectRegion() {
        val regions = listOf(
            "Almansheya",
            "Altayef",
            "Alryiadh",
            "Almamoura",
            "Alblabel",
            "Aljref",
            "Arkweet",
            "Alamarat",
            "Aldem",
            "Asahafa",
            "Burri",
            "Nasir"
        )
        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._400sdp).toInt()
        binding.tvSelectRegion.setOnClickListener {
            dialog.apply {
                setContentView(R.layout.dialog_select_region)
                window?.setLayout(dialogWidth, dialogHeight)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
            val etEnterRegion = dialog.findViewById<EditText>(R.id.etEnterRegion)
            val lvSelectRegion = dialog.findViewById<ListView>(R.id.lvSelectRegion)
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, regions)
            lvSelectRegion.adapter = adapter
            etEnterRegion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            lvSelectRegion.setOnItemClickListener { _, _, position, _ ->
                binding.tvSelectRegion.text = adapter.getItem(position)
                dialog.dismiss()
            }
        }
    }

    private fun setUpCitySpinner() {
        val cities =
            listOf(getString(R.string.add_listing_city_spinner), "Khartoum", "Omdurman", "Bahri")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        binding.spinnerCity.adapter = adapter
    }

    private fun showPermissionDeniedMessage() {
        dialog.findViewById<TextView>(R.id.tvPermissionDenied).visibility = View.VISIBLE
    }

    private fun hidePermissionDeniedMessage() {
        dialog.findViewById<TextView>(R.id.tvPermissionDenied).visibility = View.GONE
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

    companion object {
        private const val MAX_NUMBER_OF_PHOTOS = 5
        private const val PHOTO_INDEX = "photo_index"
        private const val TAG = "AddApartmentFragment"
    }
}