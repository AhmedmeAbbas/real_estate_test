package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
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
import java.util.*

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

        setUpItemClickListeners()
        setUpNightModeSwitchListener()
        observeSignInState()
        observeDisplayName()
    }

    private fun setUpItemClickListeners() {
        with(binding) {

            btnAccountSignIn.setOnClickListener {
                navigateTo(R.id.action_global_authGraph)
            }

            tvAccountProfile.setOnClickListener {
                navigateTo(R.id.action_accountFragment_to_profileGraph)
            }

            tvAccountNotifications.setOnClickListener {
                navigateTo(R.id.action_global_notificationsGraph)
            }

            tvAccountLanguage.setOnClickListener {
                navigateTo(R.id.action_accountFragment_to_languageDialog)
            }

            tvAccountFeedback.setOnClickListener {
                navigateTo(R.id.action_accountFragment_to_helpFragment)
            }

            tvAccountToS.setOnClickListener {
                navigateTo(R.id.action_global_termsFragment)
            }

            tvAccountPrivacyPolicy.setOnClickListener {
                navigateTo(R.id.action_global_privacyFragment)
            }

            tvAccountSignOut.setOnClickListener {
                accountViewModel.signOut()
            }
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

    private fun observeSignInState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountViewModel.uiState
                    .map { it.isUserSignedIn }
                    .distinctUntilChanged()
                    .collect { isSignedIn ->
                        updateViews(isSignedIn)
                    }
            }
        }
    }

    private fun updateViews(isSignedIn: Boolean) {
        val show = if (isSignedIn) View.VISIBLE else View.GONE
        val hide = if (isSignedIn) View.GONE else View.VISIBLE
        with(binding) {
            clAccountHeader.visibility = hide
            tvAccountGreeting.visibility = show
            tvAccountDisplayName.visibility = show
            tvAccountProfile.visibility = show
            tvAccountSignOut.visibility = show
        }
    }

    private fun observeDisplayName() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                accountViewModel.uiState
                    .map { it.displayName }
                    .distinctUntilChanged()
                    .collect { displayName ->
                        if (displayName.isEmpty()) {
                            binding.tvAccountDisplayName.visibility = View.GONE
                        } else {
                            binding.tvAccountDisplayName.text = displayName
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.switchNightMode.isChecked = isNightModeOn()
        setUpGreeting()
        accountViewModel.fetchDisplayName()
    }

    private fun setUpGreeting() {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 ->
                binding.tvAccountGreeting.text = getString(R.string.greeting_good_morning)
            in 12..15 ->
                binding.tvAccountGreeting.text = getString(R.string.greeting_good_afternoon)
            in 16..24 ->
                binding.tvAccountGreeting.text = getString(R.string.greeting_good_evening)
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