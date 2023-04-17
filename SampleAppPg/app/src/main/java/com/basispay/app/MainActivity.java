package com.basispay.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basispaypg.BasisPayPGConstants;
import com.basispaypg.BasisPayPaymentInitializer;
import com.basispaypg.BasisPayPaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePaymentAction();
            }
        });
    }

    private void makePaymentAction() {

        BasisPayPaymentParams pgPaymentParams = new BasisPayPaymentParams();
        pgPaymentParams.setApiKey("PG_API_KEY");//required field(*)
        pgPaymentParams.setSecureHash("PG_SECURE_HASH");//required field(*)
        pgPaymentParams.setOrderReference("PG_REFERENCE");//required field(*)
        pgPaymentParams.setCustomerName("PG_USER_NAME");//required field(*)
        pgPaymentParams.setCustomerEmail("PG_USER_EMAIL");//required field(*)
        pgPaymentParams.setCustomerMobile("PG_USER_MOBILE");//required field(*)
        pgPaymentParams.setAddress("PG_ADDRESS");//required field(*)
        pgPaymentParams.setPostalCode("PG_PINCODE");//required field(*)
        pgPaymentParams.setCity("PG_CITY");//required field(*)
        pgPaymentParams.setRegion("PG_REGION");//required field(*)
        pgPaymentParams.setCountry("PG_COUNTRY");//required field(*)

        //// optional parameters
        pgPaymentParams.setDeliveryAddress("");
        pgPaymentParams.setDeliveryCustomerName("");
        pgPaymentParams.setDeliveryCustomerMobile("");
        pgPaymentParams.setDeliveryPostalCode("");
        pgPaymentParams.setDeliveryCity("");
        pgPaymentParams.setDeliveryRegion("");
        pgPaymentParams.setDeliveryCountry("PG_COUNTRY");

        BasisPayPaymentInitializer pgPaymentInitializer = new BasisPayPaymentInitializer(pgPaymentParams,MainActivity.this,
                "PG_RETURN_URL","PG_CONNECT_URL"); //Example PG_CONNECT_URL = https://basispay.in/
        pgPaymentInitializer.initiatePaymentProcess();
    }

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
}