package org.reapsn.paceditor;

import org.reapsn.paceditor.model.PAC;
import org.reapsn.paceditor.view.MainFrame;

import java.util.LinkedList;

/**
 * Created by Reaps on 2017/6/14.
 */
public class Main {

	public static void main(String[] args) {

		LinkedList<PAC> pacs = new LinkedList<>();

		PAC pac = new PAC();
		pac.setName("gfwlist");
		pac.setProxy("PROXY 127.0.0.1:1080;");

		pacs.push(pac);

		MainFrame mainFrame = new MainFrame(pacs);

	}

}
