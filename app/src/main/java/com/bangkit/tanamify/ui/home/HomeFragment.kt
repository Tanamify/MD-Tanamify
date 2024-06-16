package com.bangkit.tanamify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.tanamify.data.local.HistoryEntity
import com.bangkit.tanamify.databinding.FragmentHomeBinding
import com.bangkit.tanamify.utils.HistoryAdapter
import com.bangkit.tanamify.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.historyList.layoutManager = layoutManager

        val adapter = HistoryAdapter { history ->
            deleteHistory(history)
        }
        binding.historyList.adapter = adapter

        viewModel.historyList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun deleteHistory(history: HistoryEntity) {
        viewModel.deleteHistory(history)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}