package gmail.ahmedmeabbas.realestateapp.account.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment: Fragment() {

    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.clNewListings.setOnClickListener {
            navigateTo(R.id.action_notificationSettingsFragment_to_newListingsDialog)
        }

        binding.clStatusUpdate.setOnClickListener {
            navigateTo(R.id.action_notificationSettingsFragment_to_statusUpdateDialog)
        }

        binding.clMessages.setOnClickListener {
            navigateTo(R.id.action_notificationSettingsFragment_to_messagesDialog)
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    private fun setUpToolbar() {
        binding.toolbarNotificationSettings.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}