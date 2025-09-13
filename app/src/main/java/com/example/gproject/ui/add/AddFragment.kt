package com.example.gproject.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gproject.databinding.FragmentAddBinding
import androidx.navigation.fragment.findNavController
import com.example.gproject.R
class AddFragment : Fragment() {

private var _binding: FragmentAddBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

    _binding = FragmentAddBinding.inflate(inflater, container, false)

    return binding.root
  }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo : 스캔 버튼 로직 추가

        // 폼 화면으로 이동
        binding.directlyAddButton.setOnClickListener {
            findNavController().navigate(R.id.directlyAddButton)
        }
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}