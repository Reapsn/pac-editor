package org.reapsn.paceditor;

import org.reapsn.paceditor.model.PAC;
import org.reapsn.paceditor.view.MainFrame;

import java.util.LinkedHashSet;

/**
 * Created by Reaps on 2017/6/14.
 */
public class Main {

	public static void main(String[] args) {

		LinkedHashSet<PAC> pacs = new LinkedHashSet<PAC>();

		PAC pac = new PAC();
		pac.setName("gfwlist");
		pac.setProxy("PROXY 127.0.0.1:1080;");
		pac.getRules().add("google.com");

		pacs.add(pac);

		MainFrame mainFrame = new MainFrame(pacs);

//		mainFrame.setPacs(pacs);

		mainFrame.setVisible(true);

	}

}
