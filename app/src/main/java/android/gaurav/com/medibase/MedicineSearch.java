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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MedicineSearch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView medicineList;
    Spinner medSpinner;
    ArrayList<String> medicines;
    String currentMed = "";

    String currentMedType = "";

    private static final String TAG = "MedicineSearch";

    ArrayList<MedicineClass> medicineClasses;
    MedicineAdapter adapter;


    FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_search);
        medicineList = findViewById(R.id.medicineList);

        mFirestore = FirebaseFirestore.getInstance();

        //Spinner
        medicines = new ArrayList<String>();
        medSpinner = findViewById(R.id.medicine_spinner);
        medSpinner.setOnItemSelectedListener(this);
        medicines.add("Select Medicine");

        //Spinner Adapter
        mFirestore.collection("Medicines").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            case ADDED: String medName = doc.getDocument().getString("Medicine");
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
        medSpinner.setAdapter(MedArray);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentMed = medicines.get(position);

        Log.e("Item",currentMed);
        //Initialisation of Recycler Adapter
        medicineClasses = new ArrayList<MedicineClass>();
        adapter = new MedicineAdapter(getApplicationContext(),R.layout.medicine_adapter,medicineClasses);
        medicineList.setAdapter(adapter);

        currentMedType ="";



        mFirestore.collection("Medicines").whereEqualTo("Medicine",currentMed)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if(querySnapshot!=null)
                        {
                            for(DocumentChange doc : querySnapshot.getDocumentChanges())
                            {
                                    currentMedType=doc.getDocument().getString("MedicineType");
                                    Log.e("medicineType",currentMedType);
                                    mFirestore.collection("Medicines").whereEqualTo("MedicineType",currentMedType)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if(querySnapshot!=null)
                                                {
                                                    for(DocumentChange doc : querySnapshot.getDocumentChanges())
                                                    {
                                                        if(doc.getType()==DocumentChange.Type.ADDED) {
                                                            MedicineClass m = new MedicineClass();
                                                            m.setPrice(doc.getDocument().getString("Price"));
                                                            m.setMedicineType(doc.getDocument().getString("MedicineType"));
                                                            m.setMedicine(doc.getDocument().getString("Medicine"));
                                                            Log.e("Medicine", doc.getDocument().getString("Medicine"));
                                                            adapter.add(m);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    break;
                            }
                        }
                    }
                });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
