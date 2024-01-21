package tr.com.cemre.imageindexer.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import tr.com.cemre.imageindexer.R;

public class AddLabelFragment extends Fragment {
    private LinearLayout layoutLabels;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_label, container, false);

        db = FirebaseFirestore.getInstance();

        layoutLabels = root.findViewById(R.id.layoutLabels);
        Button btnAddLabel = root.findViewById(R.id.btnLogin);

        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = ((EditText) root.findViewById(R.id.etLabel)).getText().toString();
                String descr = ((EditText) root.findViewById(R.id.etDescr)).getText().toString();

                addDataToFirestore(label, descr);

                fetchAndDisplayLabels();

                ((EditText) root.findViewById(R.id.etLabel)).setText("");
                ((EditText) root.findViewById(R.id.etDescr)).setText("");
            }
        });


        return root;
    }

    private void addDataToFirestore(String label, String descr) {
        Map<String, Object> data = new HashMap<>();
        data.put("label", label);
        data.put("description", descr);

        db.collection("labels")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddLabelFragment", "Document snapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddLabelFragment", "Error adding document", e);
                    }
                });
    }

    private void fetchAndDisplayLabels() {
        layoutLabels.removeAllViews();

        db.collection("labels")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                            String label = document.getString("label");

                            LinearLayout labelLayout = new LinearLayout(getContext());
                            labelLayout.setOrientation(LinearLayout.HORIZONTAL);
                            labelLayout.setGravity(Gravity.CENTER_VERTICAL);

                            ImageView iconImageView = new ImageView(getContext());
                            iconImageView.setImageResource(R.drawable.icon_flower);
                            iconImageView.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                            labelLayout.addView(iconImageView);

                            labelLayout.addView(new Space(getContext()));

                            labelLayout.getChildAt(labelLayout.indexOfChild(iconImageView) + 1).setLayoutParams(new LinearLayout.LayoutParams(
                                    30, ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            TextView labelTextView = new TextView(getContext());
                            labelTextView.setText(label);

                            labelTextView.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.purple)));

                            labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                            labelLayout.addView(labelTextView);
                            layoutLabels.addView(labelLayout);

                            layoutLabels.addView(new Space(getContext()));
                            layoutLabels.getChildAt(labelLayout.indexOfChild(labelLayout) + 1).setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 50
                            ));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddLabelFragment","Error fetching documents", e);
                    }
                });

    }
}