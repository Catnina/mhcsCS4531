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
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

//this class is just what was on his website- i haven't worked with it too much! The URL is correct. 


public class weather {
    
    public static ScrollPanel getResponse(){
          String url = "http://api.wunderground.com/api/a4e7563412689635/conditions/q/55811.json";
          url = URL.encode(url);
  
          ScrollPanel i = new ScrollPanel();
          
          final JsonpRequestBuilder jsonp = new JsonpRequestBuilder();

          jsonp.setCallbackParam("callback");
          jsonp.requestObject(url, new AsyncCallback<JavaScriptObject>() {
             
                  @Override
              public void onFailure(final Throwable caught) {
                  Window.alert("JSONP onFailure");
              }

              @Override
              public void onSuccess(JavaScriptObject s) {
              JSONObject obj = new JSONObject(s);
              String result = obj.toString();
              Window.alert(result);
              //ScrollPanel i = new ScrollPanel();
              i.add(new Label(result));
              
              }
             
          });
		return i;
}
}               
