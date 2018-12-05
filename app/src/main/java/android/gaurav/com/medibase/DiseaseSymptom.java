package android.gaurav.com.medibase;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class DiseaseSymptom extends AppCompatActivity {

    ListView diseaseList;
    ArrayList<String> diseases, select, temp, filteredDiseases, diseaseList1,symptomList, finalDiseaseList, finalSymptomList, finalDiscription, finalDiseaseDiscription ;
    Boolean flag = false;
    FirebaseFirestore firebaseFirestore;
    DiseaseDiscriptionClass diseaseDiscriptionClass;
    ArrayList<DiseaseDiscriptionClass> objects;
    DiseaseSymptomAdapter adapter;
    CountDownLatch countDownLatch;
    int i;

    public DiseaseSymptom()
    {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disease_symptom);


        select = new ArrayList<>();
        select.addAll(getIntent().getStringArrayListExtra("SelectedList"));

        diseaseList = findViewById(R.id.diseaseList);

        //Firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        objects = new ArrayList<DiseaseDiscriptionClass>();

        adapter = new DiseaseSymptomAdapter(getApplicationContext(),R.layout.disease_symptom_adapter,objects);
        diseaseList.setAdapter(adapter);

        diseases = new ArrayList<String>();
        Log.e("Size",diseases.size()+"");


        filteredDiseases = new ArrayList<String>();
        temp = new ArrayList<String>();
        diseaseList1 = new ArrayList<String>();
        symptomList = new ArrayList<String>();
        flag=false;


        Log.e("Select Size",select.size()+"");


        firebaseFirestore.collection("Symptoms").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                diseaseList1.add(doc.getDocument().getString("Disease"));
                                symptomList.add(doc.getDocument().getString("Symptom"));
                            }
                            Log.e("DiseaseList1",diseaseList1.size()+"");
                            findDiseases();
                        }
                    }
                });

    }



    public void findDiseases()
    {
        filteredDiseases = new ArrayList<String>();
        temp = new ArrayList<String>();
        flag=false;


        Log.e("Select Size",select.size()+"");


        for(int i=0; i<select.size(); i++) {
            Log.e("Select", select.get(i));

            for (int j = 0; j < symptomList.size(); j++) {
                Log.e("Symptom",symptomList.get(j)+"");
                if (symptomList.get(j).equals(select.get(i))) {
                    if (i == 0) {
                        temp.add(diseaseList1.get(j));
                        Log.e("Temp",temp.get(0)+"");

                    }
                    else if (filteredDiseases.contains(diseaseList1.get(j))) {
                        temp.add(diseaseList1.get(j));
                    }
                }
            }
            filteredDiseases.clear();
            for (int k = 0; k < temp.size(); k++)
                filteredDiseases.add(temp.get(k));
            Log.e("Filetered",filteredDiseases.size()+"");
            temp.clear();
            Log.e("Temp",temp.size()+"");
        }


        finalDiseaseList = new ArrayList<String>();
        finalSymptomList = new ArrayList<String>();
        finalDiscription = new ArrayList<String>();
        finalDiseaseDiscription = new ArrayList<String>();

        firebaseFirestore.collection("Symptoms").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentChange doc : task.getResult().getDocumentChanges())
                            {
                                finalSymptomList.add(doc.getDocument().getString("Symptom"));
                                finalDiseaseList.add(doc.getDocument().getString("Disease"));
                            }
                            Log.e("finalSymptomList",finalSymptomList.size()+"");
                            firebaseFirestore.collection("Diseases").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                for(DocumentChange doc : task.getResult().getDocumentChanges())
                                                {
                                                    finalDiscription.add(doc.getDocument().getString("Discription"));
                                                    finalDiseaseDiscription.add(doc.getDocument().getString("Disease"));

                                                }
                                                Log.e("finalDiscription",finalDiscription.size()+"");
                                                findFinals();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void findFinals()
    {
        diseases.addAll(filteredDiseases);
        Log.e("Size",diseases.size()+"");
        for (i=0;i<diseases.size();i++)
        {
            diseaseDiscriptionClass = new DiseaseDiscriptionClass();
            diseaseDiscriptionClass.setSym("");
            diseaseDiscriptionClass.setDiscription("");
            diseaseDiscriptionClass.setDisease(diseases.get(i));

            for(int k=0 ; k<symptomList.size() ; k++)
            {
                if(finalDiseaseList.get(k).equals(diseases.get(i)))
                {
                    diseaseDiscriptionClass.setSym(diseaseDiscriptionClass.getSym() + finalSymptomList.get(k) + "\n");
                }
            }

            for(int j=0 ; j<finalDiseaseDiscription.size() ; j++)
            {
                if(finalDiseaseDiscription.get(j).equals(diseases.get(i)))
                {
                    diseaseDiscriptionClass.setDiscription(finalDiscription.get(j));
                    break;
                }
            }
            adapter.add(diseaseDiscriptionClass);
        }
        Log.e("Done","Done");
    }

    public void setDiseaseList(ArrayList<String> d)
    {
        diseases = d;
    }
}
