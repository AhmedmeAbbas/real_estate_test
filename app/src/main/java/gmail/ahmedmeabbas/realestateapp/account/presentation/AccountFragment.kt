package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAccountBinding

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

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

        setUpDarkModeTextView()
        setUpLanguageListener()
        setUpSignInClickListener()
    }

    private fun setUpDarkModeTextView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            binding.tvAccountDarkMode.visibility = View.GONE
            binding.switchDarkMode.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        setUpDarkModeSwitchState()
    }

    private fun setUpDarkModeSwitchState() {
        binding.switchDarkMode.isChecked = isDarkModeOn()
    }

    private fun setUpLanguageListener() {
        binding.tvAccountLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_dialogLanguage)
        }
    }

    private fun isDarkModeOn(): Boolean {
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
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
}