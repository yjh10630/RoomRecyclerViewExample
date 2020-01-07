package com.example.roomrecyclerviewexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roomrecyclerviewexample.room.PersonDatabase
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_addperson.*

class AddPersonActivity: AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addperson)

        btn_register.setOnClickListener {
            savePerson()
        }

    }

    private fun savePerson() {
        if (et_input_name.text.isNullOrEmpty()
            || et_input_age.text.isNullOrEmpty()
            || et_input_address.text.isNullOrEmpty()) {
            Toast.makeText(this, "빈 곳은 허용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val person = Person(
                et_input_name.text.toString().trim(),
                et_input_age.text.toString().trim().toInt(),
                et_input_address.text.toString().trim()
            )

            compositeDisposable.add(
                Flowable.just(person)
                    .subscribeOn(Schedulers.io())       // AndroidSchedulers.mainThread() 는 안됨.. 왜지 ?
                    .subscribe({
                        PersonDatabase.getInstance(this)
                            ?.getPersonDao()
                            ?.insert(it)
                        finish()
                    }, {
                        Log.e("MyTag", it.message.toString())
                        Toast.makeText(this, "관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                    })
            )

        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}