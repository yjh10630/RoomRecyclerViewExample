package com.example.roomrecyclerviewexample.room

import androidx.room.*
import com.example.roomrecyclerviewexample.Person
import io.reactivex.Flowable

@Dao
interface PersonDAO {
    @Query("SELECT * FROM person")
    fun getAllPerson(): Flowable<List<Person>>

    @Query("DELETE FROM person")
    fun clearAll()


    //해당 데이터를 추가합니다.
    @Insert(onConflict = OnConflictStrategy.REPLACE) //이미 저장된 항목이 있을 경우 데이터를 덮어쓴다
    fun insert(vararg person: Person)

    //헤당 데이터를 업데이트 합니다.
    @Update
    fun update(vararg person: Person)

    //해당 데이터를 삭제합니다.
    @Delete
    fun delete(vararg person: Person)
}