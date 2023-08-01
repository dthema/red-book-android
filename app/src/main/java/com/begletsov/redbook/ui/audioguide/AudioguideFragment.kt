package com.begletsov.redbook.ui.audioguide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.begletsov.redbook.databinding.FragmentAudioguideBinding

class AudioguideFragment : Fragment() {

    private var _binding: FragmentAudioguideBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val audioguideViewModel =
            ViewModelProvider(this).get(AudioguideViewModel::class.java)

        _binding = FragmentAudioguideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textView
        audioguideViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}