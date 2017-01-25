package girondins.economycheck;

import java.io.Serializable;

/**
 * Created by Girondins on 14/09/15.
 * Klass som håller information om köp
 */
public class Input implements Serializable {
    private String userName;
    private String input;
    private String date;
    private String category;
    private String productName;

    public Input(String userName, String input, String date, String category, String productName){
        this.userName = userName;
        this.input = input;
        this.date = date;
        this.category = category;
        this.productName = productName;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getInput(){
        return  this.input;
    }

    public String getDate(){
        return this.date;
    }

    public String getCategory(){
        return this.category;
    }

    public String getProduct(){
        return this.productName;
    }

    public void setDate(String date){
        this.date = date;
    }
}
