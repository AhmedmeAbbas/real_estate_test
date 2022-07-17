package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.DialogLanguageBinding
import java.util.*

class LanguageDialog: BottomSheetDialogFragment() {

    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding!!

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

        binding.tvCancel.setOnClickListener {
            dialog?.cancel()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}