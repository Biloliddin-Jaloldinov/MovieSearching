package uz.jbzprojects.moviesearching.presentation.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.DialogAddReviewBinding

class AddReviewDialog : DialogFragment() {

    private var onSaveClickListener: ((String, String) -> Unit)? = null
    fun setSaveClickListener(block: (String, String) -> Unit) {
        onSaveClickListener = block
    }

    private lateinit var binding: DialogAddReviewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_corner_rounded);
        binding = DialogAddReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
    }

    private fun setButtons() {
        binding.btnSave.setOnClickListener {
            if (isInputCorrect()) {
                onSaveClickListener?.invoke(binding.ratingBar.rating.toString(), binding.inputComment.text.toString())
                dismiss()
            }
        }
        binding.backButton.setOnClickListener {
            dismiss()
        }
    }

    private fun isInputCorrect(): Boolean {
        if (binding.ratingBar.rating == 0f) {
            Toast.makeText(requireContext(), "Вы не поставили оценку", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
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