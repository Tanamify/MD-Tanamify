package com.bangkit.tanamify.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bangkit.tanamify.data.pref.UserPreference
import com.bangkit.tanamify.data.pref.dataStore
import com.bangkit.tanamify.databinding.FragmentProfileBinding
import com.bangkit.tanamify.ui.login.LoginActivity
import com.bangkit.tanamify.utils.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = ViewModelFactory.getInstance(requireContext())
        profileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        setLogoutClickListener()

        val textView: TextView = binding.tvName
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun setLogoutClickListener() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                profileViewModel.logout()
                val userPreference = UserPreference.getInstance(requireContext().dataStore)
                userPreference.logout()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
