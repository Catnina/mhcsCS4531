package mhcs.control;

/**
 * imports weather feed from wunderground
 * 
 * @author Janna Madden
 *
 */

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder; 
import com.google.gwt.http.client.RequestCallback; 
import com.google.gwt.http.client.RequestException; 
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//this class is just what was on his website- i haven't worked with it too much! The URL is correct. 


public class weather{
    
    public static VerticalPanel getResponse(){
          String url = "http://api.wunderground.com/api/76618a56636e14ef/conditions/q/55811.json";
          url = URL.encode(url);
          //create vertical panel. This is what will be passed back to the user-class. 
          final VerticalPanel i = new VerticalPanel();
          
          final JsonpRequestBuilder jsonp = new JsonpRequestBuilder();

          jsonp.setCallbackParam("callback");
          jsonp.requestObject(url, new AsyncCallback<JavaScriptObject>() {
          //attempt to connect with WUNDERGROUND if it's unsucessful display a window "connection failed" else add the data to the 
        	  //vertical panel that gets returned to the user-class
                  @Override
              public void onFailure(final Throwable caught) {
                  //Window.alert("JSONP onFailure");
                  i.add(new Label("wunderground connection failed."));
              }

              @Override
              public void onSuccess(JavaScriptObject s) {
              JSONObject obj = new JSONObject(s);
              String result = obj.toString();
      		  JSONObject jA =(JSONObject)JSONParser.parseLenient(result); JSONValue jTry = jA.get("current_observation");
      		  JSONObject jB = (JSONObject)JSONParser.parseLenient(jTry.toString());
      		  JSONValue temp = jB.get("temp_c");
      		  JSONValue visibility = jB.get("visibility_km");
      		  String sTemp = temp.toString();
      		  String sVisibility = visibility.toString();
      		  sVisibility = sVisibility.substring(1); //take off final quotation mark
      		  sVisibility = sVisibility.substring(0, sVisibility.length()-1);//takes off the first character which is a quotation mark
      		  i.add(new Label("Tempature (in C): " + sTemp)); //TO VIEW 
      		  i.add(new Label("Visibility (in Km): " + sVisibility)); //TO VIEW 
      		  i.add(new Image("img/wunderground.jpg")); //add the image to credit wunderground for this information 
      		  i.setWidth("6cm"); //set the width of the vertical panel to fit in the stacklayout (in the gui)
        }
             
          });
		return i;
}
}               
