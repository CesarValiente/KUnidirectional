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

package com.cesarvaliente.kunidirectional.persistence.functions

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.cesarvaliente.kunidirectional.persistence.createPersistenceItem
import com.cesarvaliente.kunidirectional.persistence.insertOrUpdate
import com.cesarvaliente.kunidirectional.persistence.queryAllItemsSortedByPosition
import com.cesarvaliente.kunidirectional.persistence.queryByLocalId
import com.cesarvaliente.kunidirectional.persistence.toPersistenceColor
import com.cesarvaliente.kunidirectional.persistence.toStoreItem
import com.cesarvaliente.kunidirectional.store.action.UpdateAction
import com.cesarvaliente.kunidirectional.store.action.UpdateAction.ReorderItemsAction
import io.realm.Realm
import io.realm.RealmConfiguration
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.cesarvaliente.kunidirectional.persistence.Color as PersistenceColor
import com.cesarvaliente.kunidirectional.persistence.Item as PersistenceItem
import com.cesarvaliente.kunidirectional.store.Color as StoreColor
import com.cesarvaliente.kunidirectional.store.Item as StoreItem
import org.hamcrest.core.Is.`is` as iz

@RunWith(AndroidJUnit4::class)
class UpdateFunctionsTest {
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
    fun should_reorder_Items() {
        val item1 = createPersistenceItem(1)
        val item2 = createPersistenceItem(2)
        val item3 = createPersistenceItem(3)

        item1.insertOrUpdate(db)
        item2.insertOrUpdate(db)
        item3.insertOrUpdate(db)

        item1.position = 4L
        item2.position = 1L
        item3.position = 2L

        val listOfStoreItems = listOf(item1.toStoreItem(), item2.toStoreItem(), item3.toStoreItem())
        val reorderItemsAction = ReorderItemsAction(listOfStoreItems)

        UpdateFunctions.apply(reorderItemsAction)

        val itemsCollection = db.queryAllItemsSortedByPosition()
        assertThat(itemsCollection, iz(not(nullValue())))

        assertThat(itemsCollection.component1().localId, iz(item2.localId))
        assertThat(itemsCollection.component2().localId, iz(item3.localId))
        assertThat(itemsCollection.component3().localId, iz(item1.localId))
    }

    @Test
    fun should_update_Item() {
        val item1 = createPersistenceItem(1)

        item1.insertOrUpdate(db)

        val NEW_TEXT = "new text"
        val NEW_COLOR = StoreColor.YELLOW
        val updateItemAction = UpdateAction.UpdateItemAction(
                localId = item1.localId, text = NEW_TEXT, color = NEW_COLOR)

        UpdateFunctions.apply(updateItemAction)

        val managedItem = db.queryByLocalId(item1.localId)
        assertThat(managedItem, iz(not(nullValue())))
        assertThat(managedItem!!.text, iz(NEW_TEXT))
        assertThat(managedItem.getColorAsEnum(), iz(NEW_COLOR.toPersistenceColor()))
    }

    @Test
    fun should_update_favorite_field() {
        val item1 = createPersistenceItem(1)

        item1.insertOrUpdate(db)

        val NEW_FAVORITE = true
        val updateFavoriteAction = UpdateAction.UpdateFavoriteAction(
                localId = item1.localId, favorite = NEW_FAVORITE)

        UpdateFunctions.apply(updateFavoriteAction)

        val managedItem = db.queryByLocalId(item1.localId)
        assertThat(managedItem, iz(not(nullValue())))
        assertThat(managedItem!!.favorite, iz(NEW_FAVORITE))
    }

    @Test
    fun should_update_color_field() {
        val item1 = createPersistenceItem(1)

        item1.insertOrUpdate(db)

        val NEW_COLOR = StoreColor.WHITE
        val updateColorAction = UpdateAction.UpdateColorAction(
                localId = item1.localId, color = NEW_COLOR)

        UpdateFunctions.apply(updateColorAction)

        val managedItem = db.queryByLocalId(item1.localId)
        assertThat(managedItem, iz(not(nullValue())))
        assertThat(managedItem!!.getColorAsEnum(), iz(NEW_COLOR.toPersistenceColor()))
    }
}