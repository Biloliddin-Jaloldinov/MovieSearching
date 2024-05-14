package uz.jbzprojects.moviesearching.presentation.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import uz.jbzprojects.moviesearching.R
import uz.jbzprojects.moviesearching.databinding.DialogAddRequestToMovieBinding
import uz.jbzprojects.moviesearching.databinding.DialogAddReviewBinding
import uz.jbzprojects.moviesearching.storage.room.entity.UserMovieRequestEntity

class AddUserRequestToAddMovieDialog(private val userID: Int) : DialogFragment() {

    private var onSaveClickListener: ((UserMovieRequestEntity) -> Unit)? = null
    fun setSaveClickListener(block: (UserMovieRequestEntity) -> Unit) {
        onSaveClickListener = block
    }

    private lateinit var binding: DialogAddRequestToMovieBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_corner_rounded);
        binding = DialogAddRequestToMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
    }

    private fun setButtons() {
        if (isInputCorrect())
            binding.btnSave.setOnClickListener {
                if (isInputCorrect()) {
                    val imgUrl = if (imageUri == null) {
                        ""
                    } else {
                        imageUri.toString()
                    }
                    onSaveClickListener?.invoke(
                        UserMovieRequestEntity(
                            imageUrl = imgUrl,
                            title = binding.inputTitle.text.toString(),
                            description = binding.inputAbout.text.toString(),
                            date = binding.inputRealisedYear.text.toString(),
                            userID = userID
                        )
                    )
                    dismiss()
                }
            }
        binding.backButton.setOnClickListener {
            dismiss()
        }
        binding.addMovie.setOnClickListener {
            openGallery()
        }
    }

    private fun isInputCorrect(): Boolean {
        if (binding.inputTitle.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Не написали название фильма", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private val PICK_IMAGE_REQUEST = 1

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data ?: return
            val contentResolver = context?.contentResolver
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            binding.imgMovie.setImageBitmap(bitmap)
        }
    }

    private var imageUri: Uri? = null
    private lateinit var bitmap: Bitmap

    override fun onStart() {
        super.onStart()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog!!.window!!.attributes = params
    }

}