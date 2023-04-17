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
	        implementation 'com.github.Baabujiventuress:basispay_android_sdkv2:1.1.4'
	}
```

## Code Explanation

Make sure you have the below permissions in your manifest file:
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
```
import com.basispaypg.BasisPayPGConstants;
import com.basispaypg.BasisPayPaymentInitializer;
import com.basispaypg.BasisPayPaymentParams;

```
 BasisPayPaymentParams pgPaymentParams = new BasisPayPaymentParams();
        pgPaymentParams.setApiKey(Const.PG_API_KEY);//required field(*)
        pgPaymentParams.setSecureHash(Const.PG_SECURE_HASH);//required field(*)
        pgPaymentParams.setOrderReference(Const.PG_REFERENCE);//required field(*)
        pgPaymentParams.setCustomerName(Const.PG_USER_NAME);//required field(*)
        pgPaymentParams.setCustomerEmail(Const.PG_USER_EMAIL);//required field(*)
        pgPaymentParams.setCustomerMobile(Const.PG_USER_MOBILE);//required field(*)
        pgPaymentParams.setAddress(Const.PG_ADDRESS);//required field(*)
        pgPaymentParams.setPostalCode(Const.PG_PINCODE);//required field(*)
        pgPaymentParams.setCity(Const.PG_CITY);//required field(*)
        pgPaymentParams.setRegion(Const.PG_REGION);//required field(*)
        pgPaymentParams.setCountry(Const.PG_COUNTRY);//required field(*)

        //// optional parameters
        pgPaymentParams.setDeliveryAddress("");
        pgPaymentParams.setDeliveryCustomerName("");
        pgPaymentParams.setDeliveryCustomerMobile("");
        pgPaymentParams.setDeliveryPostalCode("");
        pgPaymentParams.setDeliveryCity("");
        pgPaymentParams.setDeliveryRegion("");
        pgPaymentParams.setDeliveryCountry(Const.PG_COUNTRY);
   
```      
Initailize the com.basispaypg.BasisPayPaymentInitializer class with payment parameters and initiate the payment:
```
BasisPayPaymentInitializer pgPaymentInitializer = new BasisPayPaymentInitializer(pgPaymentParams,MainActivity.this,
                Const.PG_RETURN_URL,Const.PG_CONNECT_URL); //Example PG_CONNECT_URL = https://basispay.in/
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
                        String referenceNo = response.getString("referenceNumber");
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
