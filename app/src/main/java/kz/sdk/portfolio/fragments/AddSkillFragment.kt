package kz.sdk.portfolio.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentAddSkillBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Skill
import javax.inject.Inject


@AndroidEntryPoint
class AddSkillFragment:BaseFragment<FragmentAddSkillBinding>(FragmentAddSkillBinding::inflate) {
    override var showBottomNavigation = false
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao


    override fun onBindView() {
        super.onBindView()


        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.whereInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                    val Skill = Skill(
                        title = binding.nameInput.text.toString(),
                        where = binding.whereInput.text.toString(),
                    )
                    userDao.saveSkillToList(Skill)
                    showCustomDialog("Успех", "Вы добавили навык")
                }
            }
        }
}
