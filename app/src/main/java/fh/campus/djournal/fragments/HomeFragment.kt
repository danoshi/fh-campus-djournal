package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fh.campus.djournal.CreateNewJournalFragment
import fh.campus.djournal.R
import fh.campus.djournal.adapters.JournalListAdapter
import fh.campus.djournal.databinding.FragmentHomeBinding
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.JournalStore
import fh.campus.djournal.models.Util

class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    private val journalList: MutableList<Journal> = JournalStore.exampleJournals
    private var pressedNewJournal: Boolean = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val adapter = JournalListAdapter()    // instantiate a new MovieListAdapter for recyclerView
        binding.journalList.adapter = adapter // assign adapter to the recyclerView

        subscribeUI(adapter)

        val args = HomeFragmentArgs.fromBundle(requireArguments())
        pressedNewJournal = args.pressedNewJournal
        if(pressedNewJournal) {
            journalList.add(Journal(args.name, args.description))
            adapter.notifyDataSetChanged()
            pressedNewJournal = false
        }


        binding.addNewJournal.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateNewJournalFragment())
//            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun subscribeUI(adapter: JournalListAdapter) {
        //TODO remove later only for testing purposes, or change it so it fetches already created journals from user
        val journalList = JournalStore()
        adapter.submitList(journalList.defaultJournals)
    }

}