package org.reapsn.paceditor.view;

import org.reapsn.paceditor.model.PAC;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Reaps on 2017/6/14.
 */
public class MainFrame extends JFrame {

	private LinkedList<PAC> pacs;

	public MainFrame(LinkedList<PAC> pacs) {

		this.pacs = pacs;

		initFrame();
	}

	private void initFrame() {
		//设置布局管理器
		this.setLayout(new GridLayout(4,1));
		//给窗口设置标题
		this.setTitle("PAC EDITOR");
		//设置窗体大小
		this.setSize(300,200);
		//设置窗体初始位置
		this.setLocation(200, 150);
		//设置当关闭窗口时，保证JVM也退出
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//设置允许调整窗口大小
		this.setResizable(true);

		//显示窗体
		this.setVisible(true);

		for (PAC pac :
				pacs) {
			JPanel panel_pac = new JPanel();
			panel_pac.setLayout(new GridLayout(1,2));

			this.add(panel_pac);

			JTextField textField_name = new JTextField(pac.getName());
			JTextField textField_proxy = new JTextField(pac.getProxy(), 1);
			panel_pac.add(textField_name);
			panel_pac.add(textField_proxy);
		}

		this.setVisible(false);
		this.setVisible(true);
	}

	public LinkedList<PAC> getPacs() {
		return pacs;
	}



}
