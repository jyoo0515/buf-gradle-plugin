/*
 * Copyright (c) 2022 Andrew Parmet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.parmet.buf.gradle

import org.gradle.api.Task
import java.nio.file.Path

internal fun Task.configureDirectorySpecificBufExecution(
    vararg bufCommand: String
) {
    configureDirectorySpecificBufExecution(bufCommand.asList(), emptyList())
}

internal fun Task.configureDirectorySpecificBufExecution(
    bufCommand: String,
    extraArgs: Iterable<String>
) {
    configureDirectorySpecificBufExecution(listOf(bufCommand), extraArgs)
}

private fun Task.configureDirectorySpecificBufExecution(
    bufCommand: Iterable<String>,
    extraArgs: Iterable<String>
) {
    fun lintWithArgs(path: Path? = null) =
        bufCommand + listOfNotNull(path?.let(::mangle)) + extraArgs

    when {
        project.hasProtobufGradlePlugin() ->
            project.srcProtoDirs().forEach { execBuf(lintWithArgs(it)) }
        project.hasWorkspace() ->
            execBuf(bufCommand)
        else ->
            execBuf(lintWithArgs())
    }
}