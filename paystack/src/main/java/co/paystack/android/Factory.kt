package co.paystack.android

/**
 * Definition of a Factory interface with a function to create objects of a type
 * */
interface Factory<T> {
    fun create(): T
}