package com.example.qrcode
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrcode.databinding.FragmentMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment(),View.OnClickListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var mediaPlayer: MediaPlayer
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMainBinding.inflate(inflater, container, false)
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {data->
            requireActivity().runOnUiThread {
                mediaPlayer=MediaPlayer.create(requireContext(),R.raw.short_msg)
                mediaPlayer.start()
                val date= Date()
                val simpleDateFormat= SimpleDateFormat("HH:mm dd.MM.yyy ")
                val currentDate=simpleDateFormat.format(date)
                val bundle= bundleOf("data" to data.toString(),"date" to currentDate)
                val v = activity?.getSystemService(Context.VIBRATOR_SERVICE)as Vibrator
                v.vibrate(300)
                findNavController().navigate(R.id.infoFragment,bundle)
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        binding.seek.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                codeScanner.zoom=progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        setOnClick()
        return binding.root
    }
    private fun setOnClick(){
        binding.back.setOnClickListener(this)
        binding.front.setOnClickListener(this)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.back -> {
                codeScanner.camera = CodeScanner.CAMERA_FRONT
                binding.back.visibility = View.GONE
                binding.front.visibility = View.VISIBLE
            }
            R.id.front -> {
                codeScanner.camera = CodeScanner.CAMERA_BACK
                binding.back.visibility = View.VISIBLE
                binding.front.visibility = View.GONE
            }
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}