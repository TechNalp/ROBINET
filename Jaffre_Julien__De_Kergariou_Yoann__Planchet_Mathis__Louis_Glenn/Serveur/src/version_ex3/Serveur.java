package version_ex3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import stree.parser.SPrinter;

public class Serveur implements Runnable {
	GSpace space = new GSpace("Serveur", new Dimension(200, 100));
	GRect robi = new GRect();
	static String script = "";
	private Socket sock;
	BufferedReader br;
	PrintStream ps;
	Thread sc=null;
	public Serveur(Socket sock) {
		this.sock=sock;

	}


	public void run(){

		space.addElement(robi);
		space.open();
		try {
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			ps = new PrintStream(sock.getOutputStream());
			script = br.readLine();	
			this.runScript();
		}catch(java.net.SocketException e) {System.out.print("partie");
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}

	private void runScript() {
		ps.println("Script bien recu !");
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
			Iterator<SNode> itor = rootNodes.iterator();
			ps.println("script validide");

			sc=new Thread(new ThEcouteStop(br,ps));
			sc.start();
			while (itor.hasNext()&& !sc.isInterrupted()) {
				this.run2(itor.next());
			}
			if(sc.isInterrupted())
				ps.println("fin\n");
		} catch (stree.parser.SSyntaxError e) {
			ps.println("S expression invalide");

		}catch(IOException e) {e.printStackTrace();}


	}

	private void run2(SNode expr) {
		SPrinter printer = new SPrinter();
		try {
			Command cmd = getCommandFromExpr(expr);

			cmd.run();
		}catch(Exception e) {

			expr.accept(printer);

			ps.println(printer.result()+" command or object not found");
		}
	}

	Command getCommandFromExpr(SNode expr) {
		if(expr == null || expr.isLeaf()) {
			return null;
		}

		String applyToStr = expr.get(0).contents();
		String cmd = expr.get(1).contents();	

		if(applyToStr.equals("space")) {
			if(cmd.equals("setColor")) {
				try {
					return new SpaceChangeColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					//	e.printStackTrace();
					SPrinter printer = new SPrinter();
					expr.accept(printer);
					//log+=printer.result()+" argument illgal \n";
				}
			}else if(cmd.equals("sleep")) {
				return new Sleep(Integer.valueOf(expr.get(2).contents()));
			}

		}

		if(applyToStr.equals("robi")) {
			if(cmd.equals("setColor")) {
				try {
					return new RobiChangeColor((Color)(Color.class.getDeclaredField(expr.get(2).contents()).get(null)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					SPrinter printer = new SPrinter();
					expr.accept(printer);
					ps.println(printer.result()+" argument illgal \n");
				}
			}else if(cmd.equals("translate")) {
				if(expr.size()-2<2) {
					return null;
				}

				return new RobiTranslate(Integer.valueOf(expr.get(2).contents()),Integer.valueOf(expr.get(3).contents()));
			}
		}

		return null;

	}
	public interface Command {
		abstract public void run();
	}

	public class SpaceChangeColor implements Command {
		Color newColor;

		public SpaceChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run()  {
			space.setColor(newColor);
		}

	}

	public class RobiChangeColor implements Command{

		Color newColor;

		public RobiChangeColor(Color newColor) {
			this.newColor = newColor;
		}

		@Override
		public void run() {
			robi.setColor(this.newColor);

		}

	}

	public class Sleep implements Command{
		int timeToSleep;

		public Sleep(int timeToSleep) {
			this.timeToSleep = timeToSleep;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(this.timeToSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public class RobiTranslate implements Command{

		int dx;
		int dy;

		public RobiTranslate(int dx,int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		@Override
		public void run() {
			robi.translate(new Point(this.dx,this.dy));
		}

	}
}