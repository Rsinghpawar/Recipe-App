package com.rahul.foodrecipe.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rahul.foodrecipe.viewmodels.MainViewModel
import com.rahul.foodrecipe.R
import com.rahul.foodrecipe.adapters.RecipesAdapter
import com.rahul.foodrecipe.databinding.FragmentRecipesBinding
import com.rahul.foodrecipe.utils.Constants.API_KEY
import com.rahul.foodrecipe.utils.NetworkResult
import com.rahul.foodrecipe.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding : FragmentRecipesBinding?=null
    private val binding get() = _binding!!
    private val mAdapter by lazy { RecipesAdapter() }
    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        readDatabase()
        binding.btnFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }

        return binding.root

    }

    private fun readDatabase() {
        Log.d("TAG", "readDatabase: ")
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipes)
                    hideShimmer()
                } else {
                    requestApiData()
                }
            })
        }
    }


    private fun requestApiData() {
        Log.d("TAG", "requestApiData: ")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { res ->
            when (res) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    res.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Loading -> showShimmer()
                is NetworkResult.Error -> {
                    hideShimmer()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipes)
                }
            })
        }
    }


    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        showShimmer()
    }


    private fun showShimmer() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmer() {
        binding.recyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}