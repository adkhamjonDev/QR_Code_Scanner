package com.example.qrcode

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.qrcode.databinding.FragmentInfoBinding

class InfoFragment : Fragment(),View.OnClickListener {
    private lateinit var binding: FragmentInfoBinding
    private var str:String?=null
    private var date:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentInfoBinding.inflate(inflater, container, false)
        if(arguments!=null){
            str=arguments?.getString("data")as String
            date=arguments?.getString("date")as String
        }
        binding.msg.text=str
        binding.date.text=date.toString()
        binding.txt.text=str
        if(str!!.startsWith("http")){
            binding.tittle.text="Open in browser"
            binding.web.visibility=View.VISIBLE
            binding.mess.visibility=View.GONE
            binding.open.visibility=View.VISIBLE
            binding.t.text="Url Code"
        }
        else{
            binding.tittle.text="Message"
            binding.web.visibility=View.GONE
            binding.mess.visibility=View.VISIBLE
            binding.open.visibility=View.GONE
            binding.t.text="Text"
        }
        binding.close.setOnClickListener(this)
        binding.share.setOnClickListener(this)
        binding.open.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.close->{
                findNavController().popBackStack()
            }
            R.id.share->{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    str
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            R.id.open->{
                val intent= Intent("android.intent.action.VIEW")
                intent.data = Uri.parse(str.toString())
                startActivity(intent)
            }
        }
    }
}