package com.example.jale.poi_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {


    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);
        // Inflate the layout for this fragment
        String [] menueItems = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s"};
        ListView listView = view.findViewById(R.id.listviewresult);
        ArrayAdapter<String> listviewadapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,menueItems);
listView.setAdapter(listviewadapter);
        getActivity().setTitle(getString(R.string.ergebnis));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ResultItemActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", position); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);


                // Display the selected item text on TextView
               // tv.setText("Your favorite : " + selectedItem);
            }
        });



                return view;
    }

}
