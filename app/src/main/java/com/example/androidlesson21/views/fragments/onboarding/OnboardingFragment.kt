package com.example.androidlesson21.views.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidlesson21.R
import com.example.androidlesson21.databinding.FragmentDetailBinding
import com.example.androidlesson21.databinding.FragmentOnboardingBinding
import com.example.androidlesson21.models.onboarding.Onboard
import com.example.androidlesson21.views.adapters.OnboardAdapter
import com.example.androidlesson21.views.fragments.base.BaseFragment

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {


    private val onboardList = arrayListOf(
        Onboard(R.drawable.onboard_1, "Get The Freshest Fruit Salad Combo", "We deliver the best and freshest fruit salad in town. Order for a combo today!!!"),
        Onboard(R.drawable.onboard_2, "What is your firstname?", "")
    )

    private val onboardAdapter = OnboardAdapter()

    private var currentPage = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.adapter = onboardAdapter
        onboardAdapter.updateList(onboardList)

        binding.viewPager2.isUserInputEnabled = false

        binding.buttonNext.setOnClickListener {
            currentPage++
            if (currentPage < onboardList.size) {
                binding.viewPager2.setCurrentItem(currentPage, true)


            } else {
                binding.viewPager2.currentItem =  binding.viewPager2.currentItem + 1
                onboardAdapter.setNextButtonClicked(true)


                if ( binding.viewPager2.currentItem == onboardList.size - 1) {
                    binding.buttonNext.text = "Start Ordering"
                }
            }
        }

        onboardAdapter.setFragment(this)
    }

}


