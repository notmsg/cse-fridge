package com.example.gproject.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gproject.databinding.FragmentAddFormBinding
import java.util.Calendar

class AddFormFragment : Fragment() {

    private var _binding: FragmentAddFormBinding? = null
    private val binding get() = _binding!!

    // include된 레이아웃의 뷰에 쉽게 접근하기 위한 편의 속성
    private val formBinding get() = binding.dialogAddIngredient

    private val addViewModel: AddViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDatePicker()

        // buttonSave는 FragmentAddFormBinding에 직접 있으므로 그대로 사용합니다.
        binding.buttonSave.setOnClickListener {
            if (validateInput()) {
                // 값을 가져올 때는 formBinding을 통해 접근합니다.
                val name = formBinding.editTextName.text.toString()
                val category = formBinding.editTextCategory.text.toString()
                val quantity = formBinding.editTextQuantity.text.toString().toInt()
                val expirationDate = formBinding.editTextExpirationDate.text.toString()

                addViewModel.addIngredient(name, category, quantity, expirationDate)

                findNavController().popBackStack()
            }
        }
    }

    private fun setupDatePicker() {
        // DatePicker 설정 시에도 formBinding을 통해 접근합니다.
        formBinding.editTextExpirationDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val date = "$year-${month + 1}-$day"
                    formBinding.editTextExpirationDate.setText(date)
                    formBinding.textInputLayoutExpirationDate.error = null
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun validateInput(): Boolean {
        // 유효성 검사 시에도 formBinding을 통해 접근합니다.
        val name = formBinding.editTextName.text.toString()
        val category = formBinding.editTextCategory.text.toString()
        val quantity = formBinding.editTextQuantity.text.toString().toIntOrNull()
        val expirationDate = formBinding.editTextExpirationDate.text.toString()

        formBinding.textInputLayoutName.error = null
        formBinding.textInputLayoutCategory.error = null
        formBinding.textInputLayoutQuantity.error = null
        formBinding.textInputLayoutExpirationDate.error = null

        var isValid = true

        if (name.isBlank()) {
            formBinding.textInputLayoutName.error = "재료 이름을 입력해주세요."
            isValid = false
        }
        if (category.isBlank()) {
            formBinding.textInputLayoutCategory.error = "카테고리를 입력해주세요."
            isValid = false
        }
        if (quantity == null || quantity == 0) {
            formBinding.textInputLayoutQuantity.error = "수량을 1 이상 입력해주세요."
            isValid = false
        }
        if (expirationDate.isEmpty()) {
            formBinding.textInputLayoutExpirationDate.error = "유통기한을 선택해주세요."
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}