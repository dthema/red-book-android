package com.begletsov.redbook.ui.audioguide

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.begletsov.redbook.Data
import com.begletsov.redbook.R
import com.begletsov.redbook.databinding.FragmentAudioguideBinding
import com.begletsov.redbook.databinding.FragmentChoosePlaceBinding
import com.begletsov.redbook.models.Category
import com.begletsov.redbook.models.Description
import com.begletsov.redbook.models.Geopoint
import com.begletsov.redbook.models.Place
import com.begletsov.redbook.ui.audioguide.viewmodels.AudioguideViewModel
import com.begletsov.redbook.ui.audioguide.viewmodels.ChoosePlaceViewModel
import com.begletsov.redbook.ui.utils.CategoryAdapter
import com.begletsov.redbook.ui.utils.PlaceAdapter
import java.util.UUID

class ChoosePlaceFragment : Fragment() {

    private var _binding: FragmentChoosePlaceBinding? = null
    private val binding get() = _binding!!

    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var viewModel: ChoosePlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val navController = findNavController()

        val categoryId = UUID.fromString(requireArguments().getString("id"))
        val category = Data.categories.first { it.id == categoryId }

        binding.placesCategoryTitle.text = category.name
        viewModel = ViewModelProvider(this).get(ChoosePlaceViewModel::class.java)
        placeAdapter = PlaceAdapter(requireContext())
        binding.placesRecyclerView.adapter = placeAdapter

        placeAdapter.submitList(category.places)

        binding.placesBack.setOnClickListener { navController.navigate(R.id.action_navigation_choose_place_pop) }

        return root
    }
}