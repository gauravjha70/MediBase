package android.gaurav.com.medibase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button Adder,Searcher,MedSearch,AddDiseaseMed,AddSymptom, SearchSymptom, DeleteMedicine, DeleteDisease, UpdateDisease;

    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase
        mFirestore = FirebaseFirestore.getInstance();

        Adder = findViewById(R.id.adder);
        Searcher = findViewById(R.id.searcher);
        MedSearch = findViewById(R.id.searchMed);
        AddDiseaseMed = findViewById(R.id.add_disease_med);
        AddSymptom = findViewById(R.id.add_symptom);
        SearchSymptom = findViewById(R.id.search_symptom);
        DeleteMedicine = findViewById(R.id.delete_medicine);
        DeleteDisease = findViewById(R.id.delete_disease);
        UpdateDisease =findViewById(R.id.update_disease);

        Adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MedicineEntry.class);
                startActivity(i);
            }
        });

        Searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MedicineSearch.class);
                startActivity(i);
            }
        });

        AddDiseaseMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DiseaseMedEntry.class);
                startActivity(i);
            }
        });

        MedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DiseaseMedSearch.class);
                startActivity(i);
            }
        });

        AddSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Symptom_Adder.class);
                startActivity(i);
            }
        });

        SearchSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SymptomSearch.class);
                startActivity(i);
            }
        });

        DeleteMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DeleteMedicine.class);
                startActivity(i);
            }
        });

        DeleteDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DeleteDisease.class);
                startActivity(i);
            }
        });

        UpdateDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UpdateDiscription.class);
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
