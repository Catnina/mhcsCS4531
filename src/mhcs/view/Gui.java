package mhcs.view;


import java.util.ArrayList;
import java.util.Date;

import mhcs.model.Configuration;
import mhcs.model.Module;
import mhcs.model.ModuleList;
import mhcs.model.ModuleMaker;
import mhcs.control.ConfigurationBuilder;
import mhcs.control.MinConfigurationBuilder;
import mhcs.control.TenDay;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray; 
import com.google.gwt.json.client.JSONNumber; 
import com.google.gwt.json.client.JSONObject; 
import com.google.gwt.json.client.JSONParser; 
import com.google.gwt.json.client.JSONString;


public class Gui implements EntryPoint{

	private Storage moduleStore;
	private Date endDate, currentDate;
	private ModuleList moduleList; // this is the module list!! It must be passed whenever we add (or remove) modules or print the map
	private ModuleList configList; // configurations list
	private ModuleList beforeList; // configurations list
	private ModuleMaker modMaker;
	private Configuration config;
	private Label quality;

	
	private Integer caseNumb; //this integer holds which test case we are running from NASA/ESA feed (for User Story 1) 
	private SoundController soundController = new SoundController(); // this enables the use of sound output at any place throught the GUI
	private VerticalPanel container = new VerticalPanel();
	private TabLayoutPanel backPanel = new TabLayoutPanel(2, Unit.CM);// this is the panel that allows movement between pages 
	private final HorizontalPanel middlePanel = new HorizontalPanel();
	private final VerticalPanel addedModules = new VerticalPanel();  
	private final ScrollPanel sPanel = new ScrollPanel();
	private final StackLayoutPanel stackPanel = new StackLayoutPanel(Unit.CM);
	private ArrayList<String> moduleArray = new ArrayList<String>();
	private Integer choice = 0;


	private int counter = 0;
	private Label configPoss; //this label appears in the top left hand corner of the main page and tells if a minimum configuration is possible
	private ModuleMap modMap = new ModuleMap();
	private Button getConfigs, before, after, mapButton, saveConfigButton, clearButton, changeCenter;

	//these are the add button listener values (become the value in the add fields) they are set to their inital states. 
	private String conditionString = "Usable"; 
	private Integer xNumb = 1;
	private Integer yNumb = 1;
	private Integer orientNumb = 0;
	private Integer configNumb = 0;
	private FlexTable moduleInfo = new FlexTable(); 
	private int configNum;
	private Integer qualityInt = 0;

	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 * this method starts the project; it initializes the project and gets everything ready. For example, it sets up the dockLayoutPanel, 
	 * the back panel, the sound output and creates the login page. 
	 */
	   public void onModuleLoad() {	
		   caseNumb = 1;
		   moduleList = new ModuleList();
		   configList = new ModuleList();
		   backPanel.add(container, "Main Page");
		   RootLayoutPanel.get().setStylePrimaryName("translatePanel");
		   RootLayoutPanel.get().add(backPanel);   
		   soundController.setDefaultVolume(100);
		   //soundController.setPreferredSoundTypes(SoundType.HTML5);
		   makeMain();
		   //loginPage();
	   }
	   
	   /*
	    * the loginPage method makes the login page. If login credentials are correctly entered, the user will be redirected to the 
	    * main page, otherwise they will be prompted to re-enter their password information. 
	    * If the correct information is entered, sound outout will tell the user that they will hear a beep when a module is manually entered correctly
	    */
	   private void loginPage(){
		   final PopupPanel login = new PopupPanel();
		   login.setGlassEnabled(true);
		   final TextBox utb = new TextBox(); 
		   Image nasa = new Image("img/nasa.png");
		   final Label tbUser = new Label("Enter Username");
		   final PasswordTextBox ptb = new PasswordTextBox();
		   final Label tb = new Label("Enter Password:");
		   final VerticalPanel vPanel = new VerticalPanel();
		   vPanel.setWidth("4cm");
		   final Button enter = new Button("Enter");
		   vPanel.add(nasa);
		    vPanel.add(tbUser);
		    vPanel.add(utb);
		    vPanel.add(tb);
		    vPanel.add(ptb);
		    vPanel.add(enter);
		    login.add(vPanel);
		    enter.addClickHandler(new ClickHandler() {
		    	public void onClick(ClickEvent event) { 
		    	String s = ptb.getText();
		    	String userName = utb.getText();
		    	if ((s.equals("mhcs") && userName.equals("Catania")) || (s.equals("a") && userName.equals("a"))) {
		        Sound correct = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"sounds/correct.mp3");
		        correct.play();
			    	login.hide();
		    	}
		    	else {
		    		tb.setText("Incorrect Username or Password");
		    		Sound incorrect = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"sounds/incorrect.mp3");
			        incorrect.play();
		    		utb.setText("");
		    		ptb.setText("");
		    		}
		    	}
		    });
		    //login.center();
		    login.show();
	   }

