package com.faith.m_agrovet;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText) findViewById(R.id.editTextId);
        buttonGet = (Button) findViewById(R.id.buttonGet);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        TextView tv = (TextView) findViewById(R.id.textViewResult);
        tv.setTypeface(null, Typeface.BOLD);


        buttonGet.setOnClickListener(this);
    }

    private void getData() {
        String product_code = editTextId.getText().toString().trim();
        if (product_code.equals("")) {
            Toast.makeText(this, "Please enter a product_code", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","verifying...",false,false);

        String url = Config.DATA_URL+editTextId.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        String product_code="not found.\tplease report agrovet name to +254722786234\n\n";
        String name="unregistered";
        String price = "unregistered";
        String quantity ="unregistered";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            product_code = collegeData.getString(Config.KEY_CODE);
            name = collegeData.getString(Config.KEY_NAME);
            price = collegeData.getString(Config.KEY_PRICE);
            quantity = collegeData.getString(Config.KEY_QUANTITY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textViewResult.setText("Product_code:\t"+product_code+"\n\nName:\t" +name+ "\n\nPrice:\t"+ price+"\n\nQuantity:"+quantity);
    }

    @Override
    public void onClick(View v) {
        getData();
    }
}