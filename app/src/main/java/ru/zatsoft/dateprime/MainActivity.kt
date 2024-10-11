package ru.zatsoft.dateprime

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.dateprime.databinding.ActivityMainBinding
import java.io.IOException
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val REQUEST_GALLERY = 201

class MainActivity : AppCompatActivity() {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var person: Person

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val defaultFoto =
            "android.resource://" + packageName + "/" + R.drawable.ic_person_foreground
        person = Person("", "", "", defaultFoto)
        binding.ivFoto.setImageURI(Uri.parse(defaultFoto))
        binding.btnSave.setOnClickListener {
            person.name = binding.tvName.text.toString()
            person.lastName = binding.tvlastName.text.toString()
            val birthday = binding.tvBirthday.text.toString()
//    Проверка правильной даты
            try {
                val birthDate = LocalDate.parse(birthday, formatter)
                person.birthday = binding.tvBirthday.text.toString()
                val intent = Intent(this, NextActivity::class.java)
                intent.putExtra("person", person)
                startActivity(intent)
            } catch (e: DateTimeException) {
                Toast.makeText(
                    this,
                    "Неправильный день рождения (пример - 12.04.1961)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.ivFoto.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, REQUEST_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//       Загрузка изображения
            REQUEST_GALLERY -> if (resultCode === RESULT_OK && data != null && data.data != null) {
                                   val selectedPhoto: Uri? = data.data
                                   try {
                                       person.image = selectedPhoto.toString()
                                   } catch (e: IOException) {
                                       e.printStackTrace()
                                   }
                                   binding.ivFoto.setImageURI(selectedPhoto)
                               } else {
                                   Toast.makeText(
                                       applicationContext,
                                       "Ошибка загрузки изображения result.data",
                                       Toast.LENGTH_LONG
                                   ).show()
                               }
            else -> Toast.makeText(
                applicationContext,
                "Ошибка загрузки изображени requestCode = $requestCode",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
