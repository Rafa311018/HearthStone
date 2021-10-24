package com.example.hearthstoneapp.ui.mainscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.databinding.FragmentMainScreenBinding
import com.example.hearthstoneapp.ui.mainscreen.adapter.ClassesAdapter
import com.example.hearthstoneapp.util.createViewModel
import kotlinx.android.synthetic.main.grid_item_class.*
import timber.log.Timber

class MainScreen : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding

    private val viewModel: MainScreenViewModel by lazy {
        createViewModel { MainScreenViewModel(app = this.requireActivity().application,
            HearthStoneRepo.provideHearthStoneRepo()) }
    }
    private lateinit var adapter: ClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = ClassesAdapter(ClassesAdapter.OnClickListener {
            this.findNavController().navigate(MainScreenDirections.actionNavigationMainScreenToCardsFragment(
                requireNotNull(it)))
        })
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)
        binding.lifecycleOwner = this
        binding.mainScreenRV.adapter = adapter
        binding.viewModel = viewModel

        binding.mainScreenRV.visibility = View.GONE
        binding.loadingIV.visibility = View.VISIBLE

        viewModel.navitagionToCards.observe(viewLifecycleOwner, {
            if (it) {
                this.findNavController().navigate(MainScreenDirections.actionNavigationMainScreenToCardsFragment(viewModel.search.value!!))
                viewModel.doneNavigation()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservables()
        viewModel.fetchClassList()
    }

    private fun setUpObservables() {
        viewModel.hearthStoneClasses.observe(viewLifecycleOwner, { responseList ->
            adapter.setData(responseList)
            binding.mainScreenRV.visibility = View.VISIBLE
            binding.loadingIV.visibility = View.GONE
        })
    }
}