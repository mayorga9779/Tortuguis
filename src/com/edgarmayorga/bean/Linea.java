/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edgarmayorga.bean;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author DellMayorga
 */
public class Linea {
    private int posXInicial;
    private int posYInicial;
    private int posXFinal;
    private int posYFinal;
    private double rotar;
    private boolean bajar_lapiz;
    
    public Linea(){
        
    }
    
    public Linea(int posXInicial, int posYInicial, int posXFinal, int posYFinal, boolean bajar_lapiz){
        setPosXInicial(posXInicial);
        setPosYInicial(posYInicial);
        setPosXFinal(posXFinal);
        setPosYFinal(posYFinal);
        setBajar_lapiz(bajar_lapiz);
    }
    
    public void posicionar_linea(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g.drawLine(getPosXInicial(), getPosYInicial(), getPosXFinal(), getPosYFinal());
    }
    
    public void dibujar_linea(Graphics g, int posXInicial, int posYInicial, int posXFinal, int posYFinal, boolean bajar_lapiz){
        if(getBajar_lapiz()){
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(Color.red);
            g.drawLine(getPosXInicial(), getPosYInicial(), getPosXFinal(), getPosYFinal());
        }
    }

    /**
     * @return the posXInicial
     */
    public int getPosXInicial() {
        return posXInicial;
    }

    /**
     * @param posXInicial the posXInicial to set
     */
    public void setPosXInicial(int posXInicial) {
        this.posXInicial = posXInicial;
    }

    /**
     * @return the posYInicial
     */
    public int getPosYInicial() {
        return posYInicial;
    }

    /**
     * @param posYInicial the posYInicial to set
     */
    public void setPosYInicial(int posYInicial) {
        this.posYInicial = posYInicial;
    }

    /**
     * @return the posXFinal
     */
    public int getPosXFinal() {
        return posXFinal;
    }

    /**
     * @param posXFinal the posXFinal to set
     */
    public void setPosXFinal(int posXFinal) {
        this.posXFinal = posXFinal;
    }

    /**
     * @return the posYFinal
     */
    public int getPosYFinal() {
        return posYFinal;
    }

    /**
     * @param posYFinal the posYFinal to set
     */
    public void setPosYFinal(int posYFinal) {
        this.posYFinal = posYFinal;
    }

    /**
     * @return the bajar_lapiz
     */
    public boolean getBajar_lapiz() {
        return bajar_lapiz;
    }

    /**
     * @param bajar_lapiz the bajar_lapiz to set
     */
    public void setBajar_lapiz(boolean bajar_lapiz) {
        this.bajar_lapiz = bajar_lapiz;
    }

    /**
     * @return the rotar
     */
    public double getRotar() {
        return rotar;
    }

    /**
     * @param rotar the rotar to set
     */
    public void setRotar(double rotar) {
        this.rotar = rotar;
    }
}
