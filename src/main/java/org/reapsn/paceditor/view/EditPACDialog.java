package org.reapsn.paceditor.view;

import org.reapsn.paceditor.model.PAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashSet;

public class EditPACDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JPanel panel_rules;
	private PAC pac;

	public EditPACDialog(PAC pac) {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.setSize(600, 450);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.panel_rules.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);

				JScrollPane scrollPane = (JScrollPane) e.getComponent().getParent().getParent();
				JScrollBar vertical = scrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
			}
		});

		setPac(pac);

	}

	private void onOK() {
		// add your code here

		LinkedHashSet<String> rules = getPac().getRules();
		rules.clear();

		Component[] components = this.panel_rules.getComponents();
		for (Component component :
				components) {
			if (component instanceof JTextField) {

				JTextField textField = (JTextField) component;

				rules.add(textField.getText().trim());
			}
		}

		rules.remove("");

		dispose();
	}

	private void onCancel() {
		// add your code here if necessary
		dispose();
	}

	private JTextField addTextFieldForRule(final JPanel owner, final String rule) {

		final JTextField textField_rule = new JTextField(rule);
		textField_rule.setPreferredSize(new Dimension(560, 38));

		class MyKeyAdapter extends java.awt.event.KeyAdapter {

			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);

				JTextField textField = (JTextField) e.getComponent();

				if ("".equals(textField.getText())) {

					Component[] brothers = owner.getComponents();

					if (brothers != null && brothers.length > 0 && brothers[brothers.length - 1] == textField) {

						addTextFieldForRule(owner, "");

						owner.revalidate();
					}
				}
			}
		}

		textField_rule.addKeyListener(new MyKeyAdapter());

		// 同时移除上一个的 KeyListener
		Component[] brothers = owner.getComponents();
		if (brothers != null && brothers.length > 0 && brothers[brothers.length - 1] instanceof JTextField) {
			JTextField pre_textField = (JTextField) brothers[brothers.length - 1];
			KeyListener[] keyListeners = pre_textField.getKeyListeners();
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
		size.setSize(size.width, size.height + 38 + 5);
		owner.setPreferredSize(size);

		owner.add(textField_rule);

		return textField_rule;
	}

	public PAC getPac() {
		return pac;
	}

	public void setPac(PAC pac) {

		this.setTitle(pac.getName());

		// 初始宽度和高度
		this.panel_rules.setPreferredSize(new Dimension(576, 0));

		// 清空原来的输入框
		Component[] components = this.panel_rules.getComponents();
		for (Component component :
				components) {
			if (component instanceof JTextField) {
				this.panel_rules.remove(component);
			}
		}

		// 添加新的输入框
		LinkedHashSet<String> rules = pac.getRules();
		for (String rule :
				rules) {
			addTextFieldForRule(this.panel_rules, rule);
		}
		addTextFieldForRule(this.panel_rules, "");

		this.panel_rules.revalidate();

		this.pac = pac;

	}

}