/*
 * makeMain method makes the home page. This methods adds data to the dock lyaout panel which includes, 
 * the north panel: configurations possible and logout button
 * the east panel: weather and ten-day alert
 * the south panel: add module, get modules from NASA/ESA feed and other buttons. 
 * the center panel: scrollPanel holding map.  
 */
	   private void makeMain(){	
		   
		   loginPage();
		   
		   moduleInfo.setText(0, 0, "Module Code");
     	   moduleInfo.setText(0, 1, "X-Coord");
     	    moduleInfo.setText(0, 2, "Y-Coord");
     	    moduleInfo.setText(0, 3, "Condition");
     	    moduleInfo.setText(0, 4, "Rotations");
     	    moduleInfo.setText(0, 5, "Remove");
		   loadModules();
		   loadConfig();
		   
		   modMaker= new ModuleMaker(moduleList);
		   final HorizontalPanel southPanel = new HorizontalPanel();
		   southPanel.setWidth("15cm");
		   final HorizontalPanel northPanel = new HorizontalPanel();
		   northPanel.setWidth("12cm");
           
           sPanel.setSize("900px", "650px");
		   stackPanel.setSize("250px", "320px");
  //*******************************************	   
  //SouthPanel
  //*******************************************	   
		   Button addModule= new Button("Add Module", new ClickHandler() {
            public void onClick(ClickEvent event) {
            	addModuleMethod(event); //caddModuleMehtod will set up the add module pop-up panel
            }
            });
		   southPanel.add(addModule);
		   Button removeModule = new Button ("Get Modules From NASA/ESA Feed", new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	getModulesFromNASAESAFeed(event); //this method will get the data from NASA/ESA feed and return if it was sucessful
		    }
		    });
           southPanel.add(removeModule);
		   Button clearButton= new Button("Clear Modules", new ClickHandler() {
	            public void onClick(ClickEvent event) {
	            	boolean choice = Window.confirm("Do you wish to clear all modules?");
	            	if (choice) {
	            		clearModuleList();
	            		moduleInfo.setText(0, 0, "Module Code");
	              	    moduleInfo.setText(0, 1, "X-Coord");
	              	    moduleInfo.setText(0, 2, "Y-Coord");
	              	    moduleInfo.setText(0, 3, "Condition");
	              	    moduleInfo.setText(0, 4, "Rotations");
	              	    moduleInfo.setText(0, 5, "Remove");
	              	    checkMinConfig();
	            	}
	            }
	            });
          southPanel.add(clearButton);

           getConfigs = new Button ("Get Configurations", new ClickHandler() {
      	        public void onClick(ClickEvent event) {
      	        	getConfigurations();   	//this button will direct the user to get cofigurations pop=up panel where they can view 4 configuratiosn	    
      	        	}
      		    });
              getConfigs.setEnabled(false);
                 southPanel.add(getConfigs);
              
              
              //VerticalPanel mapButtons = new VerticalPanel();
              mapButton = new Button ("View Configuration Map", new ClickHandler() {
          	   public void onClick(ClickEvent event) { 
          		   Button tempButton = (Button) event.getSource();
          		   if(tempButton.getText() == "View Configuration Map") {
          			   //quality.setVisible(true);
          			   tempButton.setText("View Module Map");
          			   modMap.renderMap(configList);
          		   }
          		   else {
          			   modMap.renderMap(moduleList);
          			   tempButton.setText("View Configuration Map");
          			   //quality.setVisible(false);
          		   }
          	   }
              });
              
              //CARLOS'S CODE
              //quality = new Label ("Configuration Quality: "+ config.getQuality());
              //quality.setVisible(false);
              //southPanel.add(quality);
              
