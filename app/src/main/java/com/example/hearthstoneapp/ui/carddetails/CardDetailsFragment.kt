package com.example.hearthstoneapp.ui.carddetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteDatabase
import com.example.hearthstoneapp.databinding.FragmentCardDetailsBinding
import com.example.hearthstoneapp.databinding.FragmentMainScreenBinding
import com.example.hearthstoneapp.ui.cards.CardsFragmentArgs
import com.example.hearthstoneapp.ui.mainscreen.MainScreenViewModel
import com.example.hearthstoneapp.util.createViewModel

class CardDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCardDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: CardDetailsViewModel by lazy {
            createViewModel {
                CardDetailsViewModel(
                    dataSource, application
                )
            }
        }

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_card_details, container, false)
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