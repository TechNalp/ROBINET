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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image; //javafx image
import javafx.scene.image.ImageView;
public class MonControleur implements Initializable {
	//socket de 
	protected Socket sock;
	//envoie du script
	protected PrintStream ps ;
	//lecture des log
	protected BufferedReader  br ;
	int portimage;
	boolean connecte=false;
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
	
	}
	//bouton connec,connexion avec un port et une adresse
	@FXML
	public void connection() {

		String adr=adresse.getText();
		int por=Integer.parseInt(port.getText());
		try {
			this.sock=new Socket(adr,por);
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			//	BufferedWriter be =new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			ps = new PrintStream(sock.getOutputStream());
			log.appendText("connexion réussite\n");
			connecte=true;
			portimage=Integer.parseInt(br.readLine());
		}catch(java.net.ConnectException e) {			
			log.appendText("**le serveur n'est pas allumé**\n");

		}catch( java.net.SocketException e) {
			log.appendText("** le serveur est déconnecté\n");
		}catch(Exception e) {
			System.out.print(e);
		}
		//test image

		ImageTh imgview=new ImageTh(adr,portimage,image);
		Thread th=new Thread(imgview);
		th.start();



	}
	//envoie script + récupération des logs écrits
	public void envoie_script() {

		Runnable task=()->{
			String scrip=script.getText();
			ps.println(scrip);
			String logmes="";
			try {
				lancer_script=true;
				do {
					logmes=br.readLine();
					log.appendText(logmes+"\n");
				}
				while(logmes.compareTo("fin")!=0&&logmes.compareTo("interrompue")!=0);
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
				lancer_script=true;
				log.appendText("déconnexion de la part du serveur");
			}catch(IOException e) {	e.printStackTrace();}
		};
		//test script en cours
		if(lancer_script==false && connecte==true)
			new Thread(task).start();
		
	}
	//bouton stop
	public void stop_script() {
		if(lancer_script==true)ps.println("stop");

	}

}
