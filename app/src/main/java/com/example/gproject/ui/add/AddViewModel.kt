package com.example.gproject.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gproject.data.Ingredient
import com.example.gproject.ui.ingredient.IngredientInventory

class AddViewModel : ViewModel() {


    fun addIngredient(name: String, category: String, quantity: Int, expirationDate: String) {

        val newIngredient = Ingredient(
            ingredient_id = System.currentTimeMillis(),
            name = name,
            category = category,
            quantity = quantity,
            expiration_date = expirationDate
        )

        IngredientInventory.addIngredient(newIngredient)
    }
}