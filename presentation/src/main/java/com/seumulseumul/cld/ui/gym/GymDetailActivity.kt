package com.seumulseumul.cld.ui.gym

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seumulseumul.cld.databinding.ActivityGymDetailBinding
import com.seumulseumul.cld.ui.term.TermDetailActivity

class GymDetailActivity: AppCompatActivity() {

    companion object {
        private val TAG: String = GymDetailActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityGymDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGymDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}