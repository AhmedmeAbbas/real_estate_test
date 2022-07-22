package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

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

        setUpSignInClickListener()
        setUpNotificationsTextViewListener()
        setUpLanguageTextViewListener()
        setUpNightModeSwitchListener()
    }

    override fun onStart() {
        super.onStart()
        binding.switchNightMode.isChecked = isNightModeOn()
    }

    private fun setUpNotificationsTextViewListener() {
        binding.tvAccountNotifications.setOnClickListener {
            navigateTo(R.id.action_accountFragment_to_notificationSettingsFragment)
        }
    }

    private fun setUpLanguageTextViewListener() {
        binding.tvAccountLanguage.setOnClickListener {
            navigateTo(R.id.action_accountFragment_to_dialogLanguage)
        }
    }

    private fun setUpNightModeSwitchListener() {
        binding.switchNightMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                accountViewModel.toggleNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                accountViewModel.toggleNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
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

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    private fun isNightModeOn(): Boolean {
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}