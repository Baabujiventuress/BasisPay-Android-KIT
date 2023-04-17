# BasisPay PaymentGateWay-Android KIT
Basispay PG SDK_v2 - Android Kotlin X
BasisPay Android Payment Gateway kit for developers

## INTRODUCTION
This document describes the steps for integrating Basispay online payment gateway Android kit.This payment gateway performs the online payment transactions with less user effort. It receives the payment details as input and handles the payment flow. Finally returns the payment response to the user. User has to import the framework manually into their project for using it

## Add the JitPack repository to your build file
Step 1. Add the JitPack repository to your settings.gradle file
```
dependencyResolutionManagement {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency in app module build.gradle file
```
dependencies {
			//Tag replace to latest version (ex:- 1.0.0)
	        implementation 'com.github.Baabujiventuress:basispay-pgsdkv2-ktx:Tag'
	}

```

## Code Explanation

Make sure you have the below permissions in your manifest file:
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

```
Check the imports in payment activity
```
import com.basispaypg.BasisPayPGConstants
import com.basispaypg.BasisPayPaymentInitializer
import com.basispaypg.BasisPayPaymentParams

```
Make sure you have the below payment params in your payment activity class file:
```
 val pgPaymentParams = BasisPayPaymentParams()
        pgPaymentParams.setApiKey("YOUR_API_KEY") //required field(*)
        pgPaymentParams.setSecureHash("YOUR_SECURE_HASH") //required field(*)
        pgPaymentParams.setOrderReference("YOUR_REFERENCE_NO") //required field(*)
        pgPaymentParams.setCustomerName("XXXXX") //required field(*)
        pgPaymentParams.setCustomerEmail("XXXXX") //required field(*)
        pgPaymentParams.setCustomerMobile("XXXXX") //required field(*)
        pgPaymentParams.setAddress("XXXXX") //required field(*)
        pgPaymentParams.setPostalCode("XXXX") //required field(*)
        pgPaymentParams.setCity("XXXX") //required field(*)
        pgPaymentParams.setRegion("XXXX") //required field(*)
        pgPaymentParams.setCountry("XXX") //required field(*)

        //// optional parameters
        pgPaymentParams.setDeliveryAddress("XXXX")
        pgPaymentParams.setDeliveryCustomerName("XXXX")
        pgPaymentParams.setDeliveryCustomerMobile("XXXX")
        pgPaymentParams.setDeliveryPostalCode("XXXX")
        pgPaymentParams.setDeliveryCity("XXXX");
        pgPaymentParams.setDeliveryRegion("XXXX")
        pgPaymentParams.setDeliveryCountry("XXX")
   
```      
Initailize the com.basispaypg.BasisPayPaymentInitializer class with payment parameters and initiate the payment:
```
val pgPaymentInitializer =
            BasisPayPaymentInitializer(pgPaymentParams, this@MainActivity,
                "YOUR_RETURN_URL","YOUR_PG_CONNECT_URL")
        pgPaymentInitializer.initiatePaymentProcess()

```
## Payment Response
To receive the json response, override the onActivityResult() using the REQUEST_CODE and PAYMENT_RESPONSE variables from com.basispaypg.BasisPayPaymentParams class
```
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

```
