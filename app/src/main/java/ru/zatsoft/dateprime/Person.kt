package ru.zatsoft.dateprime

import java.io.Serializable

data class Person(var name: String, var lastName: String, var birthday: String, var image: String): Serializable
