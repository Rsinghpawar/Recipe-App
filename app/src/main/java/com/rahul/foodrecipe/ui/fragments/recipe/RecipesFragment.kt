package com.rahul.foodrecipe.ui.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rahul.foodrecipe.viewmodels.MainViewModel
import com.rahul.foodrecipe.R
import com.rahul.foodrecipe.adapters.RecipesAdapter
import com.rahul.foodrecipe.utils.Constants.API_KEY
import com.rahul.foodrecipe.utils.NetworkResult
import com.rahul.foodrecipe.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        setupRecyclerView()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.showShimmer()
        requestApiData()
    }



    private fun requestApiData(){
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner , {res ->
            when(res){
                is NetworkResult.Success ->{
                    hideShimmer()
                    res.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Loading -> showShimmer()
                is NetworkResult.Error -> TODO()
            }
        })
    }



    private fun setupRecyclerView(){
        mView.recycler_view.adapter = mAdapter
        showShimmer()
    }


    private fun showShimmer() {
        mView.recycler_view.showShimmer()
    }

    private fun hideShimmer() {
        mView.recycler_view.hideShimmer()
    }
}