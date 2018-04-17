import java.awt.*;
import java.awt.image.ImageConsumer;
import java.applet.*;
import java.util.* ;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.SwingConstants;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RuleApplet extends Applet {
	public RuleApplet() {
		setEnabled(true);
		setBackground(new Color(230, 230, 250));
	}
	 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////user selected a rule base
 
private static final long serialVersionUID = 1L;

void choice1_Clicked() {
       String rbName = choice1.getSelectedItem() ;
       choice2.removeAll(); 
       textArea1.setForeground(Color.BLUE);
       textArea2.setForeground(Color.BLUE);
       textArea3.setForeground(Color.BLUE);
       
       textArea3.setText("");   // pour le champ des variables
       textArea2.setText("");	// pour le champes de moteur d'inférence
       textArea1.setText("");  // pour le champ de la base de regles
       
       choice3.removeAll(); 
       if (rbName.equals("Vehicule"))         { currentRuleBase = vehicule;         choice11.setVisible(false); choice1.reshape(90,12,192,24); }
       if (rbName.equals("Vendeur"))          { currentRuleBase = vendeur;          choice11.setVisible(false); choice1.reshape(90,12,192,24); }
//       if (rbName.equals("Acheteur"))         { currentRuleBase = acheteur;         choice1.reshape(90,12,125,24); choice11.setVisible(true);  }
       if (rbName.equals("Acheteur"))         { currentRuleBase = acheteur;         choice11.setVisible(false);  choice1.reshape(90,12,192,24);   }

       currentRuleBase.reset() ;  // reset the rule base
       Enumeration vars = currentRuleBase.variableList.elements() ;
       while (vars.hasMoreElements()) {
         choice2.addItem(((RuleVariable)vars.nextElement()).name) ;
       }
       currentRuleBase.displayVariables(textArea3) ;
       currentRuleBase.displayRules(textArea1) ;
	
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////user selected a variable
          
void choice11_Clicked() {
	       String rbName = choice11.getSelectedItem() ;
	       textArea2.setText("");
	       textArea1.setText("");
	       choice3.removeAll();
	       if (rbName.equals("Vendeur")){ currentRuleBase = vendeur; choice11.setVisible(false); }
	       currentRuleBase.reset() ;  // reset the rule base
	       currentRuleBase.displayRules(textArea1) ;
		}	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////user selected a variable
			
void choice2_Clicked(Event event) {
       String varName = choice2.getSelectedItem() ;
       choice3.removeAll() ;
       RuleVariable rvar = (RuleVariable)currentRuleBase.variableList.get(varName);
     
	Enumeration labels = rvar.labels.elements();
       while (labels.hasMoreElements()) {
          choice3.addItem(((String)labels.nextElement())) ;
       }
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////user selected a value for a variable
    
void choice3_Clicked(Event event) {
       String varName = choice2.getSelectedItem() ;
       String varValue = choice3.getSelectedItem() ;

       RuleVariable rvar = (RuleVariable)currentRuleBase.variableList.get(varName);
       rvar.setValue(varValue) ;
       textArea3.appendText("\n" + rvar.name + " set to " + varValue) ;
	}

////////////////////////////////////////////////////////////////////////////////

void button1_Clicked(Event event) {
       String goal = textField1.getText() ;
       textArea2.setText("\n --- Starting Inferencing Cycle --- \n");      
       textArea2.setForeground(Color.BLUE);
       textArea1.setForeground(Color.red);
       textArea3.setForeground(Color.red);

       currentRuleBase.displayVariables(textArea2) ;
       if (radioButton1.getState() == true) currentRuleBase.forwardChain();
	   if (radioButton2.getState() == true) currentRuleBase.backwardChain(goal);
           
       currentRuleBase.displayVariables(textArea2) ;
      // textArea.setForeground(Color.BLUE);
       textArea2.appendText("\n --- Ending Inferencing Cycle --- \n");
	}

///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

void button3_Clicked(Event event) {

		textArea1.setText("");
		textArea2.setText("");
		textArea3.setText("");
		   textArea1.setForeground(Color.blue);
		   textArea3.setForeground(Color.blue);
        currentRuleBase.reset() ;
        currentRuleBase.displayRules(textArea1);
        currentRuleBase.displayVariables(textArea3) ;
	}

///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////display dialog to get user value for a variable
    
static public String waitForAnswer(String prompt, String labels) {
	// position dialog over parent dialog
    Point p = frame.getLocation() ;
    dlg = new RuleVarDialog(frame, true) ;
	dlg.label1.setText("   " + prompt + " (" + labels + ")");
    dlg.setLocation(400, 250) ;
    dlg.show() ;
    String ans = dlg.getText() ;
    return ans ;

}

///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

public void init() {
		super.init();

		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(624,527);
		button1 = new java.awt.Button("Find Goal");
		button1.reshape(228,468,108,38);
		add(button1);
		button3 = new java.awt.Button("Reset");
		button3.reshape(444,468,108,38);
		add(button3);
		textArea1 = new java.awt.TextArea();
		textArea1.setBackground(new Color(240, 255, 240));
		textArea1.reshape(12,48,312,144);
		add(textArea1);
		textArea2 = new java.awt.TextArea();
		textArea2.setBackground(new Color(240, 255, 240));
		textArea2.reshape(12,216,600,168);
		add(textArea2);
		label2 = new java.awt.Label("Trace Log");
		label2.reshape(24,192,168,24);
		add(label2);
		label1 = new java.awt.Label("Rule Base");
		label1.reshape(24,12,59,24);
		add(label1);
		
		choice1 = new java.awt.Choice();
		choice1.setBackground(new Color(245, 222, 179));
		add(choice1);
		choice1.reshape(90,12,192,24);
		
		choice11 = new java.awt.Choice();
		add(choice11);
		//choice1.reshape(132,12,137,24);
		choice11.reshape(220,12,100,24);
		choice11.setVisible(false);        
		
		Group1 = new CheckboxGroup();
		radioButton1 = new java.awt.Checkbox("Forward Chain", Group1, false);
		radioButton1.setBackground(new Color(220, 220, 220));
		radioButton1.reshape(36,396,156,21);
		add(radioButton1);
		choice3 = new java.awt.Choice();
		choice3.setBackground(new Color(245, 222, 179));
		add(choice3);
		choice3.reshape(480,36,135,24);
		label5 = new java.awt.Label("Value");
		label5.reshape(480,12,95,24);
		add(label5);
		choice2 = new java.awt.Choice();
		choice2.setBackground(new Color(245, 222, 179));
		add(choice2);
		choice2.reshape(336,36,137,24);
		textArea3 = new java.awt.TextArea();
		textArea3.setBackground(new Color(240, 255, 240));
		textArea3.reshape(336,72,276,122);
		add(textArea3);
		label4 = new java.awt.Label("Variable");
		label4.reshape(336,12,109,24);
		add(label4);
		radioButton2 = new java.awt.Checkbox("Backward Chain", Group1, false);
		radioButton2.setBackground(new Color(220, 220, 220));
		radioButton2.reshape(36,420,156,24);
		add(radioButton2);
		textField1 = new java.awt.TextField();
		textField1.setBackground(new Color(240, 248, 255));
		textField1.reshape(324,420,142,27);
		add(textField1);
		label3 = new java.awt.Label("Goal");
		label3.reshape(324,384,80,30);
		
		panel = new JPanel();
		panel.setBounds(-43, -150, 761, 634);
		add(panel);
		panel.setLayout(null);
		add(label3);
		
		textArea1.setEditable(false);
		textArea2.setEditable(false);
		textArea3.setEditable(false);
	 	
		//}}

	 	frame = new JFrame("Ask User") ;
	 	frame.resize(50,50) ;
	 	frame.setLocation(100,100) ;
	 	choice1.addItem("Vehicule") ;
	 	choice1.addItem("Vendeur");
        choice1.addItem("Acheteur");
        
	    vehicule = new RuleBase("Vehicule") ;
	    vehicule.setDisplay(textArea2) ;
	 	initvehiculeRuleBase(vehicule) ;
        
        vendeur = new RuleBase("Vendeur") ;
	    vendeur.setDisplay(textArea2);
	    initVendeur_AgenceVoyage_RuleBase(vendeur);
        
	    acheteur = new RuleBase("Acheteur") ;
	    acheteur.setDisplay(textArea2);
	    initAcheteurRuleBase(acheteur);

        // initialize textAreas and list controls
	    currentRuleBase =vendeur;
	    currentRuleBase.displayRules(textArea1) ;
        currentRuleBase.displayVariables(textArea3) ;
        radioButton1.setState(true) ;
        
        //ProgressSample p = new ProgressSample();
        choice1_Clicked() ; // fill variable list
       
        


	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


public boolean handleEvent(Event event) {
		if (event.target == button1 && event.id == Event.ACTION_EVENT) {
			button1_Clicked(event);
				
			//ProgressSample.initProgress(frame);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ProgressSample s = new ProgressSample();
					s.startProg(0,100,frame);
				}
			}).start();
			
			
			return true;}
		
		if (event.target == button3 && event.id == Event.ACTION_EVENT) {
			button3_Clicked(event);
			return true;}
		
		if (event.target == dlg && event.id == Event.ACTION_EVENT) {
			return dlg.handleEvent(event);}
	    
		if (event.target == choice1 && event.id == Event.ACTION_EVENT) {
			choice1_Clicked();
			return true;}
	    
		if (event.target == choice11 && event.id == Event.ACTION_EVENT) {
			choice11_Clicked();
			return true;}
	    
		if (event.target == choice2 && event.id == Event.ACTION_EVENT) {
			choice2_Clicked(event);
			return true;}
	    
		if (event.target == choice3 && event.id == Event.ACTION_EVENT) {
			choice3_Clicked(event);
			return true;}
		return super.handleEvent(event);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////DECLARE_CONTROLS

	java.awt.Button button1;
	java.awt.Button button3;
	java.awt.TextArea textArea1;
	java.awt.TextArea textArea2;
	java.awt.Label label2;
	java.awt.Label label1;
	java.awt.Choice choice1;
	java.awt.Checkbox radioButton1;
	CheckboxGroup Group1;
	java.awt.Choice choice3;
	java.awt.Choice choice11;
	java.awt.Label label5;
	java.awt.Choice choice2;
	java.awt.TextArea textArea3;
	java.awt.Label label4;
	java.awt.Checkbox radioButton2;
	java.awt.TextField textField1;
	java.awt.Label label3;
	
	static JFrame frame ;
	static RuleVarDialog dlg ;
    static RuleBase vehicule ;
    static RuleBase acheteur;
    static RuleBase vendeur;
    static RuleBase currentRuleBase ;
    private JProgressBar progressBar;
    private JPanel panel;
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////initialize the vehicule rule base (9) 

public void initvehiculeRuleBase(RuleBase rb) {
     rb.goalClauseStack = new Stack() ; // goals and subgoals
     rb.variableList = new Hashtable() ;
      
     RuleVariable vehicle = new RuleVariable("vehicle") ;
     vehicle.setLabels("Bicycle Tricycle MotorCycle Sports_Car Sedan MiniVan Sports_Utility_Vehicle") ;
     vehicle.setPromptText("quel est le type de vehicule?");
     rb.variableList.put(vehicle.name,vehicle) ;

     RuleVariable vehicleType = new RuleVariable("vehicleType") ;
     vehicleType.setLabels("cycle Automobile") ;
     vehicleType.setPromptText("What type of vehicle is it?") ;
     rb.variableList.put(vehicleType.name, vehicleType) ;

     RuleVariable size = new RuleVariable("size") ;
     size.setLabels("small medium large") ;
     size.setPromptText("What size is the vehicle?") ;
     rb.variableList.put(size.name,size) ;

     RuleVariable motor = new RuleVariable("motor") ;
     motor.setLabels("yes no") ;
     motor.setPromptText("Does the vehicle have a motor?") ;
     rb.variableList.put(motor.name,motor) ;

     RuleVariable num_wheels = new RuleVariable("num_wheels") ;
     num_wheels.setLabels("2 3 4") ;
     num_wheels.setPromptText("How many wheels does it have?");
     rb.variableList.put(num_wheels.name,num_wheels) ;

     RuleVariable num_doors = new RuleVariable("num_doors") ;
     num_doors.setLabels("2 3 4") ;
     num_doors.setPromptText("How many doors does it have?") ;
     rb.variableList.put(num_doors.name,num_doors) ;

////////////////////////////////////////////////////////////////// Note: at this point all variables values are NULL

    Condition cEquals = new Condition("=") ;
    Condition cNotEquals = new Condition("!=") ;
    Condition cLessThan = new Condition("<") ;

////////////////////////////////////////////////////////////////////////////////////////////////////////define rules

    rb.ruleList = new Vector() ;
    
    Rule Bicycle = new Rule(rb, "bicycle",
         new Clause(vehicleType,cEquals, "cycle")  ,
         new Clause(num_wheels,cEquals, "2"),
         new Clause(motor, cEquals, "no"),
         new Clause(vehicle, cEquals, "Bicycle")) ;

    Rule Tricycle = new Rule(rb, "tricycle",
         new Clause(vehicleType,cEquals, "cycle")  ,
         new Clause(num_wheels,cEquals, "3"),
         new Clause(motor, cEquals, "no"),
         new Clause(vehicle, cEquals, "Tricycle")) ;

    Rule Motorcycle = new Rule(rb, "motorcycle",
         new Clause(vehicleType,cEquals, "cycle")  ,
         new Clause(num_wheels,cEquals, "2"),
         new Clause(motor,cEquals, "yes"),
         new Clause(vehicle,cEquals, "Motorcycle")) ;

    Rule SportsCar = new Rule(rb, "sportsCar",
         new Clause(vehicleType,cEquals, "Automobile")  ,
         new Clause(size,cEquals, "small"),
         new Clause(num_doors,cEquals, "2"),
         new Clause(vehicle,cEquals, "Sports_Car")) ;

    Rule Sedan = new Rule(rb, "sedan",
         new Clause(vehicleType,cEquals, "Automobile")  ,
         new Clause(size,cEquals, "medium"),
         new Clause(num_doors,cEquals, "4"),
         new Clause(vehicle,cEquals, "Sedan")) ;

    Rule MiniVan = new Rule(rb, "miniVan",
         new Clause(vehicleType,cEquals, "Automobile")  ,
         new Clause(size,cEquals, "medium"),
         new Clause(num_doors,cEquals, "3"),
         new Clause(vehicle,cEquals, "MiniVan")) ;

    Rule SUV = new Rule(rb, "SUV",
         new Clause(vehicleType,cEquals, "Automobile")  ,
         new Clause(size,cEquals, "large"),
         new Clause(num_doors,cEquals, "4"),
         new Clause(vehicle,cEquals, "Sports_Utility_Vehicle")) ;

    Rule Cycle = new Rule(rb, "Cycle",
         new Clause(num_wheels,cLessThan, "4")  ,
         new Clause(vehicleType,cEquals, "cycle")) ;

    Rule Automobile = new Rule(rb, "Automobile",
         new Clause(num_wheels,cEquals, "4")  ,
         new Clause(motor,cEquals, "yes"),
         new Clause(vehicleType,cEquals, "Automobile")) ;

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////initialize the first seller rule base (30)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////initialize the second seller rule base (30)

public void initVendeur_AgenceVoyage_RuleBase(RuleBase rb) {
    rb.goalClauseStack = new Stack() ;
    rb.variableList = new Hashtable() ;

RuleVariable Destination = new RuleVariable("Destination") ;
Destination.setLabels("Qatar Paris New_York Tunisie Oran");
Destination.setPromptText("donnez la destination désirée?");
rb.variableList.put(Destination.name,Destination) ;

RuleVariable Prix = new RuleVariable("Prix");
Prix.setLabels("380000.00 700000.00 40000.00 51000.00 29900.00 530000.00 380000.00 700000.00 50000.00");
Prix.setPromptText(" Quel est votre Prix ?");
rb.variableList.put(Prix.name,Prix);

RuleVariable Hotel = new RuleVariable("Hotel");
Hotel.setLabels("HotelHotelMatrixx HotelHotelNippon");
Hotel.setPromptText(" Quel est Hotel ?");
rb.variableList.put(Hotel.name,Hotel);

RuleVariable Periode = new RuleVariable("Période") ;
Periode.setLabels("ETE HIVER PRINTEMPS");
Periode.setPromptText(" Quel période ?");
rb.variableList.put(Periode.name,Periode) ;

RuleVariable Duree = new RuleVariable("Durée") ;
Duree.setLabels("10jours 15Jours 45jours");
Duree.setPromptText(" Combien durée ?");
rb.variableList.put(Duree.name,Duree) ;

RuleVariable Visa = new RuleVariable("Visa") ;
Visa.setLabels("accepté refusé");
Visa.setPromptText(" Est ce qu'il y a Visa ?");
rb.variableList.put(Visa.name,Visa) ;

RuleVariable Demande_Visa = new RuleVariable("Demande_Visa") ;
Demande_Visa.setLabels("accepté refusé");
Demande_Visa.setPromptText(" Est ce que votre demande de visa a été accordé ?");
rb.variableList.put(Demande_Visa.name,Demande_Visa) ;

RuleVariable place_disponible = new RuleVariable("place_disponible");
place_disponible.setLabels("Oui Non");
place_disponible.setPromptText(" Est ce que vous disposez place ?");
rb.variableList.put(place_disponible.name,place_disponible) ;

RuleVariable Vol = new RuleVariable("Vol") ;
Vol.setLabels("National International");
Vol.setPromptText(" Quel est votre Vol ?");
rb.variableList.put(Vol.name,Vol);

RuleVariable Choix = new RuleVariable("Choix");
Choix.setLabels("Oui Non");
Choix.setPromptText(" Est ce que vous choisissez ?");
rb.variableList.put(Choix.name,Choix);

RuleVariable Reservation = new RuleVariable("Reservation");
Reservation.setLabels("Oui Non");
Reservation.setPromptText(" Est ce que vous voulez reserver ?");
rb.variableList.put(Reservation.name,Reservation);

RuleVariable Reduction = new RuleVariable("Reduction");
Reduction.setLabels("Oui Non");
Reduction.setPromptText(" Est ce qu'il y a une Reduction ?");
rb.variableList.put(Reduction.name,Reduction);
    
    // Note: at this point all variables values are NULL

    Condition cEquals = new Condition("=") ;
    Condition cNotEquals = new Condition("!=") ;
    Condition cLessThan = new Condition("<") ;
    Condition cMoreThan = new Condition(">") ;

    // define rules
   rb.ruleList = new Vector() ;
   
//36//////////////////////////////////////////////////Essence
   Rule rule17 = new Rule(rb, "prix_New_York",
			new Clause(Destination,cEquals,"New_York")  ,
			  new Clause(Duree,cEquals,"10jours"),
		      new Clause(Periode, cEquals, "ETE"),
		      new Clause(Visa, cEquals, "accepté"),
		      new Clause(Prix,cEquals,"700000.00")
		     );

	Rule rule18 = new Rule(rb, "prix_Paris",
			new Clause(Destination,cEquals,"Paris")  ,
			new Clause(Hotel,cEquals,"HotelMatrix"),
			  new Clause(Duree,cEquals,"45jours"),
		      new Clause(Periode, cEquals, "ETE"),
		      new Clause(Visa, cEquals, "accepté"),
		      new Clause(Reduction, cEquals, "Oui"),
		      new Clause(Prix,cEquals,"40000.00")
		     );

	Rule rule19 = new Rule(rb, "prix_Paris2",
			new Clause(Destination,cEquals,"Paris")  ,
			new Clause(Hotel,cEquals,"HotelNippon"),
			  new Clause(Duree,cEquals,"15Jours"),
		      new Clause(Periode, cEquals, "ETE"),
		      new Clause(Visa, cEquals, "accepté"),
		      new Clause(Reduction, cEquals, "Non"),
		      new Clause(Prix,cEquals,"40000.00")
		     );

	Rule rule20 = new Rule(rb, "prix_Tunisie",
			new Clause(Destination,cEquals,"Tunisie")  ,
			  new Clause(Duree,cEquals,"15Jours"),
		      new Clause(Periode, cEquals, "ETE"),
		      new Clause(Visa, cEquals, "refusé"),
		      new Clause(Reduction, cEquals, "Oui"),
		      new Clause(Prix,cEquals,"51000.00")
		     );

	Rule rule21 = new Rule(rb, "prix_Oran",
			new Clause(Destination,cEquals,"Oran")  ,
			  new Clause(Vol,cEquals,"National"),
		      new Clause(Periode, cEquals, "HIVER"),
		      new Clause(Prix,cEquals,"29900.00")
		     );
Rule rule1 = new Rule(rb, "reserve_Qatar_yes",
	  new Clause(Destination,cEquals,"Qatar")  ,
      new Clause(Periode,cEquals,"ETE"),
      new Clause(Duree,cEquals,"10jours"),
      new Clause(Visa, cEquals, "accepté"),
      new Clause(Prix,cEquals,"380000.00"),
      new Clause(Choix,cEquals,"Oui"),
      new Clause(place_disponible, cEquals, "Oui"),
      new Clause(Reservation, cEquals, "Oui")
      );

Rule rule2 = new Rule(rb, "reserve_Tunisie_no",
		new Clause(Destination,cEquals,"Tunisie")  ,
	    new Clause(Periode,cEquals,"ETE"),
	    new Clause(Duree,cEquals,"15Jours"),
	    new Clause(Visa, cEquals, "refusé"),
	    new Clause(Prix,cEquals,"51000.00"),
	    new Clause(Choix,cEquals,"Non"),
	    new Clause(Reduction, cEquals, "Non"),
	    new Clause(Reservation, cEquals, "Non")
	    );

Rule rule3 = new Rule(rb, "reserve_Paris_no",
		new Clause(Destination,cEquals,"Paris")  ,
		new Clause(Hotel,cEquals,"HotelMatrix"),
	      new Clause(Duree,cEquals,"45jours"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Prix,cEquals,"40000.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Non"),
	      new Clause(Reservation, cEquals, "Non")
	      );

Rule rule4 = new Rule(rb, "reserve_Paris_yes",
		new Clause(Destination,cEquals,"Paris")  ,
		new Clause(Hotel,cEquals,"HotelNippon"),
	      new Clause(Duree,cEquals,"15Jours"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Reduction,cEquals,"Non"),
	      new Clause(Prix,cEquals,"50000.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Oui"),
	      new Clause(Reservation, cEquals, "Oui")
	      );

Rule rule5 = new Rule(rb, "reserve_New_York_no",
		new Clause(Destination,cEquals,"New_York")  ,
	      new Clause(Duree,cEquals,"10jours"),
	      new Clause(Periode,cEquals,"ETE"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Prix,cEquals,"700000.00"),
	      new Clause(Choix,cEquals,"Non"),
	      new Clause(Reduction, cEquals, "Non"),
	      new Clause(Reservation, cEquals, "Non")
	      );

Rule rule6 = new Rule(rb, "reserve_Oran_yes",
		new Clause(Destination,cEquals,"Oran")  ,
		new Clause(Vol,cEquals,"National"),
	      new Clause(Periode,cEquals,"HIVER"),
	      new Clause(Reduction,cEquals,"Oui"),
	      new Clause(Prix,cEquals,"29900.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Oui"),
	      new Clause(Reservation, cEquals, "Oui")
	      );

Rule rule7 = new Rule(rb, "pdipo_Qatar_yes",
		new Clause(Destination,cEquals,"Qatar")  ,
	      new Clause(Periode,cEquals,"ETE"),
	      new Clause(Duree,cEquals,"10jours"),
	      new Clause(Visa,cEquals,"accepté"),
	      new Clause(Prix,cEquals,"29900.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Oui")
	      );

Rule rule8 = new Rule(rb, "pdipo_Oran_yes",
		new Clause(Destination,cEquals,"Oran")  ,
		new Clause(Vol,cEquals,"National"),
	      new Clause(Periode,cEquals,"HIVER"),
	      new Clause(Reduction,cEquals,"Oui"),
	      new Clause(Prix,cEquals,"29900.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Oui")
	      );

Rule rule9 = new Rule(rb, "pdipo_Paris_no",
		new Clause(Destination,cEquals,"Paris")  ,
		new Clause(Hotel,cEquals,"HotelMatrix"),
	      new Clause(Duree,cEquals,"45jours"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Reduction,cEquals,"Oui"),
	      new Clause(Prix,cEquals,"40000.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Non")
	      );

Rule rule10 = new Rule(rb, "pdipo_Paris_yes",
		new Clause(Destination,cEquals,"Paris")  ,
		new Clause(Hotel,cEquals,"HotelNippon"),
	      new Clause(Duree,cEquals,"15Jours"),
	      new Clause(Periode, cEquals, "ETE"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Reduction,cEquals,"Non"),
	      new Clause(Prix,cEquals,"50000.00"),
	      new Clause(Choix,cEquals,"Oui"),
	      new Clause(place_disponible, cEquals, "Oui")
	      );

Rule rule11 = new Rule(rb, "DEST_Qatar",
		new Clause(Destination,cEquals,"Qatar")  ,
		new Clause(Reservation,cEquals,"Oui"),
	      new Clause(Demande_Visa,cEquals,"accepté")
	      );

Rule rule12 = new Rule(rb, "DEST_New_York",
		new Clause(Destination,cEquals,"New_York")  ,
		new Clause(Reservation,cEquals,"Oui"),
	      new Clause(Demande_Visa,cEquals,"accepté")
	      );

Rule rule13 = new Rule(rb, "DEST_Paris",
		new Clause(Destination,cEquals,"Paris")  ,
		new Clause(Reservation,cEquals,"Oui"),
	      new Clause(Demande_Visa,cEquals,"accepté")
	      );

Rule rule14 = new Rule(rb, "DEST_Tunisie",
		new Clause(Destination,cEquals,"Tunisie")  ,
		new Clause(Reservation,cEquals,"Oui"),
	      new Clause(Demande_Visa,cEquals,"refusé")
	      );

Rule rule15 = new Rule(rb, "DEST_Oran",
		new Clause(Destination,cEquals,"Oran")  ,
		new Clause(Reservation,cEquals,"Oui"),
	      new Clause(Demande_Visa,cEquals,"refusé")
	      );

Rule rule16 = new Rule(rb, "prix_Qatar",
		new Clause(Destination,cEquals,"Qatar")  ,
		  new Clause(Duree,cEquals,"10jours"),
	      new Clause(Periode, cEquals, "ETE"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Prix,cEquals,"380000.00")
	     );



}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////initialize the third seller rule base (24)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////initialize the buyer rule base (38)

public void initAcheteurRuleBase(RuleBase rb){

rb.goalClauseStack = new Stack() ;
rb.variableList = new Hashtable() ;

RuleVariable Destination = new RuleVariable("Destination") ;
Destination.setLabels("Qatar Paris New_York Tunisie Oran");
Destination.setPromptText(" Quelle est la destination que vous voulez ?");
rb.variableList.put(Destination.name,Destination) ;

RuleVariable Prix = new RuleVariable("Prix");
Prix.setLabels("380000.00 700000.00 40000.00 51000.00 29900.00 530000.00 380000.00 700000.00 50000.00");
Prix.setPromptText(" Quel est son Prix ?");
rb.variableList.put(Prix.name,Prix);

RuleVariable Hotel = new RuleVariable("Hotel");
Hotel.setLabels("HotelMatrix HotelNippon");
Hotel.setPromptText(" Quel  Hotel ?");
rb.variableList.put(Hotel.name,Hotel);

RuleVariable Periode = new RuleVariable("Période") ;
Periode.setLabels("ETE HIVER PRINTEMPS");
Periode.setPromptText(" Quel période ?");
rb.variableList.put(Periode.name,Periode) ;

RuleVariable Duree = new RuleVariable("Durée") ;
Duree.setLabels("10jours 15Jours 45jours");
Duree.setPromptText(" Combien durée ?");
rb.variableList.put(Duree.name,Duree) ;

RuleVariable Visa = new RuleVariable("Visa") ;
Visa.setLabels("accepté refusé");
Visa.setPromptText(" Est ce qu'il y a Visa ?");
rb.variableList.put(Visa.name,Visa) ;

RuleVariable continent = new RuleVariable("continent");
continent.setLabels("Europe L'Amérique_du_Nord L'Amérique_latine Asie  Afrique");
continent.setPromptText(" Quel est votre continent ?");
rb.variableList.put(continent.name,continent) ;

RuleVariable Vol = new RuleVariable("Vol") ;
Vol.setLabels("National International");
Vol.setPromptText(" Quel est votre Vol ?");
rb.variableList.put(Vol.name,Vol);

RuleVariable Choix = new RuleVariable("Choix");
Choix.setLabels("Oui Non");
Choix.setPromptText(" Est ce que vous choisissez ?");
rb.variableList.put(Choix.name,Choix);

RuleVariable Reduction = new RuleVariable("Reduction");
Reduction.setLabels("Oui Non");
Reduction.setPromptText(" Est ce qu'il y a une Reduction ?");
rb.variableList.put(Reduction.name,Reduction);


// Note: at this point all variables values are NULL

Condition cEquals = new Condition("=") ;
Condition cNotEquals = new Condition("!=") ;
Condition cLessThan = new Condition("<") ;
Condition cMoreThan = new Condition(">") ;

// define rules
rb.ruleList = new Vector() ;



Rule rule1 = new Rule(rb, "Visa_Qatar",
		  new Clause(Destination,cEquals,"Qatar")  ,
		  new Clause(Visa, cEquals, "accepté"));

Rule rule2= new Rule(rb, "Visa_New_York",
		  new Clause(Destination,cEquals,"New_York")  ,
		  new Clause(Visa, cEquals, "accepté"));

Rule rule3 = new Rule(rb, "Visa_Paris",
		  new Clause(Destination,cEquals,"Paris")  ,
		  new Clause(Visa, cEquals, "accepté"));

Rule rule4 = new Rule(rb, "Visa_Tunisie",
		  new Clause(Destination,cEquals,"Tunisie")  ,
		  new Clause(Visa, cEquals, "refusé"));

Rule rule5 = new Rule(rb, "Visa_Delimit",
		  new Clause(Vol,cEquals,"National")  ,
		  new Clause(Visa, cEquals, "refusé"));

Rule rule6 = new Rule(rb, "HotelMatrix",
		  new Clause(Duree,cEquals,"45jours")  ,
		  new Clause(Hotel, cEquals, "HotelMatrix"));

Rule rule7 = new Rule(rb, "Promo_HotelMatrix",
		  new Clause(Hotel,cEquals,"HotelMatrix")  ,
		  new Clause(Reduction, cEquals, "Oui"));

Rule rule8= new Rule(rb, "Promo_HotelNippon",
		  new Clause(Hotel,cEquals,"HotelNippon")  ,
		  new Clause(Reduction, cEquals, "Non"));

Rule rule9 = new Rule(rb, "New_York",
		  new Clause(continent,cEquals,"L'Amérique_du_Nord")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Destination, cEquals, "New_York"));

Rule rule10 = new Rule(rb, "Oran",
		  new Clause(Vol,cEquals,"National")  ,
		  new Clause(Periode,cEquals,"HIVER")  ,
		  new Clause(Destination, cEquals, "Oran"));

Rule rule11 = new Rule(rb, "Qatar",
		  new Clause(continent,cEquals,"International")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Destination, cEquals, "Qatar"));

Rule rule12 = new Rule(rb, "Tunisie",
		  new Clause(Vol,cEquals,"International")  ,
		  new Clause(Visa,cEquals,"Non")  ,
		  new Clause(Prix,cEquals,"530000.00")  ,
		  new Clause(Destination, cEquals, "Tunisie"));

Rule rule13 = new Rule(rb, "Paris",
		  new Clause(Vol,cEquals,"International")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Prix,cEquals,"530000.00")  ,
		  new Clause(Destination, cEquals, "Paris"));

Rule rule14 = new Rule(rb, "Qatar2",
		  new Clause(Vol,cEquals,"International")  ,
		  new Clause(Reduction,cEquals,"Oui")  ,
		  new Clause(Visa,cEquals,"Oui")  ,
		  new Clause(Destination, cEquals, "Qatar"));

Rule rule15 = new Rule(rb, "Oran2",
		  new Clause(Vol,cEquals,"National")  ,
		  new Clause(Reduction,cEquals,"Oui")  ,
		  new Clause(Destination, cEquals, "Oran"));

Rule rule16 = new Rule(rb, "Prix_Qatar",
		  new Clause(Destination,cEquals,"Qatar"),
		  new Clause(Duree,cEquals,"10jours"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Periode,cEquals,"ETE"),
	      new Clause(Prix, cEquals, "380000.00"));

	Rule rule17 = new Rule(rb, "Prix_New_York",
		  new Clause(Destination,cEquals,"New_York")  ,
		  new Clause(Periode,cEquals,"ETE"),
		  new Clause(Duree,cEquals,"10jours"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Prix, cEquals, "700000.00"));

	Rule rule18 = new Rule(rb, "Prix_Paris1",
		  new Clause(Destination,cEquals,"Paris")  ,
		  new Clause(Hotel,cEquals,"HotelMatrix"),
	      new Clause(Duree,cEquals,"45jours"),
	      new Clause(Periode,cEquals,"ETE"),
	      new Clause(Visa, cEquals, "accepté"),
	      new Clause(Reduction, cEquals, "Oui"),
	      new Clause(Prix, cEquals, "40000.00"));

	Rule rule19 = new Rule(rb, "Prix_Paris2",
			  new Clause(Destination,cEquals,"Paris")  ,
			  new Clause(Hotel,cEquals,"HotelNippon"),
		      new Clause(Duree,cEquals,"15Jours"),
		      new Clause(Periode,cEquals,"ETE"),
		      new Clause(Visa, cEquals, "accepté"),
		      new Clause(Reduction, cEquals, "Non"),
		      new Clause(Prix, cEquals, "40000.00"));
	////////////////////////////////////////////////////////////

	Rule rule20 = new Rule(rb, "Prix_Tunisie",
			  new Clause(Destination,cEquals,"Tunisie")  ,
			  new Clause(Periode,cEquals,"ETE"),
			  new Clause(Duree,cEquals,"15Jours"),
		      new Clause(Visa, cEquals, "refusé"),
		      new Clause(Reduction, cEquals, "Oui"),
		      new Clause(Prix, cEquals, "51000.00"));			

	Rule rule21 = new Rule(rb, "Prix_Oran",
			  new Clause(Destination,cEquals,"Oran")  ,
			  new Clause(Periode,cEquals,"HIVER"),
			  new Clause(Vol,cEquals,"National"),
		      new Clause(Prix, cEquals, "29000.00"));

Rule rule22 = new Rule(rb, "Choix_Oran",
		  new Clause(Destination,cEquals,"Qatar")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Duree, cEquals, "10jours"),
		  new Clause(Visa,cEquals, "accepté"),
		  new Clause(Prix,cEquals, "380000.00"),
		  new Clause(Choix,cEquals, "Oui")
		);

