package ru.zatsoft.dateprime

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.zatsoft.dateprime.MainActivity.Companion.formatter
import ru.zatsoft.dateprime.databinding.ActivityNextBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class NextActivity : AppCompatActivity() {
    private lateinit var person: Person
    private lateinit var toolBar: Toolbar
    private lateinit var binding: ActivityNextBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "
        val defaultFoto =
            "android.resource://" + packageName + "/" + R.drawable.ic_person_foreground

        person = intent.getSerializableExtra("person") as Person
        binding.tvName.text = person.name
        binding.tvlastName.text = person.lastName
        val birthDate = LocalDate.parse(person.birthday, formatter)
//   Возраст
        val alt = ChronoUnit.YEARS.between(birthDate, LocalDate.now())
        binding.tvAge.text = alt.toString()
//    Оставшиеся дни
//    День рождения в этом году
        var newBirthDay = LocalDate.of(LocalDate.now().year, birthDate.month, birthDate.dayOfMonth)
        var dayToBirthday = newBirthDay.dayOfYear - LocalDate.now().dayOfYear
//    Если ДР в этом году уже прошел
        if (dayToBirthday < 0) {
            newBirthDay = newBirthDay.plusYears(1L)
//    Остаток дней в этом году
            val restDays = LocalDate.of(LocalDate.now().year, 12, 31).dayOfYear -
                    LocalDate.now().dayOfYear
            dayToBirthday = newBirthDay.dayOfYear + restDays
        }
        binding.tvRestDays.text = dayToBirthday.toString()

//    Оставшиеся месяцы
        var monthToBirthday = newBirthDay.monthValue - LocalDate.now().monthValue
//    Если ДР в этом году уже прошел
        if (monthToBirthday < 0) {
//    Остаток дней в этом году
            val restMonths = LocalDate.of(LocalDate.now().year, 12, 31).monthValue -
                    LocalDate.now().monthValue
            monthToBirthday = newBirthDay.monthValue + restMonths
        }
        binding.tvRestMonthes.text = monthToBirthday.toString()

        if (person.image.isEmpty()) {
            binding.ivFoto.setImageURI(Uri.parse(defaultFoto))
        } else {
            binding.ivFoto.setImageURI(Uri.parse(person.image))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
            finishAffinity()
        return super.onOptionsItemSelected(item)
    }
}