package mhcs.view;


import mhcs.model.ModuleList;
import mhcs.model.ModuleMaker;
import mhcs.control.weather;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.SoundType;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray; 
import com.google.gwt.json.client.JSONNumber; 
import com.google.gwt.json.client.JSONObject; 
import com.google.gwt.json.client.JSONParser; 
import com.google.gwt.json.client.JSONString;


public class Gui implements EntryPoint{
	
	public ModuleList moduleList;
	Integer caseNumb;
	//panel for configurations
	SoundController soundController = new SoundController();
	DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.CM);
	TabLayoutPanel backPanel = new TabLayoutPanel(2, Unit.CM);
	
	private String conditionString = "Usable";
	private Integer xNumb = 1;
	private Integer yNumb = 1;
	private Integer orientNumb = 0;
	private Integer configNumb = 0;
	   

	
	   public void onModuleLoad() {	
		   backPanel.add(dockPanel, "Main Page");
		   RootLayoutPanel.get().add(backPanel);   
		   soundController.setDefaultVolume(100);
		   soundController.setPreferredSoundTypes(SoundType.HTML5);
		   loginPage();
	   }
	   
	   private void loginPage(){
		   final PopupPanel login = new PopupPanel();
		   login.setGlassEnabled(true);
		   login.center();
		   final PasswordTextBox ptb = new PasswordTextBox();
		   final Label tb = new Label("Enter Password:");
		   final VerticalPanel vPanel = new VerticalPanel();
		   vPanel.setWidth("4cm");
		   final Button enter = new Button("Enter");
		    vPanel.add(tb);
		    vPanel.add(ptb);
		    vPanel.add(enter);
		    login.add(vPanel);
		    enter.addClickHandler(new ClickHandler() {
		    	public void onClick(ClickEvent event) { 
		    	String s = ptb.getText();
		    	if (s.equals("mhcs")) {
		        Sound intro = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"/sounds/intro.mp3");
		        intro.play();
		    	tb.setText("Redirecting to Main Page");
		    	login.hide();
		    	makeMain();
		    	}
		    	else {
		    		tb.setText("Please Re-Enter Password");
		    		ptb.setText("");
		    		}
		    	}
		    });
	   }


	   private void makeMain(){	
		   final HorizontalPanel southPanel = new HorizontalPanel();
		   southPanel.setWidth("12cm");
		   final HorizontalPanel northPanel = new HorizontalPanel();
		   northPanel.setWidth("12cm");
           final ScrollPanel sPanel = new ScrollPanel();
           sPanel.setSize("500px", "500px");
           final StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.CM);
		   
  //*******************************************	   
  //SouthPanel
  //*******************************************	   
		   Button addModule= new Button("Add Module", new ClickHandler() {
            public void onClick(ClickEvent event) {
            	addModuleMethod();
            }
            });
		   southPanel.add(addModule);
		   Button removeModule = new Button ("Get Modules From NASA/ESA Feed", new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	getModulesFromNASAESAFeed();
		    }
		    });
           southPanel.add(removeModule);
           Button getConfigs = new Button ("Get Configurations", new ClickHandler() {
   	        public void onClick(ClickEvent event) {
   	        	getConfigurations();   		    }
   		    });
              southPanel.add(getConfigs);
           
           //*******************************************	   
           //NorthPanel
           //*******************************************
           Button logoutButton= new Button("Log Out", new ClickHandler() {
               public void onClick(ClickEvent event) {
            	dockPanel.remove(southPanel);
               	dockPanel.remove(northPanel);
               	dockPanel.remove(stackPanel);
               	dockPanel.remove(sPanel);
                loginPage();
  		      }
  		   });
           Label configPoss = new Label(updateLabel());
           northPanel.add(configPoss);
           northPanel.add(logoutButton);
           
           //*******************************************	   
           //Navigation (east panel)
           //*******************************************
           VerticalPanel weatherPanel = new VerticalPanel();
           weatherPanel = makeWeatherMethod();
           VerticalPanel tenPanel = new VerticalPanel();
           //tenPanel = makeTenMethod();
           stackPanel.add(weatherPanel, new HTML("Weather"),1.5);
           stackPanel.add(tenPanel, new HTML("10-Day Alert"), 1.5);
           
           //*******************************************	   
           //Center
           //*******************************************
           //SimplePanel sHolder = new SimplePanel();
           //sHolder.setSize("500px", "500px");
           //sPanel.add(new ModuleMap().renderMap(moduleList)); 
           //sHolder.add(sPanel);
           
           //*******************************************	   
           //Add to dockPanel
           //*******************************************
           dockPanel.addNorth(northPanel,2);
           dockPanel.addEast(stackPanel,6);
           dockPanel.addSouth(southPanel,2);
           //dockPanel.add(sHolder);

	   }

	   private void addModule(ClickHandler clickHandler) {
		   // TODO Auto-generated method stub
		   }
	   
	private void getConfigs(ClickHandler clickHandler) {
		//TODO Auto-generated method stub
	}


	private void removeModule(ClickHandler clickHandler) {
		// TODO Auto-generated method stub
		
	}
	   
	 
	   
	   
	private void addModuleMethod(){
		final PopupPanel pPanel = new PopupPanel();
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		//this section of code makes the elements for adding a module
		final VerticalPanel panel1 = new VerticalPanel();
		panel1.setWidth("6cm");
	    Label configNumber = new Label("Module Identification Number");
	    final TextBox configNumberInput = new TextBox(); 
	    configNumberInput.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				configNumb = Integer.valueOf(configNumberInput.getText());			}
        }
        );
        Label coordinates = new Label("Coordinates");
        final TextBox xCoordinate = new TextBox();
        xCoordinate.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				yNumb = Integer.valueOf(xCoordinate.getText());	
			}
        }
        );
	    xCoordinate.setText("X Coordinate");
	    final TextBox yCoordinate = new TextBox();
        yCoordinate.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				yNumb = Integer.valueOf(yCoordinate.getText());	
			}
        }
        );
	    yCoordinate.setText("Y Coordinate");
        Label condition = new Label("Condition");
        final ListBox orientationSuggest = new ListBox();
        orientationSuggest.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
			orientNumb = orientationSuggest.getSelectedIndex();	
			}
        }
        );
	    orientationSuggest.addItem("None");
	    orientationSuggest.addItem("One Rotation");
	    orientationSuggest.addItem("Two Rotations");
	    orientationSuggest.addItem("Three Rotations");
        final ListBox conditionSuggest = new ListBox();
        conditionSuggest.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
			conditionString = conditionSuggest.getItemText(orientationSuggest.getSelectedIndex());	
			}
        }
        );
	    conditionSuggest.addItem("Usable");
        conditionSuggest.addItem("Usable After Repair");
	    conditionSuggest.addItem("Unusable");
	    Label orientation = new Label("Orientation");
	    final Button addModuleButton = new Button("Add Module");
		//add items to panel1
		panel1.add(configNumber);
		panel1.add(configNumberInput);
	    panel1.add(coordinates);
		panel1.add(xCoordinate);
		panel1.add(yCoordinate);
		panel1.add(condition);
		panel1.add(conditionSuggest);
		panel1.add(orientation);
		panel1.add(orientationSuggest);
		panel1.add(addModuleButton);
	    pPanel.add(panel1);
	    pPanel.show();
	    //*****************************
	    //make button to add module
	    //*****************************
	    addModuleButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
		      	boolean addModuleSucess = new ModuleMaker(moduleList).createModule(configNumb, xNumb, yNumb, orientNumb, conditionString);
	    	  	  if(addModuleSucess){
			        pPanel.hide();
	    	  		Sound intro = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"/sounds/beep.mp3");
			        intro.play();
		      		updateLabel();
	    	  	  }
          	  	  else{
        	          pPanel.hide();
		      		  final PopupPanel tempPP = new PopupPanel();
		      		  tempPP.setSize("5cm", "3cm");
		      		  VerticalPanel vPanelTemp = new VerticalPanel();
		      		  tempPP.center();
		      		  tempPP.setGlassEnabled(true);
		      		  vPanelTemp.add(new Label("Unable to Add Module"));
		      		  Button tempButton = new Button("OK", new ClickHandler(){
		      			  public void onClick(ClickEvent event){
		      				  tempPP.hide();
		      			  }
		      		  });
		      		vPanelTemp.add(tempButton);
		      		tempPP.add(vPanelTemp);
		      		tempPP.show();

          	  	  }  
		          }
		      });
}
	
	private void getModulesFromNASAESAFeed(){
		final PopupPanel pPanel = new PopupPanel();
		VerticalPanel vPanel = new VerticalPanel();
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		final ListBox testCases = new ListBox();
        testCases.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				caseNumb = testCases.getSelectedIndex()+1;	
			}
        });
	    testCases.addItem("TestCase 1");
	    testCases.addItem("TestCase 2");
	    testCases.addItem("TestCase 3");
	    testCases.addItem("TestCase 4");
	    testCases.addItem("TestCase 5");
	    testCases.addItem("TestCase 6");
	    testCases.addItem("TestCase 7");
	    testCases.addItem("TestCase 8");
	    testCases.addItem("TestCase 9");
	    testCases.addItem("TestCase 10");
	    vPanel.add(testCases);
	    Button addModuleButton = new Button("Add");
	    vPanel.add(addModuleButton);
	    addModuleButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	String proxy = "http://www.d.umn.edu/~abrooks/Proxy.php?url=";
	 		   String url = proxy+"http://www.d.umn.edu/~abrooks/SomeTests.php?q="+"caseNumb";
	 		   url = URL.encode(url);
	 		   RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url); try {
	 			   Request request = builder.sendRequest(null, new RequestCallback() {
	 			 public void onError(Request request, Throwable exception) { Window.alert("onError: Couldn't retrieve JSON");
	 			 }
	 			     public void onResponseReceived(Request request, Response response) {
	 			         if (200 == response.getStatusCode()) {
	 			             String rt = response.getText();
	 			             update(rt); //METHOD CALL TO DO SOMETHING WITH RESPONSE TEXT
	 			         } else {
	 			 Window.alert("Couldn't retrieve JSON (" + response.getStatusText() + ")");
	 			 } }
	 			   });
	 			 } catch (RequestException e) {
	 			 }
	 		   pPanel.hide();
	        	}
	        });
	    pPanel.add(vPanel);   
	    pPanel.show();
	}
	
	
	
	private void getConfigurations(){
		final PopupPanel pPanel = new PopupPanel();
		pPanel.setWidth("8cm");
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		DockLayoutPanel layoutPPanel = new DockLayoutPanel(Unit.CM);
		ListBox chooseConfig = new ListBox();
		chooseConfig.addItem("Minimum 1");
		chooseConfig.addItem("Minimum 2");
		chooseConfig.addItem("Full 1");
		chooseConfig.addItem("Full 2");
		layoutPPanel.addEast(chooseConfig, 5);
		
		StackLayoutPanel configsPanel = new StackLayoutPanel(Unit.CM);
		HorizontalPanel min = new HorizontalPanel();
		min.add(new Label("minimum configurations here"));
		HorizontalPanel full = new HorizontalPanel();
		full.add(new Label("full configurations here"));

		configsPanel.add(min, new HTML("Minimum Configurations"),1.5);
        configsPanel.add(full, new HTML("Full Configurations"), 1.5);
        layoutPPanel.add(configsPanel);

		pPanel.add(layoutPPanel);
		pPanel.show();
	}
	
	private VerticalPanel makeWeatherMethod(){
		return weather.getResponse();
		
	}
	
	private VerticalPanel makeTenMethod(){
		VerticalPanel tempPanel = new VerticalPanel();
		//TODO 
		return tempPanel;
	}
	
	private String updateLabel(){
		return "Updated String.";
	}
		/**
		String tempString;
		if (new Counts(moduleList).minConfigPoss()){
			tempString = "minimum configuration NOT possible";
		}
		else{
			tempString = "minimum configuration possible!";
		}
		return tempString;

	}*/
	
	
	
	public void update(String rt) { 
    String sAll = rt;
    JSONArray jA =(JSONArray)JSONParser.parseLenient(sAll);
    JSONNumber jN; JSONString jS; 
    double code; 
    String status;
    double turns; 
    double x;
    double y;
    Integer sucessCounter = 0;
    for (int i = 0; i < jA.size(); i++) {
    	JSONObject jO = (JSONObject)jA.get(i);
    	jN = (JSONNumber) jO.get("code");
    	code = jN.doubleValue();
    	jS = (JSONString) jO.get("status");
    	status = jS.stringValue();
    	jN = (JSONNumber) jO.get("turns");
    	turns = jN.doubleValue();
    	jN = (JSONNumber) jO.get("X");
    	x = jN.doubleValue();
    	jN = (JSONNumber) jO.get("Y");
    	y = jN.doubleValue();
      	boolean addModuleSucess = new ModuleMaker(moduleList).createModule((int)code, (int)x, (int)y, (int)turns, status);
      	if(addModuleSucess){
      		sucessCounter++;
      	}
    	}
    final PopupPanel sucesses = new PopupPanel();
    VerticalPanel tempSucesses = new VerticalPanel();
    tempSucesses.add(new Label("Number of Modules added Sucessfully: "+sucessCounter));
    Button tempButton = new Button("OK", new ClickHandler(){
		  public void onClick(ClickEvent event){
			  sucesses.hide();
		  }
	  });
    tempSucesses.add(tempButton);
    sucesses.add(tempSucesses);
    sucesses.show();
    }
}