package com.glogger.automation.window;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.glogger.automation.constants.Constants;
import com.glogger.automation.constants.Status;
import com.glogger.automation.job.DynamicTrigger;
import com.glogger.automation.job.FixTimeTrigger;
import com.glogger.automation.json.domain.Credential;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.json.domain.StatusBar;
import com.glogger.automation.listener.IWndowListener;
import com.glogger.automation.page.Login;
import com.glogger.automation.service.event.IMessageListener;
import com.glogger.automation.service.event.Message;
import com.glogger.automation.util.ApplicationManager;
import com.glogger.automation.util.ApplicationManager.ApplicationInstanceListener;
import com.glogger.automation.util.DateUtil;
import com.glogger.automation.util.MinaUtil;
import com.glogger.automation.util.PropertyUtil;

public class GloggerWindow extends MessageEnabledWindow{
	private JFrame frame;
	private Image icon;
	private SystemTray sysTray;
	private PopupMenu menu;
	private MenuItem exitMenu;
	private MenuItem showMenu;
	private MenuItem historyMenu;
	private MenuItem aboutMenu;
	private TrayIcon trayIcon;
	
	private JLabel loginText;
	private JLabel mealText;
	private JLabel clockInText;
	private JLabel clockOutText;
	private JLabel signOutText;
	private JLabel statusText;
	private JLabel statusLabel;
	private DateUtil util = DateUtil.getInstance();
	
	private HistoryWindow historyWindow;
	

	// constructor
	public GloggerWindow() {
		super(Glogger.class);
		
		initMessaging();
		
		initComponents();
		frame.setSize(300, 240);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setResizable(false);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	    
	    frame.addWindowListener(new WindowAdapter() {
	    	public void windowIconified(WindowEvent e) {
	    		frame.setVisible(false);
	    	}
		});
	    
	    frame.setIconImage(icon);
	    
	    initiliazeActions();
	    
	    statusLabel.setText("Initialized");
	    
	    if (getDate(PropertyUtil.START) == null && getDate(PropertyUtil.MEAL) == null 
	    		&& getDate(PropertyUtil.IN) == null && getDate(PropertyUtil.END) == null 
	    			&& getDate(PropertyUtil.OUT) != null ){
	    	clear();
	    }
	}
	
	private void initiliazeActions(){
		addWindowListener(new IWndowListener(){

			@Override
			public void login(Message message) {

				Glogger glogger = message.getMessage();
				loginText.setText(util.getDate(glogger.getDate()));
				statusText.setText(glogger.getStatus().name());
				
				statusLabel.setText("Current status: " + glogger.getStatus().name());
			}

			@Override
			public void meal(Message message) {
				Glogger glogger = message.getMessage();
				mealText.setText(util.getDate(glogger.getDate()));
				statusText.setText(glogger.getStatus().name());
				
				statusLabel.setText("Current status: " + glogger.getStatus().name());
			}

			@Override
			public void clockIn(Message message) {
				Glogger glogger = message.getMessage();
				clockInText.setText(util.getDate(glogger.getDate()));
				statusText.setText(glogger.getStatus().name());

				statusLabel.setText("Current status: " + glogger.getStatus().name());
			}

			@Override
			public void logout(Message message) {
				Glogger glogger = message.getMessage();
				clockOutText.setText(util.getDate(glogger.getDate()));
				statusText.setText(glogger.getStatus().name());
				
				statusLabel.setText("Current status: " + glogger.getStatus().name());
				
			}

			@Override
			public void signout(Message message) {
				Glogger glogger = message.getMessage();
				signOutText.setText(util.getDate(glogger.getDate()));
				statusText.setText(glogger.getStatus().name());

				statusLabel.setText("Current status: " + glogger.getStatus().name());
			}
	    });
	    
	    addListener(Credential.class, null, new IMessageListener() {
			
			@Override
			public void messageReceived(Message message) {
				Credential credential = message.getMessage();
				showErrorMessage(credential.getMessage());
				System.exit(0);
			}
		});
	    
	    addListener(StatusBar.class, null, new IMessageListener() {
			
			@Override
			public void messageReceived(Message message) {
				String msg = message.getMessage();
				statusLabel.setText(msg);
			}
		});
	    
	    addListener(Login.class, null, new IMessageListener() {
			
			@Override
			public void messageReceived(Message message) {
				String msg = message.getMessage();
				statusLabel.setText(msg);
				
				clear();
			}
		});
	    
	    addListener(ApplicationManager.class, null, new IMessageListener() {
			
			@Override
			public void messageReceived(Message message) {
				frame.setState ( Frame.NORMAL );
				frame.setVisible(true);
			}
		});
	    
	}
	
