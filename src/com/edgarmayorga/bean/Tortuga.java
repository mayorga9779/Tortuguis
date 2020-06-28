/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edgarmayorga.bean;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author DellMayorga
 */
public class Tortuga {
    private int puntox;
    private int puntoy;
    private int ancho;
    private int alto;
    private int puntoXInicial;  //estos puntos son nuevos
    private int puntoYInicial;
    private int puntoXFinal;
    private int puntoYFinal;
    private boolean dibujar;
    private Double rotar;
    
    public Tortuga(){
        
    }
    //los puntos finales son nuevos
    public Tortuga(int puntox, int puntoy, int ancho, int alto, Double rotar){
        setPuntox(puntox);
        setPuntoy(puntoy);
        setAncho(ancho);
        setAlto(alto);
        setPuntoXInicial(puntoXInicial);
        setPuntoYInicial(puntoYInicial);
        setPuntoXFinal(puntoXFinal);
        setPuntoYFinal(puntoYFinal);
        setDibujar(dibujar);
        setRotar(rotar);
    }
    
    public void dibujar_tortuga(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform oldt = g2.getTransform();
        //g2.transform(AffineTransform.getRotateInstance(Math.toRadians(getRotar()), getPuntox(), getPuntoy()));	
        g2.transform(AffineTransform.getRotateInstance(Math.toRadians(getRotar()), getPuntox(), (getPuntoy() + getAlto())/*getPuntoy()*/));	
        g.setColor(new Color(109, 163, 49));
        int[] puntosX = {getPuntox(), getPuntox()-(getAncho()/2), getPuntox()+(getAncho()/2)};
        int[] puntosY = {getPuntoy(), getPuntoy()+getAlto(), getPuntoy()+getAlto()};
        g.fillPolygon(puntosX, puntosY, 3);
    }
    
    public void posicionar_tortuga(Graphics g, int puntox, int puntoy, Double rotacion){
        Graphics2D g2 = (Graphics2D)g;
        setPuntox(puntox);
        setPuntoy(puntoy);
        AffineTransform oldt = g2.getTransform();
        //g2.transform(AffineTransform.getRotateInstance(Math.toRadians(getRotar()), getPuntox(), getPuntoy()));	
        g2.transform(AffineTransform.getRotateInstance(Math.toRadians(getRotar()), getPuntox(), (getPuntoy() + getAlto())/*getPuntoy()*/));	
        g.setColor(new Color(109, 163, 49));
        int[] puntosX = {getPuntox(), getPuntox()-(getAncho()/2), getPuntox()+(getAncho()/2)};
        int[] puntosY = {getPuntoy(), getPuntoy()+getAlto(), getPuntoy()+getAlto()};
        g.fillPolygon(puntosX, puntosY, 3);
    }
    /**
     * @return the puntox
     */
    public int getPuntox() {
        return puntox;
    }

    /**
     * @param puntox the puntox to set
     */
    public void setPuntox(int puntox) {
        this.puntox = puntox;
    }

    /**
     * @return the puntoy
     */
    public int getPuntoy() {
        return puntoy;
    }

    /**
     * @param puntoy the puntoy to set
     */
    public void setPuntoy(int puntoy) {
        this.puntoy = puntoy;
    }

    /**
     * @return the ancho
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * @param ancho the ancho to set
     */
    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    /**
     * @return the alto
     */
    public int getAlto() {
        return alto;
    }

    /**
     * @param alto the alto to set
     */
    public void setAlto(int alto) {
        this.alto = alto;
    }

    /**
     * @return the rotar
     */
    public Double getRotar() {
        return rotar;
    }

    /**
     * @param rotar the rotar to set
     */
    public void setRotar(Double rotar) {
        this.rotar = rotar;
    }

    /**
     * @return the puntoXInicial
     */
    public int getPuntoXInicial() {
        return puntoXInicial;
    }

    /**
     * @param puntoXInicial the puntoXInicial to set
     */
    public void setPuntoXInicial(int puntoXInicial) {
        this.puntoXInicial = puntoXInicial;
    }

    /**
     * @return the puntoYInicial
     */
    public int getPuntoYInicial() {
        return puntoYInicial;
    }

    /**
     * @param puntoYInicial the puntoYInicial to set
     */
    public void setPuntoYInicial(int puntoYInicial) {
        this.puntoYInicial = puntoYInicial;
    }

    /**
     * @return the puntoXFinal
     */
    public int getPuntoXFinal() {
        return puntoXFinal;
    }

    /**
     * @param puntoXFinal the puntoXFinal to set
     */
    public void setPuntoXFinal(int puntoXFinal) {
        this.puntoXFinal = puntoXFinal;
    }

    /**
     * @return the puntoYFinal
     */
    public int getPuntoYFinal() {
        return puntoYFinal;
    }

    /**
     * @param puntoYFinal the puntoYFinal to set
     */
    public void setPuntoYFinal(int puntoYFinal) {
        this.puntoYFinal = puntoYFinal;
    }

    /**
     * @return the dibujar
     */
    public boolean getDibujar() {
        return dibujar;
    }

    /**
     * @param dibujar the dibujar to set
     */
    public void setDibujar(boolean dibujar) {
        this.dibujar = dibujar;
    }
}
