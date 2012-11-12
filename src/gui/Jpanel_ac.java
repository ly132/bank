package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import employee.Employee;

public class Jpanel_ac extends JPanel implements ActionListener{

	Employee em;
	
	JPanel left,right,r_up,r_down;
	
	JButton open,deposit,withdw,inquire,transfer,chpw,cancel;
	JButton submit;
	
	JLabel lpid,laid,lname1,lname2,lbalance,lfrom,lto,laidto,lpw,lpw_new,lpw_new2;
	JPasswordField text_pw,text_pw_new1,text_pw_new2;
	JTextField tpid,taid,tname1,tname2,tbalance,taidto;
	
	int current_task = 0;
	
	/*
	open             Open an account.
	 deposit         Deposit to an existing account.
	 withdrawal         Withdrawal from an existing account.
	 inquire         Inquire an account for balance or operations.
	 transfer         Transfer from one account to another.
	 changepasswd         Change passwd.
	 cancel  
	*/
	public Jpanel_ac( Employee em ){
		super();
		this.em = em;
		init();
	}
	
	private void init()
	{
		open 	= new JButton("Open_Account");	open.addActionListener(this);		//0
		deposit	= new JButton("Deposit");		deposit.addActionListener(this);	//1
		withdw 	= new JButton("Withdrawal");	withdw.addActionListener(this);		//2
		inquire = new JButton("Inquire");		inquire.addActionListener(this);	//3
		transfer= new JButton("Transfer");		transfer.addActionListener(this);	//4
		chpw	= new JButton("Change_Passwd");	chpw.addActionListener(this);		//5
		cancel	= new JButton("Cancel_Account");cancel.addActionListener(this);		//6
		submit	= new JButton("Submit");		submit.addActionListener(this);
				
		lpid	= new JLabel("Persional ID:");
		laid	= new JLabel("Account ID:");
		lname1	= new JLabel("Name:");
		lname2	= new JLabel("Name:");
		lbalance= new JLabel("Sum:");
		lfrom	= new JLabel("From:");
		lto		= new JLabel("To:");
		laidto	= new JLabel("Account ID:");
		lpw		= new JLabel("Passwd:");
		lpw_new	= new JLabel("New Passwd:");
		lpw_new2= new JLabel("Confirm:");
		
		text_pw 	= new JPasswordField();	
		text_pw_new1= new JPasswordField(); 
		text_pw_new2= new JPasswordField();
		tpid		= new JTextField();
		taid		= new JTextField();
		tname1		= new JTextField();
		tname2		= new JTextField();
		tbalance	= new JTextField();
		taidto		= new JTextField();
		
		this.right = new JPanel(new BorderLayout());
		this.left = new JPanel(new GridLayout(7,1,15,15));
		this.r_down = new JPanel();
		this.r_up = new JPanel();
		
		this.setLayout(new BorderLayout());
//		this.r_up.setLayout(new GridLayout(7,2,5,5));
		this.add(left,BorderLayout.WEST);
		this.add(right,BorderLayout.CENTER);
		right.add(r_up,BorderLayout.CENTER);
		right.add(r_down,BorderLayout.SOUTH);
		
		left.add(open);
		left.add(deposit);
		left.add(withdw);
		left.add(inquire);
		left.add(transfer);
		left.add(chpw);
		left.add(cancel);
		
	}

	public void actionPerformed(ActionEvent e) {
		String button_text = ((JButton)e.getSource()).getText();
		try {
			Method method = this.getClass().getMethod(button_text);
			method.invoke(this);
		} catch (Exception e1) { e1.printStackTrace(); }
	}
	
	public void Open_Account()
	{
		this.current_task = 0;
		this.r_up.removeAll();
		this.r_up.setLayout(new GridLayout(6,2,5,5));
		r_up.add(lpid);		r_up.add(tpid);
		r_up.add(lbalance);	r_up.add(tbalance);
		r_up.add(lpw);		r_up.add(text_pw);
		r_up.add(lpw_new2);	r_up.add(text_pw_new2);
		r_up.add(submit);	r_up.validate();
	}
}