	private void initMessaging(){
		MinaUtil.getInstance().getMessageServer();
		
		setMessageClient(MinaUtil.getInstance().getMessageClient());
	}

	private void initComponents() {
		// create jframe
		frame = new JFrame("Auto G");

		// check to see if system tray is supported on OS.
		if (SystemTray.isSupported()) {
			sysTray = SystemTray.getSystemTray();

			// create dog image
			icon = Toolkit.getDefaultToolkit().getImage("logger.png");

			// create popupmenu
			menu = new PopupMenu();

			// create item
			showMenu = new MenuItem("Show");
			historyMenu = new MenuItem("History");
			aboutMenu = new MenuItem("About");
			exitMenu = new MenuItem("Exit");
			
			// add item to menu
			menu.add(historyMenu);
			menu.addSeparator();
			menu.add(showMenu);
			menu.add(aboutMenu);
			menu.addSeparator();
			menu.add(exitMenu);

			// add action listener to the item in the popup menu
			exitMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			//add actionListener to second menu item
			showMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.setState ( Frame.NORMAL );
					frame.setVisible(true);
				}
			});
			
			//add actionListener to second menu item
			historyMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showHistory();
				}
			});
			
			//add actionListener to second menu item
			aboutMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new AboutDialog(frame).setVisible(true);;
				}
			});

			// create system tray icon.
			trayIcon = new TrayIcon(icon, "Auto G", menu);
			trayIcon.setImageAutoSize(true);
			
			trayIcon.addMouseListener(new MouseAdapter() {

			    @Override
			    public void mouseClicked(MouseEvent evt) {
			        if (evt.getClickCount() == 2) {
			        	frame.setState ( Frame.NORMAL );
						frame.setVisible(true);
			        }
			    }
			});

			// add the tray icon to the system tray.
			try {
				sysTray.add(trayIcon);
			} catch (AWTException e) {
				System.out.println(e.getMessage());
			}
		}
		
		initContent();
	}
	
	private void showHistory(){
		EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
            	if (historyWindow == null){
					historyWindow = new HistoryWindow(frame);
				}else{
					if (historyWindow.getDialog().isShowing()){
						historyWindow.getDialog().setVisible(true);
					}else{
						historyWindow = new HistoryWindow(frame);
					}
				}
            }
        });
	}
	
	private void initContent(){
		frame.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
	    panel.setSize(280, 200);
	    panel.setLayout(new GridBagLayout());
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 20;
		
		JLabel loginLabel = createLabel("Login:");
		JLabel mealLabel = createLabel("Meal:");
		JLabel clockInLabel = createLabel("Clock-In:");
		JLabel clockOutLabel = createLabel("Clock-Out:");
		JLabel signOutLabel = createLabel("Sign-Out:");
		JLabel status = createLabel("Status:");
		
		loginText = createLabel(getDate(PropertyUtil.START), SwingConstants.LEFT , new Font("Serif", Font.BOLD, 14));
		mealText = createLabel(getDate(PropertyUtil.MEAL), SwingConstants.LEFT , new Font("Serif", Font.BOLD, 14));
		clockInText = createLabel(getDate(PropertyUtil.IN), SwingConstants.LEFT , new Font("Serif", Font.BOLD, 14));
		clockOutText = createLabel(getDate(PropertyUtil.END), SwingConstants.LEFT , new Font("Serif", Font.BOLD, 14));
		signOutText = createLabel(getDate(PropertyUtil.OUT), SwingConstants.LEFT , new Font("Serif", Font.BOLD, 14));
		statusText = createLabelWithBorder(getStatus(), SwingConstants.CENTER, new Font("Serif", Font.BOLD, 20));

		c.weightx = 0.5;
		addComponent(panel, loginLabel, c, 0, 0);
		addComponent(panel, loginText, c, 1, 0);
		addComponent(panel, status, c, 2, 0);
		
		addComponent(panel, mealLabel, c, 0, 1);
		addComponent(panel, mealText, c, 1, 1);
		c.gridheight = 4;
		c.fill = GridBagConstraints.BOTH;
		addComponent(panel, statusText, c, 2, 1);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridheight = 1;
		addComponent(panel, clockInLabel, c, 0, 2);
		addComponent(panel, clockInText, c, 1, 2);
		addComponent(panel, clockOutLabel, c, 0, 3);
		addComponent(panel, clockOutText, c, 1, 3);
		addComponent(panel, signOutLabel, c, 0, 4);
		addComponent(panel, signOutText, c, 1, 4);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		statusLabel = new JLabel("Initialize....");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		statusPanel.add(statusLabel);

		frame.add(panel, BorderLayout.NORTH);
		frame.add(statusPanel, BorderLayout.SOUTH);
	}
	
	private void addComponent(Container container, Component component, GridBagConstraints c, int x, int y){
		c.gridx = x;
		c.gridy = y;
		container.add(component, c);
	}
	
	public JLabel createLabelWithBorder(String text) {
		return createLabelWithBorder(text, null);
	}
	
	public JLabel createLabelWithBorder(String text, Font font) {
		return createLabelWithBorder(text, 0, null);
	}
	
	public JLabel createLabelWithBorder(String text, int contants, Font font) {
		JLabel label = createLabel(text,contants, font);
		// create a line border with the specified color and width
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

		// set the border of this component
		label.setBorder(border);
		
		return label;
	}
	
	public JLabel createLabel(String text) {
		return createLabel(text, 0, null);
	}
	
	public JLabel createLabel(String text, int contants) {
		return createLabel(text, contants, null);
	}
	

	public JLabel createLabel(String text, int contants, Font font) {
		JLabel label = new JLabel(text, contants);
		label.setFont(new Font("Serif", Font.PLAIN, 14));
		if (font != null){
			label.setFont(font);
		}
		
		return label;
	}
	
	public void showErrorMessage(String text){
	    Toolkit.getDefaultToolkit().beep();
	    JOptionPane optionPane = new JOptionPane(text,JOptionPane.ERROR_MESSAGE);
	    JDialog dialog = optionPane.createDialog(text);
	    dialog.setAlwaysOnTop(true);
	    dialog.setVisible(true);
	}
	
	public String getDate(String key){
		Date date = DateUtil.getInstance().getDateFromProperty(key);
		if (date == null)
			return "";
		
		return util.getDate(date);
	}
	
	public String getStatus(){
		String status = "";
		if (!loginText.getText().isEmpty()){
			status = Status.LOGIN.name();
		}
		if (!mealText.getText().isEmpty()){
			status = Status.MEAL.name();
		}
		if (!clockInText.getText().isEmpty()){
			status = Status.CLOCK_IN.name();
		}
		if (!clockOutText.getText().isEmpty()){
			status = Status.CLOCK_OUT.name();
		}
		if (!signOutText.getText().isEmpty()){
			status = Status.SIGN_OUT.name();
		}
		return status;
	}
	
	
	private void clear(){
		loginText.setText("");
    	mealText.setText("");
    	clockInText.setText("");
    	clockOutText.setText("");
    	signOutText.setText("");
    	statusText.setText("");
	}
	
	// main
	public static void main(String[] args) {
		
		ApplicationManager applicationMgr = ApplicationManager.getInstance();
		applicationMgr.addListener(new ApplicationInstanceListener() {
			
			@Override
			public void createIntance() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new GloggerWindow();
						new Thread(new Runnable() {
							@Override
							public void run() {
								if (Constants.IS_DYNAMIC){
									new DynamicTrigger();
								}else{
									new FixTimeTrigger();
								}
							}
						}).start();
					}
				});
			}
		});
		
		if (!applicationMgr.registerInstance()){
			// instance already running.
            System.exit(0);
		}
	}
}
