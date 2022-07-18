package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.DialogLanguageBinding
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesDataSource
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesDataSourceImpl
import kotlinx.coroutines.launch
import java.util.*

class LanguageDialog: BottomSheetDialogFragment() {

    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStoreManager: UserPreferencesDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        dataStoreManager = UserPreferencesDataSourceImpl(requireContext())

        setUpRadioGroupCheckedListener()
        setUpCancelTextView()
    }

    private fun setUpRadioGroupCheckedListener() {
        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbArabic -> viewLifecycleOwner.lifecycleScope.launch {
                    dataStoreManager.writeString(KEY_APP_LANGUAGE, "ar")
                }
                R.id.rbEnglish -> viewLifecycleOwner.lifecycleScope.launch {
                    dataStoreManager.writeString(KEY_APP_LANGUAGE, "en")
                }
            }
        }
    }

    private fun setUpCancelTextView() {
        binding.tvCancel.setOnClickListener {
            cancelDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        /**
        set up an observer for this
         */
        setUpCheckedRadioButton()
    }

    private fun setUpCheckedRadioButton() {
        if (Locale.getDefault().language == "en") {
            binding.rgLanguage.check(R.id.rbEnglish)
            return
        }
        binding.rgLanguage.check(R.id.rbArabic)
    }

    private fun cancelDialog() {
        dialog?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}