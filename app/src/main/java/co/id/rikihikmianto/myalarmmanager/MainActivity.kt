package co.id.rikihikmianto.myalarmmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import co.id.rikihikmianto.myalarmmanager.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOnceDate.setOnClickListener(this)
        binding.btnOnceTime.setOnClickListener(this)
        binding.btnSetOnceAlarm.setOnClickListener(this)

        // Listener repeating alarm
        binding.btnRepeatingTime.setOnClickListener(this)
        binding.btnRepeatingAlarm.setOnClickListener(this)

        binding.btnCancelRepeatingAlarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOnceDate -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btnOnceTime -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btnSetOnceAlarm -> {
                val onceDate = binding.tvOnceDate.text.toString()
                val onceTime = binding.tvOnceTime.text.toString()
                val onceMessage = binding.edtOnceMessage.text.toString()

                alarmReceiver.setOneTimeAlarm(
                    this,
                    AlarmReceiver.TYPE_ONE_TIME,
                    onceDate,
                    onceTime,
                    onceMessage
                )
            }
            R.id.btnRepeatingTime -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btnRepeatingAlarm -> {
                val repeatTime = binding.tvRepeatingTime.text.toString()
                val repeatMessage = binding.edtRepeatingMessage.text.toString()
                alarmReceiver.setRepeatingAlarm(
                    this,
                    AlarmReceiver.TYPE_REPEATING,
                    repeatTime,
                    repeatMessage
                )
            }
            R.id.btnCancelRepeatingAlarm -> {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
//        Siapkan date formatter-nya terlebih dahulu
        val calender = Calendar.getInstance()
        calender.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

//        Set text dari textview once
        binding.tvOnceDate.text = dateFormat.format(calender.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
//        Siapkan time formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

//        Set text dari textview berdasakan tag

        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding.tvOnceTime.text = timeFormat.format(calendar.time)
            TIME_PICKER_REPEAT_TAG -> {
                binding.tvRepeatingTime.text = timeFormat.format(calendar.time)
            }
            else -> {
            }
        }
    }
}