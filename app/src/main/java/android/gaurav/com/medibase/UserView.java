package android.gaurav.com.medibase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserView extends AppCompatActivity {

    Button Searcher,MedSearch, SearchSymptom;

    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        //Firebase
        mFirestore = FirebaseFirestore.getInstance();

        Searcher = findViewById(R.id.searcher);
        MedSearch = findViewById(R.id.searchMed);
        SearchSymptom = findViewById(R.id.search_symptom);

        Searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserView.this,MedicineSearch.class);
                startActivity(i);
            }
        });

        MedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserView.this,DiseaseMedSearch.class);
                startActivity(i);
            }
        });

        SearchSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserView.this,SymptomSearch.class);
                startActivity(i);
            }
        });

    }
}
