package fh.campus.djournal.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import fh.campus.djournal.R
import fh.campus.djournal.adapters.NoteListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentNotesBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.NoteDialogs
import fh.campus.djournal.utils.ToastMaker
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory

class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var notesToLog: List<Note>
    private var journalId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val args = NotesFragmentArgs.fromBundle(requireArguments())
        journalId = args.journalId

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)

        val dialog = NoteDialogs(requireContext(), noteViewModel)

        val adapter = NoteListAdapter(
            dataSet = listOf(),
            onNoteItemShortClicked = { note ->
                findNavController().navigate(
                    NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(
                        note.noteId,
                        note.journalIdOfNote
                    )
                )
            },
            onNoteItemLongClicked = { note -> dialog.noteOptionDialog(note) },
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.noteList.adapter = adapter // assign adapter to the recyclerView

        binding.lifecycleOwner = this
        binding.noteTrackerViewModel = noteViewModel

        noteViewModel.getNotesFromJournal(args.journalId).observe(
            viewLifecycleOwner, Observer { note ->
                adapter.updateDataSet(note)
                notesToLog = note
            }
        )

        initSpeedDial()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun initSpeedDial() {
        val speedDialView = binding.speedDial
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_note, R.drawable.ic_note_plus_black_48dp)
                .setFabBackgroundColor(resources.getColor(R.color.material_purple_500))
                .setLabel("New Note")
                .setLabelBackgroundColor(Color.TRANSPARENT)
                .create()
        )
        var drawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_microphone_black_48dp)
        val fabWithLabelView = speedDialView.addActionItem(
            SpeedDialActionItem.Builder(
                R.id
                    .fab_voice_note, drawable
            )
                .setFabBackgroundColor(resources.getColor(R.color.teal_200))
                .setLabel("New Voice Note")
                .setLabelBackgroundColor(Color.TRANSPARENT)
                .create()
        )
        fabWithLabelView?.apply {
            speedDialActionItem = speedDialActionItemBuilder
                .create()
        }
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(
                R.id.fab_drawing_note, R.drawable
                    .ic_draw_black_48dp
            )
                .setFabBackgroundColor(resources.getColor(R.color.material_blue_700))
                .setLabel("New Voice Note")
                .setLabelBackgroundColor(Color.TRANSPARENT)
                .create()
        )


        // Set option fabs clicklisteners.
        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_note -> {
                    findNavController().navigate(
                        NotesFragmentDirections.actionNotesFragmentToNoteDetailFragment(
                            -1L,
                            journalId
                        )
                    )
                    speedDialView.close() // To close the Speed Dial with animation
                }
                R.id.fab_voice_note -> {
                    //TODO: navigate to voice note fragment
                    ToastMaker().toastMaker(requireContext(), "AAAA")
                    speedDialView.close() // To close the Speed Dial with animation
                }
                R.id.fab_drawing_note -> {
                    //TODO: navigate to voice note fragment
                    ToastMaker().toastMaker(requireContext(), "BBBB")
                    speedDialView.close() // To close the Speed Dial with animation
                }
            }
            true
        })
    }


}