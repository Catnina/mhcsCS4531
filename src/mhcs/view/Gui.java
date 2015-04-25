package mhcs.view;


import mhcs.model.ModuleList;
import mhcs.model.ModuleMaker;
import mhcs.control.counters;
import mhcs.control.weather;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.storage.client.Storage;
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
	
	private Storage moduleStore;
	
	public ModuleList moduleList; // this is the module list!! It must be passed whenever we add (or remove) modules or print the map
	Integer caseNumb; //this integer holds which test case we are running from NASA/ESA feed (for User Story 1) 
	SoundController soundController = new SoundController(); // this enables the use of sound output at any place throught the GUI
	DockLayoutPanel dockPanel = new DockLayoutPanel(Unit.CM); // this is the dock that the main page is built off of
	TabLayoutPanel backPanel = new TabLayoutPanel(2, Unit.CM);// this is the panel that allows movement between pages 
															  //(such as main to configuration page etc) 
	Label configPoss; //this label appears in the top left hand corner of the main page and tells if a minimum configuration is possible
	
	//these are the add button listener values (become the value in the add fields) they are set to their inital states. 
	private String conditionString = "Usable"; 
	private Integer xNumb = 1;
	private Integer yNumb = 1;
	private Integer orientNumb = 0;
	private Integer configNumb = 0;
	   

	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 * this method starts the project; it initializes the project and gets everything ready. For example, it sets up the dockLayoutPanel, 
	 * the back panel, the sound output and creates the login page. 
	 */
	   public void onModuleLoad() {	
		   moduleList = new ModuleList();
		   moduleStore = Storage.getLocalStorageIfSupported();
		   
		   backPanel.add(dockPanel, "Main Page");
		   RootLayoutPanel.get().add(backPanel);   
		   soundController.setDefaultVolume(100);
		   soundController.setPreferredSoundTypes(SoundType.HTML5);
		   loginPage();
	   }
	   
	   /*
	    * the loginPage method makes the login page. If login credentials are correctly entered, the user will be redirected to the 
	    * main page, otherwise they will be prompted to re-enter their password information. 
	    * If the correct information is entered, sound outout will tell the user that they will hear a beep when a module is manually entered correctly
	    */
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

