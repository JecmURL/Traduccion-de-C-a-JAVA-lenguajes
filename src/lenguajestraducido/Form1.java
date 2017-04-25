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
public class Form1 extends javax.swing.JFrame {

    public static int lineas = 0;
    public static String txt;
    public static int posicion = 0;

    ArrayList<String> ListaID = new ArrayList<String>();
    ArrayList<String> ListaNumeros = new ArrayList<String>();
    ArrayList<String> ListaExpReg = new ArrayList<String>();
    ExpresionesRegulares RegExp;

    Gramatica gram = new Gramatica();
    public Form1()
    {
        InitializeComponent();
    }

    private void btnCargar_Click(object sender, EventArgs e)
    {
        Limpiar();
        rtbArchivo.Text = "";
        OpenFileDialog openFileDialog1 = new OpenFileDialog();
        openFileDialog1.Filter = "Text Files|*.txt";
        openFileDialog1.Title = "Select a Text File";
        lineas = 0;
        if (openFileDialog1.ShowDialog() == System.Windows.Forms.DialogResult.OK)
        {
            try
            {
                StreamReader sr = new StreamReader(openFileDialog1.FileName);

                string line;

                while ((line = sr.ReadLine()) != null)
                {
                    rtbArchivo.Text += line + "\r\n";
                    lineas++;
                }
                sr.Close();
            }
            catch
            {
                // Let the user know what went wrong.
                Console.WriteLine("The file could not be read:");
            }
        }

    }

    private void btnCompilar_Click(object sender, EventArgs e)
    {
        Limpiar();
        Tomar_texto();
        Comprobar_texto();
    }

    public void Limpiar()
    {
        lblCorrecto.Text = "...........";
        lblError.Text = ".............";
        ListaID.Clear();
        ListaNumeros.Clear();
        ListaExpReg.Clear();
        rtbArchivo.Select(0, posicion);
        rtbArchivo.SelectionBackColor = Color.White;
        rtbArchivo.DeselectAll();
    }
    public void Error(String error, int caracter)
    {
        lblError.Text = "Error: " + error;
        rtbArchivo.Select(0, caracter);
        rtbArchivo.SelectionBackColor = Color.Orange;
        posicion = txt.Length;
    }

    public void Tomar_texto()
    {
        txt = rtbArchivo.Text;
    }

    public void Comprobar_texto()
    {
        Tokens(0);
    }

    public void Tokens(int caracter)
    {
        try
        {
            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
            {
                caracter++;
            }
            if (gram.Analizar_Tokens(txt.Substring(caracter, 6)))
            {
                caracter += 6;
                Token(caracter);
            }
            else
            {
                Error("no empezó con palabra TOKENS", caracter);
            }
        }
        catch
        {
            MessageBox.Show("La caja de texto no puede estar vacia o falta la palabra reservada END");
        }

    }

