package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import graphicLayer.GRect;
import graphicLayer.GSpace;

public class Exercice1_0 {
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();

	public Exercice1_0() {
		space.addElement(robi);
		space.open();
		Dimension dim = space.getSize();
		robi.setColor(new Color((int) (Math.random() * 0x1000000)));
		
		//D�placement vers la droite
		Point gap = new Point(1, 0);
		for(int i = 0; i < dim.width; i++) {
			robi.translate(gap);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//D�placement vers le bas
		gap = new Point(0, 1);
		for(int i = 0; i < dim.height; i++) {
			robi.translate(gap);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//D�placement vers la gauche
		gap = new Point(-1, 0);
		for(int i = 0; i < dim.width; i++) {
			robi.translate(gap);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//D�placement vers le baut
		gap = new Point(0, -1);
		for(int i = 0; i < dim.height; i++) {
			robi.translate(gap);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Exercice1_0();
	}

}