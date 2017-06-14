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
		//���ò��ֹ�����
		this.setLayout(new GridLayout(4,1));
		//���������ñ���
		this.setTitle("PAC EDITOR");
		//���ô����С
		this.setSize(300,200);
		//���ô����ʼλ��
		this.setLocation(200, 150);
		//���õ��رմ���ʱ����֤JVMҲ�˳�
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//��������������ڴ�С
		this.setResizable(true);

		//��ʾ����
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
