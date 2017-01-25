package girondins.economycheck;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {
    TextView dynamic;
    String setTxt;


    public StartFragment() {
        // Required empty public constructor
    }

    public void showText(String setTxt){
        this.setTxt = "Information: " + setTxt;
    }

    public void showStart(String startTxt){
        this.setTxt = startTxt;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        dynamic = (TextView)view.findViewById(R.id.dynamicID);
        dynamic.setText(setTxt);
        return view;
    }


}
