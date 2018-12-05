package android.gaurav.com.medibase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateDiscription extends AppCompatActivity {

    Spinner diseaseSpinner;
    ArrayList<String> medicines;
    String currentDisease = "";

    String currentMedType = "";

    private static final String TAG = "MedicineSearch";

    ArrayList<MedicineClass> medicineClasses;
    MedicineAdapter adapter;


    FirebaseFirestore mFirestore;

    Button updateButton;

    EditText discription;
    Map<String,Object> updater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_discription);

        mFirestore = FirebaseFirestore.getInstance();

        updateButton = findViewById(R.id.update_button);
        discription = findViewById(R.id.discription);

        //Spinner
        medicines = new ArrayList<String>();
        diseaseSpinner = findViewById(R.id.disease_spinner);
        medicines.add("Select Disease");

        //Spinner Adapter
        mFirestore.collection("Diseases").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {

                }
                else
                {
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        switch(doc.getType())
                        {
                            case ADDED: String medName = doc.getDocument().getString("Disease");
                                medicines.add(medName);
                                break;
                            case REMOVED:
                            case MODIFIED:
                        }
                    }
                }
            }
        });
        //Spinner Adapter
        ArrayAdapter MedArray = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,medicines);
        MedArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diseaseSpinner.setAdapter(MedArray);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButton.setBackgroundResource(R.drawable.button_clicked_bg);
                updateButton.setEnabled(false);
                if(diseaseSpinner.getSelectedItem().toString().equals("Select Disease")) {
                    Toast.makeText(getApplicationContext(), "Select Disease", Toast.LENGTH_SHORT).show();
                    updateButton.setEnabled(true);
                    updateButton.setBackgroundResource(R.drawable.button_bg);
                }

                else
                {
                    updater = new HashMap<>();
                    updater.put("Discription",discription.getText().toString());
                    currentDisease = diseaseSpinner.getSelectedItem().toString();
                    mFirestore.collection("Diseases").whereEqualTo("Disease",currentDisease)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentChange doc : task.getResult().getDocumentChanges())
                                {
                                    Log.e("ID",doc.getDocument().getId()+"");
                                    mFirestore.collection("Diseases").document(doc.getDocument().getId()+"").update(updater)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            });
                                }
                            }
                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                            updateButton.setEnabled(true);
                            updateButton.setBackgroundResource(R.drawable.button_bg);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
                            updateButton.setEnabled(true);
                            updateButton.setBackgroundResource(R.drawable.button_bg);
                        }
                    });
                }
            }
        });

    }
}
