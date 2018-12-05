package android.gaurav.com.medibase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Symptom_Adder extends AppCompatActivity {

    EditText disease,symptom,discription;
    Button submit;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptoms_adder);

        disease = findViewById(R.id.Disease);
        symptom = findViewById(R.id.Symptoms);
        discription = findViewById(R.id.disease_discription);
        submit = findViewById(R.id.submit_button);


        firebaseFirestore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setBackgroundResource(R.drawable.button_clicked_bg);
                submit.setEnabled(false);

                //Insert into Disease if don't exist
                //Insert into Disease Medicine
                firebaseFirestore.collection("Diseases").whereEqualTo("Disease",disease.getText().toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    if(task.getResult().getDocuments().size()==0) {
                                        if (discription.getText().toString().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Enter discription", Toast.LENGTH_SHORT).show();
                                            submit.setEnabled(true);
                                            submit.setBackgroundResource(R.drawable.button_bg);
                                            return;
                                        } else
                                            addToDisease();
                                    }
                                    addToSymptom();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
                        submit.setEnabled(true);
                        submit.setBackgroundResource(R.drawable.button_bg);
                    }
                });
            }
        });

    }

    public  void addToDisease()
    {
        Map<String, String> diseases = new HashMap<>();
        diseases.put("Disease", disease.getText().toString());
        diseases.put("Discription", discription.getText().toString());
        //Insert into Diseases
        firebaseFirestore.collection("Diseases").add(diseases)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }

    public void addToSymptom()
    {
        Map<String, String> symptoms = new HashMap<>();
        symptoms.put("Disease", disease.getText().toString());
        symptoms.put("Symptom", symptom.getText().toString());
        //Insert into Diseases
        firebaseFirestore.collection("Symptoms").add(symptoms)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        submit.setEnabled(true);
                        submit.setBackgroundResource(R.drawable.button_bg);
                        Toast.makeText(Symptom_Adder.this, "Successfuly Added to Database", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
