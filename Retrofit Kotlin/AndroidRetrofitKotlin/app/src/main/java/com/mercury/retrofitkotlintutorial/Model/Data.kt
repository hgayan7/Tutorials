package com.mercury.retrofitkotlintutorial.Model

data class Data(
    val photos : ArrayList<Photo>
)

data class Photo(
    val img_src : String,
    val earth_date : String,
    val rover : Rover,
    val camera : Camera
)
data class Rover(
    val name : String
)
data class Camera(
    val name : String
)