/*
 * makeMain method makes the home page. This methods adds data to the dock lyaout panel which includes, 
 * the north panel: configurations possible and logout button
 * the east panel: weather and ten-day alert
 * the south panel: add module, get modules from NASA/ESA feed and other buttons. 
 * the center panel: scrollPanel holding map.  
 */
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
            	addModuleMethod(); //caddModuleMehtod will set up the add module pop-up panel
            }
            });
		   southPanel.add(addModule);
		   Button removeModule = new Button ("Get Modules From NASA/ESA Feed", new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	getModulesFromNASAESAFeed(); //this method will get the data from NASA/ESA feed and return if it was sucessful
		    }
		    });
           southPanel.add(removeModule);
           Button getConfigs = new Button ("Get Configurations", new ClickHandler() {
   	        public void onClick(ClickEvent event) {
   	        	getConfigurations();   	//this button will direct the user to get cofigurations pop=up panel where they can view 4 configuratiosn	    
   	        	}
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
  		      } // this button removes all the data from the page and sends the system back to the login page. 
  		   });
           configPoss = new Label("minimum configuration NOT possible"); // this button is inically set to not possible. 
           										//it will be updated in the add method if a config. becomes possible. 
           northPanel.add(configPoss);
           northPanel.add(logoutButton);
           
           //*******************************************	   
           //Navigation (east panel)
           //*******************************************
           VerticalPanel weatherPanel = new VerticalPanel();
           weatherPanel = makeWeatherMethod(); //within the weather class. 
           VerticalPanel tenPanel = new VerticalPanel(); //adds the Vertical Panel that is reutrned from the makeWeatherMethod() 
           //tenPanel = makeTenMethod();
           stackPanel.add(weatherPanel, new HTML("Weather"),1.5);
           stackPanel.add(tenPanel, new HTML("10-Day Alert"), 1.5);
           
           //*******************************************	   
           //Center
           //*******************************************
           //SimplePanel sHolder = new SimplePanel();
           //sHolder.setSize("500px", "500px");
           //sPanel.add(new ModuleMap().renderMap(moduleList)); //adds the map within a scroll panel to the center of the dock layout
           //sHolder.add(sPanel);
           
           //*******************************************	   
           //Add to dockPanel
           //*******************************************
           dockPanel.addNorth(northPanel,2);
           dockPanel.addEast(stackPanel,6);
           dockPanel.addSouth(southPanel,2);
           //dockPanel.add(sHolder);
           
           loadModules();

	   }

	/*   private void addModule(ClickHandler clickHandler) {
		   // TODO Auto-generated method stub
		   }
	   
	private void getConfigs(ClickHandler clickHandler) {
		//TODO Auto-generated method stub
	}


	private void removeModule(ClickHandler clickHandler) {
		// TODO Auto-generated method stub
		
	}*/
	   
	 
	   
	   /*
	    * add module sets up a popup panel that provides the framework for adding a module manually to the system. 
	    * for every field that will be manually entered, there is a listener to take that data from the field and pass it into
	    * the createModule method (in module maker class) 
	    * all fields are then added to the popuppanel and displayed. 
	    */
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
				configNumb = Integer.valueOf(configNumberInput.getText());	
				}
        }
        );
        Label coordinates = new Label("Coordinates");
        final TextBox xCoordinate = new TextBox();
        xCoordinate.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				xNumb = Integer.valueOf(xCoordinate.getText());	
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
			conditionString = conditionSuggest.getItemText(conditionSuggest.getSelectedIndex());	
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
	        	 //PopupPanel temp1  = new PopupPanel();
			        //temp1.add(new Label(configNumb + "   " + xNumb + "   " + yNumb + "   " + orientNumb + "   " + conditionString));
			      	 //temp1.show();
		      	boolean addModuleSucess = new ModuleMaker(moduleList).createModule(configNumb, xNumb, yNumb, orientNumb, conditionString);
		      	//this statement puts all the data collected in teh above editable fields and passes it into the moduleMaker
		      	if(addModuleSucess){
		      		//if sucessful play sound, update configuration possible and close popup panel
			        pPanel.hide();
	    	  		Sound intro = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"/sounds/beep.mp3");
			        intro.play();
			        if (new counters(moduleList).minConfigPossible()){
						configPoss.setText("minimum configuration NOT possible");
					}
					else{
						configPoss.setText("minimum configuration possible!");
					}
			        
		      		//updateLabel();
			        
			        saveModules();
	    	  	  }
          	  	  else{
          	  		  //if it's not sucessful, hid the popup panel, make a new popup that tells the user that they were unable
          	  		  //to add the module; show THIS popup panel. 
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
	
	
	/*
	 * get modules from NASA/ESA feed. Has the option of testCases 1-10 for the various test feeds. displays the listbox
	 * and a button in the popup panel. the caseNumb holds the number of the testcase that is selected (initially 1)
	 */
	private void getModulesFromNASAESAFeed(){
		final PopupPanel pPanel = new PopupPanel();
		VerticalPanel vPanel = new VerticalPanel();
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		pPanel.setSize("3cm", "2cm");
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
	    //add tests cases to the list box, make a listener for the box; add this to the vertical panel
	    vPanel.add(testCases);
	    Button addModuleButton = new Button("Add");
	    vPanel.add(addModuleButton);
	    //make add module button; add it to the vertical panel. When add is clicked, it tries to connect
	    //with with the NASA/ESA feed. 
	    addModuleButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	String proxy = "http://www.d.umn.edu/~abrooks/Proxy.php?url=";
	 		   String url = proxy+"http://www.d.umn.edu/~abrooks/SomeTests.php?q="+"caseNumb";
	 		   url = URL.encode(url);
	 		   RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url); 
	 		   try {
	 			   Request request = builder.sendRequest(null, new RequestCallback() {
	 				   public void onError(Request request, Throwable exception) { Window.alert("onError: Couldn't retrieve JSON");
	 				   pPanel.hide();//does this help? YES! Otherwise can't click other buttons after this gets thrown
	 				   }
	 			     public void onResponseReceived(Request request, Response response) {
	 			         if (200 == response.getStatusCode()) {
	 			             String rt = response.getText();
	 			             update(rt); //METHOD CALL TO DO SOMETHING WITH RESPONSE TEXT
	 			         } else {
	 			        	 Window.alert("Couldn't retrieve JSON (" + response.getStatusText() + ")"); 
	 			        	 pPanel.hide(); //needed so that user can access stuff after this alert is thrown
	 			         }
	 			        }
	 			   });
	 			 } 
	 		   catch (RequestException e) {
	 			 }
	 		   pPanel.hide();
	        	}
	        });
	    pPanel.add(vPanel);   
	    pPanel.show();
	}
	
	/*
	 * get configurations make the popuppanel that displays the 4 possible configurations. They are showed in a stackpanel. 
	 * the first "stack" is minumum, the second is full. there are two configurations displayed in each category. This will be dipslayed
	 * using the map feature. 
	 */
	private void getConfigurations(){
		final PopupPanel pPanel = new PopupPanel();
		pPanel.setSize("20cm", "15cm");
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		DockLayoutPanel layoutPPanel = new DockLayoutPanel(Unit.CM);
		VerticalPanel verticalChoicePanel = new VerticalPanel();
		ListBox chooseConfig = new ListBox();
		chooseConfig.addItem("Minimum 1");
		chooseConfig.addItem("Minimum 2");
		chooseConfig.addItem("Full 1");
		chooseConfig.addItem("Full 2");
		verticalChoicePanel.add(chooseConfig);
		Button chooseButton = new Button("Choose");
		//button needs listener
		layoutPPanel.addEast(verticalChoicePanel,5);
		layoutPPanel.addEast(chooseButton, 5);
		
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
	
	/*
	 * this calls teh weather.getResponse() method from teh weather class. This gets the weather underground data. The getResponse
	 * method returns a vertical panel which is then returned to the makeMain class. 
	 */
	private VerticalPanel makeWeatherMethod(){
		return weather.getResponse();
		
	}
	
	/*
	 * makeTenMethod make the vertical panel that displays the 10 day alert. This tells the astronauts when they need to re-calibrate
	 * their machinary. 
	 */
	private VerticalPanel makeTenMethod(){
		VerticalPanel tempPanel = new VerticalPanel();
		//TODO 
		return tempPanel;
	}
	
	/*private String updateLabel(){
		String tempString = null;
		if (new counters(moduleList).minConfigPossible()){
			tempString = "minimum configuration NOT possible";
		}
		else{
			tempString = "minimum configuration possible!";
		}
		return tempString;

	}*/
	
	
	/*
	 * this method gets the data from the NASA/ESA feed and pasrses it for the information we are looking for; 
	 * code, status, turns, x and y. 
	 * displays a popup of how many modules have been entered successfully. 
	 */
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
      	if(addModuleSucess)
      		sucessCounter++;
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
	
	/**
	 * Saves current modules to local store
	 */
	public void saveModules() {
		if(moduleStore != null) {
			moduleStore.setItem("modules", moduleList.toJSONString());
		}
	}
	
	/**
	 * Loads Modules from local store onto the module list
	 */
	public void loadModules() {
		String modules = moduleStore.getItem("modules");
		JSONArray jA = (JSONArray) JSONParser.parseLenient(modules);
		JSONNumber jN;
		JSONString jS;
		ModuleMaker make = new ModuleMaker(moduleList);
		int idNumber, xCoordinate, yCoordinate, turnsToUpright;
		String condition = null;
		
		for(int i = 0; i < jA.size(); i++) {
			JSONObject jO = (JSONObject) jA.get(i);
			jN = (JSONNumber) jO.get("code");
			idNumber = (int) jN.doubleValue();
			jS = (JSONString) jO.get("status");
			condition = jS.stringValue();
			jN = (JSONNumber) jO.get("turns");
			turnsToUpright = (int) jN.doubleValue();
			jN = (JSONNumber) jO.get("X");
			xCoordinate = (int) jN.doubleValue();
			jN = (JSONNumber) jO.get("Y");
			yCoordinate = (int) jN.doubleValue();
			make.createModule(idNumber, xCoordinate, yCoordinate, turnsToUpright, condition);
		}
		
	}
}