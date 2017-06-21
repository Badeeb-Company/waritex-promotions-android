package com.badeeb.waritex.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.badeeb.waritex.R;
import com.badeeb.waritex.model.CompanyInfo;
import com.badeeb.waritex.model.CompanyInfoInquiry;
import com.badeeb.waritex.model.JsonResponse;
import com.badeeb.waritex.network.MyVolley;
import com.badeeb.waritex.shared.AppPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyInfoFragment extends Fragment {

    // For logging purpose
    public static final String TAG = CompanyInfoFragment.class.getName();

    // attributes that will be used for JSON calls
    private String mUrl = AppPreferences.BASE_URL + "/company_info";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView - Start");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_info, container, false);

        init(view);

        Log.d(TAG, "onCreateView - End");

        return view;
    }

    private void init(View view) {
        Log.d(TAG, "init - Start");

        loadCompanyInfo(view);

        // Call this method to setup listener on UI components
        setupListeners(view);

        Log.d(TAG, "init - End");
    }

    private void loadCompanyInfo(final View view) {
        Log.d(TAG, "loadCompanyInfo - Start");

        String currentUrl = mUrl;

        Log.d(TAG, "loadCompanyInfo - URL: " + currentUrl);

        // Call products service
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, currentUrl, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Response Handling
                        Log.d(TAG, "loadCompanyInfo - onResponse - Start");

                        Log.d(TAG, "loadCompanyInfo - onResponse - Json Response: " + response.toString());

                        // Create Gson object
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
                        Gson gson = gsonBuilder.create();

                        String responseData = response.toString();
                        Type responseType = new TypeToken<JsonResponse<CompanyInfoInquiry>>() {}.getType();

                        JsonResponse<CompanyInfoInquiry> jsonResponse = gson.fromJson(responseData, responseType);

                        Log.d(TAG, "loadCompanyInfo - onResponse - Status: " + jsonResponse.getJsonMeta().getStatus());
                        Log.d(TAG, "loadCompanyInfo - onResponse - Message: " + jsonResponse.getJsonMeta().getMessage());
                        Log.d(TAG, "loadCompanyInfo - onResponse - Company About: " + jsonResponse.getResult().getCompanyInfo().getAbout());

                        // Move data to company info object object
                        if (jsonResponse.getResult().getCompanyInfo() != null) {

                            updateCompanyInfoOnView(view, jsonResponse.getResult().getCompanyInfo());

                        }

                        Log.d(TAG, "loadCompanyInfo - onResponse - End");
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Network Error Handling
                        Log.d(TAG, "loadCompanyInfo - onErrorResponse: " + error.toString());
                    }
                }
        ) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "*");
                return headers;
            }
        };

        // Adding retry policy to request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(AppPreferences.VOLLEY_TIME_OUT, AppPreferences.VOLLEY_RETRY_COUNTER, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyVolley.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

        Log.d(TAG, "loadCompanyInfo - End");
    }

    private void updateCompanyInfoOnView(View view, CompanyInfo companyInfo) {
        Log.d(TAG, "updateCompanyInfoOnView - Start");

        TextView about = (TextView) view.findViewById(R.id.company_about_description);
        about.setText(companyInfo.getAbout());

        TextView telephone = (TextView) view.findViewById(R.id.company_telephone);
        telephone.setText(companyInfo.getTelephone());

        TextView fax = (TextView) view.findViewById(R.id.company_fax);
        fax.setText(companyInfo.getFax());

        TextView websiteUrl = (TextView) view.findViewById(R.id.app_website_url);
        websiteUrl.setText(companyInfo.getWebsite());

        TextView facebookUrl = (TextView) view.findViewById(R.id.app_facebook_url);
        facebookUrl.setText("https://www.facebook.com/waritexint/");

        TextView jumiaUrl = (TextView) view.findViewById(R.id.app_jumia_url);
        jumiaUrl.setText(companyInfo.getJumiaWebsite());

        TextView souqUrl = (TextView) view.findViewById(R.id.app_souq_url);
        souqUrl.setText(companyInfo.getSouqWebsite());

        TextView whatsAppNumber = (TextView) view.findViewById(R.id.app_whatsApp_number);
        whatsAppNumber.setText(companyInfo.getWhatsappNumber());

        Log.d(TAG, "updateCompanyInfoOnView - End");
    }

    public void setupListeners(View view) {

        Log.d(TAG, "setupListeners - Start");

        // Listener for app rating image
        ImageView appRate = (ImageView) view.findViewById(R.id.app_rate_img);
        appRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setupListeners - appRate:onClick - Start");
                try {
                    Uri uri = Uri.parse(AppPreferences.PLAY_STORE_URL);

                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    startActivity(goToMarket);
                } catch (Exception e) {
                    Log.d(TAG, "setupListeners - appRate:onClick - Throw exception: "+e.getMessage());
                }

                Log.d(TAG, "setupListeners - appRate:onClick - Start");
            }
        });

        // Putting listener for app sharing image
        ImageView appShare = (ImageView) view.findViewById(R.id.app_url_share_img);
        appShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setupListeners - appShare:onClick - Start");

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.app_name));

                    String sAux = "\n"+ getResources().getText(R.string.share_app_descrption) +"\n\n";
                    sAux = sAux + AppPreferences.PLAY_STORE_URL +" \n\n";

                    i.putExtra(Intent.EXTRA_TEXT, sAux);

                    startActivity(Intent.createChooser(i, "choose one"));

                } catch(Exception e) {
                    Log.d(TAG, "setupListeners - appShare:onClick - Throw exception: "+e.getMessage());
                }

                Log.d(TAG, "setupListeners - appShare:onClick - End");
            }
        });



        Log.d(TAG, "setupListeners - End");
    }
}