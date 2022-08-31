package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAddPhotosBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class AddPhotosFragment : Fragment() {

    private var _binding: FragmentAddPhotosBinding? = null
    private val binding get() = _binding!!
    private val addPhotosViewModel: AddPhotosViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val chosenPhotoUris: MutableList<Uri?> = MutableList(MAX_NUMBER_OF_PHOTOS) { null }
    private var clickedPhotoIndex: Int = -1
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                hideInfoText()
                launchIntentForPhotos()
            } else {
                showInfoText(getString(R.string.photos_permission_denied))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
        initResultLauncher()
        setUpPhotoClickListeners()
        // init previously selected photos if the user navigates back to this fragment
        setPhotos()
        setUpSkipButton()
        setUpContinueButton()
    }

    private fun setUpProgressColor() {
        with(binding) {
            progressLayout.ivAdvertiserInfo.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line1.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPropertyAddress.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line2.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivConstructionDetails.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line3.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivHouseDetails.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line4.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivUtilities.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line5.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPhotos.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
        }
    }

    private fun setUpProgressLayout() {
        when (addPhotosViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun initResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedUri = result.data?.data
                    val clipData = result.data?.clipData
                    val photoIndex = result.data?.getIntExtra(PHOTO_INDEX, clickedPhotoIndex)
                    if (clipData != null) {
                        chosenPhotoUris[photoIndex!!] = clipData.getItemAt(0).uri
                    } else if (selectedUri != null) {
                        chosenPhotoUris[photoIndex!!] = selectedUri
                    } else return@registerForActivityResult
                    setPhotos()
                    // in case the user clicked continue and main photo is null, then selected a photo
                    if (chosenPhotoUris[0] != null) {
                        hideInfoText()
                    }
                }
            }
    }

    private fun setPhotos() {
        binding.ivPhoto1.setImageURI(chosenPhotoUris[0])
        binding.ivPhoto2.setImageURI(chosenPhotoUris[1])
        binding.ivPhoto3.setImageURI(chosenPhotoUris[2])
        binding.ivPhoto4.setImageURI(chosenPhotoUris[3])
        binding.ivPhoto5.setImageURI(chosenPhotoUris[4])
    }

    private fun setUpPhotoClickListeners() {
        binding.ivPhoto1.setOnClickListener {
            clickedPhotoIndex = 0
            onPhotoClicked()
        }
        binding.ivPhoto2.setOnClickListener {
            clickedPhotoIndex = 1
            onPhotoClicked()
        }
        binding.ivPhoto3.setOnClickListener {
            clickedPhotoIndex = 2
            onPhotoClicked()
        }
        binding.ivPhoto4.setOnClickListener {
            clickedPhotoIndex = 3
            onPhotoClicked()
        }
        binding.ivPhoto5.setOnClickListener {
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

    private fun showInfoText(message: String) {
        binding.tvInfo.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun hideInfoText() {
        binding.tvInfo.visibility = View.GONE
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            if (chosenPhotoUris[0] == null) {
                showInfoText(getString(R.string.photos_main_not_selected))
                return@setOnClickListener
            }
        }
    }

    private fun setUpSkipButton() {
        binding.tvSkip.setOnClickListener {
            for (i in chosenPhotoUris.indices) {
                chosenPhotoUris[i] = null
            }
            findNavController().navigate(R.id.action_addPhotosFragment_to_priceFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarAddPhotos.setNavigationOnClickListener {
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
        private const val TAG = "AddHouseFragment"
    }
}