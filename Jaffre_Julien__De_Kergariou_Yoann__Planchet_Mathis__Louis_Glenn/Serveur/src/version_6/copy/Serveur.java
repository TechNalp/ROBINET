package version_6.copy;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import version_6.copy.Sleep.StopException;

public class Serveur implements Runnable{
	
	
	
	
	static int nbFrameWidth = Toolkit.getDefaultToolkit().getScreenSize().width/310;
	static int nbFrameHeigt = Toolkit.getDefaultToolkit().getScreenSize().height/210;

	String screenShotName = "";
	
	//un environnement par thread
	Environment environment;

	GSpace space;
	Socket sock;
	BufferedReader br;
	PrintStream ps;
	String script;
	Socket sckImg;
	JFrame frame;
	
	boolean stop=false;
	@SuppressWarnings("resource")
	public Serveur(Socket socket) throws IOException {
		sock=socket;
		try {
			//envoie logs
			this.ps = new PrintStream(sock.getOutputStream());
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
	
	
	public void  takeScreenShot() {
		Rectangle rect = new Rectangle();
		rect.setLocation(this.frame.getContentPane().getLocationOnScreen());
		rect.setSize(this.frame.getContentPane().getSize());
		Robot rbt = null;
		try {
			rbt = new Robot();
			
			BufferedImage bi = rbt.createScreenCapture(rect);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bi, "png", byteArrayOutputStream);
				
				byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
				
				this.sckImg.getOutputStream().write(size);
				
				this.sckImg.getOutputStream().write(byteArrayOutputStream.toByteArray());
				
				this.sckImg.getOutputStream().flush();
				
				
			} catch (IOException e) {

				e.printStackTrace();
			}
			
			
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		this.screenShotName = Thread.currentThread().getName()+"_screenShot.jpg";
		this.environment = new Environment(null,this.ps);
		space = new GSpace("Client "+Thread.currentThread().getName(), new Dimension(300, 200));
		space.open();
		
		this.frame = ((JFrame)SwingUtilities.getWindowAncestor(space));
		
		int height = this.frame.getHeight();
		
		
		this.frame.setAlwaysOnTop(true);
		
		int posX =Integer.parseInt(Thread.currentThread().getName())%Serveur.nbFrameWidth;
		
		int posY = Integer.parseInt(Thread.currentThread().getName())/Serveur.nbFrameHeigt;
		
		if(posX+((Serveur.nbFrameWidth-Serveur.nbFrameHeigt)*posY)>=Serveur.nbFrameHeigt) {
			posY = posY-1;
		}
		
		this.frame.setLocation(posX*(300+5), posY*(height+5));
		
		short portImg = (short)(8001+(Integer.parseInt(Thread.currentThread().getName())));
		this.ps.println("img: "+portImg);
		ServerSocket srvImg = null;
		try {
			srvImg = new ServerSocket(portImg);
			this.sckImg = srvImg.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.setHisEnv(new Environment(this.environment,this.ps));
		
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("setDim", new SetDim());

		spaceRef.addCommand("add", new AddElement(spaceRef.getHisEnv()));
		spaceRef.addCommand("del", new DelElement(spaceRef.getHisEnv()));
		
		spaceRef.addCommand("addScript", new AddScript());
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		rectClassRef.setHisEnv(this.environment);
		ovalClassRef.setHisEnv(this.environment);
		imageClassRef.setHisEnv(this.environment);
		stringClassRef.setHisEnv(this.environment);
		
		
		environment.addReference("space", spaceRef);
		
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
		

		Thread sc=null;
		
		
		
		
		while (true) {
			// prompt

			try {
				script = br.readLine();
				//attente script
				if(script == null || script.equals("")) {
					continue;
				}
				if(script.equals("stop")) {
					ps.println("Script interrompu");
					ps.println("fin\n");
					stop=false;
				}
				
				
				ps.println("Script bien recu !");
				stop=false;
				SParser<SNode> parser = new SParser<>();
				List<SNode> rootNodes = null;
				rootNodes = parser.parse(script);
				Iterator<SNode> itor = rootNodes.iterator();
				ps.println("script valide");
				
				sc=new Thread(new ThEcouteStop(sock,Thread.currentThread()));
				sc.start();
				
				while (itor.hasNext()&& !stop){
					if(script.equals("stop")) {
						ps.println("Script interrompu");
						ps.println("fin\n");
						stop=false;
					}
					try {
						new Interpreter().compute(this.environment, itor.next());
						this.takeScreenShot();
					} catch (StopException e) {
						stop = true;
					}
				}
				if(!stop) {
					ps.println("fin\n");				    
				}else {
					ps.println("Script interrompu");
					ps.println("fin\n");
					stop=false;
				}

			}catch(stree.parser.SSyntaxError e) {
				//erreur de parsing
				ps.println("S expression invalide");
				ps.println("fin\n");
				stop= false;
			}catch(java.net.SocketException e) {//System.out.println("lo");
				break;
			}catch(IOException e) {break;}//fermeture brusque socket
		}
		System.out.println("client "+Thread.currentThread().getName()+" partie");
		
		Prog.clientIds.remove(Integer.parseInt(Thread.currentThread().getName()));
	
		this.frame.dispose();
		try {
			if(sc != null) {
				sc.interrupt();
			}
			sock.close();
			br.close();
			ps.close();
			this.sckImg.close();
			srvImg.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

