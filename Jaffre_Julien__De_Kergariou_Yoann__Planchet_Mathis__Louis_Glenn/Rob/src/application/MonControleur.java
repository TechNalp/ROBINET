package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image; //javafx image
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

public class MonControleur implements Initializable {
	//socket de 
	protected Socket sock;
	//
	protected PrintStream ps ;
	//lecture des log
	protected BufferedReader  br ;

	protected short portImg;

	protected boolean isConnected = false;

	protected boolean tryToConnect = false;

	protected Thread imgThread;

	boolean lancer_script=false;
	@FXML
	private TextField adresse ;
	@FXML
	private TextField port ;
	@FXML
	private Button send ;
	@FXML
	private Button connec ;
	@FXML
	private TextField script;
	@FXML
	private Button stop;
	@FXML
	private TextArea log;
	@FXML
	private ImageView image;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		stop.setVisible(false);
		send.setVisible(false);


		//			image.setImage(new Image(new FileInputStream(this.getClass().getResource("rd.png").getPath())));


	/*	File f=new File("./image.jpg");
		if(f.exists()) {
			try {

				if(f.canRead())System.out.print("lol");
				image.setImage(new Image(new FileInputStream(new File("./image.jpg"))));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}*/
	}
	//bouton connec,connexion avec un port et une adresse
	@FXML
	public void connection() {

		String adr=adresse.getText();
		int por=Integer.parseInt(port.getText());
		if(this.tryToConnect){
			log.appendText("Une tentative de connexion est déjà en cours\n");
			return;
		}
		new Thread(){
			@Override
			public void run(){
				MonControleur.this.tryToConnect = true;
				if(MonControleur.this.isConnected){
					log.appendText("Vous êtes déjà connecté\n");
					MonControleur.this.tryToConnect = false;
					return;
				}
				try {
					log.appendText("Tentative de connexion...\n");

					MonControleur.this.sock=new Socket(adr,por);
					br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					//	BufferedWriter be =new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
					ps = new PrintStream(sock.getOutputStream());
					log.appendText("connexion réussie\n");
					String temp = br.readLine();
					if(temp.startsWith("img:")){
						MonControleur.this.portImg = Short.parseShort(temp.split(" ")[1]);
						log.appendText("Port pour l'envoie des capture d'écran : "+MonControleur.this.portImg+"\n");
						MonControleur.this.imgThread = new Thread(new ImageTh("localhost",MonControleur.this.portImg,MonControleur.this.image));
						MonControleur.this.imgThread.start();
					}else{
						log.appendText("Erreur récéption port pour les capture d'écrans");
						System.exit(1);
					}
					MonControleur.this.isConnected = true;
					MonControleur.this.tryToConnect = false;
					send.setVisible(true);
					stop.setVisible(true);
				}catch(java.net.ConnectException e) {
					log.appendText("**le serveur n'est pas allumé**\n");
					MonControleur.this.isConnected = false;
					MonControleur.this.tryToConnect = false;

				}catch( java.net.SocketException e) {
					log.appendText("** le serveur est déconnecté\n");
					MonControleur.this.isConnected = false;
					MonControleur.this.tryToConnect = false;
				}catch(Exception e) {
					System.out.print(e);
					MonControleur.this.isConnected = false;
					MonControleur.this.tryToConnect = false;
				}
			}
		}.start();

		//test image

	//	ImageTh imgview=new ImageTh(adr,4000,image);
	//	Thread th=new Thread(imgview);
	//	th.start();



	}
	//envoie script + récupération des logs écrits
	public void envoie_script() {
		Runnable task=()->{
		//	log.appendText("script envoye lancer");
			String scrip=script.getText();
			if(this.ps == null){
				log.appendText("Veuillez vous connecter au serveur\n");
				return;
			}
			ps.println(scrip);
			String logmes="";
			try {
				lancer_script=true;
				ps.println("");
				do {
					logmes=br.readLine();
					log.appendText(logmes+"\n");
					System.out.println(logmes);
				}
				while(logmes.compareTo("fin")!=0 && logmes.compareTo("interrompue")!=0);
				//fin normale fermeture socket d'interruption
				if(logmes.equals("fin"))ps.println("");
				lancer_script=false;
			/*	try { trop de réponse de la part du serveur = crash du textarea
					Thread.sleep(2000);
					log.clear();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/


			}catch(java.net.SocketException e) {
				lancer_script=false;
				log.appendText("Le Serveur s'est déconnecté\n");
				MonControleur.this.imgThread.interrupt();
				stop.setVisible(false);
				send.setVisible(false);
				MonControleur.this.isConnected = false;
			}catch(IOException e) {	e.printStackTrace();
				MonControleur.this.imgThread.interrupt();}
		};
		//test script en cours
		if(lancer_script==false)
			new Thread(task).start();
		
	}
	//bouton stop
	public void stop_script() {
		if(lancer_script==true){
			ps.println("stop");
			lancer_script=false;
			MonControleur.this.imgThread.interrupt();
		}

	}

}