//              Button configMapButton = new Button ("View Configuration Map", new ClickHandler() {
//           	   public void onClick(ClickEvent event) { 
//           		   modMap.renderMap(configList);
//           	   }
//               });
//              mapButtons.add(moduleMapButton);
//              mapButtons.add(configMapButton);
              southPanel.add(mapButton);
              
              before = new Button ("Before", new ClickHandler() {
              	public void onClick(ClickEvent event) {
           		   Button tempButton = (Button) event.getSource();
              		if(tempButton.getText() == "Before"){
              			modMap.renderMap(beforeList);
              			before.setText("After");
              		}
              			else{
              				modMap.renderMap(configList);
              				before.setText("Before");
              			}
              	    }
              	});
              before.setVisible(false);
               southPanel.add(before);

               
               saveConfigButton = new Button("Save Configuration", new ClickHandler() {
            	   	public void onClick(ClickEvent event) {
            	   		final PopupPanel save = new PopupPanel();
            	   		save.center();
            	   		VerticalPanel vp = new VerticalPanel();
            	   		vp.add(new Label("Saving will finalize the position of the configuration!"));
            	   		FlowPanel flow = new FlowPanel();
            	   		flow.add(new Label("Are you sure you wish to save?"));
            	   		flow.add(new Button("Yes", new ClickHandler() {
            	   			public void onClick(ClickEvent event) {
            	   				saveConfig();
            	   				save.hide();
            	   			}
            	   		}));
            	   		flow.add(new Button("No", new ClickHandler() {
            	   			public void onClick(ClickEvent event) {
            	   				save.hide();
            	   			}
            	   		}));
            	   		
            	   		vp.add(flow);
            	   		save.add(vp);
            	   		save.show();
            	   	}
               });
               
               southPanel.add(saveConfigButton);
               
              
                  changeCenter = new Button ("Center of Gravity", new ClickHandler() {
                	   public void onClick(ClickEvent event) { 
                		  final PopupPanel pPanel = new PopupPanel();
                		  pPanel.center();
                		  pPanel.show();
                		  pPanel.setGlassEnabled(true);
                		  pPanel.setAutoHideEnabled(true);
                		  final HorizontalPanel xMove = new HorizontalPanel();
               	          final HorizontalPanel yMove = new HorizontalPanel();
               	          final VerticalPanel change = new VerticalPanel();
               	          final Label xLabel = new Label("x");
               	          final Label yLabel = new Label("y");
               	          final TextBox xBox = new TextBox(); 
               	          final TextBox yBox = new TextBox();   
               	          xMove.add(xLabel);
               	          xMove.add(xBox);
               	          yMove.add(yLabel);
               	          yMove.add(yBox);
               	          change.add(xMove);
               	          change.add(yMove);
                		  pPanel.add(change);
                		  Button submit = new Button("Submit", new ClickHandler() {
                			  public void onClick(ClickEvent event) {
                				  if (Integer.parseInt(xBox.getText()) < 14 || Integer.parseInt(xBox.getText()) > 86) {
                					  Window.alert("x value must be greater than 14 and less than 86");
                				  }
                				  else if (Integer.parseInt(yBox.getText()) < 14 || Integer.parseInt(yBox.getText()) > 36) {
                					  Window.alert("y value must be greater than 14 and less than 36");
                				  }
                				  else {
                					 pPanel.hide();
                  				     config.moveCenterOfGravity(Integer.parseInt(xBox.getText()), Integer.parseInt(yBox.getText()));
                  				     modMap.renderMap(config.getConfigModList());
                  				     configList = config.getConfigModList();
                				  }
                				  
               			  }
                		  });
                		  change.add(submit);
                		  pPanel.add(change);
                	   }
                    });
                 southPanel.add(changeCenter);

               
               
               quality = new Label ("Configuration Quality: "+qualityInt);
               quality.setVisible(false);
               southPanel.add(quality);

           //*******************************************	   
           //NorthPanel
           //*******************************************
           Button logoutButton= new Button("Log Out", new ClickHandler() {
               public void onClick(ClickEvent event) {
            	Sound logout = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"sounds/logout.mp3");
   		        logout.play();
               	saveModules();
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
           endDate = TenDay.createTenDay();
           currentDate = new Date();
           VerticalPanel weatherPanel = new VerticalPanel();
           weatherPanel = makeWeatherMethod(); //within the weather class.
           
           FlowPanel tenPanel = new FlowPanel();
		   tenPanel.setWidth("200px");
		   final Grid tenAlert = new Grid(3,1);
		   
		   //shows when the next date you have to recalibrate is
		   FlowPanel topAlert = new FlowPanel();
		   topAlert.setWidth("200px");
		   topAlert = makeTopTenMethod(endDate);
		   
		   //creates the countdown to the next needed recalibrate date
		   FlowPanel middleAlert = new FlowPanel();
		   middleAlert.setWidth("200px");
		   middleAlert = makeMiddleTenMethod(currentDate, endDate);
		   
		   //Recalibrate button that stores the new 10 Day
		   //and resets the topAlert and middleAlert information
		   Button Recalibrate = new Button("Recalibrate");
		    Recalibrate.addClickHandler(new ClickHandler() {
		        public void onClick(ClickEvent event) {
		        	//updates and gets the new 10 day
		        	counter = 0;
		        	TenDay.tenDayUpdate();
		        	Date newTenDay = TenDay.getTenDay();
		        	Date currentDate = new Date();
		        	
		        	//shows when the next date you have to recalibrate is
		        	FlowPanel topAlert = new FlowPanel();
		  		    topAlert.setWidth("200px");
		  		    topAlert = makeTopTenMethod(newTenDay);
		  		    tenAlert.setWidget(0,0,topAlert);
		  		    
		  		    //creates the countdown to the next needed recalibrate date
		        	FlowPanel middleAlert = new FlowPanel();
		 		   	middleAlert.setWidth("200px");
		 		   	middleAlert = makeMiddleTenMethod(currentDate, newTenDay);
		            tenAlert.setWidget(1,0,middleAlert);
		            
		            //a popup that shows what the new recalibrate date is
		            PopupPanel pPanel = new PopupPanel();
		    		pPanel.setSize("10cm", "1cm");
		    		pPanel.setPopupPosition(500, 250);
		    		pPanel.setGlassEnabled(true);
		    		pPanel.setAutoHideEnabled(true);
		    		pPanel.setWidget(new Label(newTenDay.toString() + " is the new Update date"));
		    		pPanel.show();
		        }
		    });	   
		   
		    //gets the information onto the GUI
		    tenAlert.setWidget(0,0,topAlert);
		    tenAlert.setWidget(1,0,middleAlert);
		    tenAlert.setWidget(2,0,Recalibrate);
		    tenPanel.add(tenAlert);
           stackPanel.add(weatherPanel, new HTML("Weather"),1.5);
           stackPanel.add(tenPanel, new HTML("10-Day Alert"), 1.5);
           
         //*******************************************	   
           //Timer that updates the east panel every 30 seconds
           //Used to update the count down for the ten day alert
           //*******************************************
	          
           Timer t = new Timer() {
      			//@Override
      			public void run() {
      				Date newTenDay = TenDay.getTenDay();
      				Date currentDate = new Date();
      				FlowPanel topAlert = new FlowPanel();
      			    topAlert.setWidth("200px");
      			    topAlert = makeTopTenMethod(newTenDay);
      			    
		        	FlowPanel middleAlert = new FlowPanel();
		 		   	middleAlert.setWidth("200px");
		 		   	middleAlert = makeMiddleTenMethod(currentDate, newTenDay);
		            tenAlert.setWidget(1,0,middleAlert);
      			}
      			};
      			t.scheduleRepeating(30000);
      			
                  
           
           //*******************************************	   
           //Center
           //*******************************************
           //SimplePanel sHolder = new SimplePanel();
           //sHolder.setSize("1000px", "700px");
      		middlePanel.setWidth("42cm");      		
      		//creates a flextable for all of the modules to get added to
      		
      	   
    	    ScrollPanel modInfoScrollPanel = new ScrollPanel();
      	    modInfoScrollPanel.setSize("440px", "650px");
      	    modInfoScrollPanel.add(moduleInfo);
      	    
            sPanel.add(modMap.renderMap(moduleList)); //adds the map within a scroll panel to the center of the dock layout
           middlePanel.add(modInfoScrollPanel);
           middlePanel.add(sPanel);
           middlePanel.add(stackPanel);
           //*******************************************	   
           //Add to vertical container 
           //this is the main panel that gets placed onto the rootlayout
           //*******************************************
           container.add(northPanel);
           container.add(middlePanel);
           container.add(southPanel);
           
           StackLayoutPanel configPanel = new StackLayoutPanel(Unit.CM);
           configPanel = makeConfigPanel();
           
           checkMinConfig();
	   }

	private StackLayoutPanel makeConfigPanel() {
		StackLayoutPanel returnPanel = new StackLayoutPanel(Unit.CM);
		
		return returnPanel;
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
	private void addModuleMethod(ClickEvent event){
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
	    pPanel.setPopupPosition(event.getClientX(), event.getClientY() - 200);
	    pPanel.show();
	    //*****************************
	    //make button to add module
	    //*****************************
	    addModuleButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	        	 //PopupPanel temp1  = new PopupPanel();
			        //temp1.add(new Label(configNumb + "   " + xNumb + "   " + yNumb + "   " + orientNumb + "   " + conditionString));
			      	 //temp1.show();
		      	boolean addModuleSucess = modMaker.createModule(configNumb, xNumb, yNumb, orientNumb, conditionString);
		      	//this statement puts all the data collected in teh above editable fields and passes it into the moduleMaker
		      	if(addModuleSucess){
		      		//if sucessful play sound, update configuration possible and close popup panel
			        pPanel.hide();
			        modMap.renderMap(moduleList);
			        updateModuleInfo();
			        mapButton.setText("View Configuration Map");
			        saveModules();
	    	  		Sound added = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"sounds/added.mp3");
			        added.play();
	    	  	  }
          	  	  else{
          	  		  //if it's not sucessful, hid the popup panel, make a new popup that tells the user that they were unable
          	  		  //to add the module; show THIS popup panel. 
        	          pPanel.hide();
        	          Sound unableToAdd = soundController.createSound(Sound.MIME_TYPE_AUDIO_BASIC,"sounds/unable.mp3");
      		          unableToAdd.play();
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
	private void getModulesFromNASAESAFeed(ClickEvent event){
		final PopupPanel pPanel = new PopupPanel();
		VerticalPanel vPanel = new VerticalPanel();
		pPanel.setGlassEnabled(true);
		pPanel.setAutoHideEnabled(true);
		pPanel.setSize("3cm", "2cm");
		final ListBox testCases = new ListBox();
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
	    testCases.setVisibleItemCount(10);
        testCases.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				caseNumb = testCases.getSelectedIndex()+1;	
			}
        });
	    
	    //add tests cases to the list box, make a listener for the box; add this to the vertical panel
	    vPanel.add(testCases);
	    Button addModuleButton = new Button("Add");
	    vPanel.add(addModuleButton);
	    //make add module button; add it to the vertical panel. When add is clicked, it tries to connect
	    //with with the NASA/ESA feed. 
	    addModuleButton.addClickHandler(new ClickHandler() {
	    	//Integer caseNumber = testCases.getSelectedIndex()+1;
	        public void onClick(ClickEvent event) {
	        	String proxy = "http://www.d.umn.edu/~maddenj/Proxy.php?url=";
	 		   String url = proxy+"http://www.d.umn.edu/~abrooks/SomeTests.php?q="+(testCases.getSelectedIndex()+1);
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
	 			             boolean choice = Window.confirm("Pulling a test case will delete current module list. Are you sure you want to continue?");
	 			             if (choice) {
	 			            	 update(rt); //METHOD CALL TO DO SOMETHING WITH RESPONSE TEXT
		 				         modMap.renderMap(moduleList);
		 				         mapButton.setText("View Configuration Map");
		 			             pPanel.hide();
	 			             }
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
	    pPanel.setPopupPosition(event.getClientX(), event.getClientY() - 200);
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
		final ListBox chooseConfig = new ListBox();
		chooseConfig.addChangeHandler(new ChangeHandler() {
		public void onChange(ChangeEvent event) {
			choice = chooseConfig.getSelectedIndex();
			}
        });
		chooseConfig.addItem("Minimum 1");
		chooseConfig.addItem("Minimum 2");
		chooseConfig.addItem("Full 1");
		chooseConfig.addItem("Full 2");
		verticalChoicePanel.add(chooseConfig);
		Button chooseButton = new Button("Choose", new ClickHandler(){
			  public void onClick(ClickEvent event){
					
					//make configurations
					//Configuration config;
					ConfigurationBuilder cBuild = new ConfigurationBuilder(moduleList);
					MinConfigurationBuilder mcBuild = new MinConfigurationBuilder(moduleList);
					beforeList = moduleList;
					before.setVisible(true);

					//
					if(choice == 0){
						config = mcBuild.buildMinConfigOne();
						configList = config.getConfigModList();
						configNum = 0;
					}
					else if(choice == 1){
						config = mcBuild.buildMinConfigTwo();
						configList = config.getConfigModList();
						configNum = 1;
					}
					else if(choice == 2){
						config = cBuild.buildCupConfiguration();
						configList = config.getConfigModList();
						configNum = 2;
					}
					else{
						config = cBuild.buildCrossConfiguration();
						configList = config.getConfigModList();
						configNum = 3;
					}
					
					qualityInt = config.getQuality();
	                quality.setText("Configuration Quality: "+qualityInt);
	                quality.setVisible(true);
					saveConfig();
					modMap.renderMap(configList);
					mapButton.setText("View Module Map");
					
					pPanel.hide();

					final PopupPanel tempPP = new PopupPanel();
					VerticalPanel vPanelTemp = new VerticalPanel();
				    vPanelTemp.add(new Label("Configuraiton Made."));
				    vPanelTemp.add(new Label("Click on 'View Configuation Map' to view."));
			      	Button tempButton = new Button("OK", new ClickHandler(){
			      	public void onClick(ClickEvent event){
			      		 tempPP.hide();
			      	   }
			      	});
			        vPanelTemp.add(tempButton);
			        tempPP.add(vPanelTemp);
			        tempPP.center();
			      	tempPP.show();	
			  }
		  });
		verticalChoicePanel.add(chooseButton);
		layoutPPanel.addEast(verticalChoicePanel,5);
		
		StackLayoutPanel configsPanel = new StackLayoutPanel(Unit.CM);
		HorizontalPanel min = new HorizontalPanel();
		VerticalPanel min1 = new VerticalPanel();
		min1.add(new Label("Minimum 1"));
		Image min1Img = new Image("img/min1.png");
		min1Img.setPixelSize(275, 275);
		min1.add(min1Img);
		VerticalPanel min2 = new VerticalPanel();
		min2.add(new Label("Minimum 2"));
		Image min2Img = new Image("img/min2.png");
		min2Img.setPixelSize(275, 275);
		min2.add(min2Img);
		min.add(min1);
		min.add(min2);

		HorizontalPanel full = new HorizontalPanel();
		VerticalPanel full1 = new VerticalPanel();
		full1.add(new Label("Full 1"));
		Image full1Img = new Image("img/full-Cup.png");
		full1Img.setPixelSize(275, 275);
		full1.add(full1Img);
		VerticalPanel full2 = new VerticalPanel();
		full2.add(new Label("Full 2"));
		Image full2Img = new Image("img/full-Cross.png");
		full2Img.setPixelSize(275, 275);
		full2.add(full2Img);
		full.add(full1);
		full.add(full2);

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
	
	/**
	 * creates the information that shows the date that needs to be recalibrated on
	 * @param Date The end date to recalibrate on
	 * @return The flowpanel grid to be put into a grid
	 */
	private FlowPanel makeTopTenMethod(Date endDate) {
		FlowPanel fp = new FlowPanel(); 
		Grid top = new Grid(2,1);
	    top.setWidth("200px");
	    Label recalDate1 = new Label("Need to Recalibrate on: ");
	    recalDate1.setWidth("200px");
	    Label recalDate2 = new Label(endDate.toString());
	    recalDate2.setWidth("200px");
	    top.setWidget(0, 0, recalDate1);
	    top.setWidget(1, 0, recalDate2);
	    fp.add(top);
	    return fp;
	}
	
	/**
	 * creates the information that shows the countdown to when recalibration is needed
	 * @param Date, Date The current and end date
	 * @return The flowpanel grid to be put into a grid
	 */
	private FlowPanel makeMiddleTenMethod(Date currentDate, Date endDate) {
		FlowPanel fp = new FlowPanel();
		Grid g_details = new Grid(1,2);
        g_details.setWidth("200px");
		Grid left = new Grid(4,1);
        left.setWidth("100px");
        left.setWidget(0, 0, new Label("DAYS"));
        left.setWidget(1, 0, new Label("HOURS"));
        left.setWidget(2, 0, new Label("MINUTES"));
        left.setWidget(3, 0, new Label("SECONDS"));
        g_details.setWidget(0, 0, left);
        
		long diff = endDate.getTime() - currentDate.getTime();
        boolean recalibrate = isRecalibrate(diff);
        if (recalibrate) {
        	diff = 0;
        }
		Grid right = new Grid(4,1);
        right.setWidth("100px");
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        int diffInDays = (int) ((diff) / (1000 * 60 * 60 * 24));
        
        right.setWidget(0, 0, new Label(Integer.toString(diffInDays)));
        right.setWidget(1, 0, new Label(Long.toString(diffHours)));
        right.setWidget(2, 0, new Label(Long.toString(diffMinutes)));	
        right.setWidget(3, 0, new Label(Long.toString(diffSeconds)));
        
        g_details.setWidget(0, 1, right);
        fp.add(g_details);
        return fp;
        
	}

	/**
	 * checks to see if recalibration is needed
	 * @param none
	 * @return A boolean to say if recalibration is needed
	 */
	public boolean isRecalibrate(long diff) {
		boolean recalibrate = false;
        if (diff <= 0) {
        	counter ++;
        	recalibrate = true;
        	//this will make it so ten day alert does not keep bugging you
        	//only alerts you once
        	if (counter <= 1)
        		Window.alert("Ten Day Alert needs to be recalibrated");
        }
		return recalibrate;
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
    String status;
    double turns; 
    double x;
    double y;
    clearModuleList();
    moduleInfo.clear();
    moduleInfo.setText(0, 0, "Module Code");
	moduleInfo.setText(0, 1, "X-Coord");
    moduleInfo.setText(0, 2, "Y-Coord");
    moduleInfo.setText(0, 3, "Condition");
    moduleInfo.setText(0, 4, "Rotations");
    moduleInfo.setText(0, 5, "Remove");
    Integer sucessCounter = 0;
    int row = moduleInfo.getRowCount();
    for (int i = 0; i < jA.size(); i++) {
    	final int code; 
    	JSONObject jO = (JSONObject)jA.get(i);
    	jN = (JSONNumber) jO.get("code");
    	code = (int) jN.doubleValue();
    	moduleInfo.setText(row, 0, Integer.toString((int) code));
    	moduleArray.add(Integer.toString(code));
    	jS = (JSONString) jO.get("status");
    	status = jS.stringValue();
    	moduleInfo.setText(row, 3, status);
    	jN = (JSONNumber) jO.get("turns");
    	turns = jN.doubleValue();
    	moduleInfo.setText(row, 4, Integer.toString((int) turns));
    	jN = (JSONNumber) jO.get("X");
    	x = jN.doubleValue();
    	moduleInfo.setText(row, 1, Integer.toString((int) x));
    	jN = (JSONNumber) jO.get("Y");
    	y = jN.doubleValue();
    	moduleInfo.setText(row, 2, Integer.toString((int) y));
    	Button removeModuleButton = new Button("x");
	        removeModuleButton.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  boolean delete = Window.confirm("Are you sure you wish to delete this module?");
	        	  if (delete) {
	  	        	  int removeTarget = moduleArray.indexOf(Integer.toString(code));
	  	        	  int t = Integer.parseInt(moduleInfo.getText(removeTarget + 1, 0));
	  	    		  Module target = moduleList.getModuleByIdNumber(t);
	  	  		      moduleList.removeModule(target);
	  	        	  moduleArray.remove(removeTarget);
	  	              moduleInfo.removeRow(removeTarget + 1);
	  	              modMap.renderMap(moduleList);
	  	              saveModules();
	  	              checkMinConfig();
	        	  }
	        	  else {
	        		  Window.alert("Deletion cancelled");
	        	  }
	          }
	        });
	        moduleInfo.setWidget(row, 5, removeModuleButton);
      	boolean addModuleSucess = modMaker.createModule((int)code, (int)x, (int)y, (int)turns, status);
      	if(addModuleSucess){
      		sucessCounter++;
      		row++;
      		saveModules();
    	}
    	else {
    		  int removeTarget = moduleArray.indexOf(Integer.toString(code));
        	  int t = Integer.parseInt(moduleInfo.getText(removeTarget + 1, 0));
    		  Module target = moduleList.getModuleByIdNumber(t);
  		      moduleList.removeModule(target);
        	  moduleArray.remove(removeTarget);
              moduleInfo.removeRow(removeTarget + 1);
    	}
    }
    final PopupPanel sucesses = new PopupPanel();
    VerticalPanel tempSucesses = new VerticalPanel();
    tempSucesses.add(new Label(sucessCounter + " modules added successfully."));
    Button tempButton = new Button("OK", new ClickHandler(){
		  public void onClick(ClickEvent event){
			  sucesses.hide();
		  }
	  });
    tempSucesses.add(tempButton);
    sucesses.add(tempSucesses);
    checkMinConfig();
    sucesses.center();
    sucesses.show();
    }
	
	/**
	 * Saves current modules to local store
	 */
	public void saveModules() {
		moduleStore = Storage.getLocalStorageIfSupported();
		if(moduleStore != null) {
			moduleStore.setItem("modules", moduleList.toJSONString());
		}
	}
	
	/**
	 * Saves current configuration
	 */
	public void saveConfig() {
		moduleStore = Storage.getLocalStorageIfSupported();
		if(moduleStore != null) {
			moduleStore.setItem("configNum", "" + configNum);
			moduleStore.setItem("config", configList.toJSONString());
			//moduleStore.setItem("before", beforeList.toJSONString());
		}
	}
	
	/**
	 * Loads saved configuration
	 */
	public void loadConfig() {
		moduleStore = Storage.getLocalStorageIfSupported();
		if(moduleStore != null) {
			String num = moduleStore.getItem("configNum");
			String configString = moduleStore.getItem("config");
			//String beforeString = moduleStore.getItem("before");
			if(configString != null && configString != "[]") {
				JSONArray jA = (JSONArray) JSONParser.parseLenient(configString);
				JSONNumber jN;
				JSONString jS;
				ModuleMaker make = new ModuleMaker(configList);
				int xCoordinate, yCoordinate, turnsToUpright;
				String condition = null;
				for(int i = 0; i < jA.size(); i++) {
					final int idNumber;
					JSONObject jO = (JSONObject) jA.get(i);
					jN = (JSONNumber) jO.get("code");
					idNumber = (int) jN.doubleValue();
					moduleArray.add(Integer.toString(idNumber));
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
				/*
				if(beforeString != null && beforeString != "[]") {
					JSONArray jA2 = (JSONArray) JSONParser.parseLenient(beforeString);
					JSONNumber jN2;
					JSONString jS2;
					ModuleMaker make2 = new ModuleMaker(beforeList);
					int xCoordinate2, yCoordinate2, turnsToUpright2;
					String condition2 = null;
					for(int i = 0; i < jA2.size(); i++) {
						final int idNumber2;
						JSONObject jO2 = (JSONObject) jA2.get(i);
						jN2 = (JSONNumber) jO2.get("code");
						idNumber2 = (int) jN2.doubleValue();
						moduleArray.add(Integer.toString(idNumber2));
						jS2 = (JSONString) jO2.get("status");
						condition = jS2.stringValue();
						jN2 = (JSONNumber) jO2.get("turns");
						turnsToUpright2 = (int) jN2.doubleValue();
						jN2 = (JSONNumber) jO2.get("X");
						xCoordinate2 = (int) jN2.doubleValue();
						jN2 = (JSONNumber) jO2.get("Y");
						yCoordinate2 = (int) jN2.doubleValue();
						make.createModule(idNumber2, xCoordinate2, yCoordinate2, turnsToUpright2, condition2);
					}
				}
				*/
				ConfigurationBuilder cBuild = new ConfigurationBuilder(configList);
				MinConfigurationBuilder mcBuild = new MinConfigurationBuilder(configList);
				if(num == "0"){
					config = mcBuild.buildMinConfigOne();
				}
				else if(num == "1"){
					config = mcBuild.buildMinConfigTwo();
				}
				else if(num == "2"){
					config = cBuild.buildCupConfiguration();
				}
				else if (num == "3"){
					config = cBuild.buildCrossConfiguration();
				}
				//before.setVisible(true);
				//qualityInt = config.getQuality();
				//quality.setVisible(true);
			}
		}
	}
					
	/**
	 * Loads Modules from local store onto the module list
	 */
	public void loadModules() {
		moduleStore = Storage.getLocalStorageIfSupported();
		if(moduleStore != null) {
			String modules = moduleStore.getItem("modules");
			if(modules != null) {
				JSONArray jA = (JSONArray) JSONParser.parseLenient(modules);
				JSONNumber jN;
				JSONString jS;
				ModuleMaker make = new ModuleMaker(moduleList);
				int xCoordinate, yCoordinate, turnsToUpright;
				String condition = null;
				int row = moduleInfo.getRowCount();
				for(int i = 0; i < jA.size(); i++) {
					final int idNumber;
					JSONObject jO = (JSONObject) jA.get(i);
					jN = (JSONNumber) jO.get("code");
					idNumber = (int) jN.doubleValue();
					moduleArray.add(Integer.toString(idNumber));
					moduleInfo.setText(row, 0, Integer.toString(idNumber));
					jS = (JSONString) jO.get("status");
					condition = jS.stringValue();
					moduleInfo.setText(row, 3, condition);
					jN = (JSONNumber) jO.get("turns");
					turnsToUpright = (int) jN.doubleValue();
					moduleInfo.setText(row, 4, Integer.toString(turnsToUpright));
					jN = (JSONNumber) jO.get("X");
					xCoordinate = (int) jN.doubleValue();
					moduleInfo.setText(row, 1, Integer.toString(xCoordinate));
					jN = (JSONNumber) jO.get("Y");
					yCoordinate = (int) jN.doubleValue();
					moduleInfo.setText(row, 2, Integer.toString(yCoordinate));
					make.createModule(idNumber, xCoordinate, yCoordinate, turnsToUpright, condition);
					Button removeModuleButton = new Button("x");
		  	        removeModuleButton.addClickHandler(new ClickHandler() {
		  	          public void onClick(ClickEvent event) {
		  	        	boolean delete = Window.confirm("Are you sure you wish to delete this module?");
		  	        	  if (delete) {
			  	        	  int removeTarget = moduleArray.indexOf(Integer.toString(idNumber));
			  	        	  int t = Integer.parseInt(moduleInfo.getText(removeTarget + 1, 0));
			  	    		  Module target = moduleList.getModuleByIdNumber(t);
			  	  		      moduleList.removeModule(target);
			  	        	  moduleArray.remove(removeTarget);
			  	              moduleInfo.removeRow(removeTarget + 1);
			  	              modMap.renderMap(moduleList);
			  	              mapButton.setText("View Configuration Map");
			  	              checkMinConfig();
			  	              saveModules();
		  	        	  }
		  	        	  else {
		  	        		  Window.alert("Deletion cancelled");
		  	        	  }
		  	          }
		  	        });
		  	        moduleInfo.setWidget(row, 5, removeModuleButton);
		  	        row++;
				}
			}
		}
		
	}
	
	private void checkMinConfig() {
		if (new counters(moduleList).minConfigPossible()){
			configPoss.getElement().getStyle().setColor("green");
			configPoss.setText("minimum configuration possible!");
			getConfigs.setEnabled(true);
		}
		else{
			configPoss.getElement().getStyle().setColor("red");
			configPoss.setText("minimum configuration NOT possible");
			getConfigs.setEnabled(false);
		}
	}	
	private void updateModuleInfo() {
		final int row = moduleInfo.getRowCount();
        final int conNumb = configNumb;
        final int xCord = xNumb;
        final int yCord = yNumb;
        final String condString = conditionString;
        final int oNumb = orientNumb;
        moduleArray.add(configNumb.toString());
        //adds the module info into the flextable and adds a button to remove it.
        //the modulearray is used to store modules for the moduleInfo FlexTable.
        //the moduleList class does not return an integer location of the module.
        //that is the reason for the modulearray.
        //This same logic is used in the update method for getting NasaEsa information
        //and on loading modules.
        moduleInfo.setText(row, 0, Integer.toString(conNumb));
  	    moduleInfo.setText(row, 1, Integer.toString(xCord));
  	    moduleInfo.setText(row, 2, Integer.toString(yCord));
  	    moduleInfo.setText(row, 3, condString);
  	    moduleInfo.setText(row, 4, Integer.toString(oNumb));
  	    Button removeModuleButton = new Button("x");
	        removeModuleButton.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  boolean delete = Window.confirm("Are you sure you wish to delete this module?");
	        	  if (delete) {
	  	        	  int removeTarget = moduleArray.indexOf(Integer.toString(conNumb));
	  	        	  int t = Integer.parseInt(moduleInfo.getText(removeTarget + 1, 0));
	  	    		  Module target = moduleList.getModuleByIdNumber(t);
	  	  		      moduleList.removeModule(target);
	  	  		      //modMaker.remove(target.getIdNumber());
	  	        	  moduleArray.remove(removeTarget);
	  	              moduleInfo.removeRow(removeTarget + 1);
	  	              modMap.renderMap(moduleList);
	  	              saveModules();
	  	              checkMinConfig();
	        	  }
	        	  else {
	        		  Window.alert("Deletion cancelled");
	        	  }
	          }
	        });
	        moduleInfo.setWidget(row, 5, removeModuleButton);
  	    
	        checkMinConfig();
	}
	
	public void clearModuleList() {
		 moduleList.clearList();
		 moduleArray.clear();
		 moduleInfo.removeAllRows();
		 modMap.renderMap(moduleList);
		 modMaker.clear();
		 configList.clearList();
		 saveConfig();
		 saveModules();
	}
}
