package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import employee.Employee;

public class frame extends JFrame{

	Employee em = null;
	
	JTabbedPane tab_pane;
	
	JPanel tab_0_ac,tab_1_em,tab_2_cu;
	
//	JPanel tab_0_left,tab_0_r,tab_0_r_up,tab_0_r_down;
	
	
	public frame( Employee em )
	{
		super("bank");
		this.em = em;
		init();
	}
	
	void init()
	{
		this.tab_pane = new JTabbedPane();
		this.tab_0_ac = new Jpanel_ac(this.em);
		this.tab_pane.add(tab_0_ac);
		this.add(tab_pane);
		this.setSize(750,600);
		this.setLocation(300, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	}
	
}
