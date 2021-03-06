package com.majedalmoqbeli.medicalnewapp.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.majedalmoqbeli.medicalnewapp.ClassAdapter.AdapterConsultation;
import com.majedalmoqbeli.medicalnewapp.ClassControl.MyProgressDialog;
import com.majedalmoqbeli.medicalnewapp.ClassControl.SaveSetting;
import com.majedalmoqbeli.medicalnewapp.ClassData.ConsultaionData;
import com.majedalmoqbeli.medicalnewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllConsultationFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ConsultaionData> consultaionData;
    AdapterConsultation adapter;

    Map<String, String> map = new HashMap<String, String>();
    String url = "";

    public AllConsultationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyProgressDialog.show(getActivity());
        url = SaveSetting.ServerURL + "getAllConsultaion.php";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_my_consultation_fargment, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        getConsultation();
        return myView;
    }

    public void getConsultation() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyProgressDialog.hide();
                        Log.i("Response :", response.toString());
                        try {
                            consultaionData = new ArrayList<>();
                            JSONArray array = new JSONArray(response);
                            int i = 0;
                            while (i < array.length()) {
                                JSONObject p = array.getJSONObject(i);
                                consultaionData.add(
                                        new ConsultaionData(
                                                p.getString("cons_id"),
                                                p.getString("user_id"),
                                                p.getString("cons_title"),
                                                p.getString("cons_content"),
                                                p.getString("is_public"),
                                                p.getString("cons_date_time"),
                                                p.getString("birthday"),
                                                p.getString("gender"),
                                                p.getString("weight"),
                                                p.getString("co_a_content"),
                                                p.getString("user_name"),
                                                p.getString("u_id_doc"),
                                                p.getString("doctor_name")

                                        ));
                                i++;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
                            Log.i("JSONException :", e.getMessage().toString());
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new AdapterConsultation(consultaionData, getActivity(),0);
                        recyclerView.setAdapter(adapter);
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hide();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("خطأ في الأتصال بالأنترنت");
                builder1.setIcon(R.drawable.ic_cancel);
                builder1.setMessage("تأكد من الأتصال بالانترنت ، ثم اعد المحاولة !");
                builder1.setCancelable(true);
                builder1.setPositiveButton("أعادة المحاولة",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               MyProgressDialog.show(getActivity());
                                getConsultation();
                            }
                        });
                builder1.setNegativeButton("خروج",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                Log.i("VolleyError :", error.toString());

            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
