package com.example.gproject.ui.ingredient
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gproject.data.Ingredient
import com.example.gproject.databinding.ItemIngredientBinding
import android.graphics.Paint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.view.View


class IngredientAdapter(
    private val onEditClicked: (Ingredient) -> Unit,
    private val onCookClicked: (Ingredient) -> Unit,
    private val onDeleteClicked: (Ingredient) -> Unit
) : ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder>(IngredientDiffCallback()) {

    // 확장된 아이템의 고유 ID를 저장하는 Set을 어댑터 내부에 만듭니다.
    private val expandedItemIds: MutableSet<Long> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = getItem(position)
        holder.bind(ingredient)
    }

    inner class IngredientViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.itemName.text = ingredient.name
            binding.itemDetails.text =
                "${ingredient.quantity}개 / ${ingredient.expiration_date}"
            checkExpiration(ingredient)

            // Set에 현재 아이템의 ID가 포함되어 있는지 확인하여 확장 상태를 결정합니다.
            val isExpanded = expandedItemIds.contains(ingredient.ingredient_id)
            binding.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // 아이템 클릭 시, Set의 상태를 변경하고 UI를 갱신합니다.
            binding.root.setOnClickListener {
                if (isExpanded) {
                    expandedItemIds.remove(ingredient.ingredient_id)
                } else {
                    expandedItemIds.add(ingredient.ingredient_id)
                }
                // 이 아이템의 UI만 새로고침하여 확장/축소 상태를 반영합니다.
                notifyItemChanged(adapterPosition)
            }

            binding.editButton.setOnClickListener {
                onEditClicked(ingredient)
            }
            binding.cookButton.setOnClickListener {
                onCookClicked(ingredient)
            }
            binding.deleteButton.setOnClickListener {
                onDeleteClicked(ingredient)
            }
        }

        private fun checkExpiration(ingredient: Ingredient) {
            var isExpired = false
            try {
                val formatter = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                val expirationDate = formatter.parse(ingredient.expiration_date)
                val todayCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                isExpired = expirationDate.before(todayCalendar.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (isExpired) {
                binding.itemName.paintFlags = binding.itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemDetails.paintFlags = binding.itemDetails.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.itemName.paintFlags = binding.itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.itemDetails.paintFlags = binding.itemDetails.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}

class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredient_id == newItem.ingredient_id
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}