package gui;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import employee.Foreground;

public class main {

	
	public static void main(String[] args){
		
//		new frame(new Foreground());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		 System.setProperty(
//		            "Quaqua.tabLayoutPolicy","wrap"
//		         );
//		         // set the Quaqua Look and Feel in the UIManager
//		         try { 
//		              UIManager.setLookAndFeel(
//		                  ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel()
//		              );
//		         // set UI manager properties here that affect Quaqua
//		         } catch (Exception e) {
//		             // take an appropriate action here
//		         }
		         
		new MainFrame();
	}
}
