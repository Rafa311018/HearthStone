package com.example.hearthstoneapp.ui.carddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.databinding.FragmentCardDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDetailsFragment : Fragment() {
    val viewModel: CardDetailsViewModel by viewModels()

    private lateinit var binding: FragmentCardDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_card_details, container, false)
        binding.lifecycleOwner = this
        val cardDetails = CardDetailsFragmentArgs.fromBundle(requireArguments()).cardDetails
        viewModel.idCard = cardDetails.cardId
        binding.cardDetails = cardDetails
        binding.viewModel = viewModel

        viewModel.favoriteCard.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.favoriteHeart()
            }
        })
        viewModel.favorite.observe(viewLifecycleOwner, {
            if (it){
                binding.favoriteImageView.setImageResource(R.drawable.basic_heart_fill)
            }else{
                binding.favoriteImageView.setImageResource(R.drawable.basic_heart_outline)
            }
        })
        viewModel.isFavorite()
        return binding.root
    }
}
