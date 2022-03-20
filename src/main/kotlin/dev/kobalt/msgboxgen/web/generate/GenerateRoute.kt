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

import dev.kobalt.msgboxgen.web.extension.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


fun Route.generateRoute() {
    route(GenerateRepository.pageRoute) {
        get {
            val icons =
                listOf(Pair("", "None")) + GenerateRepository.iconsPath?.let { File(it).listFiles() }?.map { file ->
                    Pair(file.nameWithoutExtension, file.nameWithoutExtension.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    })
                }.orEmpty()
            val fonts = GenerateRepository.fontsPath?.let { File(it).listFiles() }
                ?.map { Pair(it.nameWithoutExtension, it.toFont().name) } ?: listOf(Pair("", "Default"))

            call.respondHtmlContent(
                title = GenerateRepository.pageTitle,
                description = GenerateRepository.pageSubtitle
            ) {
                pageArticle(
                    GenerateRepository.pageTitle,
                    GenerateRepository.pageSubtitle
                ) {
                    h3 { text("Form") }
                    form {
                        method = FormMethod.post
                        encType = FormEncType.multipartFormData
                        name = "messagebox"
                        div {
                            pageInputTextLabel("Title", "title", "Message")
                            br { }
                            pageInputTextLabel("Message", "message", "Save changes to 'untitled'?")
                            br { }
                            pageInputTextLabel("Buttons (separate by ';')", "buttons", "Yes;No;Cancel")
                            br { }
                            pageInputSelect("Icon", "icon", icons)
                            br { }
                            pageInputSelect("Font", "font", fonts)
                            br { }
                            pageInputNumber("Window width", "width", 300)
                            br { }
                            pageInputColor(
                                "Window background color",
                                "windowBackgroundColor",
                                GenerateRepository.defaultWindowBackgroundColor
                            )
                            br { }
                            pageInputColor(
                                "Title bar gradient start color",
                                "titleBarStartColor",
                                GenerateRepository.defaultTitleBarStartColor
                            )
                            br { }
                            pageInputColor(
                                "Title bar gradient end color",
                                "titleBarEndColor",
                                GenerateRepository.defaultTitleBarEndColor
                            )
                            br { }
                            pageInputColor(
                                "Title bar text color",
                                "titleBarTextColor",
                                GenerateRepository.defaultTitleBarTextColor
                            )
                            br { }
                            pageInputColor(
                                "Button text color",
                                "buttonTextColor",
                                GenerateRepository.defaultButtonTextColor
                            )
                            br { }
                            pageInputColor(
                                "Button background color",
                                "buttonBackgroundColor",
                                GenerateRepository.defaultButtonBackgroundColor
                            )
                            br { }
                            pageInputSubmit("Begin conversion", "submit")
                        }
                    }
                    pageMarkdown(GenerateRepository.pageContent)
                }
            }
        }
        post {
            val parameters = call.receiveParameters()
            val title = parameters["title"]
            val message = parameters["message"]?.replace("\\n", "\n")
            val icon = parameters["icon"]?.takeIf { it.isNotEmpty() }?.let {
                val parent = File(GenerateRepository.iconsPath!!)
                parent.resolve("$it.png").takeIf { it.exists() }?.requireIsLocatedIn(parent)?.canonicalPath
            }
            val font = parameters["font"]?.takeIf { it.isNotEmpty() }?.let {
                val parent = File(GenerateRepository.fontsPath!!)
                parent.resolve("$it.ttf").takeIf { it.exists() }?.requireIsLocatedIn(parent)?.canonicalPath
            }
            val buttons = parameters["buttons"]
            val width = parameters["width"]?.toIntOrNull()
            val titleBarStartColor = parameters["titleBarStartColor"]
            val titleBarEndColor = parameters["titleBarEndColor"]
            val titleBarTextColor = parameters["titleBarTextColor"]
            val windowBackgroundColor = parameters["windowBackgroundColor"]
            val messageTextColor = parameters["messageTextColor"]
            val buttonTextColor = parameters["buttonTextColor"]
            val buttonBackgroundColor = parameters["buttonBackgroundColor"]
            call.respondBytes(
                contentType = ContentType.Image.PNG,
                bytes = ByteArrayOutputStream().use {
                    GenerateRepository.submit(
                        outputStream = it,
                        title = title,
                        message = message,
                        icon = icon,
                        buttons = buttons,
                        width = width,
                        font = font,
                        titleBarStartColor = titleBarStartColor,
                        titleBarEndColor = titleBarEndColor,
                        titleBarTextColor = titleBarTextColor,
                        windowBackgroundColor = windowBackgroundColor,
                        messageTextColor = messageTextColor,
                        buttonTextColor = buttonTextColor,
                        buttonBackgroundColor = buttonBackgroundColor
                    )
                    it.toByteArray()
                }
            )
        }
    }
}

