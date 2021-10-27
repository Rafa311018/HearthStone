package com.example.hearthstoneapp.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteDatabase
import com.example.hearthstoneapp.databinding.FragmentCardsBinding
import com.example.hearthstoneapp.ui.cards.adapter.CardListener
import com.example.hearthstoneapp.ui.cards.adapter.CardsAdapter
import com.example.hearthstoneapp.ui.favorites.FavoritesFragmentDirections
import com.example.hearthstoneapp.util.createViewModel

class CardsFragment : Fragment() {
    private lateinit var binding: FragmentCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: CardsViewModel by lazy {
            createViewModel {
                CardsViewModel(HearthStoneRepo.provideHearthStoneRepo(), requireActivity().application, dataSource)
            }
        }

        var adapter = CardsAdapter(CardListener { card, click ->
            if (click == "details") {
                this.findNavController().navigate(
                    FavoritesFragmentDirections.actionNavigationFavoritesToCardDetailsFragment(
                        card
                    )
                )
            }else{
                viewModel.addFavorite(card)
            }
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false)
        binding.lifecycleOwner = this
        binding.cardList.adapter = adapter
        binding.viewModel = viewModel

        viewModel.cardsFound.observe(viewLifecycleOwner, {
            adapter.setData(it, viewModel.favoriteList.value)
        })
        viewModel.showCards.observe(viewLifecycleOwner, {
            if (it) {
                binding.imgLoading.visibility = View.GONE
                binding.cardList.visibility = View.VISIBLE
            } else {
                binding.imgLoading.visibility = View.GONE
                binding.cardNotFound.visibility = View.VISIBLE
            }
        })
        viewModel.searchString.observe(viewLifecycleOwner, {
            binding.nameClass.text = it
            if (it.contains("Search results for")){
                binding.searchButton.visibility = View.VISIBLE
                binding.searchCardsTextView.visibility = View.VISIBLE
            }
        })
        viewModel.startSearch.observe(viewLifecycleOwner, {
            if (it){
                binding.imgLoading.visibility = View.VISIBLE
                binding.cardList.visibility = View.GONE
                binding.cardNotFound.visibility = View.GONE
             viewModel.doneSearch()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val card = CardsFragmentArgs.fromBundle(requireArguments()).searchCard
        val search = CardsFragmentArgs.fromBundle(requireArguments()).searchby
        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: CardsViewModel by lazy {
            createViewModel {
                CardsViewModel(HearthStoneRepo.provideHearthStoneRepo(), requireActivity().application, dataSource)
            }
        }
        viewModel.searchCards(card, search)
    }
}