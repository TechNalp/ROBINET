package version_6.copy;

import java.awt.Dimension;
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


		//socket transfert copie d'écran début +test
	/*	ServerSocket s2=new ServerSocket(4000);
		Socket client=s2.accept();
		OutputStream outputStream=client.getOutputStream();
		while(true) {
			BufferedImage image = ImageIO.read(new File("./indehx.jpg"));

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteArrayOutputStream);

			byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
			outputStream.write(size);
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.flush();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//fin
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




		Thread sc=null;

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
				sc=new Thread(new ThEcouteStop(sock,Thread.currentThread()));
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
		try {
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

