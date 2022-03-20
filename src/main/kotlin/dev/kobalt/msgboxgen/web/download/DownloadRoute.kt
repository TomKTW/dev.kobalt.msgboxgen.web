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

package dev.kobalt.msgboxgen.web.download

import dev.kobalt.msgboxgen.web.extension.pageArticle
import dev.kobalt.msgboxgen.web.extension.pageMarkdown
import dev.kobalt.msgboxgen.web.extension.respondHtmlContent
import dev.kobalt.msgboxgen.web.generate.GenerateRepository
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadRoute() {
    route("download/") {
        get {
            call.respondHtmlContent(
                title = DownloadRepository.pageTitle,
                description = DownloadRepository.pageSubtitle
            ) {
                pageArticle(
                    DownloadRepository.pageTitle,
                    DownloadRepository.pageSubtitle
                ) {
                    pageMarkdown(DownloadRepository.pageContent)
                }
            }
        }
        static {
            file("msgboxgen.jar", File(GenerateRepository.jarPath!!))
        }
    }
}
