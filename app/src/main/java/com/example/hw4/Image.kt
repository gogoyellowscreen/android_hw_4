package com.example.hw4

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val id: String,
    val description: String,
    val image: String,
    val ref: String
) : Parcelable