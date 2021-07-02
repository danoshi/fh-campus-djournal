package fh.campus.djournal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fh.campus.djournal.R
import fh.campus.djournal.adapters.AudioRecordListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentRecordsBinding
import fh.campus.djournal.models.AudioRecord
import fh.campus.djournal.repositories.AudioRecordRepository
import fh.campus.djournal.viewmodels.AudioRecordViewModel
import fh.campus.djournal.viewmodels.AudioRecordViewModelFactory

class RecordsFragment : Fragment() {
    private lateinit var binding: FragmentRecordsBinding
    private lateinit var recordViewModel: AudioRecordViewModel
    private lateinit var viewModelFactory: AudioRecordViewModelFactory
    private lateinit var records: List<AudioRecord>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_records, container, false)

        val args = RecordsFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).recordDao
        val repository = AudioRecordRepository.getInstance(dataSource)
        viewModelFactory = AudioRecordViewModelFactory(repository)

        recordViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AudioRecordViewModel::class.java)

        val adapter = AudioRecordListAdapter(
            dataSet = listOf(),
        )
        binding.recyclerview.adapter = adapter

        binding.lifecycleOwner = this
        binding.recordTrackerViewModel = recordViewModel

        recordViewModel.getRecordsFromJournal(args.journalId).observe(
            viewLifecycleOwner, Observer { record ->
                adapter.updateDataSet(record)
                records = record
            }
        )

        return binding.root
    }

}