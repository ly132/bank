/*
 * Created by JFormDesigner on Wed Nov 07 14:04:41 CST 2012
 */

package gui;

import java.awt.*;
import javax.swing.*;

/**
 * @author ly142
 */
public class Ac extends JPanel {
	public Ac() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		label6 = new JLabel();
		button1 = new JButton();
		label7 = new JLabel();
		button2 = new JButton();
		label10 = new JLabel();
		button3 = new JButton();
		label8 = new JLabel();
		button4 = new JButton();
		label12 = new JLabel();
		button5 = new JButton();
		label9 = new JLabel();
		button7 = new JButton();
		label11 = new JLabel();
		button6 = new JButton();
		panel2 = new JPanel();
		panel3 = new JPanel();

		//======== this ========
		setLayout(new BorderLayout());

		//======== panel1 ========
		{
			panel1.setLayout(new GridLayout(7, 2, 0, 10));

			//---- label6 ----
			label6.setText("text");
			panel1.add(label6);

			//---- button1 ----
			button1.setText("text");
			panel1.add(button1);

			//---- label7 ----
			label7.setText("text");
			panel1.add(label7);

			//---- button2 ----
			button2.setText("text");
			panel1.add(button2);

			//---- label10 ----
			label10.setText("text");
			panel1.add(label10);

			//---- button3 ----
			button3.setText("text");
			panel1.add(button3);

			//---- label8 ----
			label8.setText("text");
			panel1.add(label8);

			//---- button4 ----
			button4.setText("text");
			panel1.add(button4);

			//---- label12 ----
			label12.setText("text");
			panel1.add(label12);

			//---- button5 ----
			button5.setText("text");
			panel1.add(button5);

			//---- label9 ----
			label9.setText("text");
			panel1.add(label9);

			//---- button7 ----
			button7.setText("text");
			panel1.add(button7);

			//---- label11 ----
			label11.setText("text");
			panel1.add(label11);

			//---- button6 ----
			button6.setText("text");
			panel1.add(button6);
		}
		add(panel1, BorderLayout.WEST);

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
		add(panel2, BorderLayout.NORTH);

		//======== panel3 ========
		{
			panel3.setLayout(null);

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
		add(panel3, BorderLayout.SOUTH);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel label6;
	private JButton button1;
	private JLabel label7;
	private JButton button2;
	private JLabel label10;
	private JButton button3;
	private JLabel label8;
	private JButton button4;
	private JLabel label12;
	private JButton button5;
	private JLabel label9;
	private JButton button7;
	private JLabel label11;
	private JButton button6;
	private JPanel panel2;
	private JPanel panel3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
