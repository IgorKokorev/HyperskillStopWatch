package org.hyperskill.stopwatch

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.hyperskill.stopwatch.databinding.ActivityMainBinding
import org.hyperskill.stopwatch.databinding.DialogTextBinding
import java.lang.Exception
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    var isStarted: Boolean = false
    var seconds: Int = 0
    var limit = 0
    lateinit var notificationService: NotificationService

    private val timer: Runnable = object : Runnable {
        override fun run() {
            val min = (seconds / 60).toString().padStart(2, '0')
            val sec = (seconds % 60).toString().padStart(2, '0')
            binding.textView.text = "$min:$sec"
            handler.postDelayed(this, 1000)
            val color = Random.nextInt(256 * 256 * 256) + 256 * 256 * 256 * 255

            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(color)
            if (limit > 0 && seconds > limit) {
                binding.textView.setTextColor(Color.RED)
                notificationService.sendNotification(R.drawable.ic_launcher_foreground, "Title", "Text")
            }
            if (isStarted) seconds++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = "00:00"

        notificationService = NotificationService(this)

        binding.startButton.setOnClickListener {
            if(!isStarted) {
                isStarted = true
                binding.progressBar.isVisible = true
                binding.settingsButton.isEnabled = false
                handler.post(timer)
            }
        }

        binding.resetButton.setOnClickListener {
            if (isStarted) {
                isStarted = false
                binding.progressBar.isVisible = false
                binding.settingsButton.isEnabled = true
                binding.textView.setTextColor(Color.BLACK)
                seconds = 0
                handler.removeCallbacks(timer)
                binding.textView.text = "00:00"
            }
        }

        binding.settingsButton.setOnClickListener {
            val dialogTextBinding = DialogTextBinding.inflate(layoutInflater)

            AlertDialog.Builder(this)
                .setTitle("Set upper limit in seconds:")
                .setView(dialogTextBinding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    limit = try {
                        dialogTextBinding.upperLimitEditText.text.toString().toInt()
                    } catch (e: Exception) {
                        0
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    override fun onDestroy() {
        if (isStarted) handler.removeCallbacks(timer)
        super.onDestroy()
    }
}