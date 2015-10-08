package ca.csf.connect4.ui;

import ca.csf.connect4.Cell;
import ca.csf.connect4.Connect4Controller;
import ca.csf.connect4.Observer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

    private final JTextField message = new JTextField(20);
    private final JPanel centerPane = new JPanel();

    private Connect4Controller controller;
    private JButton[] controlButtons;
    private MyImageContainer[][] placeHolders;

	public View(Connect4Controller controller)
	{
        this.controller = controller;

		this.setTitle("Connect4");

		this.configureWindow();

		this.setLayout(new BorderLayout());
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new FlowLayout());
		panelNorth.add(this.message);
		this.message.setEditable(false);
		this.message.setText(UiText.WELCOME);
		this.add(panelNorth, BorderLayout.NORTH);
		this.createMenu();
		this.setVisible(true);
	}

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu(UiText.GAME);
		JMenuItem resignMenuItem = new JMenuItem(UiText.RESIGN);
		resignMenuItem.addActionListener(new ResignActionHandler());
		gameMenu.add(resignMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu(UiText.HELP);
		JMenuItem aboutMenuItem = new JMenuItem(UiText.ABOUT);
		aboutMenuItem.addActionListener(new AboutActionHandler());
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);

		this.setJMenuBar(menuBar);
	}

	private void configureWindow()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(((screenSize.width * 3) / 6), ((screenSize.height * 4) / 7));
		setLocation(((screenSize.width - getWidth()) / 2), ((screenSize.height - getHeight()) / 2));
	}

    public void initBoard(int nbRows, int nbColumns) {
        this.centerPane.removeAll();
        this.placeHolders = new MyImageContainer[nbColumns][nbRows];
        this.controlButtons = new JButton[nbColumns];

        centerPane.setLayout(new GridLayout(nbRows + 1, nbColumns));

        for (int i = 0; i < nbColumns; i++)
        {
            JButton button = new JButton(Integer.toString(i));
            this.controlButtons[i] = button;
            button.addActionListener(new ButtonHandler(i));
            centerPane.add(button);
        }

        //for (int row = nbRows - 1; row >= 0; row--)
        for (int row = 0; row < nbRows; row++)
        {
            for (int column = 0; column < nbColumns; column++)
            {
                MyImageContainer button = new MyImageContainer();
                button.setOpaque(true);
                placeHolders[column][row] = button;
                centerPane.add(button);
            }
        }
        this.add(centerPane, BorderLayout.CENTER);
        this.revalidate();
    }
    public void setIcon(int x, int y, ImageIcon icon) {
        placeHolders[x][y].setImageIcon(icon);
    }
    public void disableControlButton(int x) {
        controlButtons[x].setEnabled(false);
    }
	public void enableAllControlButtons() {
        for (JButton button : controlButtons) {
            button.setEnabled(true);
        }
	}
    public JTextField getMessage() {
        return message;
    }

	private class ButtonHandler implements ActionListener
	{
        private final int columnIndex;

		private ButtonHandler(int columnIndex)
		{
			this.columnIndex = columnIndex;
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.out.println("Action on button: " + columnIndex);
            controller.dropToken(columnIndex);
		}
    }
	private class ResignActionHandler implements ActionListener
	{
        @Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.out.println("Action on menu");
			controller.resign();
		}
    }
	private class AboutActionHandler implements ActionListener
	{
        @Override
		public void actionPerformed(ActionEvent arg0)
		{
			JOptionPane.showMessageDialog(View.this, "GUI for Connect4\n420-520-SF TP1\n\nAuthor: François Gagnon", "About", JOptionPane.INFORMATION_MESSAGE);
		}
    }
}
