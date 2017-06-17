package org.reapsn.paceditor.view;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.reapsn.paceditor.PACBuilder;
import org.reapsn.paceditor.model.PAC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by Reaps on 2017/6/16.
 */
public class ListPACFormWrapper {

	private static final Logger logger = LoggerFactory.getLogger(ListPACFormWrapper.class);

	private JPanel form;
	private JButton button_generatePAC;
	private JPanel panel_pacs;
	private JButton button_importPAC;
	private LinkedHashSet<PAC> pacs;

	public ListPACFormWrapper() {

		this.panel_pacs.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				JScrollPane scrollPane = (JScrollPane) e.getComponent().getParent().getParent();
				JScrollBar vertical = scrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
			}
		});

		button_generatePAC.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onExportPAC();
			}
		});

		button_importPAC.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onImportPAC();
			}
		});

		try {
			File pacFile = new File(String.format("%s/pac/pac.pac", FileUtils.getUserDirectoryPath()));
			if (pacFile.exists()) {
				String pacString = FileUtils.readFileToString(pacFile, "UTF-8");
				LinkedHashSet<PAC> temp_pacs = PACBuilder.decodeFromPAC(pacString);
				setPacs(temp_pacs);
			} else {
				setPacs(new LinkedHashSet<PAC>());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			setPacs(new LinkedHashSet<PAC>());
		}

	}

	private void onImportPAC() {

		try {

			JFileChooser fileChooser = new JFileChooser(FileUtils.getUserDirectoryPath() + "/pac");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("PAC文件(*.pac,*.txt)", "pac", "txt");
			fileChooser.setFileFilter(filter);

			fileChooser.setSelectedFile(new File("pac/pac.pac"));

			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getForm())) {

				File selectedFile = fileChooser.getSelectedFile();

				if (selectedFile != null) {
					String pacString = FileUtils.readFileToString(selectedFile, "UTF-8");
					LinkedHashSet<PAC> temp_pacs = PACBuilder.decodeFromPAC(pacString);

					LinkedHashSet<PAC> temp_pacs_2 = new LinkedHashSet<PAC>();
					temp_pacs_2.addAll(pacs);
					temp_pacs_2.addAll(temp_pacs);

					setPacs(temp_pacs_2);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			JOptionPane.showMessageDialog(null, e.getMessage());
		}

	}

	private void onExportPAC() {
		try {

			JFileChooser fileChooser = new JFileChooser(FileUtils.getUserDirectoryPath() + "/pac");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("PAC文件(*.pac,*.txt)", "pac", "txt");
			fileChooser.setFileFilter(filter);

			fileChooser.setSelectedFile(new File("pac.pac"));

			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(getForm())) {

				File selectedFile = fileChooser.getSelectedFile();

				if (selectedFile != null) {
					String pac = new PACBuilder(pacs).build();
					FileUtils.writeStringToFile(selectedFile, pac, "GBK");
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);

			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public JPanel getForm() {
		return form;
	}

	public void setPacs(final LinkedHashSet<PAC> pacs) {

		this.panel_pacs.setPreferredSize(new Dimension(576, 0));

		// 清空原来的输入框
		Component[] components = this.panel_pacs.getComponents();
		for (Component component :
				components) {
			if (component instanceof JPanel) {
				this.panel_pacs.remove(component);
			}
		}

		Iterator<PAC> it = pacs.iterator();

		while (it.hasNext()) {

			PAC pac = it.next();

			if (StringUtils.isBlank(pac.getProxy())
					&& pac.getRules().isEmpty()) {
				it.remove();
				continue;
			}

			addPanelForPAC(this.panel_pacs, pac);
		}

		PAC newPac = new PAC();
		pacs.add(newPac);
		addPanelForPAC(this.panel_pacs, newPac);

		this.panel_pacs.revalidate();
		this.panel_pacs.repaint();

		this.pacs = pacs;
	}

	private JPanel addPanelForPAC(final JPanel owner, final PAC pac) {
		final JPanel panel_pac = new JPanel();
		final JPanel panel_inputText = new JPanel();
		final JTextField textField_name = new JTextField(pac.getName());
		final JTextField textField_proxy = new JTextField(pac.getProxy());
		final JButton button_editPac = new JButton("edit");
		final JButton button_deletePac = new JButton("delete");

		panel_pac.setLayout(new FlowLayout());
		panel_pac.setPreferredSize(new Dimension(560, 43));

		panel_inputText.setLayout(new GridLayout(0, 2));
		panel_inputText.setPreferredSize(new Dimension(374, 38));

		panel_inputText.add(textField_name);
		panel_inputText.add(textField_proxy);

		class MyKeyAdapter extends java.awt.event.KeyAdapter {

			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);

				JTextField textField = (JTextField) e.getComponent();

				if ("".equals(textField.getText())) {

					Component[] brothers = owner.getComponents();

					if (brothers != null && brothers.length > 0 && brothers[brothers.length - 1] == textField.getParent().getParent()) {

						PAC newPac = new PAC();
						pacs.add(newPac);
						addPanelForPAC(owner, newPac);

						owner.revalidate();
					}
				}
			}
		}

		textField_name.addKeyListener(new MyKeyAdapter());
		textField_proxy.addKeyListener(new MyKeyAdapter());

		class MyFocusListener extends FocusAdapter {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);

				// 保存信息
				pac.setName((textField_name.getText().trim()));
				pac.setProxy(textField_proxy.getText().trim());
			}
		}

		textField_name.addFocusListener(new MyFocusListener());
		textField_proxy.addFocusListener(new MyFocusListener());

		button_editPac.setPreferredSize(new Dimension(80, 38));

		button_deletePac.setPreferredSize(new Dimension(80, 38));

		button_editPac.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final EditPACDialog editPACDialog = new EditPACDialog(pac);
				editPACDialog.setVisible(true);
			}
		});

		button_deletePac.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pacs.remove(pac);

				setPacs(pacs);
			}
		});

		panel_pac.add(panel_inputText);
		panel_pac.add(button_editPac);
		panel_pac.add(button_deletePac);

		// 同时移除上一个的 KeyListener
		Component[] brothers = owner.getComponents();
		if (brothers != null && brothers.length > 0 && brothers[brothers.length - 1] instanceof JPanel) {
			JPanel pre_panel = (JPanel) brothers[brothers.length - 1];
			JTextField pre_textField = (JTextField) ((JPanel) pre_panel.getComponents()[0]).getComponent(0);
			KeyListener[] keyListeners = pre_textField.getKeyListeners();
			for (KeyListener keyListener :
					keyListeners) {
				if (keyListener instanceof MyKeyAdapter) {
					pre_textField.removeKeyListener(keyListener);
					break;
				}
			}

			pre_textField = (JTextField) ((JPanel) pre_panel.getComponents()[0]).getComponent(1);
			keyListeners = pre_textField.getKeyListeners();
			for (KeyListener keyListener :
					keyListeners) {
				if (keyListener instanceof MyKeyAdapter) {
					pre_textField.removeKeyListener(keyListener);
					break;
				}
			}
		}

		// 调整 owner 的高度
		Dimension size = owner.getPreferredSize();
		size.setSize(size.width, size.height + 43 + 5);
		owner.setPreferredSize(size);

		owner.add(panel_pac);

		return panel_pac;
	}
}
