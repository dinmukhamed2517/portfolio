package kz.sdk.portfolio.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentAdminBinding
import kz.sdk.portfolio.models.Event
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class AdminFragment: BaseFragment<FragmentAdminBinding>(FragmentAdminBinding::inflate) {

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    override var showBottomNavigation = false

    private var imageUri: Uri? = null

    @Inject
    lateinit var storageReference: StorageReference

    private val imageResultLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.img.setImageURI(it)
            imageUri = it
            binding.textImg.isVisible = false
        }
    }


    override fun onBindView() {
        super.onBindView()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.uploadImg.setOnClickListener {
            selectEventImage()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.noteInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage { imageUrl ->
                    saveEventToDatabase(binding.nameInput.text.toString(), binding.noteInput.text.toString(), imageUrl, selectedDate, selectedTime)
                }
            }
        }

    }

    fun selectEventImage() {
        imageResultLauncher.launch("image/*")
    }
    private fun uploadImage(callback: (String) -> Unit) {
        imageUri?.let { uri ->
            binding.img.setImageURI(uri)
            val ref = storageReference.child(uri.lastPathSegment ?: "temp")
            ref.putFile(uri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveEventToDatabase(name: String, note: String, imageUrl: String, date: String, time: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Events")
        val eventId = databaseReference.push().key
        val event = Event(
            id = eventId,
            title = name,
            description = note,
            img = imageUrl,
        )
        eventId?.let {
            databaseReference.child(it).setValue(event).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showCustomDialog("Успех", "Курс добавлен успешно!")
                } else {
                    Toast.makeText(context, "Failed to create event: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
