package com.fitpal.fitpal;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fitpal.fitpal.model.FoodItem;
import com.fitpal.fitpal.model.MySingletone;
import com.fitpal.fitpal.model.UserMeal;
import com.fitpal.fitpal.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.fitpal.fitpal.MainActivity.GoalFromDB;
import static com.fitpal.fitpal.MainActivity.KeyUserMealsFromDB;

public class FoodResults extends AppCompatActivity {


    private TextView name,carbs,energy,fat,protein,mineral,calcium,fibre,calories,remarks;
    private Button readAgain;
    String foodToSearch,date,page,cal;
    DatabaseReference databaseReference,databaseReferenceUserMeals;
    FoodItem print;
    UserMeal um;
    float consumed;
    int count;

    static String x_app_id, x_app_key, x_remote_user_id;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_results);


        name=findViewById(R.id.FoodNameId);
        carbs=findViewById(R.id.CarbId);
        energy=findViewById(R.id.EnergyId);
        fat=findViewById(R.id.FatId);
        protein=findViewById(R.id.ProteinId);
        mineral=findViewById(R.id.MineralsId);
        calcium=findViewById(R.id.CalciumId);
        fibre=findViewById(R.id.FibreId);
        calories=findViewById(R.id.CaloriesId);

        remarks=findViewById(R.id.RemarksID);

        readAgain=findViewById(R.id.ReadAgainID);

        Intent in=getIntent();


        foodToSearch=in.getStringExtra("foodresult");
        page=in.getStringExtra("page");
        consumed=in.getFloatExtra("consumned",0);
        count=in.getIntExtra("count",1);

        Log.d("con",String.valueOf(consumed));
        Log.d("counttt",String.valueOf(count));

        Log.d("outtttt",foodToSearch);

        //databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
       databaseReferenceUserMeals=FirebaseDatabase.getInstance().getReference("UserMeals").child(String.valueOf(KeyUserMealsFromDB));


        x_app_id = "d2972203";
        x_app_key = "0f4683e999413bcdb29a139c8f9107a3";
        x_remote_user_id = "0f4683e999413bcdb29a139c8f9107a3";

        //volley code
        String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        mQueue = MySingletone.getInstance(getApplicationContext()).getRequestQueue();
        JSONObject jsonObject1=new JSONObject();

        try {
            jsonObject1.put("query", foodToSearch);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        UserMeal t1=new UserMeal();


        print=new FoodItem();
//        print.Name="";
//        print.Carbos="0";
//        print.Iron="0";
//        print.Phosphorous="0";
//        print.Fibre="0";
//        print.Calcium="0";
//        print.Minerals="0";
//        print.Protein="0";
//        print.Fat="0";
//        print.Energy="0";
//        print.Moisture="0";
        um=new UserMeal();



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonObject1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                   // Log.d("resultssss",String.valueOf(response));

                    JSONArray jsonArrayFoods=response.getJSONArray("foods");
                   // Log.d("foodssss",String.valueOf(jsonArrayFoods));

                    JSONObject productFood=null;

                    for(int i=0;i<jsonArrayFoods.length();i++){

                        productFood=jsonArrayFoods.getJSONObject(i);

                        Log.d("objjj",String.valueOf(productFood.get("nf_p").toString()));
                        Log.d("obj2",String.valueOf(productFood.get("nf_total_fat").toString()));

                        print.Name=print.Name+productFood.getString("food_name");

                        Log.d("nnna",print.Name);
//                        print.Carbos=String.valueOf(Float.parseFloat(print.Carbos)+Float.parseFloat(productFood.get("nf_total_carbohydrate").toString()));
//
//                        print.Energy=String.valueOf(Float.parseFloat(print.Energy)+Float.parseFloat(productFood.get("nf_p").toString()));;
//                        print.Fat=String.valueOf(Float.parseFloat(print.Fat)+Float.parseFloat(productFood.get("nf_total_fat").toString()));
//                        Log.d("ccac",print.Fat);
//                        print.Protein=String.valueOf(Float.parseFloat(print.Protein)+Float.parseFloat(productFood.get("nf_protein").toString()));
//                        print.Minerals=String.valueOf(Float.parseFloat(print.Minerals)+Float.parseFloat(productFood.get("nf_potassium").toString()));
//                        print.Calcium=String.valueOf(Float.parseFloat(print.Calcium)+Float.parseFloat(productFood.get("nf_saturated_fat").toString()));
//                        print.Fibre=String.valueOf(Float.parseFloat(print.Fibre)+Float.parseFloat(productFood.get("nf_dietary_fiber").toString()));
//                        cal=String.valueOf(Float.parseFloat(cal)+Float.parseFloat(productFood.get("nf_calories").toString()));
//                        Log.d("callo",cal.toString());


                        print.Energy=productFood.get("nf_p").toString();
                        print.Fat=productFood.get("nf_total_fat").toString();
                        print.Protein=productFood.get("nf_protein").toString();
                        print.Minerals=productFood.get("nf_potassium").toString();
                        print.Calcium=productFood.get("nf_saturated_fat").toString();
                        print.Fibre=productFood.get("nf_dietary_fiber").toString();
                        cal=productFood.get("nf_calories").toString();


                        name.setText(foodToSearch);
                        carbs.setText(String.valueOf(productFood.get("nf_total_carbohydrate").toString()));
                        energy.setText(String.valueOf(productFood.get("nf_p").toString()));
                        fat.setText(String.valueOf(productFood.get("nf_total_fat").toString()));
                        protein.setText(String.valueOf(productFood.get("nf_protein").toString()));
                        mineral.setText(String.valueOf(productFood.get("nf_potassium").toString()));
                        calcium.setText(String.valueOf(productFood.get("nf_saturated_fat").toString()));
                        fibre.setText(String.valueOf(productFood.get("nf_dietary_fiber").toString()));
                        calories.setText(String.valueOf(productFood.get("nf_calories").toString()));
////
//                        name.setText(name.getText()+","+String.valueOf(print.Name));
//                        carbs.setText(carbs.getText()+","+String.valueOf(print.Carbos).toString());
//                        energy.setText(energy.getText()+","+String.valueOf(print.Energy));
//                        fat.setText(fat.getText()+","+String.valueOf(print.Fat));
//                        protein.setText(protein.getText()+","+String.valueOf(print.Protein));
//                        mineral.setText(mineral.getText()+","+String.valueOf(print.Minerals));
//                        calcium.setText(calcium.getText()+","+String.valueOf(print.Calcium));
//                        fibre.setText(fibre.getText()+","+String.valueOf(print.Fibre));
//                        calories.setText(calories.getText()+","+String.valueOf(cal));

                       date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());



                        um.Calcium=String.valueOf(productFood.get("nf_saturated_fat").toString());
                        um.Carb=String.valueOf(productFood.get("nf_total_carbohydrate").toString());
                        um.Date=date;
                        um.Fat=String.valueOf(productFood.get("nf_total_fat").toString());
                        um.Fibre=String.valueOf(productFood.get("nf_dietary_fiber").toString());
                        um.Iron=print.Iron;
                        um.MealName=foodToSearch;
                        um.Phosphorous=print.Phosphorous;
                        um.Protein=String.valueOf(productFood.get("nf_protein").toString());
                        um.Calories=String.valueOf(productFood.get("nf_calories").toString());
                        Log.d("ucal",um.Calories);

//                        um.Calcium=String.valueOf(print.Calcium);
//                        um.Carb=String.valueOf(print.Carbos);
//                        um.Date=date;
//                        um.Fat=String.valueOf(print.Fat);
//                        um.Fibre=String.valueOf(print.Fibre);
//                        um.Iron=print.Iron;
//                        um.MealName=String.valueOf(print.Name);
//                        um.Phosphorous=print.Phosphorous;
//                        um.Protein=String.valueOf(print.Protein);
//                        um.Calories=String.valueOf(cal);




                    }

                    databaseReferenceUserMeals.child(String.valueOf(date+"-"+count)).setValue(um);
                    if(consumed<GoalFromDB){
                        remarks.setText("You have "+(GoalFromDB-consumed)+ " calories left");
                    }
                    else{
                        remarks.setText("Oh no, you have reached the calorie limit");
                    }



                } catch (Exception e) {

                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Called","Adapter is called");
                Log.d("Volley Error",error.toString());
                error.printStackTrace();

            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> hmap=new HashMap<>();
                hmap.put("x-app-id", x_app_id);
                hmap.put("x-app-key", x_app_key);
                hmap.put("x-remote-user-id",x_remote_user_id);
                hmap.put("Content-Type", "application/json");
                return hmap;

            }
        };

        mQueue.add(jsonObjectRequest);



        readAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page.equalsIgnoreCase("Voice")){
                    Intent i=new Intent(getApplicationContext(),VoiceInput.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i=new Intent(getApplicationContext(),BarcodeInput.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

}