    public void Token(int caracter)
    {
        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
        {
            caracter++;
        }

        if (gram.Analizar_End(txt.Substring(caracter, 3)))
        { 
            lblCorrecto.Text = "Felicitaciones el archivo no posee errores";

        }
        else
        {
            if (gram.Analizar_Token(txt.Substring(caracter, 5)))
            {
                caracter += 5;
                String numero = "";
                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                {
                    caracter++;
                }
                if (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                {
                    numero = txt.Substring(caracter, 1);
                    caracter++;
                    while (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                    {
                        numero += txt.Substring(caracter, 1);
                        caracter++;
                    }
                    if (!NumeroExiste(numero))
                    {
                        ListaNumeros.Add(numero);
                        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                        {
                            caracter++;
                        }
                        if (txt.Substring(caracter, 1).Equals("="))
                        {
                            caracter++;
                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                            {
                                caracter++;
                            }
                            ExpReg(caracter, caracter, false, 0);
                        }
                        else
                        {
                            Error("se esperaba signo '='", caracter);
                        }
                    }
                    else
                    {
                        Error("El número de Token o palabra reservada ya esta asignado", caracter);
                    }
                }
                else
                {
                    Error("se esperaba numero", caracter);
                }
            }
            else
            {
                string id = "";
                while (gram.Analizar_ID(txt.Substring(caracter, 1)))
                {
                    id += txt.Substring(caracter, 1);
                    caracter++;
                }
                if (gram.Analizar_ID(txt.Substring(caracter - id.Length, id.Length)))
                {
                    String conjunto = id.ToLower();
                    if (!IDExistente(conjunto))
                    {
                        ListaID.Add(conjunto);
                        Conjunto(caracter - id.Length, false, false);
                    }
                    else
                    {
                        Error("ID ya definido", caracter);
                    }

                }
                else
                {
                    Error("Error en definición de ID", caracter);
                }
            }
        }


    }

    public void ExpReg(int chrinicioER,  int caracter, bool HayExpReg, int parentesis)
    {
        int chrprueba = caracter;
        while (gram.Analizar_Espacio(txt.Substring(chrprueba, 1)))
        {
            chrprueba++;
        }
        if (txt.Substring(chrprueba, 1).Equals("'"))
        {
            if (txt.Substring(chrprueba + 2, 1).Equals("'"))
            {
                chrprueba += 3;
                while(gram.Analizar_Espacio(txt.Substring(chrprueba,1)))
                {
                    chrprueba++;
                }
                if (txt.Substring(chrprueba, 1).Equals("{"))
                {
                    int chrfinalER = chrprueba;
                    ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER - chrinicioER)).Trim()));
                    chrprueba++;
                    Reservadas(chrprueba);
                }
                else
                {
                    ExpReg(chrinicioER, chrprueba, true, parentesis);
                }
            }
            else
            {
                Error("se esperaba ' ' ' de cierre", caracter);
            }
        }
        else
        {
            if (txt.Substring(chrprueba, 1).Equals("\""))
            {
                if (txt.Substring(chrprueba + 2, 1).Equals("\""))
                {
                    chrprueba += 3;
                    while (gram.Analizar_Espacio(txt.Substring(chrprueba, 1)))
                    {
                        chrprueba++;
                    }
                    if (txt.Substring(chrprueba, 1).Equals("{"))
                    {
                        int chrfinalER = chrprueba;
                        ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER - chrinicioER)).Trim()));
                        chrprueba++;
                        Reservadas(chrprueba);
                    }
                    else
                    {
                        ExpReg(chrinicioER, chrprueba, true, parentesis);
                    }
                }
                else
                {
                    Error("se esperaba '\"' de cierre", caracter);
                }
            }
            else
            {
                while (gram.Analizar_Espacio(txt.Substring(chrprueba, 1)))
                {
                    chrprueba++;
                }
                string id = "";
                while (gram.Analizar_ID(txt.Substring(chrprueba,1)))
                {
                    id += txt.Substring(chrprueba, 1);
                    chrprueba++;
                }
                String conjunto = id.ToLower();
                if (gram.Analizar_ID(txt.Substring(chrprueba - id.Length, id.Length)) && !gram.Analizar_Token(conjunto))
                {
                    if (IDExistente(conjunto))
                    {

                        while (gram.Analizar_Espacio(txt.Substring(chrprueba, 1)))
                        {
                            chrprueba++;
                        }
                        if (txt.Substring(chrprueba, 1).Equals("{"))
                        {
                            int chrfinalER = chrprueba;
                            ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER - chrinicioER)).Trim()));
                            chrprueba++;
                            Reservadas(chrprueba);
                        }
                        else
                        {
                            ExpReg(chrinicioER, chrprueba, true, parentesis);
                        }
                    }
                    else
                    {
                        Error("El conjunto no ha sido declarado", caracter);
                    }

                }
                else
                {
                    chrprueba -= id.Length;
                    if (gram.Analizar_CS(txt.Substring(chrprueba, 1)))
                    {
                        int chrprevio = chrprueba - 1;
                        while (gram.Analizar_Espacio(txt.Substring(chrprevio, 1)))
                        {
                            chrprevio--;
                        }
                        string compare = txt.Substring(chrprevio, 1);
                        if (!gram.Analizar_CS(compare) && !compare.Equals("|"))
                        {
                            chrprueba++;
                            while(gram.Analizar_Espacio(txt.Substring(chrprueba,1)))
                            {
                                chrprueba++;
                            }
                            if (txt.Substring(chrprueba, 1).Equals("{"))
                            {
                                int chrfinalER = chrprueba;
                                ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER - chrinicioER)).Trim()));
                                chrprueba++;
                                Reservadas(chrprueba);
                            }
                            else
                            {
                                ExpReg(chrinicioER, chrprueba, true, parentesis);
                            }
                        }
                        else
                        {
                            Error("Error en declaración de Expresión Regular", chrprueba);
                        }
                    }
                    else
                    {
                        if (txt.Substring(chrprueba, 1).Equals("|"))
                        {
                            chrprueba++;
                            ExpReg(chrinicioER, chrprueba, false, parentesis);
                        }
                        else
                        {
                            if (txt.Substring(chrprueba, 1).Equals("("))
                            {
                                chrprueba++;
                                parentesis++;
                                ExpReg(chrinicioER, chrprueba, false, parentesis);
                            }
                            else
                            {
                                if (txt.Substring(chrprueba, 1).Equals(")"))
                                {
                                    if (parentesis != 0)
                                    {
                                        chrprueba++;
                                        parentesis--;
                                        if (txt.Substring(chrprueba, 1).Equals("{"))
                                        {
                                            int chrfinalER = chrprueba;
                                            ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER - chrinicioER)).Trim()));
                                            chrprueba++;
                                            Reservadas(chrprueba);
                                        }
                                        else
                                        {
                                            ExpReg(chrinicioER, chrprueba, false, parentesis);
                                        }
                                    }
                                    else
                                    {
                                        Error("Cerrado de Parentesis Inesperado", caracter);
                                    }
                                }
                                else
                                {
                                    if(txt.Substring(chrprueba, 1).Equals(";"))
                                    {
                                        int chrfinalER = chrprueba;
                                        chrprueba++;

                                        while(gram.Analizar_Espacio(txt.Substring(chrprueba,1)))
                                        {
                                            chrprueba++;
                                        }
                                        if(chrprueba < txt.Length)
                                        {
                                            ListaExpReg.Add((txt.Substring(chrinicioER, (chrfinalER-chrinicioER)).Trim()));
                                            Token(chrprueba);
                                        }
                                        else
                                        {
                                            Error("Se esperaba la palabra reservada END ", chrprueba);
                                        }
                                    }
                                    else
                                    {
                                        Error("Se esperaba ;", caracter);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void Reservadas(int caracter)
    {
        while(gram.Analizar_Espacio(txt.Substring(caracter,1)))
        {
            caracter++;
        }
        String numero = "";
        if(gram.Analizar_Digito(txt.Substring(caracter,1)))
        {
            numero = txt.Substring(caracter, 1);
            caracter++;
            while(gram.Analizar_Digito(txt.Substring(caracter,1)))
            {
                numero += txt.Substring(caracter, 1);
                caracter++;
            }
            if(!NumeroExiste(numero))
            {
                ListaNumeros.Add(numero);
                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                {
                    caracter++;
                }
                if (txt.Substring(caracter, 1).Equals("="))
                {
                    caracter++;
                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                    {
                        caracter++;
                    }
                    if (txt.Substring(caracter, 1).Equals("'"))
                    {
                        caracter++;
                        string id = "";
                        while (gram.Analizar_ID(txt.Substring(caracter, 1)))
                        {
                            id += txt.Substring(caracter, 1);
                            caracter++;
                        }
                        if (gram.Analizar_ID(txt.Substring(caracter - id.Length, id.Length)))
                        {
                            if (txt.Substring(caracter, 1).Equals("'"))
                            {
                                caracter++;
                                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                {
                                    caracter++;
                                }
                                if (txt.Substring(caracter, 1).Equals("}"))
                                {
                                    caracter++;
                                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                    {
                                        caracter++;
                                    }
                                    if (txt.Substring(caracter, 1).Equals(";"))
                                    {
                                        caracter++;
                                        Token(caracter);
                                    }
                                    else
                                    {
                                        Error("Se esperaba ; ", caracter);
                                    }
                                }
                                else
                                {
                                    if (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                                    {
                                        Reservadas(caracter);
                                    }
                                    else
                                    {
                                        Error("Se esperaba } ", caracter);
                                    }
                                }
                            }
                            else
                            {
                                Error("Se esperaba ' ", caracter);
                            }
                        }
                        else
                        {
                            Error("Se esperaba un ID", caracter);
                        }
                    }
                    else
                    {
                        if (txt.Substring(caracter, 1).Equals("\""))
                        {
                            caracter++;
                            string id = "";
                            while (gram.Analizar_ID(txt.Substring(caracter, 1)))
                            {
                                id += txt.Substring(caracter, 1);
                                caracter++;
                            }
                            if (gram.Analizar_ID(txt.Substring(caracter - id.Length, id.Length)))
                            {
                                if (txt.Substring(caracter, 1).Equals("\""))
                                {
                                    caracter++;
                                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                    {
                                        caracter++;
                                    }
                                    if (txt.Substring(caracter, 1).Equals("}"))
                                    {
                                        caracter++;
                                        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                        {
                                            caracter++;
                                        }
                                        if (txt.Substring(caracter, 1).Equals(";"))
                                        {
                                            caracter++;
                                            Token(caracter);
                                        }
                                        else
                                        {
                                            Error("Se esperaba ; ", caracter);
                                        }
                                    }
                                    else
                                    {
                                        if (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                                        {
                                            Reservadas(caracter);
                                        }
                                        else
                                        {
                                            Error("Se esperaba } ", caracter);
                                        }
                                    }
                                }
                                else
                                {
                                    Error("Se esperaba \" ", caracter);
                                }
                            }
                            else
                            {
                                Error("Se esperaba un ID", caracter);
                            }
                        }
                        else
                        {
                            Error("Se esperaba un \" o un ' ", caracter);
                        }
                    }
                }
                else
                {
                    Error("Se esperaba un = ", caracter);
                }
            }
            else
            {
                Error("El número de Token o palabra reservada ya esta asignado", caracter);
            }

        }
        else
        {
            Error("Se esperaba un número ", caracter);
        }
    }

    public void Conjunto(int caracter, Boolean add, Boolean close)
    {
        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
        {
            caracter++;
        }
        String id = "";
        while (!gram.Analizar_Espacio(txt.Substring(caracter, 1)) && !(txt.Substring(caracter, 1).Equals("{")))
        {
            id += txt.Substring(caracter, 1);
            caracter++;
        }
        if (gram.Analizar_ID(id))
        {

            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
            {
                caracter++;
            }
            if (txt.Substring(caracter, 1).Equals("{"))
            {
                caracter++;
                Inner_Conjunto(caracter, add, close);
            }
            else
            {
                Error("se esperaba {", caracter);
            }
        }
        else
        {
            Error("ID no valido", caracter);
        }
    }

    public void Inner_Conjunto(int caracter, Boolean add, Boolean close)
    {
        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
        {
            caracter++;
        }

        String prueba = txt.Substring(caracter, 1);
        //caso 'char'..'char' o solo 'char'
        if (txt.Substring(caracter, 1).Equals("'"))
        {
            if (txt.Substring(caracter + 2, 1).Equals("'"))
            {
                char range1 = txt.Substring(caracter+1,1).ToCharArray()[0];
                int rango1 = (int)range1;

                caracter += 3;
                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                {
                    caracter++;
                }
                if(txt.Substring(caracter,1).Equals("+"))
                {
                    caracter++;
                    Inner_Conjunto(caracter, true, true);
                }
                else
                {
                    if(txt.Substring(caracter,1).Equals("}"))
                    {
                        caracter++;
                        Token(caracter);
                    }
                    else
                    {
                        if (txt.Substring(caracter, 2).Equals(".."))
                        {
                            caracter += 2;
                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                            {
                                caracter++;
                            }
                            if (txt.Substring(caracter, 1).Equals("'"))
                            {
                                if (txt.Substring(caracter + 2, 1).Equals("'"))
                                {
                                    char range2 = txt.Substring(caracter + 1, 1).ToCharArray()[0];
                                    int rango2 = (int)range2;
                                    caracter += 3;
                                    if (rango2 > rango1)
                                    {
                                        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                        {
                                            caracter++;
                                        }
                                        if (txt.Substring(caracter, 1).Equals("+"))
                                        {
                                            caracter++;
                                            Inner_Conjunto(caracter, true, true);
                                        }
                                        else
                                        {
                                            if (txt.Substring(caracter, 1).Equals("}"))
                                            {
                                                caracter++;
                                                Token(caracter);
                                            }
                                            else
                                            {
                                                Error("Se esperaba el signo + o el } para cerrar el conjunto", caracter);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Error("El rango del conjunto no es válido", caracter);
                                    }

                                }
                                else
                                {
                                    Error("Se esperaba '", caracter);
                                }
                            }
                            else
                            {
                                Error("Se esperaba '", caracter);
                            }
                        }
                        else
                        {
                            Error("Se esperaba ..", caracter);
                        }
                    }

                }

            }
            else
            {
                Error("se esperaba '", caracter);
            }
        }
        else
        {
            //caso "char".."char" o solo "char"
            if (txt.Substring(caracter, 1).Equals("\""))
            {
                if (txt.Substring(caracter + 2, 1).Equals("\""))
                {
                    char range1 = txt.Substring(caracter + 1, 1).ToCharArray()[0];
                    int rango1 = (int)range1;
                    caracter += 3;
                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                    {
                        caracter++;
                    }
                    if (txt.Substring(caracter, 1).Equals("+"))
                    {
                        caracter++;
                        Inner_Conjunto(caracter, true, true);
                    }
                    else
                    {
                        if (txt.Substring(caracter, 1).Equals("}"))
                        {
                            caracter++;
                            Token(caracter);
                        }
                        else
                        {
                            if (txt.Substring(caracter, 2).Equals(".."))
                            {
                                caracter += 2;
                                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                {
                                    caracter++;
                                }
                                if (txt.Substring(caracter, 1).Equals("\"")||txt.Substring(caracter,1).Equals("'"))
                                {
                                    if (txt.Substring(caracter + 2, 1).Equals("\""))
                                    {
                                        char range2 = txt.Substring(caracter + 1, 1).ToCharArray()[0];
                                        int rango2 = (int)range2;
                                        caracter += 3;
                                        if(rango2 > rango1)
                                        {
                                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                            {
                                                caracter++;
                                            }
                                            if (txt.Substring(caracter, 1).Equals("+"))
                                            {
                                                caracter++;
                                                Inner_Conjunto(caracter, true, true);
                                            }
                                            else
                                            {
                                                if (txt.Substring(caracter, 1).Equals("}"))
                                                {
                                                    caracter++;
                                                    Token(caracter);
                                                }
                                                else
                                                {
                                                    Error("Se esperaba el signo + o el } para cerrar el conjunto", caracter);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            Error("El rango del conjunto no es válido", caracter);
                                        }
                                    }
                                    else
                                    {
                                        Error("Se esperaba un char ", caracter);
                                    }
                                }
                                else
                                {
                                    Error("Se esperaba \" ", caracter);
                                }
                            }
                            else
                            {
                                Error("Se esperaba ..", caracter);
                            }
                        }

                    }

                }
                else
                {
                    Error("se esperaba \" ", caracter);
                }
            }
            else
            {
                //chr(num)..chr(num) sólo char(num)
                if (gram.Analizar_CHR(txt.Substring(caracter, 3)))
                {
                    caracter += 3;
                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                    {
                        caracter++;
                    }
                    if (txt.Substring(caracter, 1).Equals("("))
                    {
                        caracter++;
                        while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                        {
                            caracter++;
                        }
                        if (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                        {
                            String valor1 = txt.Substring(caracter, 1);
                            caracter++;
                            while (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                            {
                                valor1 += txt.Substring(caracter, 1);
                                caracter++;
                            }
                            int rango1 = Convert.ToInt32(valor1);
                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                            {
                                caracter++;
                            }
                            if (txt.Substring(caracter, 1).Equals(")"))
                            {
                                caracter++;
                                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                {
                                    caracter++;
                                }

                                if(txt.Substring(caracter, 1).Equals("+"))
                                {
                                    caracter++;
                                    Inner_Conjunto(caracter, true, true);
                                }
                                else
                                {
                                    if(txt.Substring(caracter,1).Equals("}"))
                                    {
                                        caracter++;
                                        Token(caracter);
                                    }
                                    else
                                    {
                                        if (txt.Substring(caracter, 2).Equals(".."))
                                        {
                                            caracter += 2;
                                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                            {
                                                caracter++;
                                            }
                                            if (gram.Analizar_CHR(txt.Substring(caracter, 3)))
                                            {
                                                caracter += 3;
                                                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                                {
                                                    caracter++;
                                                }
                                                if (txt.Substring(caracter, 1).Equals("("))
                                                {
                                                    caracter++;
                                                    while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                                    {
                                                        caracter++;
                                                    }
                                                    if (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                                                    {
                                                        String valor2 = txt.Substring(caracter, 1);
                                                        caracter++;
                                                        while (gram.Analizar_Digito(txt.Substring(caracter, 1)))
                                                        {
                                                            valor2 += txt.Substring(caracter, 1);
                                                            caracter++;
                                                        }
                                                        int rango2 = Convert.ToInt32(valor2);

                                                        if(rango2 > rango1)
                                                        {
                                                            while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                                            {
                                                                caracter++;
                                                            }
                                                            if (txt.Substring(caracter, 1).Equals(")"))
                                                            {
                                                                caracter++;
                                                                while (gram.Analizar_Espacio(txt.Substring(caracter, 1)))
                                                                {
                                                                    caracter++;
                                                                }

                                                                if (txt.Substring(caracter, 1).Equals("+"))
                                                                {
                                                                    caracter++;
                                                                    Inner_Conjunto(caracter, true, true);
                                                                }
                                                                else
                                                                {
                                                                    if (txt.Substring(caracter, 1).Equals("}"))
                                                                    {
                                                                        caracter++;
                                                                        Token(caracter);
                                                                    }
                                                                    else
                                                                    {
                                                                        Error("Se esperaba el signo + o el }", caracter);
                                                                    }
                                                                }
                                                            }
                                                            else
                                                            {
                                                                Error("Se esperaba un )", caracter);
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Error("El rango del conjunto no es válido", caracter);
                                                        }

                                                    }
                                                    else
                                                    {
                                                        Error("Se esperaba un digito", caracter);
                                                    }
                                                }
                                                else
                                                {
                                                    Error("Se esperaba un (", caracter);
                                                }
                                            }
                                            else
                                            {
                                                Error("Error al definir CHR", caracter);
                                            }
                                        }
                                        else
                                        {
                                            Error("Se esperaba ..", caracter);
                                        }
                                    }
                                }
                            }
                            else
                            {
                                Error("Se esperaba un )", caracter);
                            }
                        }
                        else
                        {
                            Error("Se esperaba un digito", caracter);
                        }
                    }
                    else
                    {
                        Error("Se esperaba un (", caracter);
                    }
                }
                else
                {
                    Error("Se esperaba la correcta definicion de un conjunto", caracter);
                }  
            }
        }
    }

    public Boolean IDExistente(String id)
    {
        String conjunto = id.ToLower();
        return ListaID.Contains(conjunto);
    }

    public Boolean NumeroExiste(String num)
    {
        return ListaNumeros.Contains(num);
    }

    public void RevisarExpresiones(List<string> ER)
    {
        String imprimir = "";
        string[] Expresiones = ER.ToArray();
        string[] NuevasExpresiones = new string[Expresiones.Length];
        for (int i = 0; i < Expresiones.Length; i++)
        {
            int posicion = 0;
            String cadena = Expresiones[i];
            String parte1 = "";
            String parte2 = "";
            String NuevaCadena = "";
            bool bandera = false;

            while(posicion < cadena.Length)
            {
                while(gram.Analizar_Espacio(cadena.Substring(posicion,1)))
                {
                    posicion++;
                }
                if (cadena.Substring(posicion, 1).Equals("*") || cadena.Substring(posicion, 1).Equals("+") || cadena.Substring(posicion, 1).Equals("?"))
                {
                    parte1 = cadena.Substring(0, posicion).Trim();
                    parte2 = cadena.Substring(posicion).Trim();
                    NuevaCadena = parte1 + parte2;
                    cadena = NuevaCadena;
                    NuevasExpresiones[i] = NuevaCadena;
                    bandera = true;
                }                    
                posicion++;
            }
            if(!bandera)
            {
                NuevasExpresiones[i] = Expresiones[i];
            }
        }
        for(int i=0; i<NuevasExpresiones.Length; i++)
        {
            //int numhojas = 0;
            RegExp = new ExpresionesRegulares();
            Elementos raiz = RegExp.revisarExpReg1(NuevasExpresiones[i], ListaID, false, ListaNumeros, i);
            imprimir = imprimir + RegExp.getTextImprimir();
        }
        rtbCalculos.Text = imprimir;
    }

    private void button1_Click(object sender, EventArgs e)
    {
        if(lblCorrecto.Text.Equals("Felicitaciones el archivo no posee errores"))
        {
            RevisarExpresiones(ListaExpReg);
        }
    }

    private void cargarArchivoToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Limpiar();
        rtbArchivo.Text = "";
        OpenFileDialog openFileDialog1 = new OpenFileDialog();
        openFileDialog1.Filter = "Text Files|*.txt";
        openFileDialog1.Title = "Select a Text File";
        lineas = 0;
        if (openFileDialog1.ShowDialog() == System.Windows.Forms.DialogResult.OK)
        {
            try
            {
                StreamReader sr = new StreamReader(openFileDialog1.FileName);

                string line;

                while ((line = sr.ReadLine()) != null)
                {
                    rtbArchivo.Text += line + "\r\n";
                    lineas++;
                }
                sr.Close();
            }
            catch
            {
                // Let the user know what went wrong.
                Console.WriteLine("The file could not be read:");
            }
        }
    }
    
    
    /**
     * Creates new form Form1
     */
    public Form1() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Form1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
