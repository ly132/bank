/*
 * Created by JFormDesigner on Tue Nov 06 21:51:32 CST 2012
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import client.Account;
import client.accountType;
import employee.Employee;

/**
 * @author ly142
 */
public class MainFrame  {

	
	String current_ope = null;
	Account account_instance = null;
	public Employee employee_instance = null;
	
	private String jobNum_pattern	= "^\\d{0,6}$";	
	private String aid_pattern 		= "^\\d{0,6}$";
	private String passwd_pattern 	= "^\\d{0,6}$";
	private String pid_pattern 		= "^\\d{0,6}$";
	private String sum_pattern 		= "^\\d{0,8}(\\.\\d{0,2})?$";
	private String name_pattern		= "^.{0,20}$";
	private String age_pattern		= "^\\d{0,2}$";
	private String phone_pattern	= "^\\d{0,11}$";
	private String address_pattern	= "^.{0,50}$";
	
	public MainFrame()
	{
		UIManager.getDefaults().put("Button.font", new Font("Monospace",Font.BOLD, 14));
		this.initComponents();
		CheckInput acc_id_CheckInput = new CheckInput(age_pattern);
		this.textField19.setDocument(acc_id_CheckInput);
	}

	private void frame1WindowClosing(WindowEvent e) {
		System.exit(0);
	}

	private void changeCard(MouseEvent e) {
		Component[] cs = ((JButton)e.getSource()).getParent().getParent().getComponents();
		for( Component c : cs )
		{
			if( c.getName() != null && c.getName().length() > 3 && c.getName().substring(0, 3).equals("tab") )
			{
				String name = ((JButton)e.getSource()).getName();
				if( name.equals("getSubordinate") || name.equals("getAll") || name.equals("inquire") )
				{
					this.scrollPane3.setEnabled(false);
					this.scrollPane3.setVisible(false);
				}
				else if( !this.scrollPane3.isEnabled() )
				{
					this.scrollPane3.setEnabled(true);
					this.scrollPane3.setVisible(true);
				}
				CardLayout cl = (CardLayout) (((JPanel)c).getLayout());
				cl.show((Container) c, name);
				this.current_ope = name;
				this.frame1.validate();
				this.scrollPane3.repaint();
				return;				
			}
		}		
	}

	private void submit(MouseEvent e) {
		String result_str = "";
		try{
		
		if( this.account_instance == null && isCurrentOperationNeedAccountLogin(current_ope) )
		{
			JOptionPane.showMessageDialog(this.frame1, "Login an Account First.", "Login Error", JOptionPane.ERROR_MESSAGE);
			int tabIndex = this.tabbedPane1.getSelectedIndex();
			if( tabIndex == 0 )
				this.button51.doClick();
			if( tabIndex == 1 )
				this.button53.doClick();
			return;
		}
		else if( current_ope == "acclogin" )
		{
			this.account_instance = Account.getAccountInstance(
					this.textField19.getText(), 
					this.passwordField12.getPassword().toString(),
					this.employee_instance.getJobNumber()
					);
			if( this.account_instance != null )
			{
				result_str = "Account Login Success.";
				this.label43.setText(this.account_instance.getId());
				this.textField21.setEnabled(false);
				this.passwordField13.setEnabled(false);
			}
			else
				result_str = "Account Login Failed. Please Try Again.";
		}
		
		else if( current_ope == "deposit" )
		{
			result_str = this.account_instance.deposit(this.textField4.getText());
		}
		
		else if( current_ope == "withdrawal" )
		{
			result_str = this.account_instance.withdrawal(this.textField8.getText());
		}
		
		//
		else if( current_ope == "inquire" )
		{
			table3.setModel(new DefaultTableModel(
				this.account_instance.inquire(
						this.dateChooserJButton1.getText(),this.dateChooserJButton2.getText()),
				new String[] {
					"NO.", "Date", "Income", "Outcome", "Balance"
				}
			) {
				boolean[] columnEditable = new boolean[] {
					false, false, false, false, false, false
				};
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return columnEditable[columnIndex];
				}
			});
		}
//		
		else if( current_ope == "transfer" )
		{
			//sum, target account id ,target name
			result_str = this.account_instance.transfer( 
					this.textField12.getText(),this.textField1.getText(),this.textField2.getText());
		}
		
		else if( current_ope == "acclogout" )
		{
			if( this.radioButton4.isSelected() )
			{
				result_str = "Account Logout Success.";
				this.account_instance = null;
				this.textField21.setEnabled(true);
				this.passwordField13.setEnabled(true);
			}
		}
		
		else if( current_ope == "advinfo" )
		{//20 pid, //21 aid //22 psw
			if( this.account_instance == null )
				this.account_instance = Account.getAccountInstance(
						this.textField21.getText(), this.passwordField13.getPassword().toString(), this.employee_instance.getJobNumber());
			if( this.account_instance == null )
				result_str = "Login Failed. Please Try Again.";
			else
				result_str = this.account_instance.advinfo(this.textField20.getText());
		}
		
		else if( current_ope == "open" )
		{// job_num, pid,type,balance,passwd,psw
			String type = "";
			if( this.radioButton5.isSelected() )
				type = "s";
			else
				type = "t";
			result_str = Account.open(
					this.employee_instance.getJobNumber(),
					this.textField9.getText(),
					type,
					this.textField10.getText(),
					this.passwordField5.getPassword().toString(),
					this.passwordField6.getPassword().toString());
		}
		
		else if( current_ope == "chpasswd" )
		{
			if( !this.account_instance.isAdvanceInfoConfirm() )
			{
				JOptionPane.showMessageDialog(this.frame1, "Advance Info is Needed.", "Info Error", JOptionPane.ERROR_MESSAGE);
				this.button53.doClick();
				return;
			}
			result_str = this.account_instance.chpasswd(
					this.passwordField14.getPassword().toString(),
					this.passwordField7.getPassword().toString());
		}
		
		else if( current_ope == "cancel" )
		{
			if( !this.account_instance.isAdvanceInfoConfirm() )
			{
				JOptionPane.showMessageDialog(this.frame1, "Advance Info is Needed.", "Info Error", JOptionPane.ERROR_MESSAGE);
				this.button53.doClick();
				return;
			}
			if( this.radioButton7.isSelected() )
			{
				result_str = this.account_instance.cancel();
			}
		}
		
		else if( current_ope == "addattorney" )
		{
			if( !this.account_instance.isAdvanceInfoConfirm() )
			{
				JOptionPane.showMessageDialog(this.frame1, "Advance Info is Needed.", "Info Error", JOptionPane.ERROR_MESSAGE);
				this.button53.doClick();
				return;
			}
			result_str = this.account_instance.addattorney(
					this.textField27.getText(),
					this.passwordField11.getPassword().toString());
		}
		
		else if( current_ope == "add_customer" )
		{
			String type = "";
			if( this.radioButton1.isSelected() )
				type = "n";
			else if( this.radioButton2.isSelected() )
				type = "v";
			else if( this.radioButton3.isSelected() )
				type = "e";
			result_str = this.account_instance.add_customer(
					this.textField17.getText(),
					this.textField18.getText(),
					type);
		}
		
		//start employee funtion
		else if( current_ope == "addem" )
		{
			String[] addeminfos = {
					this.textField34.getText(),
					this.passwordField19.getPassword().toString(),
					this.textField35.getText(),
					this.textField38.getText(),
					this.textField39.getText(),
					this.textField40.getText()
			};
			result_str = this.employee_instance.addem(addeminfos);
		}
		
		else if( current_ope == "delem" )
		{
			String[] deleminfos = {
					this.textField36.getText(),
					this.passwordField20.getPassword().toString()
			};
			result_str = this.employee_instance.delem(deleminfos);
		}
		
		else if( current_ope == "chem" )
		{
			String[] cheminfos = {
					this.passwordField14.getPassword().toString(),
					(String)this.comboBox1.getSelectedItem(),
					this.textField41.getText(),
					this.textField42.getText()
			};
			result_str = this.employee_instance.chem(cheminfos);
		}
		
		else if( current_ope == "getSubordinate" )
		{
			Employee subem = this.employee_instance.getSub(
					this.passwordField21.getPassword().toString(),
					this.textField37.getText()
					);
			table1.setModel(new DefaultTableModel(
				new Object[][] {
					{
						subem.getJobNumber(),
						subem.getPid(),
						subem.getName(),
						subem.getAge(),
						subem.getTel(),
						subem.getAddress()
					},
				},
				new String[] {
					"Job_Num", "ID", "Name", "Age", "Phone", "Address"
				}
			) {
				boolean[] columnEditable = new boolean[] {
					false, false, false, false, false, false
				};
				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return columnEditable[columnIndex];
				}
			});
		}
		
