package version_5.copy;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import stree.parser.SPrinter;
import tools.Tools;

public class Serveur implements Runnable{
	//un environnement par thread
	static Map<String,Environment> envs=new HashMap<>();
	int windx=0,windy=0;
	GSpace space;
	Socket sock;
	BufferedReader br;
	PrintStream ps;
	String script;
	static int portimage=4000;
	boolean stop=false;
	@SuppressWarnings("resource")
	public Serveur(Socket socket,int x,int y) throws IOException {
		sock=socket;
		windx=x;windy=y;

		try {
			//envoie logs
			ps = new PrintStream(sock.getOutputStream());
			//recup script
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//socket transfert copie d'écran début +test

	}
	//fin


	//}
	@Override
	public void run() {
		ps.println(portimage);
		Environment environment=new Environment(ps);
		space = new GSpace("Serveur", new Dimension(200, 100));

		space.setLocation(windx, windy);
		space.setLayout(null);
		space.open();


		space.repaint();
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());

		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));

		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("rect.class", rectClassRef);
		environment.addReference("oval.class", ovalClassRef);
		environment.addReference("image.class", imageClassRef);
		environment.addReference("label.class", stringClassRef);
		envs.put(Thread.currentThread().getName(), environment);

		Robot robot=null;
		OutputStream outputStream=null;
		try {
	
			ServerSocket s2 = new ServerSocket(portimage);
			//section critique
			synchronized(this) {portimage++;}
			//socket transfer image
			Socket simg=s2.accept();
			outputStream=simg.getOutputStream();
			

			robot = new Robot();


		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}catch(AWTException e1) {	e1.printStackTrace();}




		Thread sc=null;

		while (true) {
			// prompt

			try {
				//attente script
				script = br.readLine();	
				//	System.out.println(script);

				ps.println("Script bien recu !");
				stop=false;
				SParser<SNode> parser = new SParser<>();
				List<SNode> rootNodes = null;
				rootNodes = parser.parse(script);

				Iterator<SNode> itor = rootNodes.iterator();

				sc=new Thread(new ThEcouteStop(sock,Thread.currentThread()));
				sc.start();
				ps.println("script valide");

				while (itor.hasNext()&& !stop)  {
					new Interpreter().compute(envs.get(Thread.currentThread().getName()), itor.next());

					BufferedImage image = robot.createScreenCapture(new Rectangle(windx+10,windy+30,space.getWidth(),space.getHeight()));
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					try {
						ImageIO.write(image, "jpg", byteArrayOutputStream);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
					//stop provoqais une interruption du thread et impossible transférer image sur le même thread
					ImageTh it=new ImageTh(byteArrayOutputStream,size,outputStream);
					Thread img=new Thread(it);
					img.start();
				}
				if(!stop) {
					ps.println("fin\n");				    
				}

			}catch(stree.parser.SSyntaxError e) {
				//erreur de parsing
				ps.println("S expression invalide");
				ps.println("fin");
			}catch(java.net.SocketException e) {System.out.println("socket"); break;
			}catch(IOException e) {System.out.println("socket"); break;}//fermeture brusque socket
		}
		System.out.println("client partie");
		try {
			//si le client quitte avant d'envoyer le script erreur null.start
			if(sc!=null)
				sc.interrupt();
			
			sock.close();
			br.close();
			ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public class Interpreter{
		//copie
		public void compute(Environment env, SNode method) {
			String receiverName = method.get(0).contents();
			Reference receiver = env.getReferenceByName(receiverName);
			SPrinter printer = new SPrinter();
			method.accept(printer);
			if(receiver != null) {
				try {
					receiver.run(method);
					ps.println("good: "+printer.result().toString());
				}catch(InterruptedException e) {
					stop=true;
					ps.println("interrompue");
				}catch(Exception e) {
					//e.printStackTrace();
					ps.println("error command: "+printer.result().toString());
				}
			}
		}

	}
}

