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

package com.cesarvaliente.kunidirectional

import com.cesarvaliente.kunidirectional.store.Dispatcher
import com.cesarvaliente.kunidirectional.store.Subscriber
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as iz

class DispatcherTest {
    lateinit var dispatcher: Dispatcher<String>

    @Before
    fun setup() {
        dispatcher = object : Dispatcher<String>() {}
    }

    @Test
    fun should_subscribe() {
        val subscriber = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
            }
        }
        assertThat(dispatcher.isEmpty(), iz(true))

        dispatcher.subscribe(subscriber)
        assertThat(dispatcher.isEmpty(), iz(false))
        assertThat(dispatcher.isSubscribed(subscriber), iz(true))
    }

    @Test
    fun should_dispatch() {
        val TEXT_SENT = "Hello"
        val subscriber = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT_SENT))
            }
        }

        dispatcher.subscribe(subscriber)
        assertThat(dispatcher.isSubscribed(subscriber), iz(true))

        dispatcher.dispatch(TEXT_SENT)
    }

    @Test
    fun should_dispatch_to_different_subscribers() {
        val TEXT = "Hello"

        val subscriber1 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }
        val subscriber2 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }

        val subscriber3 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }

        assertThat(dispatcher.isEmpty(), iz(true))

        dispatcher.subscribe(subscriber1)
        dispatcher.subscribe(subscriber2)
        dispatcher.subscribe(subscriber3)

        assertThat(dispatcher.isEmpty(), iz(false))
        assertThat(dispatcher.isSubscribed(subscriber1), iz(true))
        assertThat(dispatcher.isSubscribed(subscriber2), iz(true))
        assertThat(dispatcher.isSubscribed(subscriber3), iz(true))

        dispatcher.dispatch(TEXT)
    }

    @Test
    fun should_unsubscribe_correctly() {
        val TEXT = "Hello"

        val subscriber1 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }
        val subscriber2 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }

        val subscriber3 = object : Subscriber<String> {
            override fun onNext(data: String) {
                assertThat(data, iz(notNullValue()))
                assertThat(data, iz(TEXT))
            }
        }

        assertThat(dispatcher.isEmpty(), iz(true))

        dispatcher.subscribe(subscriber1)
        dispatcher.subscribe(subscriber2)
        dispatcher.subscribe(subscriber3)

        assertThat(dispatcher.isEmpty(), iz(false))
        assertThat(dispatcher.isSubscribed(subscriber1), iz(true))
        assertThat(dispatcher.isSubscribed(subscriber2), iz(true))
        assertThat(dispatcher.isSubscribed(subscriber3), iz(true))

        dispatcher.unsubscribe(subscriber2)
        assertThat(dispatcher.isSubscribed(subscriber2), iz(false))
        assertThat(dispatcher.isEmpty(), iz(false))

        dispatcher.unsubscribe(subscriber1)
        assertThat(dispatcher.isSubscribed(subscriber1), iz(false))
        assertThat(dispatcher.isEmpty(), iz(false))

        dispatcher.unsubscribe(subscriber3)
        assertThat(dispatcher.isSubscribed(subscriber3), iz(false))
        assertThat(dispatcher.isEmpty(), iz(true))
    }

    @Test
    fun should_unsubscribe_all() {
        val subscriber1 = object : Subscriber<String> {
            override fun onNext(data: String) {}
        }
        val subscriber2 = object : Subscriber<String> {
            override fun onNext(data: String) {}
        }

        val subscriber3 = object : Subscriber<String> {
            override fun onNext(data: String) {}
        }

        assertThat(dispatcher.isEmpty(), iz(true))

        dispatcher.subscribe(subscriber1)
        dispatcher.subscribe(subscriber2)
        dispatcher.subscribe(subscriber3)

        assertThat(dispatcher.isEmpty(), iz(false))
        assertThat(dispatcher.count(), iz(3))

        dispatcher.unsubcribeAll()
        assertThat(dispatcher.isEmpty(), iz(true))
    }
}