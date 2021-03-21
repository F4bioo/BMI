package com.fappslab.bmi.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat.getFloat
import androidx.lifecycle.ViewModelProvider
import com.fappslab.bmi.R
import com.fappslab.bmi.databinding.ActivityMainBinding
import com.fappslab.bmi.databinding.ResultMainBinding
import com.fappslab.bmi.model.Person
import com.fappslab.bmi.repository.MainRepository
import com.fappslab.bmi.util.Colors
import com.fappslab.bmi.util.Constants.FEMALE
import com.fappslab.bmi.util.Constants.LIGHT_MODE
import com.fappslab.bmi.util.Constants.MALE
import com.fappslab.bmi.util.Constants.NIGHT_MODE
import com.fappslab.bmi.util.PrefsUtil
import com.fappslab.bmi.util.PrefsUtil.PREF_KEY_MODE_NIGHT
import com.fappslab.bmi.util.state.DataState
import com.fappslab.bmi.util.state.StateEventCalc
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var count = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(MainRepository())
        ).get(MainViewModel::class.java)

        binding.cardMale.isChecked = true
        setTheme()
        setListeners()
        observeViewModelEvents()
    }

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }

    // OnClick Method
    fun onClickMain(view: View) {
        binding.run {
            when (view.id) {
                buttonHeightDec.id -> { // Height Button decrease
                    count = sliderHeight.value
                    count--
                    if (count <= 0) count = 0F
                    sliderHeight.value = count
                    textHeight.text = count.toInt().toString()
                }
                buttonHeightInc.id -> { // Height Button increase
                    count = sliderHeight.value
                    count++
                    val resValue = getFloat(resources, R.dimen.height_value_to)
                    if (count >= resValue) count = resValue
                    sliderHeight.value = count
                    textHeight.text = count.toInt().toString()
                }
                buttonWeightDec.id -> { // Weight Button decrease
                    count = sliderWeight.value
                    count--
                    if (count <= 0) count = 0F
                    sliderWeight.value = count
                    textWeight.text = count.toInt().toString()
                }
                buttonWeightInc.id -> { // Weight Button increase
                    count = sliderWeight.value
                    count++
                    val resValue = getFloat(resources, R.dimen.weight_value_to)
                    if (count >= resValue) count = resValue
                    sliderWeight.value = count
                    textWeight.text = count.toInt().toString()
                }
                buttonAgeDec.id -> { // Age Button decrease
                    count = sliderAge.value
                    count--
                    if (count <= 0) count = 0F
                    sliderAge.value = count
                    textAge.text = count.toInt().toString()
                }
                buttonAgeInc.id -> { // Age Button increase
                    count = sliderAge.value
                    count++
                    val resValue = getFloat(resources, R.dimen.age_value_to)
                    if (count >= resValue) count = resValue
                    sliderAge.value = count
                    textAge.text = count.toInt().toString()
                }
                buttonCalculate.id -> {
                    // Calculate
                    val person = Person(getGender(), getHeight(), getWeight(), getAge())
                    viewModel.setStateEvent(person, StateEventCalc.StateEvent)
                }
                fabLightMode.id -> {
                    // Set Night mode
                    viewModel.setStateTheme(PrefsUtil.getPref(this@MainActivity), NIGHT_MODE)
                }
                fabNightMode.id -> {
                    // Set Light mode
                    viewModel.setStateTheme(PrefsUtil.getPref(this@MainActivity), LIGHT_MODE)
                }
            }
        }
    }

    private fun setListeners() {
        binding.run {
            // Cards Listeners
            cardMale.setOnClickListener {
                cardMale.isChecked = true
                cardFemale.isChecked = false
            }
            cardFemale.setOnClickListener {
                cardMale.isChecked = false
                cardFemale.isChecked = true
            }

            // Sliders Listeners
            sliderHeight.addOnChangeListener { _, value, _ ->
                textHeight.text = value.toInt().toString()
            }
            sliderWeight.addOnChangeListener { _, value, _ ->
                textWeight.text = value.toInt().toString()
            }
            sliderAge.addOnChangeListener { _, value, _ ->
                textAge.text = value.toInt().toString()
            }
        }
    }

    // Observe Events from ViewModel
    private fun observeViewModelEvents() {
        viewModel.calcEvent.observe(this) { _dataState ->
            when (_dataState) {
                is DataState.Success -> {
                    val bmi = _dataState.data.bmi
                    val bfp = _dataState.data.bfp
                    val bmiTable = _dataState.data.bmiTable
                    dialogResult(bmi, bfp, bmiTable)
                }
                is DataState.Error -> {
                    Toast.makeText(
                        this,
                        _dataState.message, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.themeEvent.observe(this) {
            setTheme()
        }
    }

    // Get values from sliders and cards
    private fun getGender() = if (binding.cardMale.isChecked) MALE else FEMALE

    private fun getHeight() = binding.sliderHeight.value

    private fun getWeight() = binding.sliderWeight.value.toInt()

    private fun getAge() = binding.sliderAge.value.toInt()

    private fun dialogResult(bmi: String, bfp: String, @StringRes bmiTable: Int) {
        val binding = ResultMainBinding.inflate(layoutInflater)
        // BMI
        binding.halfGauge.value = bmi.toFloat()
        // Convert from vararg to IntArray
        binding.halfGauge.setLineColors(*Colors.array())

        // BFP
        binding.textBfpResult.text = bfp
        binding.textBmiTable.setText(bmiTable)

        // Show a dialog for result
        MaterialAlertDialogBuilder(this, R.style.AlertDialogAppearance)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }

    private fun setTheme() {
        // Get value from Shared Preferences Zero is default
        val child = PrefsUtil.getPref(this)
            .getInt(PREF_KEY_MODE_NIGHT, 0)

        // Show/Hide Fab button (0 LightMode). (1 Night mode)
        binding.flipper.displayedChild = child

        when (child == 0) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
