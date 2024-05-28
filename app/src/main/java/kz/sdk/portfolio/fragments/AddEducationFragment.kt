package kz.sdk.portfolio.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentAddEducationBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Education
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class AddEducationFragment:BaseFragment<FragmentAddEducationBinding>(FragmentAddEducationBinding::inflate) {
    private lateinit var selectedDate1: String
    private lateinit var selectedDate2: String
    override var showBottomNavigation = false


    @Inject
    lateinit var storageReference: StorageReference
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao





    override fun onBindView() {
        super.onBindView()
        binding.date1Btn.setOnClickListener {
            showDatePickerDialog(requireContext()){year, month, dayOfMonth ->
                selectedDate1 = "$dayOfMonth/${month + 1}/$year"
                binding.selectedDate1.text = selectedDate1
            }
        }
        binding.date2Btn.setOnClickListener {
            showDatePickerDialog(requireContext()){year, month, dayOfMonth ->
                selectedDate2 = "$dayOfMonth/${month + 1}/$year"
                binding.selectedDate2.text = selectedDate2
            }
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.degreeInput.text.isNullOrEmpty() || binding.specialtyInput.text.isNullOrEmpty() || binding.gradeInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Пожалуйста заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val education = Education(
                    title = binding.nameInput.text.toString(),
                    degree = binding.degreeInput.text.toString(),
                    specialty = binding.specialtyInput.text.toString(),
                    grade = binding.gradeInput.text.toString().toDouble(),
                    period = "$selectedDate1 $selectedDate2 "

                )
                userDao.saveEducationToList(education)

                showCustomDialog("Успех", "Вы добавили образование")
            }
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

