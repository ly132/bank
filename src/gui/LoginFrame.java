/*
 * Created by JFormDesigner on Wed Nov 14 19:20:32 CST 2012
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import employee.Employee;
import employee.Foreground;

/**
 * @author ly142
 */
public class LoginFrame extends JFrame {
	public LoginFrame() {
		initComponents();
	}

	private void login(MouseEvent e) {
		Employee em = Employee.login(this.textField1.getText(),String.valueOf(this.passwordField1.getPassword()));
		if( em != null )
		{
			this.setVisible(false);
			new MainFrame(em);
			this.dispose();
		}
		else
			JOptionPane.showMessageDialog(this, "Job Number or Passwd Not Correct.", "Login Failed", JOptionPane.ERROR_MESSAGE);
	}

	private void loginframeClosing(WindowEvent e) {
		System.exit(0);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		panel2 = new JPanel();
		label1 = new JLabel();
		textField1 = new JTextField();
		label2 = new JLabel();
		passwordField1 = new JPasswordField();
		button1 = new JButton();

		//======== this ========
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				loginframeClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panel1 ========
		{
			panel1.setLayout(null);

			//======== panel2 ========
			{
				panel2.setLayout(new GridBagLayout());
				((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 35, 0, 35, 0, 0};
				((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0, 1.0, 1.0E-4};

				//---- label1 ----
				label1.setText("Job Number");
				panel2.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
				panel2.add(textField1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- label2 ----
				label2.setText("Passwd");
				panel2.add(label2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(10, 0, 0, 0), 0, 0));
				panel2.add(passwordField1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- button1 ----
				button1.setText("Login");
				button1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						login(e);
					}
				});
				panel2.add(button1, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(40, 25, 40, 25), 0, 0));
			}
			panel1.add(panel2);
			panel2.setBounds(70, 40, 230, 220);

			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < panel1.getComponentCount(); i++) {
					Rectangle bounds = panel1.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = panel1.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				panel1.setMinimumSize(preferredSize);
				panel1.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(panel1, BorderLayout.CENTER);
		setSize(395, 300);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		this.setVisible(true);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JPanel panel2;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label2;
	private JPasswordField passwordField1;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
