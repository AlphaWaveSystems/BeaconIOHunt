package com.alphaws.javadaytrivia.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphaws.javadaytrivia.R;

/**
 * Created by oscarvargas on 06/02/15.
 */
public class FragmentMuseum extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_museum, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.fragment_museum_text);
        text.setText("Es una obra pictórica del pintor renacentista italiano Leonardo da Vinci. Fue adquirida por el rey Francisco I de Francia a principios del siglo XVI y desde entonces es propiedad del Estado Francés.\n\nSu nombre, La Gioconda (la alegre, en castellano), deriva de la tesis más aceptada acerca de la identidad de la modelo: la esposa de Francesco Bartolomeo de Giocondo, que realmente se llamaba Lisa Gherardini, de donde viene su otro nombre: Mona (señora, del italiano antiguo) Lisa.");
        return rootView;
    }

}
