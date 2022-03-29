package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import java.awt.Point;

public class Exercice1_0 {
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();
	int speed = 10;
	
	public Exercice1_0() {
		space.addElement(robi);
		space.open();
		Point position = robi.getPosition();
		System.out.println(robi.getWidth()+" "+robi.getX());
		System.out.println(space.getWidth()-(robi.getWidth()));
		while(true) {
			//Déplacement vers le bord droit
			while(robi.getX()!=space.getWidth()-(robi.getWidth())) {
				//Gestion du redimensionnement
				if(robi.getX()>=space.getWidth()-(robi.getWidth())) {
					robi.translate(new Point((space.getWidth()-(robi.getWidth())-robi.getX()),0));
				}else {
					robi.translate(new Point(1,0));
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//Déplacement vers le bord bas
			while(robi.getY()!=space.getHeight()-(robi.getHeight())) {
				//Gestion du redimensionnement verticale
				if(robi.getY()>=space.getHeight()-(robi.getHeight())) {
					robi.translate(new Point(0,(space.getHeight()-(robi.getHeight())-robi.getY())));
				}else {
					robi.translate(new Point(0,1));
				}
				//Gestion du redimensionnement Horizontale
				if(robi.getX()!=space.getWidth()-(robi.getWidth())) {
					robi.translate(new Point((space.getWidth()-(robi.getHeight())-robi.getX()),0));
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//Déplacement vers le bord gauche
			while(robi.getX()!=0) {
				
				robi.translate(new Point(-1,0));
				//Gestion du redimensionnement verticale
				if(robi.getY()!=space.getHeight()-(robi.getHeight())) {
					robi.translate(new Point(0,(space.getHeight()-(robi.getHeight())-robi.getY())));
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//Déplacement vers le bord haut
			while(robi.getY() != 0) {
				robi.translate(new Point(0,-1));
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			robi.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
			
			
		}
	}

	public static void main(String[] args) {
		new Exercice1_0();
	}

}