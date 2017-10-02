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
import com.cesarvaliente.kunidirectional.persistence.assertIsEqualsTo
import com.cesarvaliente.kunidirectional.persistence.createStoreItem
import com.cesarvaliente.kunidirectional.persistence.queryAllItemsSortedByPosition
import com.cesarvaliente.kunidirectional.persistence.toPersistenceItem
import com.cesarvaliente.kunidirectional.store.CreationAction
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
class CreationHandlerTest {
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
    fun should_create_Item() {
        val item = createStoreItem(1)

        val createItemAction = with(item) {
            CreationAction.CreateItemAction(
                    localId = localId,
                    text = text!!,
                    favorite = favorite,
                    color = color,
                    position = position)
        }

        CreationHandler.handle(createItemAction, {})

        val itemsCollection = db.queryAllItemsSortedByPosition()
        assertThat(itemsCollection, iz(not(emptyList<PersistenceItem>())))
        assertThat(itemsCollection.size, iz(1))

        itemsCollection.component1().assertIsEqualsTo(item.toPersistenceItem())
    }
}