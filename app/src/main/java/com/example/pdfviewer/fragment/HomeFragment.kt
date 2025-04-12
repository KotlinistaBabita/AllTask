package com.example.pdfviewer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfviewer.R
import com.example.pdfviewer.adapter.ServiceAdapter
import com.example.pdfviewer.local.AppDatabase
import com.example.pdfviewer.local.ServiceItemEntity
import com.example.pdfviewer.remote.RetrofitClient
import com.example.pdfviewer.remote.ServiceRepository
import com.example.pdfviewer.remote.ServiceViewModel
import com.example.pdfviewer.remote.ServiceViewModelFactory



class HomeFragment : Fragment() {
    private lateinit var viewModel: ServiceViewModel
    private lateinit var adapter: ServiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = ServiceRepository(
            apiService = RetrofitClient.apiService,
            serviceDao = AppDatabase.getDatabase(requireContext()).serviceDao()
        )
        val factory = ServiceViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[ServiceViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvApiData)
        adapter = ServiceAdapter(
            onDeleteClick = {
                viewModel.deleteItem(requireContext(), it) },
            onEditClick = {
                    item -> showUpdateDialog(item)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.storedItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)

        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchDataFromApi()
    }

    private fun showUpdateDialog(item: ServiceItemEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.update_dialog, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.etName)

        nameEditText.setText(item.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Update Item")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedItem = item.copy(
                    name = nameEditText.text.toString(),

                )
                viewModel.updateItem(updatedItem)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }





}