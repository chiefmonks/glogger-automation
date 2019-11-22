package com.glogger.automation.window;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = -1713206472131146147L;

	public AboutDialog(Frame parent) {
        super(parent);

        initUI();
    }

    private void initUI() {

    	Image image = Toolkit.getDefaultToolkit().getImage("logger.png");
    	Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    	
        ImageIcon icon = new ImageIcon(newImage);
        JLabel label = new JLabel(icon);

        JLabel name = new JLabel("Auto G 2.0.0");
        name.setFont(new Font("Serif", Font.BOLD, 13));
        
        JLabel author = new JLabel("By chiefmonks");
        author.setFont(new Font("Serif", Font.BOLD, 11));

        JButton btn = new JButton("OK");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        createLayout(name, author, label, btn);

        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("About");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getParent());
    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER)
                .addComponent(arg[0])
                .addComponent(arg[1])
                .addComponent(arg[2])
                .addComponent(arg[3])
                .addGap(50)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10)
                .addComponent(arg[0])
                .addGap(5)
                .addComponent(arg[1])
                .addGap(10)
                .addComponent(arg[2])
                .addGap(10)
                .addComponent(arg[3])
                .addGap(10)
        );

        pack();
    }
}
