package com.example.hearthstoneapp.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteDatabase
import com.example.hearthstoneapp.databinding.FragmentFavoritesBinding
import com.example.hearthstoneapp.ui.favorites.adapter.FavoritesAdapter
import com.example.hearthstoneapp.ui.favorites.adapter.FavoritesListener
import com.example.hearthstoneapp.util.createViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = FavoriteDatabase.getInstance(application).favoriteDatabaseDao

        val viewModel: FavoritesViewModel by lazy {
            createViewModel {
                FavoritesViewModel(
                    dataSource
                )
            }
        }
        var adapter = FavoritesAdapter(FavoritesListener { favorite ->
            val card = viewModel.updateCard(favorite)
            this.findNavController().navigate(FavoritesFragmentDirections.actionNavigationFavoritesToCardDetailsFragment(
                card
            ))
        })

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner = this
        binding.favoriteList.adapter = adapter

        viewModel.cardsFound.observe(viewLifecycleOwner, {
            if (it?.size!! > 0) {
                adapter.setData(it)
                binding.noFavorites.visibility = View.GONE
                binding.favoriteList.visibility = View.VISIBLE
            }else{
                binding.noFavorites.visibility = View.VISIBLE
                binding.favoriteList.visibility = View.GONE
            }
        })
        viewModel.updateList()

        return binding.root
    }
}