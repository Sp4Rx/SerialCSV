package io.github.sp4rx.serialcsv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.sp4rx.serialcsv.model.Data;
import io.github.sp4rx.serialcsvlibarary.SerialCSV;

/**
 * Created by suvajit on 28/2/18.
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btExport;
    TextView tvMessage;
    List<Data> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = findViewById(R.id.tvMessage);
        btExport = findViewById(R.id.btExport);
        btExport.setOnClickListener(this);


        //Prepare demo data
        data = new ArrayList<>();
        data.add(new Data("Suvajit Sarkar", "Gaming", "Android Developer", 987654321));
        data.add(new Data("Sourav Sarkar", "Music", "Pro Guitarist", 987654321));
        data.add(new Data("Suchandra Bhandari", "Photography", null, 987654321));
        data.add(null);
        data.add(new Data("Dhiraj Kumar", "Coding", "Web Developer", 987654321));

    }

    @Override
    public void onClick(View view) {
        if (view == btExport) {

            //Export to csv
            new SerialCSV<Data>()
                    .setCallback(new SerialCSV.Listener() {
                        @Override
                        public void onSuccess(File file) {
                            tvMessage.setText(file.toString());
                        }

                        @Override
                        public void onFailure(String message) {
                            tvMessage.setText(message);
                        }
                    })
                    .setHeading("A", "B", "C", "D")
                    .export(data, Data.class);


        }
    }
}
