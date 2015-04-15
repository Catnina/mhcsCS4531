package ModuleMapTests;

import mhcs.model.StandardModule;
import mhcs.view.ModuleInfoPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ModuleInfoPopupTest {
	public void onModuleLoad() {
		Button b1 = new Button("Click me to show popup");
	    b1.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        // Instantiate the popup and show it.
	        new ModuleInfoPopup(new StandardModule(1, 7, 8, 1, "Good")).show();
	      }
	    });

	    RootPanel.get().add(b1);
	}

}
