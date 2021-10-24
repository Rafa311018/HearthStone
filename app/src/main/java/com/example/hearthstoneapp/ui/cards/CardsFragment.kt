package com.example.hearthstoneapp.ui.cards

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.databinding.FragmentCardsBinding
import com.example.hearthstoneapp.ui.cards.adapter.CardsAdapter
import com.example.hearthstoneapp.ui.mainscreen.adapter.ClassesAdapter
import com.example.hearthstoneapp.util.createViewModel

class CardsFragment : Fragment() {

    private lateinit var binding: FragmentCardsBinding
    val viewModel: CardsViewModel by lazy {
        createViewModel { CardsViewModel(HearthStoneRepo.provideHearthStoneRepo())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var adapter = CardsAdapter()
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cards, container,false)
        binding.lifecycleOwner = this
        binding.cardList.adapter = adapter

        viewModel.cardsFound.observe(viewLifecycleOwner, {
            adapter.setData(it)
            Log.d("Yoshi", "${viewModel.cardsFound.value}")
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val search = CardsFragmentArgs.fromBundle(requireArguments()).searchCard
        viewModel.searchCards(search)
    }
}