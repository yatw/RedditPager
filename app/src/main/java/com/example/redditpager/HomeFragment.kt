package com.example.redditpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.redditpager.api.AuthHelper
import com.example.redditpager.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class HomeFragment: Fragment() {


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking(Dispatchers.IO) {
            try{
                AuthHelper.authenticate(requireContext())
            }catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(requireContext(), "reddit authentication failed, check wifi", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnPaging.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pagingFragment)
        }

        binding.btnMediator.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mediatorFragment)
        }

        binding.btnPaging2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_paging2Fragment)
        }
    }


}