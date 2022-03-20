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

package dev.kobalt.msgboxgen.web.index

import dev.kobalt.msgboxgen.web.about.AboutRepository
import dev.kobalt.msgboxgen.web.download.DownloadRepository
import dev.kobalt.msgboxgen.web.generate.GenerateRepository
import dev.kobalt.msgboxgen.web.legal.LegalRepository
import dev.kobalt.msgboxgen.web.source.SourceRepository

object IndexRepository {

    val pageTitle = "Index"
    val pageSubtitle = "Make your own message box."

    val pageLinks = listOf(
        Triple(AboutRepository.pageRoute, AboutRepository.pageTitle, AboutRepository.pageSubtitle),
        Triple(GenerateRepository.pageRoute, GenerateRepository.pageTitle, GenerateRepository.pageSubtitle),
        Triple(DownloadRepository.pageRoute, DownloadRepository.pageTitle, DownloadRepository.pageSubtitle),
        Triple(SourceRepository.pageRoute, SourceRepository.pageTitle, SourceRepository.pageSubtitle),
        Triple(LegalRepository.pageRoute, LegalRepository.pageTitle, LegalRepository.pageSubtitle)
    )

}