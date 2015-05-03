package mhcs.control;

/**
 * imports weather feed from wunderground
 * 
 * @author Janna Madden
 *
 */

import java.util.Date;

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
          String url2 = "http://api.wunderground.com/api/76618a56636e14ef/astronomy/q/US/MN/Duluth.json";
          url2 = URL.encode(url2);
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
        }
             
          });     
          
          jsonp.setCallbackParam("callback");
          jsonp.requestObject(url2, new AsyncCallback<JavaScriptObject>() {
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
      		  JSONObject jA =(JSONObject)JSONParser.parseLenient(result); JSONValue jTry = jA.get("sun_phase");
      		  JSONObject jA2 =(JSONObject)JSONParser.parseLenient(jTry.toString()); JSONValue jTry2 = jA2.get("sunset");
      		  JSONObject jB = (JSONObject)JSONParser.parseLenient(jTry2.toString());
      		  JSONValue hour = jB.get("hour");
      		  JSONValue minute = jB.get("minute");
      		  String hourString = hour.toString();
      		  String minuteString = minute.toString();
      		  hourString = hourString.substring(1); //take off final quotation mark
      		  hourString = hourString.substring(0, hourString.length()-1);//takes off the first character which is a quotation mark
      		  minuteString = minuteString.substring(1); //take off final quotation mark
    		  minuteString = minuteString.substring(0, minuteString.length()-1);//takes off the first character which is a quotation mark
      		  i.add(new Label("Time of Sunset: " + hourString + ":" + minuteString)); //TO VIEW 
      		  
              Date currentDate = new Date();
              Integer hours = Integer.parseInt(hourString)-(Integer)(currentDate.getHours());
              Integer minutes = Integer.parseInt(minuteString)-(Integer)(currentDate.getMinutes());
              if (minutes < 0){
            	  hours = hours-1;
            	  minutes = 60 + minutes;
              }
              i.add(new Label("Time Until Sunset: " + hours.toString() + " hours and " + minutes.toString() + " minutes."));
      		  
      		  i.setWidth("6cm"); //set the width of the vertical panel to fit in the stacklayout (in the gui)
        }
             
          });
                    
  		  i.add(new Image("img/wunderground.jpg")); //add the image to credit wunderground for this information 
		return i;
}
}               
