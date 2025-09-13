package com.example.gproject.ui.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gproject.data.Ingredient
import com.example.gproject.databinding.FragmentIngredientBinding
import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import com.example.gproject.databinding.DialogAddIngredientBinding
import java.util.Calendar

class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: IngredientViewModel by viewModels()
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun showEditIngredientDialog(ingredientToEdit: Ingredient) {
        val dialogBinding = DialogAddIngredientBinding.inflate(layoutInflater)

        dialogBinding.editTextName.setText(ingredientToEdit.name)
        dialogBinding.editTextCategory.setText(ingredientToEdit.category)
        dialogBinding.editTextQuantity.setText(ingredientToEdit.quantity.toString())
        dialogBinding.editTextExpirationDate.setText(ingredientToEdit.expiration_date)

        setupDatePicker(dialogBinding)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("재료 수정")
            .setView(dialogBinding.root)
            .setPositiveButton("수정", null)
            .setNegativeButton("취소", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                if (validateInput(dialogBinding)) {
                    val updatedIngredient = Ingredient(
                        ingredient_id = ingredientToEdit.ingredient_id,
                        name = dialogBinding.editTextName.text.toString(),
                        category = dialogBinding.editTextCategory.text.toString(),
                        quantity = dialogBinding.editTextQuantity.text.toString().toInt(),
                        expiration_date = dialogBinding.editTextExpirationDate.text.toString()
                    )
                    viewModel.editIngredient(updatedIngredient)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun setupDatePicker(dialogBinding: DialogAddIngredientBinding) {
        dialogBinding.editTextExpirationDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                dialogBinding.editTextExpirationDate.setText(date)
                dialogBinding.textInputLayoutExpirationDate.error = null
            }, year, month, day).show()
        }
    }

    private fun validateInput(dialogBinding: DialogAddIngredientBinding): Boolean {
        val name = dialogBinding.editTextName.text.toString()
        val category = dialogBinding.editTextCategory.text.toString()
        val quantity = dialogBinding.editTextQuantity.text.toString().toIntOrNull()
        val expirationDate = dialogBinding.editTextExpirationDate.text.toString()

        dialogBinding.textInputLayoutName.error = null
        dialogBinding.textInputLayoutCategory.error = null
        dialogBinding.textInputLayoutQuantity.error = null
        dialogBinding.textInputLayoutExpirationDate.error = null

        var isValid = true

        if (name.isBlank()) {
            dialogBinding.textInputLayoutName.error = "재료 이름을 입력해주세요."
            isValid = false
        }
        if (category.isBlank()) {
            dialogBinding.textInputLayoutCategory.error = "카테고리를 입력해주세요."
            isValid = false
        }
        if (quantity == null || quantity == 0) {
            dialogBinding.textInputLayoutQuantity.error = "수량을 1 이상 입력해주세요."
            isValid = false
        }
        if (expirationDate.isEmpty()) {
            dialogBinding.textInputLayoutExpirationDate.error = "유통기한을 선택해주세요."
            isValid = false
        }
        return isValid
    }

    private fun setupRecyclerView() {
        ingredientAdapter = IngredientAdapter(
            onEditClicked = { ingredient ->
                showEditIngredientDialog(ingredient)
            },
            onCookClicked = { ingredient ->
                // todo : 레시피 연결
            },
            onDeleteClicked = { ingredient ->
                viewModel.removeIngredient(ingredient)
            }
        )

        binding.recyclerViewIngredients.apply {
            adapter = ingredientAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.ingredients.observe(viewLifecycleOwner) { ingredients ->
            ingredientAdapter.submitList(ingredients)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
