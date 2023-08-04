package com.begletsov.redbook.ui.excursion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.begletsov.redbook.databinding.FragmentExcursionBinding

class ExcursionFragment : Fragment() {

    private var _binding: FragmentExcursionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val excursionViewModel =
            ViewModelProvider(this).get(ExcursionViewModel::class.java)

        _binding = FragmentExcursionBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}