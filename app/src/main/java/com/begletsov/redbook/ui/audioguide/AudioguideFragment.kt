package com.begletsov.redbook.ui.audioguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.begletsov.redbook.databinding.FragmentAudioguideBinding
import com.begletsov.redbook.models.Category
import com.begletsov.redbook.ui.audioguide.viewmodels.AudioguideViewModel
import com.begletsov.redbook.ui.utils.CategoryAdapter
import java.util.UUID

class AudioguideFragment : Fragment() {

    private var _binding: FragmentAudioguideBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel: AudioguideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioguideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(AudioguideViewModel::class.java)
        categoryAdapter = CategoryAdapter(requireContext())
        binding.audioguideCategoryRecycler.adapter = categoryAdapter

        categoryAdapter.submitList(listOf(
            Category(UUID.randomUUID(), "Достопримечательности", ""),
            Category(UUID.randomUUID(), "Музеи", ""),
            Category(UUID.randomUUID(), "Парки", ""),
            Category(UUID.randomUUID(), "Пирсы", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Бары", ""),
            Category(UUID.randomUUID(), "Другое", "")))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}