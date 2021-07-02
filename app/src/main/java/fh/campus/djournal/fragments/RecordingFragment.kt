package fh.campus.djournal.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentRecordingBinding
import fh.campus.djournal.models.AudioRecord
import fh.campus.djournal.repositories.AudioRecordRepository
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.Timer
import fh.campus.djournal.utils.Util
import fh.campus.djournal.viewmodels.AudioRecordViewModel
import fh.campus.djournal.viewmodels.AudioRecordViewModelFactory
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_recording.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_CODE = 200

class RecordingFragment : Fragment(), Timer.OnTimerTickListener {
    private lateinit var binding: FragmentRecordingBinding
    private lateinit var viewModelFactory: AudioRecordViewModelFactory
    private lateinit var recordViewModel: AudioRecordViewModel

    private lateinit var amplitudes: ArrayList<Float>
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private lateinit var recorder: MediaRecorder
    private var dirPath = ""
    private var fileName = ""
    private var isRecording = false
    private var isPaused = false

    private var duration = ""
    private var journalId = 0L

    private lateinit var timer: Timer
    private lateinit var vibrator: Vibrator

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recording, container, false)

        permissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(),
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted)
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bttmSheet.bottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val args = RecordingFragmentArgs.fromBundle(requireArguments())
        journalId = args.journalId

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).recordDao
        val repository = AudioRecordRepository.getInstance(dataSource)
        viewModelFactory = AudioRecordViewModelFactory(repository)

        recordViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(AudioRecordViewModel::class.java)

        timer = Timer(this)
        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.btnRecord.setOnClickListener {
            when {
                isPaused -> resumeRecording()
                isRecording -> pauseRecording()
                else -> startRecording()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        50,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }


        binding.btnList.setOnClickListener {
            findNavController().navigate(RecordingFragmentDirections.actionRecordingFragmentToRecordsFragment(journalId))
        }


        binding.btnDone.setOnClickListener {
            stopRecording()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBG.visibility = View.VISIBLE
            fileNameInput.setText(fileName)
        }

        binding.bttmSheet.btnCancel.setOnClickListener {
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        binding.bttmSheet.btnOK.setOnClickListener {
            dismiss()
            save()
        }

        binding.bottomSheetBG.setOnClickListener {
            File("$dirPath$fileName.mp3").delete()
            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            stopRecording()
            File("$dirPath$fileName.mp3").delete()
        }

        binding.btnDelete.isClickable = false


        return binding.root
    }


    private fun save() {
        val newFileName = fileNameInput.text.toString()
        if (newFileName != fileName) {
            val newFile = File("$dirPath$newFileName.mp3")
            File("$dirPath$fileName.mp3").renameTo(newFile)
        }
        val filePath = "$dirPath$newFileName.mp3"
        val timestamp = Util().getDateTime()
        val ampsPath = "$dirPath$newFileName"

        try {
            val fileOutputStream = FileOutputStream(ampsPath)
            val out = ObjectOutputStream(fileOutputStream)
            out.writeObject(amplitudes)
            fileOutputStream.close()
            out.close()
        } catch (e: IOException) {
        }

        val record = AudioRecord(newFileName, filePath, timestamp, duration, ampsPath, journalId)
        recordViewModel.addRecord(record)
    }

    private fun dismiss() {
        bottomSheetBG.visibility = View.GONE
        hideKeyboard(fileNameInput)

        Handler(Looper.getMainLooper()).postDelayed({
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }, 100)
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE)
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        recorder.pause()
        isPaused = true
        binding.btnRecord.setImageResource(R.drawable.ic_record)
        timer.pause()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        recorder.resume()
        isPaused = false
        binding.btnRecord.setImageResource(R.drawable.ic_pause)

        timer.start()
    }

    private fun startRecording() {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)
            return
        }
        recorder = MediaRecorder()
        dirPath = "${activity?.externalCacheDir?.absolutePath}/"

        var date = Util().getDateTime()
        fileName = "audio_record_$date"

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$dirPath$fileName.mp3")

            try {
                prepare()
            } catch (e: IOException) {
            }
            start()
        }

        binding.btnRecord.setImageResource(R.drawable.ic_pause)
        isRecording = true
        isPaused = false

        timer.start()

        binding.btnDelete.isClickable = true
        binding.btnDelete.setImageResource(R.drawable.ic_delete)

        binding.btnList.visibility = View.GONE
        binding.btnDone.visibility = View.VISIBLE
    }

    private fun stopRecording() {
        timer.stop()

        recorder.apply {
            stop()
            release()
        }

        isPaused = false
        isRecording = false
        btnList.visibility = View.VISIBLE
        btnDone.visibility = View.GONE
        btnDelete.isClickable = false
        binding.btnDelete.setImageResource(R.drawable.ic_delete_disabled)
        binding.btnRecord.setImageResource(R.drawable.ic_record)
        binding.tvTimer.text = "00:00:00"
        amplitudes = waveFormView.clear()
    }

    override fun onTimerTick(duration: String) {
        binding.tvTimer.text = duration
        this.duration = duration.dropLast(3)
        waveFormView.addAmplitude(recorder.maxAmplitude.toFloat())
    }
}