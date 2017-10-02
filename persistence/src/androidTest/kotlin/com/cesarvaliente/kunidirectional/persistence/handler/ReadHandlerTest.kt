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

package com.cesarvaliente.kunidirectional.persistence.handler

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.cesarvaliente.kunidirectional.persistence.createPersistenceItem
import com.cesarvaliente.kunidirectional.persistence.insertOrUpdate
import com.cesarvaliente.kunidirectional.persistence.toStoreItem
import com.cesarvaliente.kunidirectional.store.Action
import com.cesarvaliente.kunidirectional.store.ReadAction
import com.cesarvaliente.kunidirectional.store.ReadAction.FetchItemsAction
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.realm.Realm
import io.realm.RealmConfiguration
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.cesarvaliente.kunidirectional.persistence.Color as PersistenceColor
import com.cesarvaliente.kunidirectional.persistence.Item as PersistenceItem
import com.cesarvaliente.kunidirectional.store.Color as StoreColor
import com.cesarvaliente.kunidirectional.store.Item as StoreItem
import org.hamcrest.core.Is.`is` as iz

@RunWith(AndroidJUnit4::class)
class ReadHandlerTest {
    lateinit var config: RealmConfiguration
    lateinit var db: Realm

    @Before
    fun setup() {
        Realm.init(InstrumentationRegistry.getTargetContext())
        config = RealmConfiguration.Builder()
                .name("test.realm")
                .inMemory()
                .build()
        Realm.setDefaultConfiguration(config)
        db = Realm.getInstance(config)
    }

    @After
    fun clean() {
        db.close()
        Realm.deleteRealm(config)
    }

    @Test
    fun should_fetch_all_Items() {
        val item1 = createPersistenceItem(1)
        val item2 = createPersistenceItem(2)
        val item3 = createPersistenceItem(3)

        item1.insertOrUpdate(db)
        item2.insertOrUpdate(db)
        item3.insertOrUpdate(db)

        val fetchItemsAction = FetchItemsAction()
        val actionDispatcherSpy = mock<(Action) -> Unit> { }

        ReadHandler.handle(action = fetchItemsAction, actionDispatcher = actionDispatcherSpy)
        argumentCaptor<ReadAction.ItemsLoadedAction>().apply {
            verify(actionDispatcherSpy).invoke(capture())

            with(lastValue.items) {
                assertThat(this, iz(not(emptyList())))
                assertThat(this.size, iz(3))

                assertThat(component1(), iz(item1.toStoreItem()))
                assertThat(component2(), iz(item2.toStoreItem()))
                assertThat(component3(), iz(item3.toStoreItem()))
            }
        }
    }
}