package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentCreateAccountBinding
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr
import java.util.*

class CreateAccountFragment: Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpCreateAccountButton()
        setUpEditTextColor()
        setUpToSText()
    }

    private fun setUpToolbar() {
        binding.toolbarCreateAccount.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpCreateAccountButton() {
        binding.btnCreateAccount.tvButton.text = getString(R.string.create_account_button)

        binding.btnCreateAccount.root.setOnClickListener {
            binding.btnCreateAccount.apply {
                progressBar.visibility = View.VISIBLE
                tvButton.visibility = View.GONE
                root.isEnabled = false
            }
        }
    }

    private fun setUpToSText() {
        val text = resources.getString(R.string.create_account_tos)
        val textColor = requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)
        var click1Span1 = 0
        var click1Span2 = 0
        var click2Span1 = 0
        var click2Span2 = 0
        if (Locale.getDefault().language.equals("en")) {
            click1Span1 = 40; click1Span2 = 56; click2Span1 = 61; click2Span2 = 75
        } else if (Locale.getDefault().language.equals("ar")) {
            click1Span1 = 33; click1Span2 = 44; click2Span1 = 47; click2Span2 = 61
        }
        val spannableString = SpannableString(text)
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_createAccountFragment_to_termsFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(p0: View) {
                navigateTo(R.id.action_createAccountFragment_to_privacyFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = textColor
            }
        }
        spannableString.setSpan(clickableSpan1, click1Span1, click1Span2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan2, click2Span1, click2Span2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvToSCreateAccount.text = spannableString
        binding.tvToSCreateAccount.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setUpEditTextColor() {
        val colorSecondary = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEmailCreateAccount.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.emailCreateAccountTIL.apply {
                val mailOutlined =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_mail, null)
                val mailFilled =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_mail_selected, null)
                val drawable = if (hasFocus) mailFilled else mailOutlined
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etPasswordCreateAccount.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val lockOutlined =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
            val lockFilled =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock_selected, null)
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.passwordCreateAccountTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etConfirmPasswordCreateAccount.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val lockOutlined =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
            val lockFilled =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock_selected, null)
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.confirmPasswordCreateAccountTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
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