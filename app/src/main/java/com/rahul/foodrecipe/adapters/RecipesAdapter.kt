package com.rahul.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rahul.foodrecipe.databinding.ItemRecipesBinding
import com.rahul.foodrecipe.models.FoodRecipes
import com.rahul.foodrecipe.models.Result
import com.rahul.foodrecipe.utils.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipe = emptyList<Result>()

    class MyViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(result: Result) {
                binding.result = result
                binding.executePendingBindings()
            }
            companion object{
                fun from(parent: ViewGroup) : MyViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ItemRecipesBinding.inflate(layoutInflater , parent , false)
                    return MyViewHolder(binding)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = recipe[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int = recipe.size

    fun setData(newData : FoodRecipes) {
        val recipesDiffUtil = RecipesDiffUtil(recipe , newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipe  = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}