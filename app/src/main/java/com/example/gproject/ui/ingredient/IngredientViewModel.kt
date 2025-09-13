package com.example.gproject.ui.ingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gproject.data.Ingredient

// FridgeApp의 역할을 수행하는 ViewModel
class IngredientViewModel : ViewModel() {
    private val inventory = IngredientInventory

    val ingredients: LiveData<List<Ingredient>> = inventory.ingredients

    fun addIngredient(newIngredient: Ingredient) {
        inventory.addIngredient(newIngredient)
    }

    fun removeIngredient(ingredientToRemove: Ingredient) {
        inventory.removeIngredient(ingredientToRemove)
    }
    fun editIngredient(ingredientToEdit : Ingredient){
        inventory.editIngredient(ingredientToEdit)
    }
}