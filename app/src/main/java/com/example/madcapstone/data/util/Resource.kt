package com.example.madcapstone.data.util

/**
 * A generic class that determines the state of a network request.
 * This class is used to wrap the data that is returned from the network request.
 * @param <T> The type of the data that is returned from the network request.
 *
 * @property data The data that is returned from the network request.
 * @property message The message that is returned from the network request.
 *
 * @author Julian Kruithof
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
    class Initial<T> : Resource<T>()
    class Empty<T> : Resource<T>()


}