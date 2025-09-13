package com.example.gproject.ui.ingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gproject.data.Ingredient

object IngredientInventory {
    private val _ingredients = MutableLiveData<List<Ingredient>>()
    val ingredients: LiveData<List<Ingredient>> get() = _ingredients
    private val dbIngredients = mutableListOf<Ingredient>()

    init {
        loadMockIngredients()
    }

    private fun loadMockIngredients() {
        dbIngredients.addAll(
            listOf(
                Ingredient(1, "양파", "채소", 2, "2025-8-25"),
                Ingredient(2, "소고기", "육류", 1, "2025-8-22"),
                Ingredient(3, "우유", "유제품", 1, "2025-8-20")
            )
        )
        sortDuedate()
    }

    private fun sortDuedate() {
        dbIngredients.sortBy { it.expiration_date }
        _ingredients.value = dbIngredients.toList()
    }

    fun addIngredient(newIngredient: Ingredient) {
        dbIngredients.add(newIngredient)
        sortDuedate()
    }

    fun removeIngredient(ingredientToRemove: Ingredient) {
        dbIngredients.remove(ingredientToRemove)
        _ingredients.value = dbIngredients.toList()
    }
    fun editIngredient(ingredientToEdit : Ingredient){
        val index = dbIngredients.indexOfFirst { it.ingredient_id == ingredientToEdit.ingredient_id }

        if (index != -1) {
            dbIngredients[index] = ingredientToEdit
            sortDuedate()
        }
    }

// todo
// getAvailableIngredients() – 사용 가능한 재료 리스트 반환
// - canMakeRecipe(recipe: Recipe) – 주어진 레시피가 가능한지 판단
// - getMakeableRecipes(recipes: List<Recipe>) – 가능한 레시피 필터링
// 패키지 분리 고려

}