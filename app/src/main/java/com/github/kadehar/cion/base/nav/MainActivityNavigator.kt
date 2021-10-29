package com.github.kadehar.cion.base.nav

import androidx.appcompat.app.AppCompatActivity
import com.github.kadehar.cion.R
import com.github.terrakok.cicerone.androidx.AppNavigator

class MainActivityNavigator(activity: AppCompatActivity) : AppNavigator(
    activity = activity,
    containerId = R.layout.activity_main
)