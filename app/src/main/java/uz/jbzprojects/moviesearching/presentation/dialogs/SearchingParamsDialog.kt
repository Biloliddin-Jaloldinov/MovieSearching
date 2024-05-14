package uz.jbzprojects.moviesearching.presentation.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.DialogFilterForSearchingBinding

class SearchingParamsDialog(private var year: String, private var selectedLanguage: String) : DialogFragment() {

    private var onSaveClickListener: ((String, String) -> Unit)? = null
    fun setSaveClickListener(block: (String, String) -> Unit) {
        onSaveClickListener = block
    }

    private lateinit var binding: DialogFilterForSearchingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_corner_rounded);
        binding = DialogFilterForSearchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setButtons()
    }

    private fun initViews() {
        if (selectedLanguage == "ru-RU") {
            binding.radioEnglish.isChecked = false
            binding.radioRussia.isChecked = true
        } else {
            binding.radioEnglish.isChecked = true
            binding.radioRussia.isChecked = false
        }

        binding.inputRealisedYear.setText(year)
    }

    private fun setButtons() {
        binding.btnSave.setOnClickListener {
            onSaveClickListener?.invoke(selectedLanguage, "")
        }
        binding.btnSelectEnglish.setOnClickListener {
            binding.radioRussia.isChecked = false
            binding.radioEnglish.isChecked = true
            selectedLanguage = "en-US"
        }
        binding.btnSelectRussian.setOnClickListener {
            binding.radioRussia.isChecked = true
            binding.radioEnglish.isChecked = false
            selectedLanguage = "ru-RU"
        }
        binding.backButton.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            onSaveClickListener?.invoke(binding.inputRealisedYear.text.toString(), selectedLanguage)
            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.window!!.attributes = params
    }
}