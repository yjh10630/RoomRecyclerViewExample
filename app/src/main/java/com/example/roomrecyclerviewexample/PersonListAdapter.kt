package com.example.roomrecyclerviewexample

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.roomrecyclerviewexample.room.PersonDatabase
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_list_item.view.*

class PersonListAdapter: ListAdapter<Person, PersonListAdapter.ItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(parent)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.onBindView(getItem(position))

    /**
     * ItemViewHolder
     */
    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.view_list_item, parent, false)
    ) {
        fun onBindView(person: Person) {
            itemView.apply {
                tv_name.text = person.name
                tv_age.text = person.age.toString()
                tv_address.text = person.address

                btn_delete.setOnClickListener {
                    Flowable.just(person)
                        .subscribeOn(Schedulers.io())       // AndroidSchedulers.mainThread() 는 안됨.. 왜지 ?
                        .subscribe({
                            PersonDatabase.getInstance(itemView.context)
                                ?.getPersonDao()
                                ?.delete(it)
                        }, {
                            Log.e("MyTag", it.message.toString())
                            Toast.makeText(itemView.context, "관리자에게 문의해 주세요.", Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }

    }
}

class DiffUtilCallback: DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean = (oldItem == newItem)
    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean = (oldItem == newItem)
}