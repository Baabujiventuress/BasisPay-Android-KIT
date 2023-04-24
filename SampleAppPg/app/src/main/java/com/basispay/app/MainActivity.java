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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private JSONObject response;
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

        initiate();
    }

    private void initiate() {

        //TODO Create order reference and secure hash from backend rest api services
        //TODO After get the response to set params for payment action
        URL url = null;
        try {
            url = new URL("http://example.com");  //Replace to Rest Controller Backend api url
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // use a string builder to bufferize the response body
            // read from the input strea.
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            // use the string builder directly,
            // or convert it into a String
            String body = sb.toString();
            response = new JSONObject(body);
            Log.d("HTTP-GET", body);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    private void makePaymentAction() {

        BasisPayPaymentParams pgPaymentParams = new BasisPayPaymentParams();
        try {
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
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        BasisPayPaymentInitializer pgPaymentInitializer = new BasisPayPaymentInitializer(pgPaymentParams,MainActivity.this,
                Const.PG_RETURN_URL,false); //TEST = false or LIVE = true
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
}