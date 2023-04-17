package com.basispay.sampleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basispay.sampleapp.databinding.ActivityMainBinding
import com.basispaypg.BasisPayPGConstants
import com.basispaypg.BasisPayPaymentInitializer
import com.basispaypg.BasisPayPaymentParams
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btn.setOnClickListener(View.OnClickListener {
            makePaymentAction()
        })
    }

    private fun makePaymentAction() {

        val pgPaymentParams = BasisPayPaymentParams()
        pgPaymentParams.setApiKey("API_KEY") //required field(*)
        pgPaymentParams.setSecureHash("SECURE_HASH") //required field(*)
        pgPaymentParams.setOrderReference("ORDER_REFERENCE") //required field(*)
        pgPaymentParams.setCustomerName("CUSTOMER_NAME") //required field(*)
        pgPaymentParams.setCustomerEmail("CUSTOMER_MAIL") //required field(*)
        pgPaymentParams.setCustomerMobile("CUSTOMER_MOBILE") //required field(*)
        pgPaymentParams.setAddress("ADDRESS") //required field(*)
        pgPaymentParams.setPostalCode("POSTAL_CODE") //required field(*)
        pgPaymentParams.setCity("CITY") //required field(*)
        pgPaymentParams.setRegion("REGION") //required field(*)
        pgPaymentParams.setCountry("IND") //required field(*)

        //// optional parameters
        pgPaymentParams.setDeliveryAddress("")
        pgPaymentParams.setDeliveryCustomerName("")
        pgPaymentParams.setDeliveryCustomerMobile("")
        pgPaymentParams.setDeliveryPostalCode("")
        pgPaymentParams.setDeliveryCity("");
        pgPaymentParams.setDeliveryRegion("")
        pgPaymentParams.setDeliveryCountry("IND")

        val pgPaymentInitializer =
            BasisPayPaymentInitializer(pgPaymentParams, this@MainActivity,
                "YOUR_RETURN_URL",
            "YOUR_PG_CONNECT_URL")
        pgPaymentInitializer.initiatePaymentProcess()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == BasisPayPGConstants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    val paymentResponse =
                        data!!.getStringExtra(BasisPayPGConstants.PAYMENT_RESPONSE)
                    println(paymentResponse)
                    Log.d("Res", paymentResponse!!)
                    if (paymentResponse == "null") {
                        Toast.makeText(this, "Transaction Error!", Toast.LENGTH_SHORT).show()
                    } else {
                        val response = JSONObject(paymentResponse)
                        Log.d("Res", response.toString())
                        val referenceNo = response.getString("referenceNumber")
                        val success = response.getBoolean("success")

                        binding.tv1.text = "Reference No: $referenceNo"
                        binding.tv2.text = "Is Success: $success"

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d("Res", "Declined!")
            }
        }
    }
}