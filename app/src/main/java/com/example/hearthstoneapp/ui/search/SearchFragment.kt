package com.example.hearthstoneapp.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.databinding.FragmentSearchBinding
import com.example.hearthstoneapp.ui.search.adapter.CardListener
import com.example.hearthstoneapp.ui.search.adapter.CardsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding
    private var positionC: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var adapter = CardsAdapter(CardListener { card, click, position ->
            if (click == "details") {
                this.findNavController().navigate(
                    SearchFragmentDirections.actionCardsFragmentToCardDetailsFragment(
                        card
                    )
                )
            } else {
                viewModel.clickFavorite(card)
                positionC = position
            }
        })

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container, false
        )
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
            if (it.contains("Search results for")) {
                binding.searchButton.visibility = View.VISIBLE
                binding.searchCardsTextView.visibility = View.VISIBLE
            }
        })
        viewModel.startSearch.observe(viewLifecycleOwner, {
            if (it) {
                binding.imgLoading.visibility = View.VISIBLE
                binding.cardList.visibility = View.GONE
                binding.cardNotFound.visibility = View.GONE
                viewModel.doneSearch()
            }
        })
        viewModel.isFavorite.observe(viewLifecycleOwner, { favorite ->
            if (favorite == null) viewModel.addFavorite()
            else viewModel.deleteFavorite()
        })
        viewModel.updateData.observe(viewLifecycleOwner, {
            if (it) {
                callApi()
                viewModel.doneUpdate()
            }
        })
        viewModel.updateItem.observe(viewLifecycleOwner, {
            if (it){
                adapter.notifyItemChanged(positionC)
                viewModel.doneUpdateItem()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApi()
    }

    private fun callApi() {
        val card = SearchFragmentArgs.fromBundle(requireArguments()).searchCard
        val search = SearchFragmentArgs.fromBundle(requireArguments()).searchby

        viewModel.searchCards(card, search)
    }
}
