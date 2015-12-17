package ca.csf.connect4.client.ui;

import ca.csf.connect4.shared.GameConfig;
import ca.csf.connect4.shared.Observer;

import ca.csf.connect4.server.models.Game;

import ca.csf.connect4.client.ClientController;
import ca.csf.connect4.shared.models.Cell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class View extends JFrame implements Observer, Serializable {

	private static final long serialVersionUID = 5185109962736774530L;
	private static final String iconsPath = "/resources/";
	private static final String[] iconsName = { "RedToken.png", "BlackToken.png" };

	private ClientController controller;
	private ImageIcon[] icons;
	private int observerId;

    private JMenuItem resignMenuItem;
    private final JTextField message = new JTextField(20);
    private final JPanel centerPane = new JPanel();
    private JButton[] controlButtons;
    private MyImageContainer[][] placeHolders;

	public View(ClientController controller) {
		try {
			initIcons();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, UiText.Errors.LOAD_RESOURCES_FAILED,
					UiText.Errors.ERROR,
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
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
		GameConfig config = this.controller.getConfig();
        this.addWindowListener(new WindowHandler(this, this.controller));
		initBoard(config.getRows(), config.getColumns());
        this.observerId = this.controller.registerObserver(this);
		this.setVisible(true);

	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu(UiText.GAME);
		this.resignMenuItem = new JMenuItem(UiText.RESIGN);
		this.resignMenuItem.addActionListener(new ResignActionHandler());
		gameMenu.add(resignMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu(UiText.HELP);
		JMenuItem aboutMenuItem = new JMenuItem(UiText.ABOUT);
		aboutMenuItem.addActionListener(new AboutActionHandler());
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);

		this.setJMenuBar(menuBar);
	}
	private void configureWindow() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(((screenSize.width * 3) / 6), ((screenSize.height * 4) / 7));
		setLocation(((screenSize.width - getWidth()) / 2), ((screenSize.height - getHeight()) / 2));
	}
	private void initIcons() throws IOException {
		int numberPlayers = Game.DEFAULT_NB_PLAYERS;
		icons = new ImageIcon[numberPlayers];
		StringBuilder pathBuilder = new StringBuilder();
		for (int i = 0; i < numberPlayers; ++i) {
			String path = pathBuilder.append(iconsPath).append(iconsName[i]).toString();
			icons[i] = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path)));
			pathBuilder.delete(0, pathBuilder.length());
		}
	}

    public void initBoard(int rows, int columns) {
        this.centerPane.removeAll();
        this.placeHolders = new MyImageContainer[columns][rows];
        this.controlButtons = new JButton[columns];

        centerPane.setLayout(new GridLayout(rows + 1, columns));

        for (int i = 0; i < columns; i++) {
            JButton button = new JButton("" + i);
            this.controlButtons[i] = button;
            button.addActionListener(new ButtonHandler(i));
            centerPane.add(button);
        }

        //for (int row = nbRows - 1; row >= 0; row--)
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
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
	public void changeControlButtonsEnableState(boolean state) {
        for (JButton button : controlButtons) {
            button.setEnabled(state);
        }
	}

	@Override
	public void gameWon(String winner) {
		StringBuilder sb = new StringBuilder();
		sb.append(UiText.GAME_OVER)
				.append(winner)
				.append(UiText.WINS_THE_GAME);
		this.message.setText(sb.toString());
		changeControlButtonsEnableState(false);
	}

	@Override
	public void columnFull(int x) {
		disableControlButton(x);
	}

	@Override
	public void boardFull() {
		this.message.setText(UiText.BOARD_FULL);
		changeControlButtonsEnableState(false);
	}

	@Override
	public void gameResigned(String winner) {
		StringBuilder sb = new StringBuilder();
		sb.append(UiText.GAME_RESIGNED)
				.append(winner)
				.append(UiText.WINS_THE_GAME);
		this.message.setText(sb.toString());
		changeControlButtonsEnableState(false);
	}

	@Override
	public void resetGame(int columns, int rows) {
		initBoard(rows, columns);
		changeControlButtonsEnableState(false);
	}

    @Override
    public void setUIEnabled(boolean enabled) {
        changeControlButtonsEnableState(enabled);
        this.resignMenuItem.setEnabled(enabled);
    }

    @Override
	public void updateCell(int x, int y, Cell cell) {
        if (cell == Cell.EMPTY) return;
		ImageIcon lastPlayedIcon = icons[cell.ordinal()];
		setIcon(x, y, lastPlayedIcon);
	}

	@Override
	public void setGrid(Cell[][] cells) {
        GameConfig config = this.controller.getConfig();
        int columns = config.getColumns();
        int rows = config.getRows();
        for (int i = 0; i < columns; i++ ) {
            for (int j = 0; j < rows; j++) {
                this.updateCell(i, j, cells[i][j]);
            }
        }
	}

	@Override
	public void updatePlayerTurn(String playerTurn) {
		this.message.setText(UiText.YOUR_TURN + playerTurn + UiText.YOUR_TURN_END);
	}

    private class WindowHandler extends WindowAdapter {

        private View view;
        private ClientController controller;

        public WindowHandler(View view, ClientController controller) {
            this.view = view;
            this.controller = controller;
        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            this.controller.unregisterObserver(view.observerId);
            super.windowClosing(windowEvent);
        }
    }
	private class ButtonHandler implements ActionListener {
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
	private class ResignActionHandler implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.out.println("Action on menu");
			controller.resign();
		}
    }
	private class AboutActionHandler implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent arg0)
		{
			JOptionPane.showMessageDialog(View.this, "GUI for Connect4\n420-520-SF TP1\n\nAuthor: François Gagnon", "About", JOptionPane.INFORMATION_MESSAGE);
		}
    }
}
