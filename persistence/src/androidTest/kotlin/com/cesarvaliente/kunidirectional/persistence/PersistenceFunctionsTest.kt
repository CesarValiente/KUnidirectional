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

package com.cesarvaliente.kunidirectional.persistence

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.realm.Realm
import io.realm.RealmConfiguration
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.core.Is.`is` as iz


@RunWith(AndroidJUnit4::class)
class PersistenceFunctionsTest {
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
    fun should_db_be_empty() {
        val itemsCollection = db.queryAllItemsSortedByPosition()
        assertThat(itemsCollection, iz(emptyList<Item>()))
    }

    @Test
    fun should_db_not_be_empty() {
        val item = createPersistenceItem(1)
        item.insertOrUpdate(db)

        val itemsCollection = db.queryAllItemsSortedByPosition()

        assertThat(itemsCollection, iz(not(emptyList<Item>())))
        assertThat(itemsCollection.size, iz(1))
        itemsCollection.component1().assertIsEqualsTo(item)
    }

    @Test
    fun should_query_by_localId_correctly() {
        val item = createPersistenceItem(1)
        item.insertOrUpdate(db)

        val managedItem = db.queryByLocalId(item.localId)

        assertThat(managedItem, iz(notNullValue()))
        managedItem!!.assertIsEqualsTo(item)
    }

    @Test
    fun should_query_all_items_sorted_by_position() {
        val item1 = createPersistenceItem(3)
        val item2 = createPersistenceItem(2)
        val item3 = createPersistenceItem(1)

        item1.insertOrUpdate(db)
        item2.insertOrUpdate(db)
        item3.insertOrUpdate(db)

        val itemsCollection = db.queryAllItemsSortedByPosition()

        assertThat(itemsCollection, iz(not(emptyList<Item>())))
        assertThat(itemsCollection.size, iz(3))
        itemsCollection.component1().assertIsEqualsTo(item3)
        itemsCollection.component2().assertIsEqualsTo(item2)
        itemsCollection.component3().assertIsEqualsTo(item1)
    }

    @Test
    fun should_update_Item_correctly() {
        val item = createPersistenceItem(1)

        item.insertOrUpdate(db)

        var managedItem = db.queryByLocalId(item.localId)
        assertThat(managedItem, iz(notNullValue()))
        managedItem!!.assertIsEqualsTo(item)

        item.text = "Item modified"
        item.insertOrUpdate(db)

        managedItem = db.queryByLocalId(item.localId)
        assertThat(managedItem, iz(notNullValue()))
        assertThat(managedItem!!.text, iz(item.text))
    }

    @Test
    fun should_update_Item_defining_changes() {
        val item = createPersistenceItem(1)

        val managedItem = item.insertOrUpdate(db)

        var itemsCollection = db.queryAllItemsSortedByPosition()
        assertThat(itemsCollection, iz(not(emptyList<Item>())))
        assertThat(itemsCollection.size, iz(1))

        managedItem.update(db) { text = "Item modified" }

        val itemUpdated = db.queryByLocalId(item.localId)
        assertThat(itemUpdated, iz(notNullValue()))
        assertThat(itemUpdated!!.text, iz("Item modified"))
    }

    @Test
    fun should_delete_an_Item_correctly() {
        val item1 = createPersistenceItem(1)
        val item2 = createPersistenceItem(2)
        val item3 = createPersistenceItem(3)

        item1.insertOrUpdate(db)
        val managedItem2 = item2.insertOrUpdate(db)
        item3.insertOrUpdate(db)

        var itemsCollection = db.queryAllItemsSortedByPosition()

        assertThat(itemsCollection, iz(not(emptyList<Item>())))
        assertThat(itemsCollection.size, iz(3))

        managedItem2.delete(db)

        itemsCollection = db.queryAllItemsSortedByPosition()

        assertThat(itemsCollection, iz(not(emptyList<Item>())))
        assertThat(itemsCollection.size, iz(2))
        assertThat(itemsCollection.contains(item2), iz(false))
    }
}
