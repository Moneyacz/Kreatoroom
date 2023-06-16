package br.com.setupbuilder.controller

import android.content.Context
import android.content.Intent
import android.widget.TextView
import br.com.setupbuilder.model.Setup
import br.com.setupbuilder.view.ViewSetupActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class SetupController {
    val userUid = UserController().getUID()
    val setupFirebase = FirebaseFirestore.getInstance().collection("setup")
    val partController = PartController()

    public fun listSetupsByTime(order: String): Task<QuerySnapshot> {
        var dir = Query.Direction.ASCENDING
        if (order.equals("desc"))
            dir = Query.Direction.DESCENDING
        return setupFirebase.whereEqualTo("userUid", userUid.toString()).orderBy("timestamp", dir)
            .get()
    }

    public fun listSetupsByPrice(order: String): Task<QuerySnapshot> {
        var dir = Query.Direction.ASCENDING
        if (order.equals("desc"))
            dir = Query.Direction.DESCENDING
        return setupFirebase.whereEqualTo("userUid", userUid.toString()).orderBy("price", dir).get()
    }

    public fun add(setup: Setup): Task<DocumentReference> {
        return setupFirebase.add(setup)
    }

    public fun addPart(
        partName: String,
        asin: String,
        priceNew: Double,
        setupName: String,
        context: Context
    ) {
        val data = hashMapOf(partName to asin, "price" to 0.0)
        var price = 0.0
        var total = 0.0
        listSetupsByTime("inc").addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.data.get("name")?.equals(setupName)!!) {

                    total = document.data.get("price") as Double

                    if (document.data.get(partName) != null) {
                        partController.listPartsByAsin(document.data.get(partName).toString())
                            .addOnSuccessListener { response ->
                                for (r in response) {
                                    price = r.data.get("price") as Double

                                }
                            }
                    }

                    data.put("price", total - price + priceNew)
                    document.reference.set(data, SetOptions.merge()).addOnSuccessListener {
                        val intent = Intent(context, ViewSetupActivity::class.java)
                        intent.putExtra("name", setupName)
                        context.startActivity(intent)
                    }
                }
            }
        }


    }

    public fun deletePart(partName: String, price: Double, setupName: String) {
        val updates = hashMapOf<String, Any>(
            partName to FieldValue.delete()
        )

        listSetupsByTime("inc").addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.data.get("name")?.equals(setupName)!!) {
                    updates.put("price", document.data.get("price") as Double - price)
                    document.reference.update(updates)
                }
            }
        }
    }


    public fun getByName(setupName: String): Task<QuerySnapshot> {
        return setupFirebase.whereEqualTo("name", setupName)
            .whereEqualTo("userUid", userUid.toString()).get()

    }

    public fun getPartName(name: String, setupName: String, view: TextView) {
        getByName(setupName).addOnSuccessListener { documents ->
            for (document in documents) {
                if (document.data.get(name) !== null) {
                    partController.listPartsByAsin(document.data.get(name).toString())
                        .addOnSuccessListener { response ->
                            for (r in response) {
                                view.text = r.data.get("name").toString()

                            }
                        }

                }
            }
        }
    }


}