package com.nuryazid.datetimepicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nuryazid.wheelpicker.ui.DatePicker
import com.nuryazid.wheelpicker.ui.TimePicker
import kotlinx.android.synthetic.main.activity_sample.*
import java.util.*

class SampleActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        btnTimePicker.setOnClickListener(this)
        btnDatePicker.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDatePicker -> {

                val minDate = Calendar.getInstance()
                minDate.set(Calendar.YEAR, 2000)

                val maxDate = Calendar.getInstance()
                maxDate.set(Calendar.YEAR, 2021)

                DatePicker(this)
                        .setLoop()
                        .minDay(minDate)
                        .maxDay(maxDate)
                        .setPickerListener {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        }
                        .show()
            }
            R.id.btnTimePicker -> {
                TimePicker(this)
                        .setPickerListener {
                            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                        }
                        .show()
            }
        }
    }
}
