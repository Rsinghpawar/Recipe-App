package com.rahul.foodrecipe.bindingadpaters

import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rahul.foodrecipe.data.database.RecipesEntity
import com.rahul.foodrecipe.models.FoodRecipes
import com.rahul.foodrecipe.utils.NetworkResult
import org.w3c.dom.Text

class RecipesFragmentBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipes>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            } else if( apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success) {
                imageView.visibility = View.INVISIBLE
            }

        }

        @JvmStatic
        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipes>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if( apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Success) {
                textView.visibility = View.INVISIBLE
            }

        }
    }
}