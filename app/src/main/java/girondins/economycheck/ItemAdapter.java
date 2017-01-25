package girondins.economycheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Girondins on 15/09/15.
 */

/**
 * ArrayAdapter som tar emot Input objekt
 */
public class ItemAdapter extends ArrayAdapter<Input> {
    private LayoutInflater inflater;
    private String content;
    private String category;
    private int imageResource;

    public ItemAdapter(Context context, Input[] objects) {
        super(context,R.layout.listview_layout,objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Hämtar view och inflatear layout till adaptern
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView categoryName;
        ImageView categoryPic;
        if(convertView==null){
            convertView = (LinearLayout)inflater.inflate(R.layout.listview_layout,parent,false);
        }
        categoryName = (InOutcomeView) convertView.findViewById(R.id.listTxtID);
        categoryPic = (ImageView) convertView.findViewById(R.id.categoryPicID);
        extractContent(this.getItem(position));
        categoryName.setText(content);
        setImage(category);
        categoryPic.setImageResource(imageResource);

    return convertView;
    }

    /**
     * Metod som sätter rätt bild till rätt kategori
     * @param category
     */
    public void setImage(String category){
        if(category.equals("Home")){
            imageResource = R.drawable.home;
        }
        if(category.equals("Entertainment")){
            imageResource = R.drawable.entertainment;
        }
        if(category.equals("Food")){
            imageResource = R.drawable.food;
        }
        if(category.equals("Other")){
            imageResource = R.drawable.other;
        }

        if(category.equals("Salary")){
            imageResource = R.drawable.salary;
        }

    }

    /**
     * Metod som tar ut/sätter samman information om varje input
     * @param input
     */
    public void extractContent(Input input){
        content = input.getCategory() + ", " + input.getProduct() + ", " + input.getInput() + ", " + input.getDate();
        category = input.getCategory();
    }
}
