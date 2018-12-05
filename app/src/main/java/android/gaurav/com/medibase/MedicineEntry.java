package android.gaurav.com.medibase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MedicineEntry extends AppCompatActivity {

    //Firebase
    FirebaseFirestore mFirestore;

    EditText medicine,medType,price;
    Button submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_add);

        //Firebase
        mFirestore = FirebaseFirestore.getInstance();

        medicine = findViewById(R.id.MedicineName);
        medType = findViewById(R.id.MedicineType);
        price = findViewById(R.id.Price);
        submit = findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setBackgroundResource(R.drawable.button_clicked_bg);
                submit.setEnabled(false);

                Map<String,String> Med = new HashMap<>();
                Med.put("Medicine",medicine.getText().toString());
                Med.put("MedicineType",medType.getText().toString());
                Med.put("Price",price.getText().toString());

                mFirestore.collection("Medicines").add(Med)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Data Added Successfully !!",Toast.LENGTH_SHORT).show();
                                submit.setEnabled(true);
                                submit.setBackgroundResource(R.drawable.button_bg);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error in adding data",Toast.LENGTH_SHORT).show();
                                submit.setEnabled(true);
                                submit.setBackgroundResource(R.drawable.button_bg);
                            }
                        });
            }
        });
    }
}
