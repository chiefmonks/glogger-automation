package com.glogger.automation.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.glogger.automation.json.domain.Activity;
import com.glogger.automation.json.domain.Glogger;
import com.glogger.automation.page.History;
import com.glogger.automation.service.event.IMessageListener;
import com.glogger.automation.service.event.Message;
import com.glogger.automation.ui.ActivityTableModel;
import com.glogger.automation.ui.MultiLineTableCellRenderer;

public class HistoryWindow extends MessageEnabledWindow{
	
	private JFrame 		frame;
	private JDialog 	dialog;
	private	JPanel		topPanel;
	private	JTable		table;
	private	JScrollPane scrollPane;
	private ActivityTableModel model;
	
	public HistoryWindow(JFrame frame){
		super(Glogger.class);
		this.frame = frame;
		
		initComponents();
		
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage("logger.png"));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setTitle("Recent Activity");
		dialog.setSize(500, 300);
		dialog.setBackground( Color.gray );
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		
		
		initializeActions();
		
		dialog.setVisible(true);
	}
	
	private void initComponents(){
		dialog = new JDialog(this.frame);

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		dialog.getContentPane().add( topPanel );


		// Create columns names
		String columnNames[] = { " " };

		// Create some data
		String dataValues[][] ={{"Loading....."}};
		
		// Create a new table instance
		table = new JTable(dataValues, columnNames);
		model = new ActivityTableModel();
		
		// Add the table to a scrolling pane
		scrollPane = new JScrollPane( table );
		topPanel.add( scrollPane, BorderLayout.CENTER );
		
	}
	
	private void initializeActions(){
		dialog.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosed(WindowEvent e) {
		        removeAllWindowListeners();
		    }
		});
		
		addListener(Activity.class, null, new IMessageListener() {
			
			@Override
			public void messageReceived(Message message) {
				List<Activity> activities = message.getMessage();
				
				model.setActivities(activities);
				table.setModel(model);
				
				initializeRenderer();
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				new History();
			}
		}).start();
	}
	
	private void initializeRenderer(){
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		TableColumn tc = table.getColumn("Status");
		tc.setCellRenderer(renderer);
		
		MultiLineTableCellRenderer cellRender = new MultiLineTableCellRenderer();
		cellRender.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		
		tc = table.getColumn("Start Date");
		tc.setCellRenderer(cellRender);
		
		tc = table.getColumn("End Date");
		tc.setCellRenderer(cellRender);
		
		table.requestFocus();
		table.changeSelection(0,0,false, false);
		
	}
	
	public JDialog getDialog() {
		return dialog;
	}
}
