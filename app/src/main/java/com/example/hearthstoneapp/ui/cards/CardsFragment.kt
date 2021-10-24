package com.example.hearthstoneapp.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.databinding.FragmentCardsBinding
import com.example.hearthstoneapp.databinding.FragmentMainScreenBinding
import com.example.hearthstoneapp.ui.mainscreen.MainScreenViewModel
import com.example.hearthstoneapp.util.createViewModel

class CardsFragment : Fragment() {

    private lateinit var binding: FragmentCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val card = CardsFragmentArgs.fromBundle(requireArguments()).listCardResponse
        val viewModel: CardsViewModel by lazy {
            createViewModel { CardsViewModel(card)
            }
        }
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cards, container,false)
        binding.lifecycleOwner = this

        viewModel.listCard.observe(viewLifecycleOwner, {
            binding.nameClass.text = it.name
        })
        return binding.root
    }
}