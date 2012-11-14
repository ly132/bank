package gui;

import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// should be six figures.
public class CheckInput extends PlainDocument{

	private String pattern = null;
//	private int maxLength = -1;
	private Toolkit toolkit = null;
//	private double maxValue = -1;
	
	public CheckInput(String p)
	{
		this.pattern = p;
//		this.maxLength = l;
		this.toolkit = Toolkit.getDefaultToolkit();
	}
	
//	public CheckInput(){};
//	public CheckInput(String p)	{this.pattern = p;}
//	public CheckInput(int l) {this.maxLength = l;}
	
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
//		char[] ch = str.toCharArray();
//		this.gett
//		for (int i = 0; i < ch.length; i++) {
//		    String temp = String.valueOf(ch[i]);
//		    // 如果要输入的字符不在允许范围内
//			if( !Pattern.compile(pattern).matcher(temp).find() )
//			   return;
//		}
		String thistxt = this.getText(0, getLength());
		String tmp = thistxt.substring(0,offs) + str + thistxt.substring(offs);
		if( !Pattern.compile(pattern).matcher(tmp).find() )
		{
			this.toolkit.beep();
			return;
		}
		// 如果有字符长度限制，并且现在的字符长度已经大于或等于限制
//		if (maxLength > -1 && this.getLength() >= maxLength) {
//		    return;
//		}
		super.insertString(offs, str, a);
//		super.remove(offs, len);
	}
	
//	public void removeString(int offs, int len) throws BadLocationException
//	{
//		String thistxt = this.getText(0, getLength());
//		String tmp = thistxt.substring(0,offs) + thistxt.substring(offs+len);
//		if( !Pattern.compile(pattern).matcher(tmp).find() )
//		{
//			this.toolkit.beep();
//			return;
//		}
//		super.remove(offs, len);
//	}
	
}
