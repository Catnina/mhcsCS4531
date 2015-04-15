package mhcs.control;

/**
 * imports weather feed from wunderground
 * 
 * @author Janna Madden
 *
 */

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder; 
import com.google.gwt.http.client.RequestCallback; 
import com.google.gwt.http.client.RequestException; 
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

//this class is just what was on his website- i haven't worked with it too much! The URL is correct. 


public class weather{
	String url ="http://api.wunderground.com/api/76618a56636e14ef/c onditions/q/55812.json";
	url = URL.encode(url);
	
	RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url); try {
		  Request request = builder.sendRequest(null, new RequestCallback() {
		public void onError(Request request, Throwable exception) { Window.alert("onError: Couldn't retrieve JSON");
		}
		    public void onResponseReceived(Request request, Response response) {
		        if (200 == response.getStatusCode()) {
		            String rt = response.getText();
		            mhcs.view.updateWeather(rt); //METHOD CALL TO DO SOMETHING WITH RESPONSE TEXT
		        } else {
		Window.alert("Couldn't retrieve JSON (" + response.getStatusCode() + ")");
		} }
		  });
		} catch (RequestException e) {
		}
		Window.alert("RequestException: Couldn't retrieve JSON");
	}

	public void update(String rt) { 
		VerticalPanel vp = new VerticalPanel(); 
		vp.add(new Label(rt)); //TO VIEW RootLayoutPanel.get().add(vp);
	}	
