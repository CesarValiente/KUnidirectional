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

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

fun setupPersistence(context: Context, dbName: String = context.getString(R.string.database_name)) {
    configureDb(context, dbName)
}

private fun configureDb(context: Context, dbName: String = context.getString(R.string.database_name)) {
    Realm.init(context)
    val realmConfig = RealmConfiguration.Builder()
            .name(dbName)
            .deleteRealmIfMigrationNeeded()
            .build()
    Realm.setDefaultConfiguration(realmConfig)
}

fun Realm.queryAllItemsSortedByPosition(): RealmResults<Item> =
        this.where(Item::class.java).findAll().sort(POSITION)

fun Realm.queryByLocalId(id: String): Item? =
        this.where(Item::class.java).equalTo(LOCAL_ID, id).findFirst()

fun Item.insertOrUpdate(db: Realm): Item {
    val managedItem = db.insertOrUpdateInTransaction(this)
    return managedItem
}

fun <T : RealmModel> Realm.insertOrUpdateInTransaction(model: T): T =
        with(this) {
            beginTransaction()
            val managedItem = copyToRealmOrUpdate(model)
            commitTransaction()
            return managedItem
        }

fun Item.update(db: Realm, changes: (Item.() -> Unit)): Item {
    executeTransaction(db) { this.changes() }
    return this
}

private fun executeTransaction(db: Realm, changes: () -> Unit) {
    db.executeTransaction { changes() }
}

fun Item.delete(db: Realm) {
    executeTransaction(db) { RealmObject.deleteFromRealm(this) }
}
