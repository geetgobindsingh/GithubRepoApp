package com.geetgobindingh.githubrepoapp.utils.file

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object FileReader {
    @Throws(Exception::class)
    fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = ""
        while (reader.readLine().also({ line = it }) != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }

    @Throws(Exception::class)
    fun getStringFromFile(context: Context, filePath: String): String {
        val stream: InputStream = context.getResources().getAssets().open(filePath)
        val ret = convertStreamToString(stream)
        //Make sure you close all streams.
        stream.close()
        return ret
    }
}