package kz.sdk.portfolio.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentAddRewardBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.License
import kz.sdk.portfolio.models.Reward
import java.util.Calendar
import javax.inject.Inject



@AndroidEntryPoint
class AddRewardFragment:BaseFragment<FragmentAddRewardBinding>(FragmentAddRewardBinding::inflate) {  override var showBottomNavigation = false

    private var imageUri: Uri? = null

    private lateinit var issuedDate: String


    @Inject
    lateinit var storageReference: StorageReference

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao

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

        binding.date1Btn.setOnClickListener {
            showDatePickerDialog(requireContext()){year, month, dayOfMonth ->
                issuedDate = "$dayOfMonth/${month + 1}/$year"
                binding.selectedDate1.text = issuedDate
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.uploadImg.setOnClickListener {
            selectEventImage()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage { imageUrl ->
                    val reward = Reward(
                        title = binding.nameInput.text.toString(),
                        issues = "Получено $issuedDate",
                        img = imageUrl
                    )
                    userDao.saveRewardToList(reward)
                    showCustomDialog("Успех", "Вы добавили сертификат")
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
    fun showDatePickerDialog(context: Context, onDateSet: (year: Int, month: Int, dayOfMonth: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                onDateSet(selectedYear, selectedMonth, selectedDayOfMonth)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

}