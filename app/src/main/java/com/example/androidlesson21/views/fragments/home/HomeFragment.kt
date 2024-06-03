package com.example.androidlesson21.views.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.androidlesson21.R
import com.example.androidlesson21.databinding.FragmentHomeBinding
import com.example.androidlesson21.utilities.gone
import com.example.androidlesson21.utilities.visible
import com.example.androidlesson21.viewmodels.HomeViewModel
import com.example.androidlesson21.views.adapters.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidlesson21.utilities.highlightName
import com.example.androidlesson21.views.fragments.base.BaseFragment


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    private var recipeAdapter = RecipeAdapter()

    private val viewModel by viewModels<HomeViewModel>()

    private val args: HomeFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeData()
        viewModel.getAllRecipes()

        val greeting = "Hello ${args.name}, What fruit salad combo do you want today?"

        binding.textView1.text = greeting.highlightName(this, args.name, R.color.BlazingOrange)

        binding.editText.addTextChangedListener { text ->
            val searchText = text.toString().trim()
            viewModel.searchRecipes(searchText)

            if(searchText.isNotBlank() && searchText.isNotEmpty())
            {
                binding.editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0)
                binding.editText.compoundDrawables[0].setTint(ContextCompat.getColor(requireContext(), R.color.BlazingOrange))
            }
            else
            {
                binding.editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0)
                binding.editText.compoundDrawables[0].setTint(ContextCompat.getColor(requireContext(), R.color.GrapeCreme))
            }

        }


        recipeAdapter.onClickItem = { itemId ->

            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(itemId.toInt())
            findNavController().navigate(action)
        }



    }



    private fun setUpRecyclerView() {

        val spanCount = if (isTablet()) 4 else 2
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        binding.recycleViewHome.layoutManager = gridLayoutManager
        binding.recycleViewHome.adapter = recipeAdapter
    }


    private fun isTablet(): Boolean {

        val configuration = resources.configuration
        return configuration.smallestScreenWidthDp >= 600
    }

    private fun observeData() {
        viewModel.recipes.observe(viewLifecycleOwner) { products ->
            products?.let {
                recipeAdapter.updateList(it)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarContainer.progressBar2.visible()
                hideOtherWidgets()
            } else {
                binding.progressBarContainer.progressBar2.gone()
                showOtherWidgets()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                Log.e("responseError", it)
            }
        }
    }



    private fun hideOtherWidgets() {


        binding.recycleViewHome.gone()

    }

    private fun showOtherWidgets() {

        binding.recycleViewHome.visible()

    }





}