Rule rule23 = new Rule(rb, "Choix_New_York",
		  new Clause(Destination,cEquals,"New_York")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Duree, cEquals, "10jours"),
		  new Clause(Visa,cEquals, "accepté"),
		  new Clause(Prix,cEquals, "700000.00"),
		  new Clause(Reduction,cEquals, "Non"),
		  new Clause(Choix,cEquals, "Non")
		);

Rule rule24 = new Rule(rb, "Choix_Paris",
		  new Clause(Destination,cEquals,"Paris")  ,
		  new Clause(Hotel,cEquals,"HotelMatrix"),
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Duree, cEquals, "45jours"),
		  new Clause(Visa,cEquals, "accepté"),
		  new Clause(Prix,cEquals, "40000.00"),
		  new Clause(Reduction,cEquals, "Oui"),
		  new Clause(Choix,cEquals, "Oui")
		);

Rule rule25 = new Rule(rb, "Choix_Paris2",
		  new Clause(Destination,cEquals,"Paris")  ,
		  new Clause(Hotel,cEquals,"HotelNippon"),
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Duree, cEquals, "15Jours"),
		  new Clause(Visa,cEquals, "accepté"),
		  new Clause(Prix,cEquals, "50000.00"),
		  new Clause(Reduction,cEquals, "Non"),
		  new Clause(Choix,cEquals, "Oui")
		);

