package com.example.chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    

    EditText editText;
    ImageView sendbutton;
    Object jobj;

    List<modules> list;
    RecyclerView recyclerView;
    chatAdapters adapters;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // find by ids

        editText = findViewById(R.id.edittextinput);
        sendbutton = findViewById(R.id.sendbutton);
        recyclerView = findViewById(R.id.recyclerviewchats);
        TextView welcometext = findViewById(R.id.welcometext);

        // array list

        list = new ArrayList<>();

        // set adapters

        adapters = new chatAdapters(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapters);


        //dialog box

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Please Enter your Query..");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            dialog.cancel();
        });


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    String question = editText.getText().toString().trim();
                    addtochat(modules.SEND_BY_ME,question);
                    calltheAPI(question);
                    editText.setText("");
                    welcometext.setVisibility(View.GONE);

                } else {
                    builder.show();

                }

            }
        });
    }

    void addresponse(String response)
    {

        addtochat(modules.SEND_BY_BOT,response);
    }

    void calltheAPI(String question)
    {
        JSONObject jsonbody = new JSONObject();

        try {
            jsonbody.put("model","text-davinci-003");
            jsonbody.put("prompt",question);
            jsonbody.put("max_tokens",4000);
            jsonbody.put("temperature",0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonbody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addresponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addresponse(result.trim());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
                else
                {
                    addresponse("Failed to load response due to "+response.body().string());
                }

            }
        });

    }

    void addtochat(String message,String sendby)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.add(new modules(message,sendby));
                adapters.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapters.getItemCount());

            }
        });
    }

}