		//start getall
		else if( current_ope == "getAll" )
		{
			LinkedList<Employee> ems = this.employee_instance.getSub(
					this.passwordField22.getPassword().toString());
			Object[][] infos = new Object[ems.size()][6];
			int count = 0;
			for( Employee e1 : ems )
			{
				infos[count][0] = e1.getJobNumber();
				infos[count][1] = e1.getPid();
				infos[count][2] = e1.getName();
				infos[count][3] = e1.getAge();
				infos[count][4] = e1.getTel();
				infos[count][5] = e1.getAddress();
				count++;
			}
			table2.setModel(new DefaultTableModel(
					infos,
					new String[] {
						"Job_Num", "ID", "Name", "Age", "Phone", "Address"
					}
				) {
					boolean[] columnEditable = new boolean[] {
						false, false, false, false, false, false
					};
					@Override
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return columnEditable[columnIndex];
					}
				});
		}
		//end getall
		//end
		}catch( Exception e1 )
		{
			e1.printStackTrace();
			result_str = e1.getCause().getMessage();
		}
		this.textArea1.setText(result_str);
		this.frame1.validate();
		this.frame1.repaint();
	}
	
	private boolean isCurrentOperationNeedAccountLogin(String ope) {
		return (
				ope.equals("deposit") 		||
				ope.equals("withdrawal") 	||
				ope.equals("inquire") 		||
				ope.equals("transfer") 		||
				ope.equals("chpasswd") 		||
				ope.equals("cancel") 		||
				ope.equals("addattorney")
				);				
	}

	private void enter_donothing(KeyEvent e) {}

	private void enter_accid(KeyEvent e) {
		// TODO add your code here
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		frame1 = new JFrame();
		label41 = new JLabel();
		button50 = new JButton();
		label42 = new JLabel();
		label43 = new JLabel();
		scrollPane3 = new JScrollPane();
		textArea1 = new JTextArea();
		tabbedPane1 = new JTabbedPane();
		panel3 = new JPanel();
		button8 = new JButton();
		button10 = new JButton();
		panel7 = new JPanel();
		button51 = new JButton();
		button4 = new JButton();
		button6 = new JButton();
		button7 = new JButton();
		button5 = new JButton();
		button52 = new JButton();
		tab1 = new JPanel();
		panel2 = new JPanel();
		acclogin = new JPanel();
		panel18 = new JPanel();
		label7 = new JLabel();
		textField19 = new JTextField();
		button54 = new JButton();
		label35 = new JLabel();
		passwordField12 = new JPasswordField();
		button63 = new JButton();
		deposit = new JPanel();
		panel9 = new JPanel();
		label4 = new JLabel();
		textField4 = new JTextField();
		button13 = new JButton();
		withdrawal = new JPanel();
		panel10 = new JPanel();
		label5 = new JLabel();
		textField8 = new JTextField();
		button18 = new JButton();
		inquire = new JPanel();
		panel11 = new JPanel();
		label10 = new JLabel();
		dateChooserJButton1 = new DateChooserJButton();
		label17 = new JLabel();
		dateChooserJButton2 = new DateChooserJButton();
		scrollPane4 = new JScrollPane();
		table3 = new JTable();
		transfer = new JPanel();
		panel15 = new JPanel();
		label20 = new JLabel();
		textField12 = new JTextField();
		button25 = new JButton();
		label21 = new JLabel();
		textField1 = new JTextField();
		button26 = new JButton();
		label23 = new JLabel();
		textField2 = new JTextField();
		button17 = new JButton();
		acclogout = new JPanel();
		panel21 = new JPanel();
		label34 = new JLabel();
		radioButton4 = new JRadioButton();
		panel6 = new JPanel();
		button36 = new JButton();
		button37 = new JButton();
		panel12 = new JPanel();
		button53 = new JButton();
		button1 = new JButton();
		button2 = new JButton();
		button3 = new JButton();
		button39 = new JButton();
		button38 = new JButton();
		tab2 = new JPanel();
		panel5 = new JPanel();
		advinfo = new JPanel();
		panel23 = new JPanel();
		label36 = new JLabel();
		textField20 = new JTextField();
		button56 = new JButton();
		label37 = new JLabel();
		textField21 = new JTextField();
		button64 = new JButton();
		label38 = new JLabel();
		passwordField13 = new JPasswordField();
		button65 = new JButton();
		add_customer = new JPanel();
		panel19 = new JPanel();
		label31 = new JLabel();
		textField17 = new JTextField();
		button45 = new JButton();
		label33 = new JLabel();
		textField18 = new JTextField();
		button46 = new JButton();
		label32 = new JLabel();
		panel1 = new JPanel();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		radioButton3 = new JRadioButton();
		addattorney = new JPanel();
		panel24 = new JPanel();
		label49 = new JLabel();
		textField27 = new JTextField();
		button61 = new JButton();
		label50 = new JLabel();
		passwordField11 = new JPasswordField();
		button62 = new JButton();
		open = new JPanel();
		panel13 = new JPanel();
		label12 = new JLabel();
		textField9 = new JTextField();
		button20 = new JButton();
		label13 = new JLabel();
		panel14 = new JPanel();
		radioButton5 = new JRadioButton();
		radioButton6 = new JRadioButton();
		label14 = new JLabel();
		textField10 = new JTextField();
		button21 = new JButton();
		label15 = new JLabel();
		passwordField5 = new JPasswordField();
		button22 = new JButton();
		label16 = new JLabel();
		passwordField6 = new JPasswordField();
		button23 = new JButton();
		panel8 = new JPanel();
		chpasswd = new JPanel();
		panel16 = new JPanel();
		label27 = new JLabel();
		passwordField4 = new JPasswordField();
		button31 = new JButton();
		label28 = new JLabel();
		passwordField7 = new JPasswordField();
		button32 = new JButton();
		cancel = new JPanel();
		panel17 = new JPanel();
		label29 = new JLabel();
		radioButton7 = new JRadioButton();
		panel4 = new JPanel();
		button40 = new JButton();
		button41 = new JButton();
		panel20 = new JPanel();
		button42 = new JButton();
		button43 = new JButton();
		button44 = new JButton();
		button47 = new JButton();
		button48 = new JButton();
		tab3 = new JPanel();
		panel22 = new JPanel();
		addem = new JPanel();
		panel29 = new JPanel();
		label59 = new JLabel();
		textField34 = new JTextField();
		button74 = new JButton();
		label60 = new JLabel();
		passwordField19 = new JPasswordField();
		button75 = new JButton();
		label61 = new JLabel();
		textField35 = new JTextField();
		button76 = new JButton();
		label62 = new JLabel();
		textField38 = new JTextField();
		button77 = new JButton();
		label63 = new JLabel();
		textField39 = new JTextField();
		button78 = new JButton();
		label67 = new JLabel();
		textField40 = new JTextField();
		button49 = new JButton();
		panel26 = new JPanel();
		delem = new JPanel();
		panel30 = new JPanel();
		label64 = new JLabel();
		textField36 = new JTextField();
		button79 = new JButton();
		label65 = new JLabel();
		passwordField20 = new JPasswordField();
		button80 = new JButton();
		chem = new JPanel();
		panel25 = new JPanel();
		label40 = new JLabel();
		passwordField14 = new JPasswordField();
		button55 = new JButton();
		label39 = new JLabel();
		comboBox1 = new JComboBox();
		label66 = new JLabel();
		textField41 = new JTextField();
		button57 = new JButton();
		label68 = new JLabel();
		textField42 = new JTextField();
		button81 = new JButton();
		panel27 = new JPanel();
		getSubordinate = new JPanel();
		panel31 = new JPanel();
		label69 = new JLabel();
		textField37 = new JTextField();
		button82 = new JButton();
		label70 = new JLabel();
		passwordField21 = new JPasswordField();
		button83 = new JButton();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		getAll = new JPanel();
		panel32 = new JPanel();
		label72 = new JLabel();
		passwordField22 = new JPasswordField();
		button85 = new JButton();
		scrollPane2 = new JScrollPane();
		table2 = new JTable();

		//======== frame1 ========
		{
			frame1.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					frame1WindowClosing(e);
				}
			});
			Container frame1ContentPane = frame1.getContentPane();
			frame1ContentPane.setLayout(null);

			//---- label41 ----
			label41.setText("JobNum:123456   Name:wkrjelw");
			frame1ContentPane.add(label41);
			label41.setBounds(450, 7, 205, 25);

			//---- button50 ----
			button50.setText("Logout");
			button50.setMargin(new Insets(2, 0, 2, 0));
			frame1ContentPane.add(button50);
			button50.setBounds(663, 7, 60, 25);

			//---- label42 ----
			label42.setText("Current Account ID");
			frame1ContentPane.add(label42);
			label42.setBounds(new Rectangle(new Point(30, 440), label42.getPreferredSize()));

			//---- label43 ----
			label43.setText("123456");
			frame1ContentPane.add(label43);
			label43.setBounds(new Rectangle(new Point(60, 463), label43.getPreferredSize()));

			//======== scrollPane3 ========
			{
				scrollPane3.setBorder(new TitledBorder("Result"));

				//---- textArea1 ----
				textArea1.setText("eeeeeeeeeeeeeeeeeeeeeeee");
				textArea1.setBorder(null);
				scrollPane3.setViewportView(textArea1);
			}
			frame1ContentPane.add(scrollPane3);
			scrollPane3.setBounds(190, 315, 455, 90);

			//======== tabbedPane1 ========
			{
				tabbedPane1.setBorder(new EmptyBorder(10, 10, 10, 10));
				tabbedPane1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						submit(e);
					}
				});

				//======== panel3 ========
				{
					panel3.setLayout(null);

					//---- button8 ----
					button8.setText("Submit");
					button8.setFont(new Font("Monospaced", Font.PLAIN, 16));
					button8.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							submit(e);
						}
					});
					panel3.add(button8);
					button8.setBounds(170, 393, 175, 40);

					//---- button10 ----
					button10.setText("Clear All");
					panel3.add(button10);
					button10.setBounds(463, 393, 175, 40);

					//======== panel7 ========
					{
						panel7.setBackground(UIManager.getColor("Button.background"));
						panel7.setLayout(new GridLayout(7, 1, 0, 20));

						//---- button51 ----
						button51.setText("Acc Login");
						button51.setName("acclogin");
						button51.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button51);

						//---- button4 ----
						button4.setText("Deposit");
						button4.setName("deposit");
						button4.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button4);

						//---- button6 ----
						button6.setText("Withdrawal");
						button6.setMargin(new Insets(2, 0, 2, 0));
						button6.setName("withdrawal");
						button6.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button6);

						//---- button7 ----
						button7.setText("Inquire");
						button7.setMargin(new Insets(2, 0, 2, 0));
						button7.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button7.setName("inquire");
						button7.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button7);

						//---- button5 ----
						button5.setText("Transfer");
						button5.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button5.setMargin(new Insets(2, 0, 2, 0));
						button5.setName("transfer");
						button5.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button5);

						//---- button52 ----
						button52.setText("Acc Logout");
						button52.setName("acclogout");
						button52.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel7.add(button52);
					}
					panel3.add(panel7);
					panel7.setBounds(30, 30, 95, 420);

					//======== tab1 ========
					{
						tab1.setBackground(Color.white);
						tab1.setName("tab1");
						tab1.setLayout(new CardLayout());

						//======== panel2 ========
						{
							panel2.setLayout(null);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel2.getComponentCount(); i++) {
									Rectangle bounds = panel2.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel2.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel2.setMinimumSize(preferredSize);
								panel2.setPreferredSize(preferredSize);
							}
						}
						tab1.add(panel2, "card8");

						//======== acclogin ========
						{
							acclogin.setBackground(UIManager.getColor("Button.background"));
							acclogin.setLayout(null);

							//======== panel18 ========
							{
								panel18.setBorder(new CompoundBorder(
									new TitledBorder(null, "Account Login Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel18.setLayout(new GridBagLayout());
								((GridBagLayout)panel18.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel18.getLayout()).rowHeights = new int[] {45, 45, 45, 46, 27, 0};
								((GridBagLayout)panel18.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel18.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label7 ----
								label7.setText("Account ID:");
								label7.setHorizontalAlignment(SwingConstants.RIGHT);
								label7.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel18.add(label7, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- textField19 ----
								textField19.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel18.add(textField19, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button54 ----
								button54.setText("Clear");
								panel18.add(button54, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label35 ----
								label35.setText("Passwd:");
								label35.setFont(new Font("Times New Roman", label35.getFont().getStyle(), label35.getFont().getSize() + 2));
								label35.setHorizontalAlignment(SwingConstants.RIGHT);
								panel18.add(label35, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel18.add(passwordField12, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button63 ----
								button63.setText("Clear");
								panel18.add(button63, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							acclogin.add(panel18);
							panel18.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < acclogin.getComponentCount(); i++) {
									Rectangle bounds = acclogin.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = acclogin.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								acclogin.setMinimumSize(preferredSize);
								acclogin.setPreferredSize(preferredSize);
							}
						}
						tab1.add(acclogin, "acclogin");

						//======== deposit ========
						{
							deposit.setBackground(UIManager.getColor("Button.background"));
							deposit.setLayout(null);

							//======== panel9 ========
							{
								panel9.setBorder(new CompoundBorder(
									new TitledBorder(null, "Deposit Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel9.setLayout(new GridBagLayout());
								((GridBagLayout)panel9.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel9.getLayout()).rowHeights = new int[] {45, 45, 45, 46, 27, 0};
								((GridBagLayout)panel9.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel9.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label4 ----
								label4.setText("Deposit Sum:");
								label4.setFont(new Font("Times New Roman", label4.getFont().getStyle(), label4.getFont().getSize() + 2));
								label4.setHorizontalAlignment(SwingConstants.RIGHT);
								panel9.add(label4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel9.add(textField4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button13 ----
								button13.setText("Clear");
								panel9.add(button13, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							deposit.add(panel9);
							panel9.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < deposit.getComponentCount(); i++) {
									Rectangle bounds = deposit.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = deposit.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								deposit.setMinimumSize(preferredSize);
								deposit.setPreferredSize(preferredSize);
							}
						}
						tab1.add(deposit, "deposit");

						//======== withdrawal ========
						{
							withdrawal.setBackground(UIManager.getColor("Button.background"));
							withdrawal.setLayout(null);

							//======== panel10 ========
							{
								panel10.setBorder(new CompoundBorder(
									new TitledBorder(null, "Withdrawal Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel10.setLayout(new GridBagLayout());
								((GridBagLayout)panel10.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel10.getLayout()).rowHeights = new int[] {45, 45, 45, 46, 27, 0};
								((GridBagLayout)panel10.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel10.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label5 ----
								label5.setText("Withdrawal Sum:");
								label5.setFont(new Font("Times New Roman", label5.getFont().getStyle(), label5.getFont().getSize() + 2));
								label5.setHorizontalAlignment(SwingConstants.RIGHT);
								panel10.add(label5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel10.add(textField8, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button18 ----
								button18.setText("Clear");
								panel10.add(button18, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							withdrawal.add(panel10);
							panel10.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < withdrawal.getComponentCount(); i++) {
									Rectangle bounds = withdrawal.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = withdrawal.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								withdrawal.setMinimumSize(preferredSize);
								withdrawal.setPreferredSize(preferredSize);
							}
						}
						tab1.add(withdrawal, "withdrawal");

						//======== inquire ========
						{
							inquire.setBackground(UIManager.getColor("Button.background"));
							inquire.setLayout(null);

							//======== panel11 ========
							{
								panel11.setBorder(new CompoundBorder(
									new TitledBorder(null, "Inquire Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel11.setLayout(new GridBagLayout());
								((GridBagLayout)panel11.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel11.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel11.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel11.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label10 ----
								label10.setText("Start Time:");
								label10.setHorizontalAlignment(SwingConstants.RIGHT);
								label10.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel11.add(label10, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- dateChooserJButton1 ----
								dateChooserJButton1.setText("text");
								dateChooserJButton1.setFont(new Font("Times New Roman", Font.BOLD, 16));
								panel11.add(dateChooserJButton1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- label17 ----
								label17.setText("End Time:");
								label17.setHorizontalAlignment(SwingConstants.RIGHT);
								label17.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel11.add(label17, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- dateChooserJButton2 ----
								dateChooserJButton2.setText("text");
								dateChooserJButton2.setFont(new Font("Times New Roman", Font.BOLD, 16));
								panel11.add(dateChooserJButton2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//======== scrollPane4 ========
								{

									//---- table3 ----
									table3.setModel(new DefaultTableModel(
										new Object[][] {
											{"1", "2012-11-12", "10000000000", "10000000000", "90000000000"},
											{"2", null, null, null, null},
										},
										new String[] {
											"NO.", "Date", "Income", "Outcome", "Balance"
										}
									) {
										boolean[] columnEditable = new boolean[] {
											false, false, false, false, false
										};
										@Override
										public boolean isCellEditable(int rowIndex, int columnIndex) {
											return columnEditable[columnIndex];
										}
									});
									table3.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
									scrollPane4.setViewportView(table3);
								}
								panel11.add(scrollPane4, new GridBagConstraints(0, 2, 3, 3, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							}
							inquire.add(panel11);
							panel11.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < inquire.getComponentCount(); i++) {
									Rectangle bounds = inquire.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = inquire.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								inquire.setMinimumSize(preferredSize);
								inquire.setPreferredSize(preferredSize);
							}
						}
						tab1.add(inquire, "inquire");

						//======== transfer ========
						{
							transfer.setBackground(UIManager.getColor("Button.background"));
							transfer.setLayout(null);

							//======== panel15 ========
							{
								panel15.setBorder(new CompoundBorder(
									new TitledBorder(null, "Transfer Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel15.setLayout(new GridBagLayout());
								((GridBagLayout)panel15.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel15.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel15.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel15.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label20 ----
								label20.setText("Transfer Sum:");
								label20.setFont(new Font("Times New Roman", label20.getFont().getStyle(), label20.getFont().getSize() + 2));
								label20.setHorizontalAlignment(SwingConstants.RIGHT);
								panel15.add(label20, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel15.add(textField12, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button25 ----
								button25.setText("Clear");
								panel15.add(button25, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label21 ----
								label21.setText("Target Account ID:");
								label21.setFont(new Font("Times New Roman", label21.getFont().getStyle(), label21.getFont().getSize() + 2));
								label21.setHorizontalAlignment(SwingConstants.RIGHT);
								panel15.add(label21, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel15.add(textField1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button26 ----
								button26.setText("Clear");
								panel15.add(button26, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label23 ----
								label23.setText("Target Name:");
								label23.setFont(new Font("Times New Roman", label23.getFont().getStyle(), label23.getFont().getSize() + 2));
								label23.setHorizontalAlignment(SwingConstants.RIGHT);
								panel15.add(label23, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel15.add(textField2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button17 ----
								button17.setText("Clear");
								panel15.add(button17, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							transfer.add(panel15);
							panel15.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < transfer.getComponentCount(); i++) {
									Rectangle bounds = transfer.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = transfer.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								transfer.setMinimumSize(preferredSize);
								transfer.setPreferredSize(preferredSize);
							}
						}
						tab1.add(transfer, "transfer");

						//======== acclogout ========
						{
							acclogout.setBackground(UIManager.getColor("Button.background"));
							acclogout.setLayout(null);

							//======== panel21 ========
							{
								panel21.setBorder(new CompoundBorder(
									new TitledBorder(null, "Account Logout", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel21.setLayout(new GridBagLayout());
								((GridBagLayout)panel21.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel21.getLayout()).rowHeights = new int[] {45, 45, 45, 46, 27, 0};
								((GridBagLayout)panel21.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel21.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label34 ----
								label34.setText("Confirm Logout:");
								label34.setHorizontalAlignment(SwingConstants.RIGHT);
								label34.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel21.add(label34, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- radioButton4 ----
								radioButton4.setText("Yes");
								panel21.add(radioButton4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
									new Insets(0, 0, 20, 20), 0, 0));
							}
							acclogout.add(panel21);
							panel21.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < acclogout.getComponentCount(); i++) {
									Rectangle bounds = acclogout.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = acclogout.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								acclogout.setMinimumSize(preferredSize);
								acclogout.setPreferredSize(preferredSize);
							}
						}
						tab1.add(acclogout, "acclogout");
					}
					panel3.add(tab1);
					tab1.setBounds(150, 15, 540, 375);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < panel3.getComponentCount(); i++) {
							Rectangle bounds = panel3.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel3.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel3.setMinimumSize(preferredSize);
						panel3.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("Basic Operation", panel3);


				//======== panel6 ========
				{
					panel6.setLayout(null);

					//---- button36 ----
					button36.setText("Submit");
					button36.setFont(new Font("Times New Roman", Font.PLAIN, 16));
					button36.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							submit(e);
						}
					});
					panel6.add(button36);
					button36.setBounds(170, 393, 175, 40);

					//---- button37 ----
					button37.setText("Clear All");
					panel6.add(button37);
					button37.setBounds(463, 393, 175, 40);

					//======== panel12 ========
					{
						panel12.setBackground(UIManager.getColor("Button.background"));
						panel12.setLayout(new GridLayout(7, 1, 0, 20));

						//---- button53 ----
						button53.setText("Advanced Info");
						button53.setMargin(new Insets(2, 0, 2, 0));
						button53.setName("advinfo");
						button53.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button53);

						//---- button1 ----
						button1.setText("Open Account");
						button1.setMargin(new Insets(2, 0, 2, 0));
						button1.setName("open");
						button1.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button1);

						//---- button2 ----
						button2.setText("Ch_Passwd");
						button2.setMargin(new Insets(2, 0, 2, 0));
						button2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button2.setName("chpasswd");
						button2.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button2);

						//---- button3 ----
						button3.setText("Cancel_Acct");
						button3.setMargin(new Insets(2, 0, 2, 0));
						button3.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button3.setActionCommand("Cancel_Acct");
						button3.setName("cancel");
						button3.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button3);

						//---- button39 ----
						button39.setText("Add Attorney");
						button39.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button39.setMargin(new Insets(2, 0, 2, 0));
						button39.setName("addattorney");
						button39.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button39);

						//---- button38 ----
						button38.setText("Add Customer");
						button38.setMargin(new Insets(2, 0, 2, 0));
						button38.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button38.setName("add_customer");
						button38.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel12.add(button38);
					}
					panel6.add(panel12);
					panel12.setBounds(30, 30, 95, 420);

					//======== tab2 ========
					{
						tab2.setBackground(Color.white);
						tab2.setName("tab2");
						tab2.setLayout(new CardLayout());

						//======== panel5 ========
						{
							panel5.setLayout(null);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel5.getComponentCount(); i++) {
									Rectangle bounds = panel5.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel5.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel5.setMinimumSize(preferredSize);
								panel5.setPreferredSize(preferredSize);
							}
						}
						tab2.add(panel5, "card8");

						//======== advinfo ========
						{
							advinfo.setBackground(UIManager.getColor("Button.background"));
							advinfo.setLayout(null);

							//======== panel23 ========
							{
								panel23.setBorder(new CompoundBorder(
									new TitledBorder(null, "Addvance Account Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel23.setLayout(new GridBagLayout());
								((GridBagLayout)panel23.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel23.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel23.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel23.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label36 ----
								label36.setText("Persional ID:");
								label36.setHorizontalAlignment(SwingConstants.RIGHT);
								label36.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel23.add(label36, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- textField20 ----
								textField20.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel23.add(textField20, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button56 ----
								button56.setText("Clear");
								panel23.add(button56, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label37 ----
								label37.setText("Account ID:");
								label37.setFont(new Font("Times New Roman", label37.getFont().getStyle(), label37.getFont().getSize() + 2));
								label37.setHorizontalAlignment(SwingConstants.RIGHT);
								panel23.add(label37, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel23.add(textField21, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button64 ----
								button64.setText("Clear");
								panel23.add(button64, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label38 ----
								label38.setText("Passwd:");
								label38.setHorizontalAlignment(SwingConstants.RIGHT);
								label38.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel23.add(label38, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel23.add(passwordField13, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button65 ----
								button65.setText("Clear");
								panel23.add(button65, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							advinfo.add(panel23);
							panel23.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < advinfo.getComponentCount(); i++) {
									Rectangle bounds = advinfo.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = advinfo.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								advinfo.setMinimumSize(preferredSize);
								advinfo.setPreferredSize(preferredSize);
							}
						}
						tab2.add(advinfo, "advinfo");

						//======== add_customer ========
						{
							add_customer.setBackground(UIManager.getColor("Button.background"));
							add_customer.setLayout(null);

							//======== panel19 ========
							{
								panel19.setBorder(new CompoundBorder(
									new TitledBorder(null, "Add Customer Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel19.setLayout(new GridBagLayout());
								((GridBagLayout)panel19.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel19.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 80, 0};
								((GridBagLayout)panel19.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel19.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label31 ----
								label31.setText("Persional ID:");
								label31.setHorizontalAlignment(SwingConstants.RIGHT);
								label31.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel19.add(label31, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- textField17 ----
								textField17.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel19.add(textField17, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button45 ----
								button45.setText("Clear");
								panel19.add(button45, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label33 ----
								label33.setText("Name:");
								label33.setFont(new Font("Times New Roman", label33.getFont().getStyle(), label33.getFont().getSize() + 2));
								label33.setHorizontalAlignment(SwingConstants.RIGHT);
								panel19.add(label33, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel19.add(textField18, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button46 ----
								button46.setText("Clear");
								panel19.add(button46, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label32 ----
								label32.setText("Customer Type:");
								label32.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								label32.setHorizontalAlignment(SwingConstants.RIGHT);
								panel19.add(label32, new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//======== panel1 ========
								{
									panel1.setLayout(new GridLayout(3, 1));

									//---- radioButton1 ----
									radioButton1.setText("Normal Customer");
									radioButton1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
									radioButton1.setSelected(true);
									panel1.add(radioButton1);

									//---- radioButton2 ----
									radioButton2.setText("VIP Customer");
									radioButton2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
									panel1.add(radioButton2);

									//---- radioButton3 ----
									radioButton3.setText("Enterprise Customer");
									radioButton3.setFont(new Font("Times New Roman", Font.PLAIN, 14));
									panel1.add(radioButton3);
								}
								panel19.add(panel1, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
							}
							add_customer.add(panel19);
							panel19.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < add_customer.getComponentCount(); i++) {
									Rectangle bounds = add_customer.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = add_customer.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								add_customer.setMinimumSize(preferredSize);
								add_customer.setPreferredSize(preferredSize);
							}
						}
						tab2.add(add_customer, "add_customer");

						//======== addattorney ========
						{
							addattorney.setBackground(UIManager.getColor("Button.background"));
							addattorney.setLayout(null);

							//======== panel24 ========
							{
								panel24.setBorder(new CompoundBorder(
									new TitledBorder(null, "Add Attorney Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel24.setLayout(new GridBagLayout());
								((GridBagLayout)panel24.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel24.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel24.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel24.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label49 ----
								label49.setText("Attorney ID:");
								label49.setFont(new Font("Times New Roman", label49.getFont().getStyle(), label49.getFont().getSize() + 2));
								label49.setHorizontalAlignment(SwingConstants.RIGHT);
								panel24.add(label49, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel24.add(textField27, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button61 ----
								button61.setText("Clear");
								panel24.add(button61, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label50 ----
								label50.setText("Attorney Passwd:");
								label50.setFont(new Font("Times New Roman", label50.getFont().getStyle(), label50.getFont().getSize() + 2));
								label50.setHorizontalAlignment(SwingConstants.RIGHT);
								panel24.add(label50, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel24.add(passwordField11, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button62 ----
								button62.setText("Clear");
								panel24.add(button62, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							addattorney.add(panel24);
							panel24.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < addattorney.getComponentCount(); i++) {
									Rectangle bounds = addattorney.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = addattorney.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								addattorney.setMinimumSize(preferredSize);
								addattorney.setPreferredSize(preferredSize);
							}
						}
						tab2.add(addattorney, "addattorney");

						//======== open ========
						{
							open.setBackground(UIManager.getColor("Button.background"));
							open.setLayout(null);

							//======== panel13 ========
							{
								panel13.setBorder(new CompoundBorder(
									new TitledBorder(null, "Open Account Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel13.setLayout(new GridBagLayout());
								((GridBagLayout)panel13.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel13.getLayout()).rowHeights = new int[] {35, 35, 35, 35, 35, 80, 0};
								((GridBagLayout)panel13.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel13.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0E-4};

								//---- label12 ----
								label12.setText("Persional ID:");
								label12.setHorizontalAlignment(SwingConstants.RIGHT);
								label12.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel13.add(label12, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- textField9 ----
								textField9.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel13.add(textField9, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button20 ----
								button20.setText("Clear");
								panel13.add(button20, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label13 ----
								label13.setText("Account Type:");
								label13.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								label13.setHorizontalAlignment(SwingConstants.RIGHT);
								panel13.add(label13, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//======== panel14 ========
								{
									panel14.setLayout(new BorderLayout());

									//---- radioButton5 ----
									radioButton5.setText("Saving Account");
									radioButton5.setSelected(true);
									radioButton5.setFont(new Font("Times New Roman", Font.PLAIN, 14));
									panel14.add(radioButton5, BorderLayout.WEST);

									//---- radioButton6 ----
									radioButton6.setText("Time Account");
									radioButton6.setFont(new Font("Times New Roman", Font.PLAIN, 14));
									panel14.add(radioButton6, BorderLayout.CENTER);
								}
								panel13.add(panel14, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- label14 ----
								label14.setText("Initial Balance:");
								label14.setFont(new Font("Times New Roman", label14.getFont().getStyle(), label14.getFont().getSize() + 2));
								label14.setHorizontalAlignment(SwingConstants.RIGHT);
								panel13.add(label14, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel13.add(textField10, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button21 ----
								button21.setText("Clear");
								panel13.add(button21, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label15 ----
								label15.setText("Passwd:");
								label15.setFont(new Font("Times New Roman", label15.getFont().getStyle(), label15.getFont().getSize() + 2));
								label15.setHorizontalAlignment(SwingConstants.RIGHT);
								panel13.add(label15, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel13.add(passwordField5, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button22 ----
								button22.setText("Clear");
								panel13.add(button22, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label16 ----
								label16.setText("Confirm Passwd:");
								label16.setHorizontalAlignment(SwingConstants.RIGHT);
								label16.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel13.add(label16, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel13.add(passwordField6, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button23 ----
								button23.setText("Clear");
								panel13.add(button23, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//======== panel8 ========
								{
									panel8.setLayout(new BorderLayout());
								}
								panel13.add(panel8, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							}
							open.add(panel13);
							panel13.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < open.getComponentCount(); i++) {
									Rectangle bounds = open.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = open.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								open.setMinimumSize(preferredSize);
								open.setPreferredSize(preferredSize);
							}
						}
						tab2.add(open, "open");

						//======== chpasswd ========
						{
							chpasswd.setBackground(UIManager.getColor("Button.background"));
							chpasswd.setLayout(null);

							//======== panel16 ========
							{
								panel16.setBorder(new CompoundBorder(
									new TitledBorder(null, "Change Passwd Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel16.setLayout(new GridBagLayout());
								((GridBagLayout)panel16.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel16.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel16.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel16.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label27 ----
								label27.setText("New Passwd:");
								label27.setFont(new Font("Times New Roman", label27.getFont().getStyle(), label27.getFont().getSize() + 2));
								label27.setHorizontalAlignment(SwingConstants.RIGHT);
								panel16.add(label27, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel16.add(passwordField4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button31 ----
								button31.setText("Clear");
								panel16.add(button31, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label28 ----
								label28.setText("Confirm Passwd:");
								label28.setFont(new Font("Times New Roman", label28.getFont().getStyle(), label28.getFont().getSize() + 2));
								label28.setHorizontalAlignment(SwingConstants.RIGHT);
								panel16.add(label28, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel16.add(passwordField7, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button32 ----
								button32.setText("Clear");
								panel16.add(button32, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							chpasswd.add(panel16);
							panel16.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < chpasswd.getComponentCount(); i++) {
									Rectangle bounds = chpasswd.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = chpasswd.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								chpasswd.setMinimumSize(preferredSize);
								chpasswd.setPreferredSize(preferredSize);
							}
						}
						tab2.add(chpasswd, "chpasswd");

						//======== cancel ========
						{
							cancel.setBackground(UIManager.getColor("Button.background"));
							cancel.setLayout(null);

							//======== panel17 ========
							{
								panel17.setBorder(new CompoundBorder(
									new TitledBorder(null, "Cancel Account", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel17.setLayout(new GridBagLayout());
								((GridBagLayout)panel17.getLayout()).columnWidths = new int[] {120, 220, 50, 0};
								((GridBagLayout)panel17.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel17.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel17.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label29 ----
								label29.setText("Canel Account Confirm:");
								label29.setFont(new Font("Times New Roman", label29.getFont().getStyle(), label29.getFont().getSize() + 2));
								label29.setHorizontalAlignment(SwingConstants.RIGHT);
								panel17.add(label29, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- radioButton7 ----
								radioButton7.setText("Yes");
								panel17.add(radioButton7, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
							}
							cancel.add(panel17);
							panel17.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < cancel.getComponentCount(); i++) {
									Rectangle bounds = cancel.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = cancel.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								cancel.setMinimumSize(preferredSize);
								cancel.setPreferredSize(preferredSize);
							}
						}
						tab2.add(cancel, "cancel");
					}
					panel6.add(tab2);
					tab2.setBounds(150, 15, 540, 375);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < panel6.getComponentCount(); i++) {
							Rectangle bounds = panel6.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel6.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel6.setMinimumSize(preferredSize);
						panel6.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("Advanced Operation", panel6);


				//======== panel4 ========
				{
					panel4.setLayout(null);

					//---- button40 ----
					button40.setText("Submit");
					button40.setFont(new Font("Times New Roman", Font.PLAIN, 16));
					panel4.add(button40);
					button40.setBounds(170, 393, 175, 40);

					//---- button41 ----
					button41.setText("Clear All");
					panel4.add(button41);
					button41.setBounds(463, 393, 175, 40);

					//======== panel20 ========
					{
						panel20.setBackground(UIManager.getColor("Button.background"));
						panel20.setLayout(new GridLayout(7, 1, 0, 20));

						//---- button42 ----
						button42.setText("Add");
						button42.setMargin(new Insets(2, 0, 2, 0));
						button42.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button42.setName("addem");
						button42.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel20.add(button42);

						//---- button43 ----
						button43.setText("Del");
						button43.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button43.setName("delem");
						button43.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel20.add(button43);

						//---- button44 ----
						button44.setText("Change Info");
						button44.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button44.setMargin(new Insets(2, 0, 2, 0));
						button44.setName("chem");
						button44.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel20.add(button44);

						//---- button47 ----
						button47.setText("Check Subordinate");
						button47.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button47.setMargin(new Insets(2, 0, 2, 0));
						button47.setName("getSubordinate");
						button47.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel20.add(button47);

						//---- button48 ----
						button48.setText("Check All");
						button48.setFont(new Font("Times New Roman", Font.PLAIN, 14));
						button48.setMargin(new Insets(2, 0, 2, 0));
						button48.setName("getAll");
						button48.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								changeCard(e);
							}
						});
						panel20.add(button48);
					}
					panel4.add(panel20);
					panel20.setBounds(30, 30, 95, 420);

					//======== tab3 ========
					{
						tab3.setBackground(Color.white);
						tab3.setName("tab3");
						tab3.setLayout(new CardLayout());

						//======== panel22 ========
						{
							panel22.setLayout(null);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel22.getComponentCount(); i++) {
									Rectangle bounds = panel22.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel22.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel22.setMinimumSize(preferredSize);
								panel22.setPreferredSize(preferredSize);
							}
						}
						tab3.add(panel22, "card8");

						//======== addem ========
						{
							addem.setBackground(UIManager.getColor("Button.background"));
							addem.setName("addem");
							addem.setLayout(null);

							//======== panel29 ========
							{
								panel29.setBorder(new CompoundBorder(
									new TitledBorder(null, "Add Employee", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel29.setLayout(new GridBagLayout());
								((GridBagLayout)panel29.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel29.getLayout()).rowHeights = new int[] {30, 30, 30, 30, 30, 30, 80, 0};
								((GridBagLayout)panel29.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel29.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0E-4};

								//---- label59 ----
								label59.setText("Persional ID:");
								label59.setHorizontalAlignment(SwingConstants.RIGHT);
								label59.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel29.add(label59, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- textField34 ----
								textField34.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel29.add(textField34, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button74 ----
								button74.setText("Clear");
								panel29.add(button74, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label60 ----
								label60.setText("Passwd:");
								label60.setFont(new Font("Times New Roman", label60.getFont().getStyle(), label60.getFont().getSize() + 2));
								label60.setHorizontalAlignment(SwingConstants.RIGHT);
								panel29.add(label60, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel29.add(passwordField19, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button75 ----
								button75.setText("Clear");
								panel29.add(button75, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label61 ----
								label61.setText("Name:");
								label61.setHorizontalAlignment(SwingConstants.RIGHT);
								label61.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel29.add(label61, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel29.add(textField35, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button76 ----
								button76.setText("Clear");
								panel29.add(button76, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label62 ----
								label62.setText("Age:");
								label62.setFont(new Font("Times New Roman", label62.getFont().getStyle(), label62.getFont().getSize() + 2));
								label62.setHorizontalAlignment(SwingConstants.RIGHT);
								panel29.add(label62, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel29.add(textField38, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button77 ----
								button77.setText("Clear");
								panel29.add(button77, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label63 ----
								label63.setText("Phone:");
								label63.setFont(new Font("Times New Roman", label63.getFont().getStyle(), label63.getFont().getSize() + 2));
								label63.setHorizontalAlignment(SwingConstants.RIGHT);
								panel29.add(label63, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel29.add(textField39, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button78 ----
								button78.setText("Clear");
								panel29.add(button78, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//---- label67 ----
								label67.setText("Address:");
								label67.setFont(new Font("Times New Roman", label67.getFont().getStyle(), label67.getFont().getSize() + 2));
								label67.setHorizontalAlignment(SwingConstants.RIGHT);
								panel29.add(label67, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));
								panel29.add(textField40, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 20), 0, 0));

								//---- button49 ----
								button49.setText("text");
								panel29.add(button49, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

								//======== panel26 ========
								{
									panel26.setLayout(new BorderLayout());
								}
								panel29.add(panel26, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 20), 0, 0));
							}
							addem.add(panel29);
							panel29.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < addem.getComponentCount(); i++) {
									Rectangle bounds = addem.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = addem.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								addem.setMinimumSize(preferredSize);
								addem.setPreferredSize(preferredSize);
							}
						}
						tab3.add(addem, "addem");

						//======== delem ========
						{
							delem.setBackground(UIManager.getColor("Button.background"));
							delem.setLayout(null);

							//======== panel30 ========
							{
								panel30.setBorder(new CompoundBorder(
									new TitledBorder(null, "Delete Employee", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel30.setLayout(new GridBagLayout());
								((GridBagLayout)panel30.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel30.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel30.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel30.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label64 ----
								label64.setText("Target Persional ID:");
								label64.setHorizontalAlignment(SwingConstants.RIGHT);
								label64.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel30.add(label64, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- textField36 ----
								textField36.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel30.add(textField36, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button79 ----
								button79.setText("Clear");
								panel30.add(button79, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label65 ----
								label65.setText("Your Passwd:");
								label65.setFont(new Font("Times New Roman", label65.getFont().getStyle(), label65.getFont().getSize() + 2));
								label65.setHorizontalAlignment(SwingConstants.RIGHT);
								panel30.add(label65, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel30.add(passwordField20, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button80 ----
								button80.setText("Clear");
								panel30.add(button80, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));
							}
							delem.add(panel30);
							panel30.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < delem.getComponentCount(); i++) {
									Rectangle bounds = delem.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = delem.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								delem.setMinimumSize(preferredSize);
								delem.setPreferredSize(preferredSize);
							}
						}
						tab3.add(delem, "delem");

						//======== chem ========
						{
							chem.setBackground(UIManager.getColor("Button.background"));
							chem.setLayout(null);

							//======== panel25 ========
							{
								panel25.setBorder(new CompoundBorder(
									new TitledBorder(null, "Change Employee Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel25.setLayout(new GridBagLayout());
								((GridBagLayout)panel25.getLayout()).columnWidths = new int[] {98, 259, 50, 0};
								((GridBagLayout)panel25.getLayout()).rowHeights = new int[] {45, 45, 45, 46, 80, 0};
								((GridBagLayout)panel25.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel25.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label40 ----
								label40.setText("Your Passwd:");
								label40.setFont(new Font("Times New Roman", label40.getFont().getStyle(), label40.getFont().getSize() + 2));
								label40.setHorizontalAlignment(SwingConstants.RIGHT);
								panel25.add(label40, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel25.add(passwordField14, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button55 ----
								button55.setText("Clear");
								panel25.add(button55, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label39 ----
								label39.setText("Type:");
								label39.setFont(new Font("Times New Roman", label39.getFont().getStyle(), label39.getFont().getSize() + 2));
								label39.setHorizontalAlignment(SwingConstants.RIGHT);
								panel25.add(label39, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- comboBox1 ----
								comboBox1.setModel(new DefaultComboBoxModel(new String[] {
									"Passwd",
									"Age",
									"Phone",
									"Address"
								}));
								comboBox1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel25.add(comboBox1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
									new Insets(15, 0, 35, 150), 0, 0));

								//---- label66 ----
								label66.setText("New Value:");
								label66.setFont(new Font("Times New Roman", label66.getFont().getStyle(), label66.getFont().getSize() + 2));
								label66.setHorizontalAlignment(SwingConstants.RIGHT);
								panel25.add(label66, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel25.add(textField41, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button57 ----
								button57.setText("Clear");
								panel25.add(button57, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label68 ----
								label68.setText("Enter Again:");
								label68.setFont(new Font("Times New Roman", label68.getFont().getStyle(), label68.getFont().getSize() + 2));
								label68.setHorizontalAlignment(SwingConstants.RIGHT);
								panel25.add(label68, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel25.add(textField42, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button81 ----
								button81.setText("Clear");
								panel25.add(button81, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//======== panel27 ========
								{
									panel27.setLayout(new BorderLayout());
								}
								panel25.add(panel27, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 20), 0, 0));
							}
							chem.add(panel25);
							panel25.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < chem.getComponentCount(); i++) {
									Rectangle bounds = chem.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = chem.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								chem.setMinimumSize(preferredSize);
								chem.setPreferredSize(preferredSize);
							}
						}
						tab3.add(chem, "chem");

						//======== getSubordinate ========
						{
							getSubordinate.setBackground(UIManager.getColor("Button.background"));
							getSubordinate.setLayout(null);

							//======== panel31 ========
							{
								panel31.setBorder(new CompoundBorder(
									new TitledBorder(null, "Get Subordinate Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel31.setLayout(new GridBagLayout());
								((GridBagLayout)panel31.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel31.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel31.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel31.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label69 ----
								label69.setText("Subordinate ID:");
								label69.setHorizontalAlignment(SwingConstants.RIGHT);
								label69.setFont(new Font("Times New Roman", Font.PLAIN, 14));
								panel31.add(label69, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- textField37 ----
								textField37.setFont(new Font("\u9ed1\u4f53", Font.BOLD, 18));
								panel31.add(textField37, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button82 ----
								button82.setText("Clear");
								panel31.add(button82, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//---- label70 ----
								label70.setText("Your Passwd:");
								label70.setFont(new Font("Times New Roman", label70.getFont().getStyle(), label70.getFont().getSize() + 2));
								label70.setHorizontalAlignment(SwingConstants.RIGHT);
								panel31.add(label70, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel31.add(passwordField21, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button83 ----
								button83.setText("Clear");
								panel31.add(button83, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//======== scrollPane1 ========
								{

									//---- table1 ----
									table1.setModel(new DefaultTableModel(
										new Object[][] {
											{"000000", "000000", "treterter", "45", "12345678901", "dddddddddddddddddddd"},
										},
										new String[] {
											"Job_Num", "ID", "Name", "Age", "Phone", "Address"
										}
									) {
										boolean[] columnEditable = new boolean[] {
											false, false, false, false, false, false
										};
										@Override
										public boolean isCellEditable(int rowIndex, int columnIndex) {
											return columnEditable[columnIndex];
										}
									});
									{
										TableColumnModel cm = table1.getColumnModel();
										cm.getColumn(5).setPreferredWidth(150);
									}
									table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
									scrollPane1.setViewportView(table1);
								}
								panel31.add(scrollPane1, new GridBagConstraints(0, 2, 3, 3, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							}
							getSubordinate.add(panel31);
							panel31.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < getSubordinate.getComponentCount(); i++) {
									Rectangle bounds = getSubordinate.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = getSubordinate.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								getSubordinate.setMinimumSize(preferredSize);
								getSubordinate.setPreferredSize(preferredSize);
							}
						}
						tab3.add(getSubordinate, "getSubordinate");

						//======== getAll ========
						{
							getAll.setBackground(UIManager.getColor("Button.background"));
							getAll.setLayout(null);

							//======== panel32 ========
							{
								panel32.setBorder(new CompoundBorder(
									new TitledBorder(null, "Get Subordinate Information", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
										new Font("Times New Roman", Font.PLAIN, 12)),
									new EmptyBorder(5, 5, 5, 5)));
								panel32.setLayout(new GridBagLayout());
								((GridBagLayout)panel32.getLayout()).columnWidths = new int[] {100, 245, 50, 0};
								((GridBagLayout)panel32.getLayout()).rowHeights = new int[] {45, 45, 45, 45, 25, 0};
								((GridBagLayout)panel32.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
								((GridBagLayout)panel32.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

								//---- label72 ----
								label72.setText("Your Passwd:");
								label72.setFont(new Font("Times New Roman", label72.getFont().getStyle(), label72.getFont().getSize() + 2));
								label72.setHorizontalAlignment(SwingConstants.RIGHT);
								panel32.add(label72, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));
								panel32.add(passwordField22, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 20), 0, 0));

								//---- button85 ----
								button85.setText("Clear");
								panel32.add(button85, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 20, 0), 0, 0));

								//======== scrollPane2 ========
								{

									//---- table2 ----
									table2.setModel(new DefaultTableModel(
										new Object[][] {
											{null, "", null, null, null, "efkw;rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"},
										},
										new String[] {
											"Job_Num", "ID", "Name", "Age", "Phone", "Address"
										}
									) {
										boolean[] columnEditable = new boolean[] {
											false, false, false, false, false, false
										};
										@Override
										public boolean isCellEditable(int rowIndex, int columnIndex) {
											return columnEditable[columnIndex];
										}
									});
									{
										TableColumnModel cm = table2.getColumnModel();
										cm.getColumn(5).setPreferredWidth(150);
										cm.getColumn(5).setCellEditor(new DefaultCellEditor(
											new JComboBox(new DefaultComboBoxModel(new String[] {
												"fs;f",
												"fdslf;",
												"fsdnkvds;",
												"fdsfksd",
												"efkw;rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"
											}))));
									}
									table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
									scrollPane2.setViewportView(table2);
								}
								panel32.add(scrollPane2, new GridBagConstraints(0, 1, 3, 4, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));
							}
							getAll.add(panel32);
							panel32.setBounds(20, 20, 470, 340);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < getAll.getComponentCount(); i++) {
									Rectangle bounds = getAll.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = getAll.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								getAll.setMinimumSize(preferredSize);
								getAll.setPreferredSize(preferredSize);
							}
						}
						tab3.add(getAll, "getAll");
					}
					panel4.add(tab3);
					tab3.setBounds(150, 15, 540, 375);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < panel4.getComponentCount(); i++) {
							Rectangle bounds = panel4.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel4.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel4.setMinimumSize(preferredSize);
						panel4.setPreferredSize(preferredSize);
					}
				}
				tabbedPane1.addTab("Employee Operation", panel4);

			}
			frame1ContentPane.add(tabbedPane1);
			tabbedPane1.setBounds(0, 0, 734, 514);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < frame1ContentPane.getComponentCount(); i++) {
					Rectangle bounds = frame1ContentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = frame1ContentPane.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				frame1ContentPane.setMinimumSize(preferredSize);
				frame1ContentPane.setPreferredSize(preferredSize);
			}
			frame1.setSize(750, 550);
			frame1.setLocationRelativeTo(frame1.getOwner());
		}

		//---- buttonGroup2 ----
		ButtonGroup buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(radioButton1);
		buttonGroup2.add(radioButton2);
		buttonGroup2.add(radioButton3);

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButton5);
		buttonGroup1.add(radioButton6);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	
//		this.tabbedPane1.add(Ac);
		
		this.panel9.getLayout();
		this.frame1.setVisible(true);
		
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JFrame frame1;
	private JLabel label41;
	private JButton button50;
	private JLabel label42;
	private JLabel label43;
	private JScrollPane scrollPane3;
	private JTextArea textArea1;
	private JTabbedPane tabbedPane1;
	private JPanel panel3;
	private JButton button8;
	private JButton button10;
	private JPanel panel7;
	private JButton button51;
	private JButton button4;
	private JButton button6;
	private JButton button7;
	private JButton button5;
	private JButton button52;
	private JPanel tab1;
	private JPanel panel2;
	private JPanel acclogin;
	private JPanel panel18;
	private JLabel label7;
	private JTextField textField19;
	private JButton button54;
	private JLabel label35;
	private JPasswordField passwordField12;
	private JButton button63;
	private JPanel deposit;
	private JPanel panel9;
	private JLabel label4;
	private JTextField textField4;
	private JButton button13;
	private JPanel withdrawal;
	private JPanel panel10;
	private JLabel label5;
	private JTextField textField8;
	private JButton button18;
	private JPanel inquire;
	private JPanel panel11;
	private JLabel label10;
	private DateChooserJButton dateChooserJButton1;
	private JLabel label17;
	private DateChooserJButton dateChooserJButton2;
	private JScrollPane scrollPane4;
	private JTable table3;
	private JPanel transfer;
	private JPanel panel15;
	private JLabel label20;
	private JTextField textField12;
	private JButton button25;
	private JLabel label21;
	private JTextField textField1;
	private JButton button26;
	private JLabel label23;
	private JTextField textField2;
	private JButton button17;
	private JPanel acclogout;
	private JPanel panel21;
	private JLabel label34;
	private JRadioButton radioButton4;
	private JPanel panel6;
	private JButton button36;
	private JButton button37;
	private JPanel panel12;
	private JButton button53;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button39;
	private JButton button38;
	private JPanel tab2;
	private JPanel panel5;
	private JPanel advinfo;
	private JPanel panel23;
	private JLabel label36;
	private JTextField textField20;
	private JButton button56;
	private JLabel label37;
	private JTextField textField21;
	private JButton button64;
	private JLabel label38;
	private JPasswordField passwordField13;
	private JButton button65;
	private JPanel add_customer;
	private JPanel panel19;
	private JLabel label31;
	private JTextField textField17;
	private JButton button45;
	private JLabel label33;
	private JTextField textField18;
	private JButton button46;
	private JLabel label32;
	private JPanel panel1;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JRadioButton radioButton3;
	private JPanel addattorney;
	private JPanel panel24;
	private JLabel label49;
	private JTextField textField27;
	private JButton button61;
	private JLabel label50;
	private JPasswordField passwordField11;
	private JButton button62;
	private JPanel open;
	private JPanel panel13;
	private JLabel label12;
	private JTextField textField9;
	private JButton button20;
	private JLabel label13;
	private JPanel panel14;
	private JRadioButton radioButton5;
	private JRadioButton radioButton6;
	private JLabel label14;
	private JTextField textField10;
	private JButton button21;
	private JLabel label15;
	private JPasswordField passwordField5;
	private JButton button22;
	private JLabel label16;
	private JPasswordField passwordField6;
	private JButton button23;
	private JPanel panel8;
	private JPanel chpasswd;
	private JPanel panel16;
	private JLabel label27;
	private JPasswordField passwordField4;
	private JButton button31;
	private JLabel label28;
	private JPasswordField passwordField7;
	private JButton button32;
	private JPanel cancel;
	private JPanel panel17;
	private JLabel label29;
	private JRadioButton radioButton7;
	private JPanel panel4;
	private JButton button40;
	private JButton button41;
	private JPanel panel20;
	private JButton button42;
	private JButton button43;
	private JButton button44;
	private JButton button47;
	private JButton button48;
	private JPanel tab3;
	private JPanel panel22;
	private JPanel addem;
	private JPanel panel29;
	private JLabel label59;
	private JTextField textField34;
	private JButton button74;
	private JLabel label60;
	private JPasswordField passwordField19;
	private JButton button75;
	private JLabel label61;
	private JTextField textField35;
	private JButton button76;
	private JLabel label62;
	private JTextField textField38;
	private JButton button77;
	private JLabel label63;
	private JTextField textField39;
	private JButton button78;
	private JLabel label67;
	private JTextField textField40;
	private JButton button49;
	private JPanel panel26;
	private JPanel delem;
	private JPanel panel30;
	private JLabel label64;
	private JTextField textField36;
	private JButton button79;
	private JLabel label65;
	private JPasswordField passwordField20;
	private JButton button80;
	private JPanel chem;
	private JPanel panel25;
	private JLabel label40;
	private JPasswordField passwordField14;
	private JButton button55;
	private JLabel label39;
	private JComboBox comboBox1;
	private JLabel label66;
	private JTextField textField41;
	private JButton button57;
	private JLabel label68;
	private JTextField textField42;
	private JButton button81;
	private JPanel panel27;
	private JPanel getSubordinate;
	private JPanel panel31;
	private JLabel label69;
	private JTextField textField37;
	private JButton button82;
	private JLabel label70;
	private JPasswordField passwordField21;
	private JButton button83;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JPanel getAll;
	private JPanel panel32;
	private JLabel label72;
	private JPasswordField passwordField22;
	private JButton button85;
	private JScrollPane scrollPane2;
	private JTable table2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
