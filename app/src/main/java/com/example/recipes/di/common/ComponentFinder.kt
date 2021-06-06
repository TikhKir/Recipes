package com.example.recipes.di.common

import androidx.fragment.app.Fragment

/**
 * Traverses parent fragments, fragments in backstack, activity and application in search of given class.
 * Throws IllegalArgument exception if it fails to find one.
 * Must be called after onAttach().
 */

object ComponentFinder {

    inline fun <reified T: Any> find(fragment: Fragment): T {
        return find(T::class.java, fragment)
    }

    @JvmStatic
    fun <T: Any> find(clazz: Class<T>, fragment: Fragment): T {

        var parentFragment: Fragment? = fragment
        while (true) {
            parentFragment = parentFragment?.parentFragment ?: break
            if (parentFragment instanceOf clazz) return parentFragment as T
        }

        val activity = fragment.activity
        activity?.supportFragmentManager?.fragments
        ?.forEach { if (it instanceOf clazz) return it as T }

        if (activity instanceOf clazz) return activity as T

        val application = activity?.application
        if (application instanceOf clazz) return application as T

        throw IllegalArgumentException(
            String.format("No ${clazz.simpleName} was found for %s", fragment.javaClass.canonicalName)
        )
    }

    private infix fun Any?.instanceOf(clazz: Class<*>) = clazz.isInstance(this)
}