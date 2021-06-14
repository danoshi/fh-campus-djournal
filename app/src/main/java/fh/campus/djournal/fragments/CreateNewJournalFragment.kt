package fh.campus.djournal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import fh.campus.djournal.R
import fh.campus.djournal.databinding.FragmentCreateNewJournalBinding
import fh.campus.djournal.models.Journal

class CreateNewJournalFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewJournalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_new_journal,
            container,
            false
        )

        binding.btnCreate.setOnClickListener {
            onCreate()
        }

        binding.btnCancel.setOnClickListener {
            onCancel()
        }

        return binding.root
    }

    private fun onCreate() {
        val pressedNewJournal = true
        val title = binding.editTextNewJournalName.text.toString()
        val description = binding.editTextNewJournalDescription.text.toString()
        val color = binding.editTextNewJournalColor.text.toString()
        if(title.isEmpty() && description.isEmpty()) {
            binding.editTextNewJournalName.setError("Please Enter a Name")
            binding.editTextNewJournalDescription.setError("Please Enter a Description")
        } else if (title.isEmpty()) {
            binding.editTextNewJournalName.setError("Please Enter a Name")
        } else if (description.isEmpty()) {
            binding.editTextNewJournalDescription.setError("Please Enter a Description")
        } else {
            findNavController().navigate(CreateNewJournalFragmentDirections.actionCreateNewJournalFragmentToHomeFragment(pressedNewJournal,title, description))
        }

//        findNavController().navigate(CreateNewJournalFragmentDirections.actionCreateNewJournalFragmentToHomeFragment(title, description))
    }

    private fun onCancel() {
        findNavController().navigate(CreateNewJournalFragmentDirections.actionCreateNewJournalFragmentToHomeFragment(false,"", ""))
    }

}