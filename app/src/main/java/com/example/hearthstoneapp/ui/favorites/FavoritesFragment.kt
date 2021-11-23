package com.example.hearthstoneapp.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteDatabase
import com.example.hearthstoneapp.databinding.FragmentFavoritesBinding
import com.example.hearthstoneapp.ui.favorites.adapter.FavoritesAdapter
import com.example.hearthstoneapp.ui.favorites.adapter.FavoritesListener
import com.example.hearthstoneapp.util.createViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var adapter = FavoritesAdapter(FavoritesListener { favorite, click, position ->
            if (click == "details") {
                val card = viewModel.updateCard(favorite)
                this.findNavController().navigate(
                    FavoritesFragmentDirections.actionNavigationFavoritesToCardDetailsFragment(
                        card
                    )
                )
            } else {
                viewModel.deleteFavorite(favorite)
                viewModel.updateList()
            }
        })

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )
        binding.lifecycleOwner = this
        binding.favoriteList.adapter = adapter

        viewModel.cardsFound.observe(viewLifecycleOwner, {
            if (it?.size!! > 0) {
                adapter.setData(it)
                binding.noFavorites.visibility = View.GONE
                binding.favoriteList.visibility = View.VISIBLE
            } else {
                binding.noFavorites.visibility = View.VISIBLE
                binding.favoriteList.visibility = View.GONE
            }
        })
        viewModel.updateList()

        return binding.root
    }
}
