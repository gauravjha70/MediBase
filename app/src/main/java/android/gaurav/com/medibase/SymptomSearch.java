package android.gaurav.com.medibase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SymptomSearch extends AppCompatActivity {

    Spinner s1,s2,s3,s4;
    Button submit;
    RelativeLayout fragmentContainer;
    ArrayList<String> symptoms;


    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smptom_search);

        submit = findViewById(R.id.submit_button);
        fragmentContainer = findViewById(R.id.fragment_container);

        firebaseFirestore = FirebaseFirestore.getInstance();

        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        s3 = findViewById(R.id.spinner3);
        s4 = findViewById(R.id.spinner4);

        //Spinner Initialisation
        symptoms = new ArrayList<String>();
        symptoms.add("Select Symptom");
        firebaseFirestore.collection("Symptoms").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        Log.e("Success","false");

                        if(task.isSuccessful())
                        {
                            Log.e("Success",task.getResult().size()+"");
                            QuerySnapshot querySnapshot = task.getResult();
                            for(DocumentChange doc : querySnapshot.getDocumentChanges())
                            {
                                String symp = doc.getDocument().getString("Symptom");
                                Log.e("Symptom1234",symp);
                                if(!symptoms.contains(symp))
                                {
                                    symptoms.add(symp);
                                    Log.e("Symptom",symp);
                                }
                            }
                        }
                    }
                });

        ArrayAdapter sympAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,symptoms);
        sympAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(sympAdapter);
        s2.setAdapter(sympAdapter);
        s3.setAdapter(sympAdapter);
        s4.setAdapter(sympAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selected = new ArrayList<String>();
                if(s1.getSelectedItem().toString()!="Select Symptom")
                {
                    selected.add(s1.getSelectedItem().toString());
                }
                if(s2.getSelectedItem().toString()!="Select Symptom")
                {
                    selected.add(s2.getSelectedItem().toString());
                }
                if(s3.getSelectedItem().toString()!="Select Symptom")
                {
                    selected.add(s3.getSelectedItem().toString());
                }
                if(s4.getSelectedItem().toString()!="Select Symptom")
                {
                    selected.add(s4.getSelectedItem().toString());
                }

                Intent i = new Intent(SymptomSearch.this,DiseaseSymptom.class);
                i.putStringArrayListExtra("SelectedList",selected);
                startActivity(i);

            }
        });

    }

}
