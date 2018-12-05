package android.gaurav.com.medibase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DiseaseMedSearch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore firebaseFirestore;
    Spinner spinner;
    ArrayList<String> diseases;
    String currentDisease = "";


    MedicineAdapter medicineAdapter;
    ArrayList<MedicineClass> medicineClasses;
    ListView medicineList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_search);

        //Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        medicineList = findViewById(R.id.medicineList);

        //Spinner Initialisation
        spinner = findViewById(R.id.disease_spinner);
        spinner.setOnItemSelectedListener(this);
        diseases = new ArrayList<String>();
        diseases.add("Select Disease");
        firebaseFirestore.collection("DiseaseMed").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if(querySnapshot!=null)
                        {
                            for(DocumentChange doc : querySnapshot.getDocumentChanges())
                            {
                                if(!diseases.contains(doc.getDocument().getString("Disease")))
                                    diseases.add(doc.getDocument().getString("Disease"));
                            }
                        }
                    }
                });
        ArrayAdapter diseaseAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,diseases);
        diseaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(diseaseAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentDisease = diseases.get(position);

        Log.e("Disease",currentDisease);
        medicineClasses = new ArrayList<MedicineClass>();
        medicineAdapter = new MedicineAdapter(getApplicationContext(),R.layout.medicine_adapter,medicineClasses);
        medicineList.setAdapter(medicineAdapter);

        firebaseFirestore.collection("DiseaseMed").whereEqualTo("Disease",currentDisease)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentChange doc : querySnapshot.getDocumentChanges())
                    {
                        Log.e("Medicine",doc.getDocument().getString("Medicine"));
                        firebaseFirestore.collection("Medicines").whereEqualTo("Medicine",doc.getDocument().getString("Medicine"))
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    Log.e("MedicineName",querySnapshot.getDocuments().get(0).getString("Medicine"));
                                    MedicineClass m = new MedicineClass();
                                    m.setMedicine(querySnapshot.getDocuments().get(0).getString("Medicine"));
                                    m.setMedicineType(querySnapshot.getDocuments().get(0).getString("MedicineType"));
                                    m.setPrice(querySnapshot.getDocuments().get(0).getString("Price"));

                                    medicineAdapter.add(m);
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
