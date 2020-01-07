package com.example.roomrecyclerviewexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomrecyclerviewexample.room.PersonDatabase
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private val listAdapter by lazy { PersonListAdapter() }
    private val personList = MutableLiveData<List<Person>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_person_list.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = listAdapter
            personList.apply {
                value = listOf()
                observe(this@MainActivity, Observer<List<Person>> {
                    listAdapter.submitList(it)
                })
            }
        }

        add_btn.setOnClickListener {
            val intent = Intent(this, AddPersonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getPersonData()
    }

    private fun getPersonData() {
        compositeDisposable.add(
            PersonDatabase.getInstance(this)
                ?.getPersonDao()
                ?.getAllPerson()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    Log.d("MyTag", Gson().toJson(it))
                    if (it.isNotEmpty()) {
                        tv_empty.visibility = View.GONE
                        rv_person_list.visibility = View.VISIBLE
                        personList.value = it
                    } else {
                        tv_empty.visibility = View.VISIBLE
                        rv_person_list.visibility = View.GONE
                    }
                },{
                    Log.e("MyTag", it.message.toString())
                })!!
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
