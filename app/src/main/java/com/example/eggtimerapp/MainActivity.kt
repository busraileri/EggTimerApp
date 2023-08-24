package com.example.eggtimerapp

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.eggtimerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var soundPool: SoundPool
    private var tickTockSoundId: Int = 0
    private var finishSoundId: Int = 0
    private var selectedTimerDuration: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.progressBar.progress = 0 //Progress bar reset on startup

        //creating soundpool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        //load file of sounds
        tickTockSoundId = soundPool.load(this, R.raw.tick_tock, 1)
        finishSoundId = soundPool.load(this, R.raw.finish_sound, 1)

        //operations that will occur when you click the egg buttons
        binding.btnSoft.setOnClickListener { startTimer(240, "SOFT", tickTockSoundId, finishSoundId) } // 4 min
        binding.btnMedium.setOnClickListener { startTimer(360, "MEDIUM", tickTockSoundId, finishSoundId) } // 6 min
        binding.btnHard.setOnClickListener { startTimer(720, "HARD", tickTockSoundId, finishSoundId) } // 12 min
    }

    private fun startTimer(timerDuration: Long, eggName: String, tickTockSound: Int, finishSound: Int) {
        //show the names of the eggs
        binding.textEgg.text = eggName
        binding.textEgg.visibility = View.VISIBLE

        selectedTimerDuration = timerDuration

        //setup progress bar
        binding.progressBar.max = selectedTimerDuration.toInt()
        binding.progressBar.progress = 0

        //start Countdown Timer
        countdownTimer = object : CountDownTimer(selectedTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingTime = millisUntilFinished / 1000
                val progress = (selectedTimerDuration - remainingTime).toInt()
                binding.progressBar.progress = progress
                playSound(tickTockSound)
            }  

            override fun onFinish() {
                binding.textEgg.text = getString(R.string.text_done)
                playSound(finishSound)
            }
        }
        countdownTimer.start()
    }

    private fun playSound(soundId: Int) {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

}
