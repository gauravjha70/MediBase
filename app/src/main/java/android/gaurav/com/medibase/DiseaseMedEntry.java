package android.gaurav.com.medibase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class DiseaseMedEntry extends AppCompatActivity {

    EditText medicine, disease, medicineType, price, discription;

    Button submit;
    Boolean flag = false;

    //Firebase
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_med_entry);

        //Firebase
        mFirestore = FirebaseFirestore.getInstance();

        medicine = findViewById(R.id.MedicineName);
        disease = findViewById(R.id.diseaseName);
        discription = findViewById(R.id.disease_discription);
        medicineType = findViewById(R.id.MedicineType);
        price = findViewById(R.id.Price);
        submit = findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setBackgroundResource(R.drawable.button_clicked_bg);
                submit.setEnabled(false);
                flag = false;

                mFirestore.collection("Disease").whereEqualTo("Disease", disease.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.e("Task","asdfg");
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if(querySnapshot.getDocuments().size()==0) {
                                if (discription.getText().toString().equals("")) {
                                    Log.e("Task", "Not success");
                                    Toast.makeText(getApplicationContext(), "Enter discription", Toast.LENGTH_SHORT).show();
                                    submit.setEnabled(true);
                                    submit.setBackgroundResource(R.drawable.button_bg);
                                    return;
                                } else {
                                    addToDiseases();
                                }
                            }
                        }

                        addToMedicines();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        submit.setEnabled(true);
                        submit.setBackgroundResource(R.drawable.button_bg);
                    }
                });
            }
        });
    }

    public void addToMedicines() {
        Map<String, String> diseaseMed = new HashMap<>();
        diseaseMed.put("Disease", disease.getText().toString());
        diseaseMed.put("Medicine", medicine.getText().toString());

        //Insert into DiseaseMed
        mFirestore.collection("DiseaseMed").add(diseaseMed)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Map<String, String> Med = new HashMap<>();
                        Med.put("Medicine", medicine.getText().toString());
                        Med.put("MedicineType", medicineType.getText().toString());
                        Med.put("Price", price.getText().toString());

                        //Insert into Medicines
                        mFirestore.collection("Medicines").add(Med)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(), "Data Added Successfully !!", Toast.LENGTH_SHORT).show();
                                        submit.setEnabled(true);
                                        submit.setBackgroundResource(R.drawable.button_bg);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error in adding data to medicine collection", Toast.LENGTH_SHORT).show();
                                        submit.setEnabled(true);
                                        submit.setBackgroundResource(R.drawable.button_bg);

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DiseaseMedEntry.this, "Error !!", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void addToDiseases()
    {
        Map<String, String> diseases = new HashMap<>();
        diseases.put("Disease", disease.getText().toString());
        diseases.put("Discription", discription.getText().toString());
        //Insert into Diseases
        mFirestore.collection("Diseases").add(diseases)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }
}




