package ca.csf.connect4.client.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyImageContainer extends JLabel
{

	private static final long serialVersionUID = 1L;
	private ImageIcon icon = null;
	private int borderSize = 1;
	
	public MyImageContainer() {
		super();
		super.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
	}
	
	public void setImageIcon(ImageIcon icon)
	{
		if(icon != null)
		{
			//TODO ideally, we should strech the image evenly in both directions...
			this.icon = icon;
			Image image = icon.getImage(); 
			Image newimg = image.getScaledInstance(this.getCustomWidth(), this.getCustomHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			this.setIcon(new ImageIcon(newimg));
		}
	}
	
	public void addBorder()
	{
		this.borderSize = 5;
		super.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	}
	
	private int getCustomWidth()
	{
		return (int)Math.round(this.getSize().getWidth()) - this.borderSize*2;
	}
	
	private int getCustomHeight()
	{
		return (int)Math.round(this.getSize().getHeight()) - this.borderSize*2;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		this.setImageIcon(this.icon);
	}
}
