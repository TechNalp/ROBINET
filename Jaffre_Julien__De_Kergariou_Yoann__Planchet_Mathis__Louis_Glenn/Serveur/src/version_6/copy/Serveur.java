package version_6.copy;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Serveur implements Runnable{
	//un environnement par thread
	static Map<String,Environment> envs=new HashMap<>();

	GSpace space;
	Socket sock;
	BufferedReader br;
	PrintStream ps;
	String script;
	boolean stop=false;
	@SuppressWarnings("resource")
	public Serveur(Socket socket) throws IOException {
		sock=socket;
		try {
			//envoie logs
			ps = new PrintStream(sock.getOutputStream());
			//recup script
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//socket transfert copie d'écran
		ServerSocket s2=new ServerSocket(4000);
	
		Socket client=s2.accept();
	
		File f = new File(this.getClass().getResource("indehx.jpg").getPath());

		int t=256,x;
		byte[] buff = new byte[t];

		BufferedInputStream br2= new BufferedInputStream(new FileInputStream(f));
		PrintStream ps2 = new PrintStream(client.getOutputStream());
	System.out.println((int) f.length());
	ps.println(f.length());
		//lecture de l'image et envoie des bytes sur la deuxième socket
		while ((x = br2.read(buff))>-1) {
			ps2.write(buff,0,x);
		
		}
	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		File f2 = new File(this.getClass().getResource("ixchel.jpg").getPath());
		ps = new PrintStream(client.getOutputStream());
		BufferedInputStream br3= new BufferedInputStream(new FileInputStream(f2));
	
		while ((x = br3.read(buff)) != -1) {
			System.out.println(x+"l");
			ps.write(buff,0,x);
		}
		ps.close();
	//	br3.close();
		*/
	
	}
	@Override
	public void run() {
		
		Environment environment=new Environment(ps);
		space = new GSpace("Serveur", new Dimension(200, 100));
		space.open();

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






		while (true) {
			// prompt
			
			try {
				//attente script
				script = br.readLine();	
				ps.println("Script bien recu !");
				stop=false;
				SParser<SNode> parser = new SParser<>();
				List<SNode> rootNodes = null;
				rootNodes = parser.parse(script);
				Iterator<SNode> itor = rootNodes.iterator();
				ps.println("script validide");
				Thread sc=new Thread(new ThEcouteStop(sock,Thread.currentThread()));
				sc.start();
				while (itor.hasNext()&& !stop)  {
					new Interpreter().compute(envs.get(Thread.currentThread().getName()), itor.next());
				}
				if(!stop) {
					ps.println("fin\n");				    
				}

			}catch(stree.parser.SSyntaxError e) {
				//erreur de parsing
				ps.println("S expression invalide");
			}catch(java.net.SocketException e) {System.out.println("lo");break;
			}catch(IOException e) {break;}//fermeture brusque socket
		}
		System.out.println("client partie");
	}
	public class Interpreter{
		public void compute(Environment env, SNode method) {
			String receiverName = method.get(0).contents();
			Reference receiver = env.getReferenceByName(receiverName);
			if(receiver != null) {
				try {
					receiver.run(method);
				}catch(InterruptedException e) {
					stop=true;
					ps.println("interrompue");
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}

