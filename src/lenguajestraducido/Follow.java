/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenguajestraducido;

import java.util.*;

/**
 *
 * @author FalconJecm
 */
public class Follow {
    List<Elementos> nodos;
    List<Integer> hojasfollow;
    public List<Follow> follow;
    Elementos izquierdo;
    Elementos derecho;
    int hoja;
    Form1 form;
    String textoImprimir;


    public int posicion;
    public ArrayList<Integer> elementos;

    public Follow()
    {
        posicion = 0;
        elementos = null;
    }

    public void agregarPosicion(int nuevaPosicion)
    {
        posicion = nuevaPosicion;
    }

    public void agregarElementos(ArrayList<Integer> nuevosElementos)
    {
        elementos = nuevosElementos;
    }

    public ArrayList<Integer> elemnetosAList()
    {
        //int[] myArray = (int[])elementos.ToArray(typeof(int));

        return elementos;
    }
    
}
