/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.edgarmayorga.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import com.edgarmayorga.bean.Linea;
import com.edgarmayorga.bean.Tortuga;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 *
 * @author DellMayorga
 * Programa Tortuguis, creado por Edgar Mayorga, del curso de Programacion II UMG Agosto 2017
 * basado en el programa de Berkeley Logo
 */
public class Ventana extends javax.swing.JFrame {
    private Tortuga tortuga = null;
    private Linea linea = null;
    private ArrayList<Tortuga> lstPosTortuga = null;    //Para guardar las posiciones de la tortuguita
    private ArrayList<Linea> lstPosLinea = null;        //Para guardar las posiciones de las lineas
    private int ancho;
    private int alto;
    private int puntox;
    private int puntoy;
    private int pasos;
    private int ultimo_puntox;
    private int ultimo_puntoy;
    private int puntoXInicial;
    private int puntoYInicial;
    private int puntoXFinal;
    private int puntoYFinal;
    private Double rotacion;
    private Double grados_acumulados;
    private String texto;
    private String ultimo_comando;
    private String[] cadena_comandos;
    private String[] comandos_repetir;
    private boolean bajar_lapiz;
    private boolean inicio;
    
    public void inicializar(){
        this.setTitle("Tortuguis");
        setInicio(true);
        setBajar_lapiz(true);
        setRotacion(0.00);
        setGrados_acumulados(getRotacion());
        setLstPosTortuga(new ArrayList<>());
        setLstPosLinea(new ArrayList<>());
        setAncho(getPnlLienzo().getWidth());
        setAlto(getPnlLienzo().getHeight());
        setPuntox(getPnlLienzo().getWidth()/2);
        setPuntoy(getPnlLienzo().getHeight()/2);
        setTexto(getTaComandos().getText());
        setUltimo_puntox(getPuntox());
        setUltimo_puntoy(getPuntoy());
        setPuntoXInicial(getPuntox());
        setPuntoYInicial(getPuntoy());
        setPuntoXFinal(getPuntox());
        setPuntoYFinal(getPuntoy());
        //para que se muestre la imagen cuando se corra en el IDE
        //this.setIconImage(new ImageIcon(getClass().getResource("../imagenes/tortuguis.jpg")).getImage());
        //Para que se muestre la imagen cuando se corra el .jar
        this.setIconImage(new ImageIcon(getClass().getResource("/imagenes/tortuguis.jpg")).getImage());
        lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
        lblGradosAcum.setText(getGrados_acumulados().toString());
        setTortuga(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion())); //Inicializo el objeto Tortuga
        setLinea(new Linea(getPuntoXInicial(), getPuntoYInicial() + getTortuga().getAlto(), getPuntoXFinal(), getPuntoYFinal() + getTortuga().getAlto(), getBajar_lapiz()));//Inicializo el objeto Linea
    }
    
    public Ventana() {
        initComponents();
        inicializar();
        //evento del teclado
        taComandos.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try{
                    if(e.getKeyCode() == 10){   //detecto la tecla ENTER
                        setTexto(getTexto() + getTaComandos().getText());
                        cadena_comandos = new String[getTexto().length()];
                        cadena_comandos = getTexto().split("\n");
                        //Obtenemos el último comando ingresado
                        ultimo_comando = cadena_comandos[cadena_comandos.length - 1];
                        //Haciendo el tratamiento para el ultimo comando, obteniendo el nombre del comando y el valor
                        //solo para los comandos FD, BK, RT, LT, PU, PD
                        String[] comando = new String[2];
                        //tratamiento para el comando REPEAT
                        String[] repetir = new String[2];
                        //dando tratamiento al comando PU o PD
                        if(getUltimo_comando().toUpperCase().equals("PU")){
                            ultimo_comando = "PU 0";
                        } else if(getUltimo_comando().toUpperCase().equals("PD")){
                            ultimo_comando = "PD 0";
                        } else if(getUltimo_comando().toUpperCase().equals("HOME")){
                            ultimo_comando = "HOME 0";
                        } else if(getUltimo_comando().toUpperCase().equals("SCRSHOT")){  //adicional agrego un comando para hacer una captura de pantalla
                            ultimo_comando = "SCRSHOT 0";
                        } else if(getUltimo_comando().toUpperCase().equals("HELP")) {
                            ultimo_comando = "HELP 0";
                        } else if(getUltimo_comando().toUpperCase().equals("HELPHIDD")) {
                            ultimo_comando = "HELPHIDD 0";
                        } else if(getUltimo_comando().toUpperCase().contains("REPEAT")) {
                            
                            String comando_repeat = getUltimo_comando().substring(0, getUltimo_comando().indexOf("["));
                            repetir = comando_repeat.split(" ");
                            String valor_repeat = getUltimo_comando().substring(getUltimo_comando().indexOf("[")+1, getUltimo_comando().indexOf("]"));
                            comandos_repetir = new String[valor_repeat.length()];
                            //separamos la cadena de los argumentos del comando repeat(lo que está dentro de corchetes) y los guardamos en el array
                            comandos_repetir = valor_repeat.split(" ");
                        }
                        //validando que el último comando contiene un espacio en blanco, por ejemplo FD 50
                        if(getUltimo_comando().contains(" ")){
                            comando = getUltimo_comando().split(" ");   //Separo el ultimo comando en 2 partes, el comando y el valor del comando
                            
                            //Comienza la lógica de los comandos
                            if(comando[0].toUpperCase().equals("FD")){
                                if(getInicio() == false){   //si no es la posicion inicial de la tortuga
                                    if(!getLstPosTortuga().isEmpty()){  //verifico si la lista tiene tiene tortugas ingresadas
                                        getLstPosTortuga().remove(0);   //elimino el dibujo anterior(la tortuga), para poder pintar el nuevo
                                        if(getGrados_acumulados() == 0){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante1_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 1 && getGrados_acumulados() <= 89){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 90){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 91 && getGrados_acumulados() <= 179){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_positivo_adelante(getGrados_acumulados());    
                                        } else if(getGrados_acumulados() == 180){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 181 && getGrados_acumulados() <= 269){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 270){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 271 && getGrados_acumulados() <= 359){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 360){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante4_positivo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -1.00 && getGrados_acumulados() >= -89.00){ //Condiciones cuando los grados son negativos
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -90){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -180){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -270){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_negativo_adelante(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -360){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante4_negativo_adelante(getGrados_acumulados());
                                        }
                                    }
                                }
                            }else if(comando[0].toUpperCase().equals("BK")){
                                if(getInicio() == false){
                                    if(!getLstPosTortuga().isEmpty()){
                                        getLstPosTortuga().remove(0);   //elimino el dibujo anterior(la tortuga), para poder pintar el nuevo
                                        if(getGrados_acumulados() == 0){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante1_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 1 && getGrados_acumulados() <= 89){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 90){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 91 && getGrados_acumulados() <= 179){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 180){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 181 && getGrados_acumulados() <= 269){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == 270){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() >= 271 && getGrados_acumulados() <= 359){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_positivo_atras(getGrados_acumulados());        
                                        } else if(getGrados_acumulados() == 360){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante4_positivo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -1.00 && getGrados_acumulados() >= -89.00){ //Condiciones cuando los grados son negativos
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -90){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante1_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -180){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante2_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -270){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante3_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
                                            setPasos(Integer.parseInt(comando[1]));
                                            cuadrante4_negativo_atras(getGrados_acumulados());
                                        } else if(getGrados_acumulados() == -360){
                                            setPasos(Integer.parseInt(comando[1])); //Guardo el numero de pasos
                                            cuadrante4_negativo_atras(getGrados_acumulados());
                                        }
                                    }
                                }
                            }else if(comando[0].toUpperCase().equals("RT")){
                                if(getInicio() == false){
                                    if(!getLstPosTortuga().isEmpty()){
                                        getLstPosTortuga().remove(0);
                                        setRotacion(getRotacion() + Double.parseDouble(comando[1]));
                                        double grados_acumulados_antes = getGrados_acumulados();
                                        setGrados_acumulados(getRotacion());
                                        double grados_acumulados_despues = getGrados_acumulados();
                                        //llevando el control de los grados, reseteo los grados cuando se pase de 360 grados
                                        if(getGrados_acumulados() > 360){ 
                                            double recalcular_grados = getGrados_acumulados() / 360.00;
                                            int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                                            double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                                            double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumuladro de grados
                                            setGrados_acumulados(nuevos_grados);
                                        } else if(getGrados_acumulados() < -360){  //en el caso que gire al otro lado, y los angulos se pasen de -360
                                            double recalcular_grados = getGrados_acumulados() / 360.00;
                                            int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                                            double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                                            double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumulado de grados
                                            setGrados_acumulados(nuevos_grados);
                                        } else{ //si no se ha pasado de 360 grados, que siga acumulando los grados que se ingresan en la consola
                                            setGrados_acumulados(getRotacion());
                                        }
                                        lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
                                        lblGradosAcum.setText(getGrados_acumulados().toString());
                                        getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
                                    }
                                }
                            }else if(comando[0].toUpperCase().equals("LT")){
                                if(getInicio() == false){
                                    if(!getLstPosTortuga().isEmpty()){
                                        getLstPosTortuga().remove(0);
                                        setRotacion(getRotacion() - Double.parseDouble(comando[1]));
                                        setGrados_acumulados(getRotacion());
                                        double grados_acumulados_despues = getGrados_acumulados();
                                        //llevando el control de los grados, reseteo los grados cuando se pase de 360 grados
                                        if(getGrados_acumulados() < -360){  //aqui era positivo y >
                                            double recalcular_grados = getGrados_acumulados() / 360.00;
                                            int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                                            double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                                            double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumulado de grados
                                            setGrados_acumulados(nuevos_grados);
                                        } else if(getGrados_acumulados() > 360){ //en el caso que gire al otro lado, y los angulos se pasen de 360
                                            double recalcular_grados = getGrados_acumulados() / 360.00;
                                            int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                                            double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                                            double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumuladro de grados
                                            setGrados_acumulados(nuevos_grados);
                                        } else {
                                            setGrados_acumulados(getRotacion());
                                        }
                                        lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
                                        lblGradosAcum.setText(getGrados_acumulados().toString());
                                        getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
                                    }
                                }
                            } else if(comando[0].toUpperCase().equals("PU")){
                                setBajar_lapiz(false);
                            } else if(comando[0].toUpperCase().equals("PD")){
                                setBajar_lapiz(true);
                            } else if(comando[0].toUpperCase().equals("HOME")){
                                if(!getLstPosTortuga().isEmpty()){
                                    getLstPosTortuga().clear();
                                    inicializar();
                                }
                                if(!getLstPosLinea().isEmpty()){
                                    getLstPosLinea().clear();
                                    inicializar();
                                }
                            } else if(comando[0].toUpperCase().equals("HELP")) {
                                pnlComandos.setVisible(true);
                            } else if(comando[0].toUpperCase().equals("HELPHIDD")) {
                                pnlComandos.setVisible(false);
                            }else if(comando[0].toUpperCase().equals("SCRSHOT")){
                                BufferedImage captura = new Robot().createScreenCapture(new Rectangle(getPnlLienzo().getLocationOnScreen().x, getPnlLienzo().getLocationOnScreen().y, getPnlLienzo().getWidth(), getPnlLienzo().getHeight()));
                                //voy a poner como nombre del archivo de imagen la fecha y la hora, ese va a ser el nombre del archivo
                                Date date = new Date();
                                DateFormat fecha = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                                //guardamos como jpg, primero verifico si existe la carpeta en donde voy a guardar los screenshot
                                File carpeta = new File("c:\\scrshot_tortuguis");
                                if(!carpeta.exists()){
                                    carpeta.mkdir();
                                    File file = new File(carpeta + "\\" + fecha.format(date) +".jpg");
                                    ImageIO.write(captura, "jpg", file);
                                } else {
                                    File file = new File(carpeta + "\\" + fecha.format(date) +".jpg");
                                    ImageIO.write(captura, "jpg", file);
                                }
                            } else if(repetir[0].toUpperCase().equals("REPEAT")){
                                int comandos = 1;
                                int valores = 0;
                                int posicion_comando = 0;
                                int posicion_valor = 1;
                                
                                for(int i = 1; i <= Integer.parseInt(repetir[1]); i++)
                                {
                                    while(comandos <= comandos_repetir.length / 2){
                                        comando_repetir(Integer.parseInt(repetir[1]), comandos_repetir[posicion_comando].toString(), comandos_repetir[posicion_valor].toString());
                                        posicion_comando = posicion_comando + 2;
                                        posicion_valor = posicion_valor + 2;
                                        comandos++;
                                    }
                                    posicion_comando = 0;
                                    posicion_valor = 1;
                                    comandos = 1;
                                }
                            } else{
                                getTaComandos().setText(getTexto() + "\nNo se reconoce el comando");
                            }
                        } else {
                            if(!comando[0].toUpperCase().equals("PU") || !comando[0].toUpperCase().equals("PD"))
                                getTaComandos().setText(getTexto() + "\nSintaxis del comando incorrecta");
                        }
                        repaint();
                    }
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
    }
    
    public void comando_repetir(int repeticion, String comando, String valor_comando){
        if(comando.toUpperCase().equals("FD")){
            if(getInicio() == false){   //si no es la posicion inicial de la tortuga
                if(!getLstPosTortuga().isEmpty()){  //verifico si la lista tiene tiene tortugas ingresadas
                    getLstPosTortuga().remove(0);   //elimino el dibujo anterior(la tortuga), para poder pintar el nuevo
                    if(getGrados_acumulados() == 0){
                        setPasos(Integer.parseInt(valor_comando)); //Guardo el numero de pasos
                        cuadrante1_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 1 && getGrados_acumulados() <= 89){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 90){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 91 && getGrados_acumulados() <= 179){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_positivo_adelante(getGrados_acumulados());    
                    } else if(getGrados_acumulados() == 180){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 181 && getGrados_acumulados() <= 269){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 270){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 271 && getGrados_acumulados() <= 359){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 360){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -1.00 && getGrados_acumulados() >= -89.00){ //Condiciones cuando los grados son negativos
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -90){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -180){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -270){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_negativo_adelante(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -360){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_negativo_adelante(getGrados_acumulados());
                    }
                }
            }
        }else if(comando.toUpperCase().equals("BK")){
            if(getInicio() == false){
                if(!getLstPosTortuga().isEmpty()){
                    getLstPosTortuga().remove(0);   //elimino el dibujo anterior(la tortuga), para poder pintar el nuevo
                    if(getGrados_acumulados() == 0){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 1 && getGrados_acumulados() <= 89){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 90){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 91 && getGrados_acumulados() <= 179){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 180){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 181 && getGrados_acumulados() <= 269){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == 270){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() >= 271 && getGrados_acumulados() <= 359){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_atras(getGrados_acumulados());        
                    } else if(getGrados_acumulados() == 360){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_positivo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -1.00 && getGrados_acumulados() >= -89.00){ //Condiciones cuando los grados son negativos
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -90){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante1_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -180){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante2_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -270){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante3_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_negativo_atras(getGrados_acumulados());
                    } else if(getGrados_acumulados() == -360){
                        setPasos(Integer.parseInt(valor_comando));
                        cuadrante4_negativo_atras(getGrados_acumulados());
                    }
                }
            }
        }else if(comando.toUpperCase().equals("RT")){
            if(getInicio() == false){
                if(!getLstPosTortuga().isEmpty()){
                    getLstPosTortuga().remove(0);
                    setRotacion(getRotacion() + Double.parseDouble(valor_comando));
                    double grados_acumulados_antes = getGrados_acumulados();
                    setGrados_acumulados(getRotacion());
                    double grados_acumulados_despues = getGrados_acumulados();
                    //llevando el control de los grados, reseteo los grados cuando se pase de 360 grados
                    if(getGrados_acumulados() > 360){ 
                        double recalcular_grados = getGrados_acumulados() / 360.00;
                        int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                        double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                        double nuevos_grados = parte_decimal * 360.00; //convierto los decimales a grados, que son los que resetean el acumuladro de grados
                        int entero = (int) Math.round(nuevos_grados);
                        nuevos_grados = entero;
                        setGrados_acumulados(nuevos_grados);
                    } else if(getGrados_acumulados() < -360){  //en el caso que gire al otro lado, y los angulos se pasen de -360
                        double recalcular_grados = getGrados_acumulados() / 360.00;
                        int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                        double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                        double nuevos_grados = parte_decimal * 360.00; //convierto los decimales a grados, que son los que resetean el acumulado de grados
                        int entero = (int) Math.round(nuevos_grados);
                        nuevos_grados = entero;
                        setGrados_acumulados(nuevos_grados);
                    } else{ //si no se ha pasado de 360 grados, que siga acumulando los grados que se ingresan en la consola
                        setGrados_acumulados(getRotacion());
                    }
                    lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
                    lblGradosAcum.setText(getGrados_acumulados().toString());
                    getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
                }
            }
        }else if(comando.toUpperCase().equals("LT")){
            if(getInicio() == false){
                if(!getLstPosTortuga().isEmpty()){
                    getLstPosTortuga().remove(0);
                    setRotacion(getRotacion() - Double.parseDouble(valor_comando));
                    setGrados_acumulados(getRotacion());
                    double grados_acumulados_despues = getGrados_acumulados();
                    //llevando el control de los grados, reseteo los grados cuando se pase de 360 grados
                    if(getGrados_acumulados() < -360){  //aqui era positivo y >
                        double recalcular_grados = getGrados_acumulados() / 360.00;
                        int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                        double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                        double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumulado de grados
                        int entero = (int) Math.round(nuevos_grados);
                        nuevos_grados = entero;
                        setGrados_acumulados(nuevos_grados);
                    } else if(getGrados_acumulados() > 360){ //en el caso que gire al otro lado, y los angulos se pasen de 360
                        double recalcular_grados = getGrados_acumulados() / 360.00;
                        int parte_entera = (int)recalcular_grados;  //obteniendo la parte entera
                        double parte_decimal = recalcular_grados - parte_entera;    //obteniendo la parte decimal
                        double nuevos_grados = parte_decimal * 360; //convierto los decimales a grados, que son los que resetean el acumuladro de grados
                        int entero = (int) Math.round(nuevos_grados);
                        nuevos_grados = entero;
                        setGrados_acumulados(nuevos_grados);
                    } else {
                        setGrados_acumulados(getRotacion());
                    }
                    lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
                    lblGradosAcum.setText(getGrados_acumulados().toString());
                    getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
                }
            }
        }
    }
    
    public int distancia_angular_x(double grados_acumulados, int pasos){
        double desplazamiento_x = 0.0;
        if(grados_acumulados >= 0.0 && grados_acumulados <= 90.0){
            desplazamiento_x = pasos * Math.sin(Math.toRadians(grados_acumulados));
        } else if(grados_acumulados >= 91.0 && grados_acumulados <= 180.0){
            desplazamiento_x = pasos * Math.cos(Math.toRadians(grados_acumulados - 90));
        } else if(grados_acumulados >= 181.0 && grados_acumulados <= 270.0){
            desplazamiento_x = pasos * Math.sin(Math.toRadians(grados_acumulados - 180));
        } else if(grados_acumulados >= 271.0 && grados_acumulados <= 360.0){
            desplazamiento_x = pasos * Math.cos(Math.toRadians(grados_acumulados - 270));
        } if(grados_acumulados <= 0.0 && grados_acumulados >= -90.0){    //Logica para los grados negativos en x
            desplazamiento_x = pasos * Math.sin(Math.toRadians(grados_acumulados));
        } else if(grados_acumulados <= -91.0 && grados_acumulados >= -180.0){
            desplazamiento_x = pasos * Math.cos(Math.toRadians(grados_acumulados - 90));
        } else if(grados_acumulados <= -181.0 && grados_acumulados >= -270.0){
            desplazamiento_x = pasos * Math.sin(Math.toRadians((grados_acumulados * -1) - 180));
        } else if(grados_acumulados <= -271.0 && grados_acumulados >= -360.0){
            desplazamiento_x = pasos * Math.cos(Math.toRadians(grados_acumulados - 270));
        }
        //System.out.println("x: " + (int) Math.round(desplazamiento_x));
        return (int) Math.round(desplazamiento_x);
    }
    
    public int distancia_angular_y(double grados_acumulados, int pasos){
        double desplazamiento_y = 0.0;
        if(grados_acumulados >= 0.0 && grados_acumulados <= 90.0){
            desplazamiento_y = pasos * Math.cos(Math.toRadians(grados_acumulados));
        } if(grados_acumulados >= 91.0 && grados_acumulados <= 180.0){
            desplazamiento_y = pasos * Math.sin(Math.toRadians(grados_acumulados - 90));
        } else if(grados_acumulados >= 181.0 && grados_acumulados <= 270.0){
            desplazamiento_y = pasos * Math.cos(Math.toRadians(grados_acumulados - 180));
        } else if(grados_acumulados >= 271.0 && grados_acumulados <= 360.0){
            desplazamiento_y = pasos * Math.sin(Math.toRadians(grados_acumulados - 270));
        } if(grados_acumulados <= 0.0 && grados_acumulados >= -90.0){        //Logica para los grados negativos en y
            desplazamiento_y = pasos * Math.cos(Math.toRadians(grados_acumulados));
        } if(grados_acumulados <= -91.0 && grados_acumulados >= -180.0){
            desplazamiento_y = pasos * Math.sin(Math.toRadians(grados_acumulados - 90));
        } else if(grados_acumulados <= -181.0 && grados_acumulados >= -270.0){
            desplazamiento_y = pasos * Math.cos(Math.toRadians(grados_acumulados - 180));
        } else if(grados_acumulados <= -271.0 && grados_acumulados >= -360.0){
            desplazamiento_y = pasos * Math.sin(Math.toRadians(grados_acumulados - 270));
        }
        //System.out.println("y: " + (int) Math.round(desplazamiento_y));
        return (int) Math.round(desplazamiento_y);
    }
    
    public void cuadrante1_positivo_adelante(double grados_acumulados){
        if(grados_acumulados == 0.00) {
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() - getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+getTortuga().getAlto(), getPuntoXFinal(), getPuntoYFinal()+getTortuga().getAlto(), getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(grados_acumulados >= 1 && grados_acumulados <= 89) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+getTortuga().getAlto(), getPuntoXFinal(), getPuntoYFinal()+getTortuga().getAlto(), getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante2_positivo_adelante(double grados_acumulados){
        if(grados_acumulados == 90) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
             //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() +25, getPuntoXFinal() -25+ getTortuga().getAlto(), getPuntoYFinal() + getTortuga().getAlto(), getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(grados_acumulados >= 91 && grados_acumulados <= 179) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() + getTortuga().getAlto(), getPuntoXFinal(), getPuntoYFinal()+ getTortuga().getAlto(), getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante3_positivo_adelante(double grados_acumulados){
        if(grados_acumulados == 180.00) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox());
            setPuntoy(getUltimo_puntoy() + getPasos());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal() + 25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(grados_acumulados >= 181 && grados_acumulados <= 269) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        }
    }
    
    public void cuadrante4_positivo_adelante(Double grados_acumulados){
        if(grados_acumulados == 270.00) {
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() + 25, getPuntoXFinal(), getPuntoYFinal() + 25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(grados_acumulados >= 271.00 && grados_acumulados <= 359) {
            setPuntoXInicial(getPuntox());
                setPuntoYInicial(getPuntoy());
                //puntos para dibujar la tortuguita
                setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
                setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
                setUltimo_puntox(getPuntox());
                setUltimo_puntoy(getPuntoy());
                lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
                lblGradosAcum.setText(getGrados_acumulados().toString());
                //punto final para la linea
                setPuntoXFinal(getUltimo_puntox());
                setPuntoYFinal(getUltimo_puntoy());
                getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() +25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
                getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(grados_acumulados == 360.00) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() - getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        }
    }
    
    public void cuadrante1_positivo_atras(Double grados_acumulados){
        if(getGrados_acumulados() == 0){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() + getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(getGrados_acumulados() >= 1 && getGrados_acumulados() <= 89){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/                    
        }
    }
    
    public void cuadrante2_positivo_atras(Double grados_acumulados){
        if(getGrados_acumulados() == 90){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal() +25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(getGrados_acumulados() >= 91 && getGrados_acumulados() <= 179){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() +25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        }
    }
    
    public void cuadrante3_positivo_atras(Double grados_acumulados){
        if(getGrados_acumulados() == 180){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox());
            setPuntoy(getUltimo_puntoy() - getPasos());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial() +25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(getGrados_acumulados() >= 181 && getGrados_acumulados() <= 269){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos())/*getPasos()*/);
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        }
    }
    
    public void cuadrante4_positivo_atras(Double grados_acumulados){
        if(getGrados_acumulados() == 270){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() >= 271 && getGrados_acumulados() <= 359){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == 360) {
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() + getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        }
    }
    
    public void cuadrante4_negativo_adelante(Double grados_acumulados){
        if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -360){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() - getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
             //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante3_negativo_adelante(Double grados_acumulados){
        if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
        } else if(getGrados_acumulados() == -270){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante2_negativo_adelante(Double grados_acumulados){
        if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -180){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox());
            setPuntoy(getUltimo_puntoy() + getPasos());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante1_negativo_adelante(Double grados_acumulados){
        if(grados_acumulados <= -1.00 && grados_acumulados >= -89.00){ //Condiciones cuando los grados son negativos
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(grados_acumulados == -90){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante4_negativo_atras(Double grados_acumulados){
        if(getGrados_acumulados() <= -271 && getGrados_acumulados() >= -359){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -360){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getPuntox()); //guardo la posicion x
            setPuntoy(getUltimo_puntoy() + getPasos()); //guardo la posicion y
            setUltimo_puntox(getPuntox());  //guardo la ultima posicion x
            setUltimo_puntoy(getPuntoy());  //guardo la ultima posicion y
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante3_negativo_atras(Double grados_acumulados){
        if(getGrados_acumulados() <= -181 && getGrados_acumulados() >= -269){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -270){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante2_negativo_atras(Double grados_acumulados){
        if(getGrados_acumulados() <= -91 && getGrados_acumulados() >= -179){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() - distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -180){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox());
            setPuntoy(getUltimo_puntoy() - getPasos());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    public void cuadrante1_negativo_atras(Double grados_acumulados){
        if(getGrados_acumulados() <= -1.00 && getGrados_acumulados() >= -89.00){ //Condiciones cuando los grados son negativos
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() - distancia_angular_x(getGrados_acumulados(), getPasos()));
            setPuntoy(getUltimo_puntoy() + distancia_angular_y(getGrados_acumulados(), getPasos()));
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        } else if(getGrados_acumulados() == -90){
            //puntos para dibujar la linea
            setPuntoXInicial(getPuntox());
            setPuntoYInicial(getPuntoy());
            //puntos para dibujar la tortuguita
            setPuntox(getUltimo_puntox() + getPasos());
            setPuntoy(getUltimo_puntoy());
            setUltimo_puntox(getPuntox());
            setUltimo_puntoy(getPuntoy());
            lblCoordenada.setText("x: " + getUltimo_puntox() + " y: " + getUltimo_puntoy());
            lblGradosAcum.setText(getGrados_acumulados().toString());
            //punto final para la linea
            setPuntoXFinal(getUltimo_puntox());
            setPuntoYFinal(getUltimo_puntoy());
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial()+25, getPuntoXFinal(), getPuntoYFinal()+25, getBajar_lapiz()));
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  //agrego a la estructura de datos las nuevas coordenadas de la tortuga
        }
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(getInicio() == true){
            setPuntox(getTortuga().getPuntox() - 0);
            setPuntoy(getTortuga().getPuntoy() - 0);
            setPuntoXInicial(getLinea().getPosXInicial() - 0);
            setPuntoYInicial(getLinea().getPosYInicial() - 0);
            setPuntoXFinal(getLinea().getPosXFinal() - 0);
            setPuntoYFinal(getLinea().getPosYFinal() - 0);
            //********Para que no se borre lo que pinté, voy a guardar las figuras en una estructura de datos.***********
            //agrego a la estructura de datos las nuevos puntos para pintar la linea
            getLstPosLinea().add(new Linea(getPuntoXInicial(), getPuntoYInicial(), getPuntoXFinal(), getPuntoYFinal(), getBajar_lapiz()));
            getLinea().posicionar_linea(g);
            //agrego a la estructura de datos las nuevas coordenadas de la tortuga*/
            getLstPosTortuga().add(new Tortuga(getPuntox(), getPuntoy(), 25, 25, getRotacion()));  
            getTortuga().dibujar_tortuga(g);
            setInicio(false);
        }
        for(Linea linea : getLstPosLinea()){    //recorro la listas de puntos de la linea
            linea.dibujar_linea(g, getPuntoXInicial(), getPuntoYInicial(), getPuntoXFinal(), getPuntoYFinal(), getBajar_lapiz());
        }
        for(Tortuga tortuga : getLstPosTortuga()){  //recorro la lista de posiciones de la tortuguita
            tortuga.posicionar_tortuga(g, getPuntox(), getPuntoy(), getRotacion());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlLienzo = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblCoordenada = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblGradosAcum = new javax.swing.JLabel();
        pnlComandos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taComandos = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        pnlLienzo.setBackground(new java.awt.Color(255, 255, 255));
        pnlLienzo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlLienzo.setLayout(null);

        jLabel7.setForeground(new java.awt.Color(0, 51, 255));
        jLabel7.setText("Coordenada (x, y):");
        pnlLienzo.add(jLabel7);
        jLabel7.setBounds(880, 560, 120, 16);

        lblCoordenada.setFont(new java.awt.Font("Comic Sans MS", 1, 13)); // NOI18N
        lblCoordenada.setForeground(new java.awt.Color(255, 0, 0));
        lblCoordenada.setText("jLabel8");
        pnlLienzo.add(lblCoordenada);
        lblCoordenada.setBounds(1000, 560, 120, 19);

        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setText("Grados acumulados:");
        pnlLienzo.add(jLabel8);
        jLabel8.setBounds(880, 580, 120, 16);

        lblGradosAcum.setFont(new java.awt.Font("Comic Sans MS", 1, 13)); // NOI18N
        lblGradosAcum.setForeground(new java.awt.Color(255, 0, 0));
        lblGradosAcum.setText("jLabel9");
        pnlLienzo.add(lblGradosAcum);
        lblGradosAcum.setBounds(1000, 580, 120, 19);

        pnlComandos.setBackground(new java.awt.Color(255, 255, 255));
        pnlComandos.setBorder(javax.swing.BorderFactory.createTitledBorder("Comandos"));
        pnlComandos.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("Adelante: FD");
        pnlComandos.add(jLabel1);
        jLabel1.setBounds(20, 30, 140, 15);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 255));
        jLabel2.setText("Atras: BK");
        pnlComandos.add(jLabel2);
        jLabel2.setBounds(20, 50, 140, 15);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 51, 255));
        jLabel3.setText("Girar izquierda: LT");
        pnlComandos.add(jLabel3);
        jLabel3.setBounds(20, 70, 140, 15);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setText("Girar derecha: RT");
        pnlComandos.add(jLabel4);
        jLabel4.setBounds(20, 90, 140, 15);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 51, 255));
        jLabel5.setText("Lapiz arriba: PU");
        pnlComandos.add(jLabel5);
        jLabel5.setBounds(20, 110, 140, 15);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 255));
        jLabel9.setText("Lapiz abajo: PD");
        pnlComandos.add(jLabel9);
        jLabel9.setBounds(20, 130, 140, 15);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 51, 255));
        jLabel10.setText("Limpiar: HOME");
        pnlComandos.add(jLabel10);
        jLabel10.setBounds(20, 170, 140, 15);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 255));
        jLabel11.setText("REPEAT N[C 1 C 2 C 3...C N]");
        pnlComandos.add(jLabel11);
        jLabel11.setBounds(20, 150, 170, 15);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 51, 255));
        jLabel6.setText("Ocultar ayuda: HELPHIDD");
        pnlComandos.add(jLabel6);
        jLabel6.setBounds(20, 230, 170, 15);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 255));
        jLabel12.setText("Tomar foto: SCRSHOT");
        pnlComandos.add(jLabel12);
        jLabel12.setBounds(20, 190, 170, 15);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 255));
        jLabel13.setText("Mostrar ayuda: HELP");
        pnlComandos.add(jLabel13);
        jLabel13.setBounds(20, 210, 170, 15);

        pnlLienzo.add(pnlComandos);
        pnlComandos.setBounds(10, 10, 190, 260);

        getContentPane().add(pnlLienzo);
        pnlLienzo.setBounds(0, 0, 1130, 600);

        taComandos.setBackground(new java.awt.Color(204, 204, 204));
        taComandos.setColumns(20);
        taComandos.setFont(new java.awt.Font("Consolas", 0, 22)); // NOI18N
        taComandos.setRows(5);
        taComandos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                taComandosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(taComandos);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 600, 1130, 160);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void taComandosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taComandosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_taComandosKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCoordenada;
    private javax.swing.JLabel lblGradosAcum;
    private javax.swing.JPanel pnlComandos;
    private javax.swing.JPanel pnlLienzo;
    private javax.swing.JTextArea taComandos;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the tortuga
     */
    public Tortuga getTortuga() {
        return tortuga;
    }

    /**
     * @param tortuga the tortuga to set
     */
    public void setTortuga(Tortuga tortuga) {
        this.tortuga = tortuga;
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
     * @return the jScrollPane1
     */
    public javax.swing.JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    /**
     * @param jScrollPane1 the jScrollPane1 to set
     */
    public void setjScrollPane1(javax.swing.JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    /**
     * @return the pnlLienzo
     */
    public javax.swing.JPanel getPnlLienzo() {
        return pnlLienzo;
    }

    /**
     * @param pnlLienzo the pnlLienzo to set
     */
    public void setPnlLienzo(javax.swing.JPanel pnlLienzo) {
        this.pnlLienzo = pnlLienzo;
    }

    /**
     * @return the taComandos
     */
    public javax.swing.JTextArea getTaComandos() {
        return taComandos;
    }

    /**
     * @param taComandos the taComandos to set
     */
    public void setTaComandos(javax.swing.JTextArea taComandos) {
        this.taComandos = taComandos;
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the ultimo_comando
     */
    public String getUltimo_comando() {
        return ultimo_comando;
    }

    /**
     * @param ultimo_comando the ultimo_comando to set
     */
    public void setUltimo_comando(String ultimo_comando) {
        this.setUltimo_comando(ultimo_comando);
    }

    /**
     * @return the cadena_comandos
     */
    public String[] getCadena_comandos() {
        return cadena_comandos;
    }

    /**
     * @param cadena_comandos the cadena_comandos to set
     */
    public void setCadena_comandos(String[] cadena_comandos) {
        this.cadena_comandos = cadena_comandos;
    }

    /**
     * @return the lstTortuga
     */
    public ArrayList<Tortuga> getLstPosTortuga() {
        return lstPosTortuga;
    }

    /**
     * @param lstTortuga the lstTortuga to set
     */
    public void setLstPosTortuga(ArrayList<Tortuga> lstPosTortuga) {
        this.lstPosTortuga = lstPosTortuga;
    }

    /**
     * @return the inicio
     */
    public boolean getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(boolean inicio) {
        this.inicio = inicio;
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
     * @return the rotacion
     */
    public Double getRotacion() {
        return rotacion;
    }

    /**
     * @param rotacion the rotacion to set
     */
    public void setRotacion(Double rotacion) {
        this.rotacion = rotacion;
    }

    /**
     * @return the grados_acumulados
     */
    public Double getGrados_acumulados() {
        return grados_acumulados;
    }

    /**
     * @param grados_acumulados the grados_acumulados to set
     */
    public void setGrados_acumulados(Double grados_acumulados) {
        this.grados_acumulados = grados_acumulados;
    }

    /**
     * @return the ultimo_puntox
     */
    public int getUltimo_puntox() {
        return ultimo_puntox;
    }

    /**
     * @param ultimo_puntox the ultimo_puntox to set
     */
    public void setUltimo_puntox(int ultimo_puntox) {
        this.ultimo_puntox = ultimo_puntox;
    }

    /**
     * @return the ultimo_puntoy
     */
    public int getUltimo_puntoy() {
        return ultimo_puntoy;
    }

    /**
     * @param ultimo_puntoy the ultimo_puntoy to set
     */
    public void setUltimo_puntoy(int ultimo_puntoy) {
        this.ultimo_puntoy = ultimo_puntoy;
    }

    /**
     * @return the pasos
     */
    public int getPasos() {
        return pasos;
    }

    /**
     * @param pasos the pasos to set
     */
    public void setPasos(int pasos) {
        this.pasos = pasos;
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
     * @return the lstPosLinea
     */
    public ArrayList<Linea> getLstPosLinea() {
        return lstPosLinea;
    }

    /**
     * @param lstPosLinea the lstPosLinea to set
     */
    public void setLstPosLinea(ArrayList<Linea> lstPosLinea) {
        this.lstPosLinea = lstPosLinea;
    }

    /**
     * @return the linea
     */
    public Linea getLinea() {
        return linea;
    }

    /**
     * @param linea the linea to set
     */
    public void setLinea(Linea linea) {
        this.linea = linea;
    }
}