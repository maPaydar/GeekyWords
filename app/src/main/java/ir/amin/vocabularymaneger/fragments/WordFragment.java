package ir.amin.vocabularymaneger.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.amin.vocabularymaneger.R;
import ir.android.widget.TfTextview;

/**
 * Created by amin on 9/8/16.
 */

public class WordFragment extends Fragment {

    private View mainview;
    private View detailView;
    private TfTextview mainWord, translate, def, syn, opp, exm;

    public WordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mainview == null) {
            mainview = inflater.inflate(R.layout.fragment_word, container, false);
            mainWord = (TfTextview) mainview.findViewById(R.id.main_word);
            translate = (TfTextview) mainview.findViewById(R.id.translate);
            def = (TfTextview) mainview.findViewById(R.id.def);
            syn = (TfTextview) mainview.findViewById(R.id.syn);
            opp = (TfTextview) mainview.findViewById(R.id.opp);
            exm = (TfTextview) mainview.findViewById(R.id.exm);

            String[] word = this.getArguments().getStringArray("word");
            mainWord.setText(word[0]);
            translate.setText(word[1]);
            def.setText(word[2]);
            syn.setText(word[3]);
            opp.setText(word[4]);
            exm.setText(word[5]);

            mainview.setOnClickListener(new View.OnClickListener() {
                int i = 1;
                @Override
                public void onClick(View view) {
                    if (i == 1) {
                        mainview.findViewById(R.id.main_view).setVisibility(View.INVISIBLE);
                        mainview.findViewById(R.id.detail_view).setVisibility(View.VISIBLE);
                        i = 0;
                    } else {
                        mainview.findViewById(R.id.main_view).setVisibility(View.VISIBLE);
                        mainview.findViewById(R.id.detail_view).setVisibility(View.INVISIBLE);
                        i = 1;
                    }
                }
            });
        }
        return mainview;
    }

}
