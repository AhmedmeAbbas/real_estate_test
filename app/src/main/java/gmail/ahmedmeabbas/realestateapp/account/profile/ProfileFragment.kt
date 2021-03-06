package gmail.ahmedmeabbas.realestateapp.account.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentProfileBinding

class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpClickListeners()
    }

    private fun setUpToolbar() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpClickListeners() {
        binding.clDisplayName.setOnClickListener {
            navigateTo(R.id.action_profileFragment_to_displayNameDialog)
        }

        binding.clEmail.setOnClickListener {
            navigateTo(R.id.action_profileFragment_to_editEmailDialog)
        }

        binding.clPassword.setOnClickListener {
            navigateTo(R.id.action_profileFragment_to_editPasswordDialog)
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}