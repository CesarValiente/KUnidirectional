/**
 * Copyright (C) 2017 Cesar Valiente & Corey Shaw
 *
 * https://github.com/CesarValiente
 * https://github.com/coshaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cesarvaliente.kunidirectional.store

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

interface ThreadExecutor {
    fun execute(block: () -> Unit)
}

abstract class ThreadExecutorService(open val executorService: ExecutorService) : ThreadExecutor {
    override fun execute(block: () -> Unit) {
        executorService.execute { block() }
    }
}

class StoreThreadService : ThreadExecutorService(ExecutorServices.store)

object ExecutorServices {

    val store: ExecutorService by lazy {
        store()
    }

    private fun store(): ExecutorService =
            Executors.newSingleThreadExecutor(NamedThreadFactory("store"))

    val persistence: ExecutorService by lazy {
        persistence()
    }

    private fun persistence(): ExecutorService =
            Executors.newSingleThreadExecutor(NamedThreadFactory("persistence"))
}

class NamedThreadFactory(private val name: String) : ThreadFactory {

    private val threadNumber = AtomicInteger(1)

    override fun newThread(runnable: Runnable): Thread {
        return Thread(runnable, "$name  -  ${threadNumber.andIncrement}")
    }
}
