# BasisPay-Android PG-KIT
BasisPay Android Payment Gateway kit for developers

## INTRODUCTION
This document describes the steps for integrating Basispay online payment gateway Android kit.This payment gateway performs the online payment transactions with less user effort. It receives the payment details as input and handles the payment flow. Finally returns the payment response to the user. User has to import the framework manually into their project for using it

## Add the JitPack repository to your build file
Step 1. Add the JitPack repository to your build file
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.Baabujiventuress:basispay_android_sdkv2:1.1.6'
	}
```

## Code Explanation

Make sure you have the below permissions in your manifest file:
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
Check the imports in payment activity
````
import com.basispaypg.BasisPayPGConstants;
import com.basispaypg.BasisPayPaymentInitializer;
import com.basispaypg.BasisPayPaymentParams;
````
Get order reference and secure hash from backend rest api services
````
private fun initiate() {
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
````
Make sure you have the below payment params in your payment activity class file:
```
 BasisPayPaymentParams pgPaymentParams = new BasisPayPaymentParams();
        pgPaymentParams.setApiKey(response.getString(Const.API_KEY)); //required field(*)
            pgPaymentParams.setSecureHash(response.getString(Const.SECURE_HASH)); //required field(*)
            pgPaymentParams.setOrderReference(response.getString(Const.ORDER_REFERENCE)); //required field(*)
            pgPaymentParams.setCustomerName(response.getString(Const.CUSTOMER_NAME)); //required field(*)
            pgPaymentParams.setCustomerEmail(response.getString(Const.CUSTOMER_MAIL)); //required field(*)
            pgPaymentParams.setCustomerMobile(response.getString(Const.CUSTOMER_MOBILE)); //required field(*)
            pgPaymentParams.setAddress(response.getString(Const.ADDRESS)); //required field(*)
            pgPaymentParams.setPostalCode(response.getString(Const.POSTAL_CODE)); //required field(*)
            pgPaymentParams.setCity(response.getString(Const.CITY)); //required field(*)
            pgPaymentParams.setRegion(response.getString(Const.REGION)); //required field(*)
            pgPaymentParams.setCountry(response.getString(Const.COUNTRY)); //required field(*)

            //// optional parameters
            pgPaymentParams.setDeliveryAddress(response.getString(Const.DELIVERY_ADDRESS));
            pgPaymentParams.setDeliveryCustomerName(response.getString(Const.DELIVERY_CUSTOMER_ADDRESS));
            pgPaymentParams.setDeliveryCustomerMobile(response.getString(Const.DELIVERY_CUSTOMER_MOBILE));
            pgPaymentParams.setDeliveryPostalCode(response.getString(Const.DELIVERY_POSTAL_CODE));
            pgPaymentParams.setDeliveryCity(response.getString(Const.DELIVERY_CITY));
            pgPaymentParams.setDeliveryRegion(response.getString(Const.DELIVERY_REGION));
            pgPaymentParams.setDeliveryCountry(response.getString(Const.DELIVERY_COUNTRY));
   
```      
Initailize the com.basispaypg.BasisPayPaymentInitializer class with payment parameters and initiate the payment:
```
BasisPayPaymentInitializer pgPaymentInitializer = new BasisPayPaymentInitializer(pgPaymentParams,MainActivity.this,
                Const.PG_RETURN_URL,false); //TEST = false or LIVE = true
        pgPaymentInitializer.initiatePaymentProcess();

```
## Payment Response
To receive the json response, override the onActivityResult() using the REQUEST_CODE and PAYMENT_RESPONSE variables from com.basispaypg.BasisPayPaymentParams class
```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BasisPayPGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(BasisPayPGConstants.PAYMENT_RESPONSE);
                    System.out.println(paymentResponse);
                    Log.e("Res",paymentResponse);
                    if (paymentResponse.equals("null")) {
                        Toast.makeText(this, "Transaction Error!", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
                        Log.e("Res", response.toString());
                        String referenceNo = response.getString("referenceNo");
                        boolean success = response.getBoolean("success");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }

```
