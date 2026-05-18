package com.example.zerog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zerog.databinding.FragmentHistoryBinding
import com.example.zerog.ui.viewmodel.GravityViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GravityViewModel by activityViewModels()
    private lateinit var adapter: GravityLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GravityLogAdapter(
            onItemClick = { log ->
                // Click → edit name dialog
                val til = TextInputLayout(requireContext()).apply {
                    hint = "Item name"
                    setPadding(48, 16, 48, 0)
                }
                val input = TextInputEditText(requireContext()).apply {
                    setText(log.itemName)
                }
                til.addView(input)

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_edit_title))
                    .setView(til)
                    .setPositiveButton(getString(R.string.btn_update)) { _, _ ->
                        val newName = input.text.toString().trim()
                        if (newName.isNotEmpty()) viewModel.update(log.copy(itemName = newName))
                    }
                    .setNegativeButton(getString(R.string.btn_cancel), null)
                    .show()
            },
            onDeleteClick = { log ->
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_delete_title))
                    .setMessage("Remove \"${log.itemName}\" from the log?")
                    .setPositiveButton(getString(R.string.btn_delete)) { _, _ ->
                        viewModel.delete(log)
                    }
                    .setNegativeButton(getString(R.string.btn_cancel), null)
                    .show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allLogs.collect { logs ->
                    adapter.submitList(logs)
                    val isEmpty = logs.isEmpty()
                    binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
