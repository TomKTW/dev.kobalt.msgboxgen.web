/*
 * dev.kobalt.msgboxgen
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.msgboxgen.web.generate

import dev.kobalt.msgboxgen.web.resource.ResourceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.awt.Color
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream

object GenerateRepository {

    val pageTitle = "Generate"
    val pageSubtitle = "Create your own message box."
    val pageRoute = "generate/"
    val pageContent = ResourceRepository.getText("generate.md")!!

    var jarPath: String? = null
    var iconsPath: String? = null
    var fontsPath: String? = null

    val defaultTitleBarStartColor = Color(0, 0, 128)
    val defaultTitleBarEndColor = Color(16, 132, 208)
    val defaultTitleBarTextColor = Color(255, 255, 255)
    val defaultWindowBackgroundColor = Color(192, 192, 192)
    val defaultMessageTextColor = Color(0, 0, 0)
    val defaultButtonTextColor = Color(0, 0, 0)
    val defaultButtonBackgroundColor = Color(192, 192, 192)

    private val semaphore = Semaphore(5)

    suspend fun generate(
        outputStream: OutputStream,
        title: String? = null,
        message: String? = null,
        buttons: String? = null,
        icon: String? = null,
        font: String? = null,
        width: Int? = null,
        titleBarStartColor: String? = null,
        titleBarEndColor: String? = null,
        titleBarTextColor: String? = null,
        windowBackgroundColor: String? = null,
        messageTextColor: String? = null,
        buttonTextColor: String? = null,
        buttonBackgroundColor: String? = null
    ) = withContext(Dispatchers.IO) {
        val list = mutableListOf<String>()
        list.add("java")
        jarPath?.let { list.add("-jar"); list.add(it) }
        title?.let { list.add("--title"); list.add(it) }
        message?.let { list.add("--message"); list.add(it) }
        buttons?.let { list.add("--buttons"); list.add(it) }
        icon?.let { list.add("--iconPath"); list.add(it) }
        font?.let { list.add("--fontPath"); list.add(it) }
        width?.let { list.add("--width"); list.add(it.toString()) }
        titleBarStartColor?.let { list.add("--titleBarStartColor"); list.add(it) }
        titleBarEndColor?.let { list.add("--titleBarEndColor"); list.add(it) }
        titleBarTextColor?.let { list.add("--titleBarTextColor"); list.add(it) }
        windowBackgroundColor?.let { list.add("--windowBackgroundColor"); list.add(it) }
        messageTextColor?.let { list.add("--messageTextColor"); list.add(it) }
        buttonTextColor?.let { list.add("--buttonTextColor"); list.add(it) }
        buttonBackgroundColor?.let { list.add("--buttonBackgroundColor"); list.add(it) }
        val process = ProcessBuilder(list).start()

        val stdout = BufferedInputStream(process.inputStream)
        val stderr = BufferedReader(InputStreamReader(process.errorStream))
        val stderrJob = launch(Dispatchers.IO) {
            println(stderr.readText())
        }
        val stdoutJob = async(Dispatchers.IO) {
            outputStream.write(stdout.readBytes())
        }
        stderrJob.join()
        stdoutJob.await()
        outputStream
    }

    suspend fun submit(
        outputStream: OutputStream,
        title: String? = null,
        message: String? = null,
        buttons: String? = null,
        icon: String? = null,
        font: String? = null,
        width: Int? = null,
        titleBarStartColor: String? = null,
        titleBarEndColor: String? = null,
        titleBarTextColor: String? = null,
        windowBackgroundColor: String? = null,
        messageTextColor: String? = null,
        buttonTextColor: String? = null,
        buttonBackgroundColor: String? = null
    ): OutputStream {
        return withContext(Dispatchers.IO) {
            semaphore.withPermit {
                withTimeout(5000) {
                    generate(
                        outputStream,
                        title,
                        message,
                        buttons,
                        icon,
                        font,
                        width,
                        titleBarStartColor,
                        titleBarEndColor,
                        titleBarTextColor,
                        windowBackgroundColor,
                        messageTextColor,
                        buttonTextColor,
                        buttonBackgroundColor
                    )
                }
            }
        }
    }

}