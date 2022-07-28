package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSignInBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import java.util.*

class SignInFragment: Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpButtonListeners()
        setUpToSText()
    }

    private fun setUpToolbar() {
        binding.toolbarSignIn.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpButtonListeners() {
        binding.btnSignInEmail.setOnClickListener {
            navigateTo(R.id.action_signInFragment_to_emailSignInFragment)
        }

        binding.btnSignInCreateAccount.setOnClickListener {
            navigateTo(R.id.action_signInFragment_to_createAccountFragment)
        }
    }

    private fun setUpToSText() {
        val text = resources.getString(R.string.sign_in_tos)
        val textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
        var click1Span1 = 0
        var click1Span2 = 0
        var click2Span1 = 0
        var click2Span2 = 0
        if (Locale.getDefault().language.equals("en")) {
            click1Span1 = 31; click1Span2 = 47; click2Span1 = 52; click2Span2 = 66
        } else if (Locale.getDefault().language.equals("ar")) {
            click1Span1 = 35; click1Span2 = 46; click2Span1 = 48; click2Span2 = 62
        }
        val spannableString = SpannableString(text)
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_signInFragment_to_termsFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_signInFragment_to_privacyFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        spannableString.setSpan(clickableSpan1, click1Span1, click1Span2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan2, click2Span1, click2Span2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvToSSignIn.text = spannableString
        binding.tvToSSignIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}