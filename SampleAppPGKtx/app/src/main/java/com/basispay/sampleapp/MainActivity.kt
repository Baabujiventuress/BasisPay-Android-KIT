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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var response: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btn.setOnClickListener(View.OnClickListener {
            makePaymentAction()
        })

        initiate();
    }

    private fun initiate() {
        //TODO Create order reference and secure hash from backend rest api services
        //TODO After get the response to set params for payment action
        var url: URL? = URL("http://example.com") // Backend api
        val connection: HttpURLConnection = url?.openConnection() as HttpURLConnection

        try {
            val br = BufferedReader(InputStreamReader(connection.getInputStream()))

            // use a string builder to bufferize the response body
            // read from the input strea.
            val sb = StringBuilder()
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }

            // use the string builder directly,
            // or convert it into a String
            response = JSONObject(sb.toString())
            Log.d("RES", response.toString())
            //TODO Get ORDER REFERENCE ID form backend api
            /**
             *if (response.isSuccessful) {
            if (response.code() == 200) {
            response = response.body()
            }
            }
             */
        } finally {
            connection.disconnect()
        }

    }

    private fun makePaymentAction() {

        val pgPaymentParams = BasisPayPaymentParams()
        pgPaymentParams.setApiKey(response.getString(Const.API_KEY)) //required field(*)
        pgPaymentParams.setSecureHash(response.getString(Const.SECURE_HASH)) //required field(*)
        pgPaymentParams.setOrderReference(response.getString(Const.ORDER_REFERENCE)) //required field(*)
        pgPaymentParams.setCustomerName(response.getString(Const.CUSTOMER_NAME)) //required field(*)
        pgPaymentParams.setCustomerEmail(response.getString(Const.CUSTOMER_MAIL)) //required field(*)
        pgPaymentParams.setCustomerMobile(response.getString(Const.CUSTOMER_MOBILE)) //required field(*)
        pgPaymentParams.setAddress(response.getString(Const.ADDRESS)) //required field(*)
        pgPaymentParams.setPostalCode(response.getString(Const.POSTAL_CODE)) //required field(*)
        pgPaymentParams.setCity(response.getString(Const.CITY)) //required field(*)
        pgPaymentParams.setRegion(response.getString(Const.REGION)) //required field(*)
        pgPaymentParams.setCountry(response.getString(Const.COUNTRY)) //required field(*)

        //// optional parameters
        pgPaymentParams.setDeliveryAddress(response.getString(Const.DELIVERY_ADDRESS))
        pgPaymentParams.setDeliveryCustomerName(response.getString(Const.DELIVERY_CUSTOMER_ADDRESS))
        pgPaymentParams.setDeliveryCustomerMobile(response.getString(Const.DELIVERY_CUSTOMER_MOBILE))
        pgPaymentParams.setDeliveryPostalCode(response.getString(Const.DELIVERY_POSTAL_CODE))
        pgPaymentParams.setDeliveryCity(response.getString(Const.DELIVERY_CITY))
        pgPaymentParams.setDeliveryRegion(response.getString(Const.DELIVERY_REGION))
        pgPaymentParams.setDeliveryCountry(response.getString(Const.DELIVERY_COUNTRY))

        val pgPaymentInitializer =
            BasisPayPaymentInitializer(pgPaymentParams, this@MainActivity,
                response.getString(Const.RETURN_URL),
            false) //TEST = false or LIVE = true
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
                        val referenceNo = response.getString("referenceNo")
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