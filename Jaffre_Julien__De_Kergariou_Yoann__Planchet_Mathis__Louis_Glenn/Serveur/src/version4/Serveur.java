package version4;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import exercice4.Environment2;
import exercice4.Exercice4_1_0.Environment;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import stree.parser.SPrinter;
import version_ex3.Serveur.Command;

public class Serveur {
	GSpace space = new GSpace("Serveur", new Dimension(200, 100));
	GRect robi = new GRect();
	Environment2 environment = new Environment2();
	static String script = "";
	private Socket sock;
	BufferedReader br;
	PrintStream ps;
	String log="";
	public Serveur(Socket sock) {
		this.sock=sock;

	}
	public void run() {

		space.addElement(robi);
		space.open();
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			ps = new PrintStream(sock.getOutputStream());
			script = br.readLine();

			//ps.println("Script bien recu !");
			this.runScript();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void runScript() {

		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
			Iterator<SNode> itor = rootNodes.iterator();
			while (itor.hasNext() ) {
				this.run2(itor.next());
			}
			System.out.print("yo");
			ps.println("fin");
		} catch (stree.parser.SSyntaxError e) {
			log+="S expression invalide";

		}catch(IOException e) {e.printStackTrace();}

		try {

			if(br.readLine().equals("oui"))ps.println(log);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}



}