Rule rule26 = new Rule(rb, "Choix_Tunisie",
		  new Clause(Destination,cEquals,"Tunisie")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Duree, cEquals, "15Jours"),
		  new Clause(Visa,cEquals, "refusé"),
		  new Clause(Prix,cEquals, "51000.00"),
		  new Clause(Choix,cEquals, "Non")
		);

Rule rule27 = new Rule(rb, "Choix_Oran2",
		  new Clause(Destination,cEquals,"Oran")  ,
		  new Clause(Vol,cEquals,"National")  ,
		  new Clause(Periode,cEquals,"HIVER")  ,
		  new Clause(Reduction,cEquals, "Oui"),
		  new Clause(Prix,cEquals, "29900.00"),
		  new Clause(Choix,cEquals, "Oui")
		);

Rule rule28 = new Rule(rb, "Tunisie2",
		  new Clause(continent,cEquals,"Afrique")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Destination,cEquals, "Tunisie")
		);

Rule rule29 = new Rule(rb, "Qatar3",
		  new Clause(continent,cEquals,"Asie")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Destination,cEquals, "Qatar")
		);

Rule rule30 = new Rule(rb, "Paris2",
		  new Clause(continent,cEquals,"Europe")  ,
		  new Clause(Periode,cEquals,"ETE")  ,
		  new Clause(Destination,cEquals, "Paris")
		);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////The end

}

