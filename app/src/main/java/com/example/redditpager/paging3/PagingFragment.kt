package com.example.redditpager.paging3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.redditpager.api.AuthHelper
import com.example.redditpager.databinding.FragmentPagingBinding
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PagingFragment: Fragment() {


    private lateinit var binding: FragmentPagingBinding

    // get the view model
    val viewModel: PagerViewModel by lazy {

        val submissionClient = AuthHelper.getClient().submissionClient

        ViewModelProvider(this,
            PagerViewModel.Factory(this, PagingRepository(submissionClient))
        ).get(PagerViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagingBinding.inflate(inflater, container, false)
        return binding.root
    }


    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SubmissionAdapter()
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = HeaderFooterAdapter{ adapter.retry()},
            footer = HeaderFooterAdapter{ adapter.retry()}
        )

        val pagingData = viewModel.pagingDataFlow


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                launch {
                    pagingData.collectLatest{
                        adapter.submitData(it)
                    }
                }

                launch {
                    adapter.loadStateFlow.collectLatest { loadState ->

                        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                        // show empty list
                        binding.emptyList.isVisible = isListEmpty
                        // Only show the list if refresh succeeds.
                        binding.list.isVisible = !isListEmpty
                        // Show loading spinner during initial load or refresh.
                        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        // Show the retry state if initial load or refresh fails.
                        binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                        // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                        val errorState = loadState.source.append as? LoadState.Error
                            ?: loadState.source.prepend as? LoadState.Error
                            ?: loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error
                        errorState?.let {
                            Toast.makeText(
                                requireContext(),
                                "\uD83D\uDE28 Wooops ${it.error}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

    }


}