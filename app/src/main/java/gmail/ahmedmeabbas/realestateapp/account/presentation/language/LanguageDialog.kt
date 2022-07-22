package gmail.ahmedmeabbas.realestateapp.account.presentation.language

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.DialogLanguageBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LanguageDialog : BottomSheetDialogFragment() {

    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding!!
    private val languageDialogViewModel: LanguageDialogViewModel by activityViewModels()

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

        setUpRadioGroupCheckedListener()
        setUpCancelTextView()
        observeLanguageChange()
    }

    private fun observeLanguageChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                languageDialogViewModel.uiState.map { uiState ->
                    uiState.languageCode
                }
                    .collect { language ->
                        if (language == "en") {
                            binding.rgLanguage.check(R.id.rbEnglish)
                        } else {
                            binding.rgLanguage.check(R.id.rbArabic)
                        }
                    }
            }
        }
    }


    private fun setUpRadioGroupCheckedListener() {
        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbArabic -> languageDialogViewModel.changeAppLanguage("ar")
                R.id.rbEnglish -> languageDialogViewModel.changeAppLanguage("en")
            }
        }
    }

    private fun setUpCancelTextView() {
        binding.tvCancel.setOnClickListener {
            cancelDialog()
        }
    }

    private fun cancelDialog() {
        dialog?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LanguageDialog"
    }
}