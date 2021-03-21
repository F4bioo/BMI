package com.fappslab.bmi.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.fappslab.bmi.R
import com.fappslab.bmi.model.Person
import com.fappslab.bmi.model.Result
import com.fappslab.bmi.util.PrefsUtil.PREF_KEY_MODE_NIGHT
import com.fappslab.bmi.util.state.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.pow

class MainRepository {

    fun calc(person: Person): Flow<DataState<Result>> = flow {
        try {
            if (person.height > 0) {
                val bmi = calcBMI(person.weight, person.height)
                val bfp = calcBFP(bmi.toFloat(), person.age, person.gender)
                val bmiTable = bmiTable(bmi.toFloat())

                val result = Result(bmi, bfp, bmiTable)
                emit(DataState.Success(result))
            }

        } catch (e: Exception) {
            // If an error occurs send a message
            emit(DataState.Error("Exception: ${e.message}"))
        }
    }

    // How to calculate (BMI) - Body Mass Index
    // BMI = weight(kg) / [height(m)]2
    private fun calcBMI(weight: Int, height: Float): String {
        // (height / 100) -> convert Height from cm to m
        val pow = (height / 100).toDouble().pow(2)
        val bmi = (weight / pow)
        return if (bmi < 0) "0" else "%.2f".format(bmi)
    }

    // How to calculate  (BFP) - Body Fat Percentage based on BMI
    // For Children
    // gender M = 1, F = 0
    // (1.52 * bmi) - (0.7 * age) - (3.6 * gender) + 1.4
    // ----------
    // For Adults
    // gender: M = 1, F = 0
    // (1.2 * imc) + (0.23 * age) - (10.8 * gender) - 5.4
    private fun calcBFP(bmi: Float, age: Int, gender: Int): String {
        val bfp = ((1.2 * bmi) + (0.23 * age) - (10.8 * gender) - 5.4)
        return if (bfp < 0) "0" else "%.2f".format(bfp)
    }

    // >=  0.00 && <= 15.99
    // >= 16.00 && <= 16.99
    // >= 17.00 && <= 17.49
    // >= 18.50 && <= 24.99
    // >= 25.00 && <= 29.99
    // >= 30.00 && <= 34.99
    // >= 35.00 && <= 39.99
    // >= 40.00
    // Return a @StringRes Int
    private fun bmiTable(bmi: Float): Int {
        return when {
            bmi < 16.0 -> R.string.very_severely_underweight
            bmi >= 16.0 && bmi < 17.0 -> R.string.severely_underweight
            bmi >= 17.0 && bmi < 18.5 -> R.string.underweight
            bmi >= 18.5 && bmi < 25.0 -> R.string.normal
            bmi >= 25.0 && bmi < 30.0 -> R.string.overweight
            bmi >= 30.0 && bmi < 35.0 -> R.string.obese_class_i
            bmi >= 35.0 && bmi < 40.0 -> R.string.obese_class_ii
            else -> R.string.obese_class_iii
        }
    }

    // Set Theme
    fun setKey(pref: SharedPreferences, child: Int): Int {
        pref.edit {
            putInt(PREF_KEY_MODE_NIGHT, child)
            apply()
        }

        // if 0 is LightMode. if 1 NightMode.
        return child
    }
}
