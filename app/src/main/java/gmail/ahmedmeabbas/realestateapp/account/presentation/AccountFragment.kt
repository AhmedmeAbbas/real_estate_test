package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAccountBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.setNightMode(isNightMode())
        setUpSignInClickListener()
        setUpLanguageTextViewListener()
        setUpNightModeListener()
        setUpNightModeChangeListener()
    }

    private fun setUpNightModeChangeListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountViewModel.uiState
                    .map { uiState -> uiState.isNightMode}
                    .distinctUntilChanged()
                    .collect {
                        binding.switchDarkMode.isChecked = it
                    }
            }
        }
    }

    private fun setUpNightModeListener() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                accountViewModel.toggleNightMode(true)
            } else {
                accountViewModel.toggleNightMode(false)
            }
        }
    }

    private fun setUpLanguageTextViewListener() {
        binding.tvAccountLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_dialogLanguage)
        }
    }

    private fun isNightMode(): Boolean {
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == UI_MODE_NIGHT_YES
    }

    private fun setUpSignInClickListener() {
        binding.btnAccountSignIn.setOnClickListener {
            binding.apply {
                clAccountHeader.background = null
                tvAccountSignInSub.visibility = View.GONE
                btnAccountSignIn.visibility = View.GONE
                tvAccountSignInHeader.text = "Welcome"
                tvAccountDisplayName.visibility = View.VISIBLE
                tvAccountProfile.visibility = View.VISIBLE
                tvAccountSignOut.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}