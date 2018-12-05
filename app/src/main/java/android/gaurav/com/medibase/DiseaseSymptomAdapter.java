package android.gaurav.com.medibase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class DiseaseSymptomAdapter extends ArrayAdapter<DiseaseDiscriptionClass> {

    Context context;
    ArrayList<DiseaseDiscriptionClass> arrayList;

    public DiseaseSymptomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DiseaseDiscriptionClass> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.disease_symptom_adapter, null);
        }

        TextView dis = convertView.findViewById(R.id.disease_name);
        TextView sym = convertView.findViewById(R.id.Symptoms);
        TextView discription = convertView.findViewById(R.id.discription);

        DiseaseDiscriptionClass diseaseDiscriptionClass = getItem(position);

        if(diseaseDiscriptionClass!=null)
        {
            dis.setText(diseaseDiscriptionClass.getDisease());
            sym.setText(diseaseDiscriptionClass.getSym());
            discription.setText(diseaseDiscriptionClass.getDiscription());
        }


        return convertView;
    }
}
