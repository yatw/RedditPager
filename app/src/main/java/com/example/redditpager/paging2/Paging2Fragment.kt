package com.example.redditpager.paging2

import com.example.redditpager.paging3.HeaderFooterAdapter
import com.example.redditpager.paging3.PagerViewModel
import com.example.redditpager.paging3.PagingRepository
import com.example.redditpager.paging3.SubmissionAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.redditpager.api.AuthHelper
import com.example.redditpager.databinding.FragmentPagingBinding
import com.example.redditpager.db.PagerDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// exact same as PagingFragment
class Paging2Fragment: Fragment() {


    private lateinit var binding: FragmentPagingBinding

    // get the view model
    @ExperimentalPagingApi
    val viewModel: PagerViewModel by lazy {

        val submissionClient = AuthHelper.getClient().submissionClient
        val db = PagerDatabase.getInstance(requireContext())

        ViewModelProvider(this,
            PagerViewModel.Factory(this, PagingRepository(submissionClient, db), false)
        ).get(PagerViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagingBinding.inflate(inflater, container, false)
        return binding.root
    }


    @ExperimentalPagingApi
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SubmissionAdapter2()

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = HeaderFooterAdapter{ },
            footer = HeaderFooterAdapter{ }
        )


        // paging2 use liveData, not flow
        val pagingData = viewModel.pagingDataLiveData

        pagingData.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })



        val isListEmpty = false
        // show empty list
        binding.emptyList.isVisible = isListEmpty
        // Only show the list if refresh succeeds.
        binding.list.isVisible = !isListEmpty
        // Show loading spinner during initial load or refresh.
        binding.progressBar.isVisible = false
        // Show the retry state if initial load or refresh fails.
        binding.retryButton.isVisible = false


    }


}