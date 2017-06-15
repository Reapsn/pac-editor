package org.reapsn.paceditor.view;

import org.reapsn.paceditor.model.PAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;

/**
 * Created by Reaps on 2017/6/15.
 */
public class MainFrame extends JFrame {

	private LinkedHashSet<PAC> pacs;
	private JPanel contentPanel;

	public MainFrame(LinkedHashSet<PAC> pacs) {
		initFrame();
		setPacs(pacs);
	}

	private void initFrame() {
		this.setLayout(new GridLayout(1, 1));
		this.setTitle("PAC EDITOR");
		this.setSize(600, 450);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new GridLayout(1, 1));
		this.add(contentPanel);
	}

	public LinkedHashSet<PAC> getPacs() {
		return pacs;
	}

	public void setPacs(final LinkedHashSet<PAC> pacs) {

		for (PAC pac :
				pacs) {

			this.contentPanel.add(createPanelForPAC(pac));
		}
		this.setVisible(false);
		this.setVisible(true);
		this.pacs = pacs;
	}

	private JPanel createPanelForPAC(final PAC pac) {
		JPanel panel_pac = new JPanel();
		panel_pac.setLayout(new GridLayout(1, 3));

		final JTextField textField_name = new JTextField(pac.getName());
		final JTextField textField_proxy = new JTextField(pac.getProxy());
		final JButton button_editPac = new JButton("oker");

		button_editPac.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final EditPACDialog editPACDialog = new EditPACDialog(pac);
				editPACDialog.setVisible(true);
			}
		});

		panel_pac.add(textField_name);
		panel_pac.add(textField_proxy);
		panel_pac.add(button_editPac);

		return panel_pac;
	}

}
