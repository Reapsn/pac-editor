package org.reapsn.paceditor.view;

import org.reapsn.paceditor.model.PAC;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashSet;

/**
 * Created by Reaps on 2017/6/15.
 */
public class MainFrame extends JFrame {

	public MainFrame() {
		this.setLayout(new FlowLayout());
		this.setTitle("PAC EDITOR");
		this.setSize(600, 450);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);

		setContentPane(new ListPACFormWrapper().getForm());
	}
}
