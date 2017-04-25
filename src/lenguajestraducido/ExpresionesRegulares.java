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
public class ExpresionesRegulares {
    ArrayList<Elementos> nodos;
    ArrayList<Integer> hojasfollow;
    public List<Follow> follow;
    Elementos izquierdo;
    Elementos derecho;
    int hoja;
    Form1 form;
    String textoImprimir;

    public String expreg1 = "[(](\\w|\\W)+[)]\\s*\\n*\\t*\\r*[(](\\w|\\W)+[)]";
    public String expreg5 = "^[(](\\w|\\W)+[)](['*']|['+']|['?'])$";
    public String expreg4 = "(^[(](\\w|\\W)+[)]$)(\\w|\\W)*";
    public String expreg3 = "(\\S)+(\\s|\r|\n|\t)*[|](\\s|\\r|\\n|\\t)*(\\S)+";
    //public String expreg2 = @"(\S)+(\s|\r|\n|\t)+(\S)+";
    public String expreg2 = "(^[A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[?]|[;]|[:]|[!]|[@]|[*]|[(]|[)][+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-]$)+(\\s|\\r|\\n|\\t)+(^[A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[?]|[;]|[:]|[!]|[@]|[*]|[(]|[)][+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-]$)+";
    public String expreg6 = "((\\S)+\\s*\\n*\\t*\\r*([\\*]|[\\+]|[\\?]))+";
    public String expreg7 = "(\\'(\\w|\\W)+\\')+";
    public String expreg8 = "(\\\"(\\w|\\W)+\\\")+";
    public String expreg9 = "(^[(](\\w|\\W)+[)]([*]|[+]|[?])?(\\s|\\r|\\n|\\t)+[(](\\w|\\W)+[)]([*]|[+]|[?])?)+";
    public String expreg10 = "([A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[?]|[;]|[:]|[!]|[@]|[*]|[|]|[\\][+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-])*[(](\\S)*[)]([A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[?]|[;]|[:]|[!]|[@]|[*]|[|]|[\\][+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-])*";
    //public String expreg11 = @"([A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[\?]|[;]|[:]|[!]|[@]|[\*]|[\|]|[\][\+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-])*[(](\S)*[)]([\*]|[\+]|[\?])*(\s|\r|\n|\t)*([A-z]|[a-z]|[0-9]|[.]|[/]|[<]|[>]|[\?]|[;]|[:]|[!]|[@]|[\*]|[\|]|[\][\+]|[#]|[$]|[%]|[^]|[&]|[{]|[}]|[=]|[-])*";
    public String expreg11 = "(\\S)*[(](\\S)+[)]([*]|[+]|[?])?(\\S)*";
    public String expreg12 = "(\\w|\\W)*(\\s|\\r|\\n|\\t)*[)]([\\*]|[\\+]|[\\?])?(\\s|\\r|\\n|\\t)*[\\|][(]?(\\s|\\r|\\n|\\t)*(w\\|\\W)*";
    public String cuantificadores = "([\\*]|[\\+]|[\\?])";
    public String espacios = "(\\s|\\r|\\n|\\t)+";

    //Expresiones regulares char y ID
    private String expregChar = "^(\\w|\\W)$";//CHAR
    private String expregID = "(\\w|\\W)*";//ID


    public ExpresionesRegulares()
    {
        nodos = new ArrayList<Elementos>();
        hojasfollow = new ArrayList<Integer>();
        follow = new ArrayList<Follow>();
        izquierdo = new Elementos();
        derecho = new Elementos();
        hoja = 0;
        textoImprimir = "";
    }

    public Elementos revisarExpReg1(String expregular, List<String> listConj, bool error, List<String> listnum, int token)
    {
        String[] NumerosToken = listnum.ToArray();
        Elementos nodoHoja = new Elementos();
        Elementos c1 = new Elementos();
        Elementos c2 = new Elementos();

        c1 = revisarExpReg(expregular, listConj, error);

        if (!c1.errorFound)
        {
            error = false;
            c2.errorFound = false;
            hoja++;
            c2.texto = "#";
            c2.first.add(hoja);
            c2.last.add(hoja);
            hojasfollow.add(hoja);
            Follow ff = new Follow();
            ff.agregarPosicion(hoja);
            follow.add(ff);
            String first = pasarAString(c2.first);
            String last = pasarAString(c2.last);
            textoImprimir = textoImprimir + "\r\n" + "# First: " + first + " Last: " + last;

            calcularFollow('.', c1, c2);
            nodoHoja.calcularFirstLast('.', c1, c2);
            nodoHoja.texto = ".";

            first = pasarAString(nodoHoja.first).toString();
            last = pasarAString(nodoHoja.last);
            textoImprimir = textoImprimir + "\r\n" + ".----------------------------> First: " + first + " Last: " + last;
            textoImprimir = "\r\n" + "TOKEN " + NumerosToken[token] + "\r\n" + textoImprimir;
            textoImprimir = "\r\n" + textoImprimir + "\r\n\r\n" + "FOLLOW" + "\r\n" + followAString(follow);
        }
        else
        {
            nodoHoja = c1;
        }
        return nodoHoja;
    }

    public Elementos revisarExpReg(String expregular, List<String> listConj, Boolean error)
    {
        Elementos nodoHoja = null;
        Elementos c1 = new Elementos();
        Elementos c2 = new Elementos();

        Boolean errorFound = false;
        if (!expregular.isEmpty() && error != true)
        {
            if (!expregular.equalsIgnoreCase("*") && !expregular.equalsIgnoreCase("+") && !expregular.equalsIgnoreCase("?"))
            {
                if (expregular.matches(expreg1))// concatenar parentesis
                {
                    nodoHoja = new Elementos();
                    int posicion = expregular.indexOf(')');
                    String expRegularA = expregular.substring(0, posicion + 1).trim();
                    String expRegularB = expregular.substring(posicion + 1).trim();
                    c1 = revisarExpReg(expRegularA, listConj, errorFound);
                    c2 = revisarExpReg(expRegularB, listConj, errorFound);
                    calcularFollow('.', c1, c2);
                    nodoHoja.calcularFirstLast('.', c1, c2);
                    nodoHoja.texto = ".";

                    String first = pasarAString(nodoHoja.first);
                    String last = pasarAString(nodoHoja.last);
                    textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;

                }
                else
                {
                    if (expregular.matches(expreg9)) //no sé que es esto
                    {
                        int posicion = expregular.indexOf(')');
                        String temporal = expregular.substring(posicion + 1, 1);
                        if (temporal.equalsIgnoreCase("*") || temporal.equalsIgnoreCase("+") || temporal.equalsIgnoreCase("?"))
                        {
                            char cuantificador = temporal.charAt(0); // Convert.ToChar(temporal); # Converts the first character to char
                            String expRegularA = expregular.substring(0, posicion + 2).trim();
                            String expRegularB = expregular.substring(posicion + 2).trim();
                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                            calcularFollow('.', c1, c2);
                            nodoHoja.calcularFirstLast('.', c1, c2);
                            nodoHoja.texto = ".";

                            String first = pasarAString(nodoHoja.first).toString();
                            String last = pasarAString(nodoHoja.last);
                            textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;

                        }
                        else
                        {
                            String expRegularA = expregular.substring(0, posicion + 1).trim();
                            String expRegularB = expregular.substring(posicion + 1).trim();
                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                            calcularFollow('.', c1, c2);
                            nodoHoja.calcularFirstLast('.', c1, c2);
                            nodoHoja.texto = ".";

                            String first = pasarAString(nodoHoja.first);
                            String last = pasarAString(nodoHoja.last);
                            textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                        }


                    }
                    else
                    {
                        Boolean hayOs = buscarO(expregular);

                        if (expregular.matches(expreg5) && (!hayOs))//(    )cuant
                        {
                            nodoHoja = new Elementos();
                            int posicion = expregular.indexOf('(');
                            int posicion2 = expregular.length() - 1;
                            char cuantificador = (expregular.substring(posicion2)).charAt(0);
                            String expRegularA = expregular.substring(0, posicion2).trim();
                            String expRegularB = expregular.substring(posicion2 + 1).trim();
                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                            c1.calcularFirstLast(cuantificador, c1, null);
                            c1.texto = "" + cuantificador;
                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                            if (c2 == null)
                            {
                                calcularFollow(cuantificador, c1, c2);
                                nodoHoja = c1;
                                String first = pasarAString(nodoHoja.first);
                                String last = pasarAString(nodoHoja.last);
                                textoImprimir = textoImprimir + "\r\n" + "" + cuantificador + "----------------------------> First: " + first + " Last: " + last;
                            }
                            else
                            {
                                calcularFollow('.', c1, c2);
                                nodoHoja.calcularFirstLast('.', c1, c2);
                                nodoHoja.texto = ".";
                                String first = pasarAString(nodoHoja.first);
                                String last = pasarAString(nodoHoja.last);
                                textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                            }
                        }
                        else
                        {
                            //int cantParCerrar = expregular.Count(f => f == '(');
                            //int cantParAbrir = expregular.Count(f => f == ')');
                            int resultado = 0;
                            if (Regex.IsMatch(expregular, expreg4))
                            {
                                String temporal = expregular.Substring(1, expregular.Length - 1);
                                int posicionCerrar = temporal.IndexOf(')');
                                int posicionAbrir = temporal.IndexOf('(');
                                resultado = posicionAbrir - posicionCerrar;
                            }


                            if (Regex.IsMatch(expregular, expreg4) && (resultado < 0))// (    )
                            {
                                nodoHoja = new Elementos();
                                int posicion = expregular.IndexOf('(');
                                int posicion2 = expregular.Length - 1;
                                String expRegularA = expregular.Substring(posicion + 1, posicion2 - 1).TrimStart().TrimEnd();
                                String expRegularB = expregular.Substring(posicion2 + 1).TrimStart().TrimEnd();
                                c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                if (c2 == null)
                                {
                                    nodoHoja = c1;
                                }
                                else
                                {
                                    calcularFollow('.', c1, c2);
                                    nodoHoja.calcularFirstLast('.', c1, c2);
                                    nodoHoja.texto = ".";
                                    String first = pasarAString(nodoHoja.first);
                                    String last = pasarAString(nodoHoja.last);
                                    textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                                }
                            }

                            else
                            {
                                int parantesisAbrir = expregular.IndexOf('(');
                                int parentesisCerrar = expregular.LastIndexOf(')');
                                int saber = parentesisCerrar - parantesisAbrir + 1;//Si da la longitud de la cadena

                                if (Regex.IsMatch(expregular, expreg11) && (saber != expregular.Length))
                                {
                                    nodoHoja = new Elementos();
                                    int posicion = expregular.IndexOf('(');
                                    int posicion2 = expregular.LastIndexOf(')');
                                    char cuantificador = Convert.ToChar(expregular.Substring(posicion2 + 1, 1));
                                    if (cuantificador == '*' || cuantificador == '+' || cuantificador == '?')
                                    {
                                        if (posicion == 0)
                                        {
                                            String expRegularA = expregular.Substring(posicion, posicion2 + 2);
                                            String expRegularB = expregular.Substring(posicion2 + 2);
                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                            calcularFollow('.', c1, c2);
                                            nodoHoja.calcularFirstLast('.', c1, c2);
                                            nodoHoja.texto = ".";
                                        }
                                        else
                                        {
                                            String expRegularA = expregular.Substring(0, posicion);
                                            String expRegularB = expregular.Substring(posicion);
                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                            calcularFollow('.', c1, c2);
                                            nodoHoja.calcularFirstLast('.', c1, c2);
                                            nodoHoja.texto = ".";
                                        }
                                        String first = pasarAString(nodoHoja.first);
                                        String last = pasarAString(nodoHoja.last);
                                        textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                                    }
                                    else
                                    {
                                        if (posicion == 0)
                                        {
                                            String expRegularA = expregular.Substring(posicion, posicion2 + 1);
                                            String expRegularB = expregular.Substring(posicion2 + 1);
                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                            calcularFollow('.', c1, c2);
                                            nodoHoja.calcularFirstLast('.', c1, c2);
                                            nodoHoja.texto = ".";
                                        }
                                        else
                                        {
                                            String expRegularA = expregular.Substring(0, posicion);
                                            String expRegularB = expregular.Substring(posicion);
                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                            calcularFollow('.', c1, c2);
                                            nodoHoja.calcularFirstLast('.', c1, c2);
                                            nodoHoja.texto = ".";
                                        }
                                        String first = pasarAString(nodoHoja.first);
                                        String last = pasarAString(nodoHoja.last);
                                        textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                                    }

                                }
                                else
                                {
                                    if (Regex.IsMatch(expregular, expreg12))
                                    {
                                        expregular = remplazarOs(expregular);
                                    }

                                    if (Regex.IsMatch(expregular, expreg12))
                                    {
                                        nodoHoja = new Elementos();
                                        int posicion = expregular.IndexOf('|');
                                        expregular = expregular.Replace("¬", "|");
                                        String expRegularA = expregular.Substring(0, posicion).TrimStart().TrimEnd();
                                        String expRegularB = expregular.Substring(posicion + 1).TrimStart().TrimEnd();
                                        c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                        c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                        nodoHoja.calcularFirstLast('|', c1, c2);
                                        nodoHoja.texto = "|";
                                        String first = pasarAString(nodoHoja.first);
                                        String last = pasarAString(nodoHoja.last);
                                        textoImprimir = textoImprimir + "\r\n" + "|  ----------------------------> First: " + first + " Last: " + last;
                                    }
                                    else
                                    {
                                        if (Regex.IsMatch(expregular, expreg3))//   |
                                        {
                                            nodoHoja = new Elementos();
                                            int posicion = expregular.IndexOf('|');
                                            String expRegularA = expregular.Substring(0, posicion).TrimStart().TrimEnd();
                                            String expRegularB = expregular.Substring(posicion + 1).TrimStart().TrimEnd();
                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                            nodoHoja.calcularFirstLast('|', c1, c2);
                                            nodoHoja.texto = "|";
                                            String first = pasarAString(nodoHoja.first);
                                            String last = pasarAString(nodoHoja.last);
                                            textoImprimir = textoImprimir + "\r\n" + "|  ----------------------------> First: " + first + " Last: " + last;

                                        }

                                        else
                                        {
                                            if (Regex.IsMatch(expregular, expreg2))//concatenar espacio
                                            {
                                                nodoHoja = new Elementos();
                                                var match = Regex.Match(expregular, espacios);
                                                int pos = -1;
                                                if (match.Success)
                                                {

                                                    pos = match.Index;
                                                }
                                                String expRegularA = expregular.Substring(0, pos + 1).TrimStart().TrimEnd();
                                                String expRegularB = expregular.Substring(pos + 1).TrimStart().TrimEnd();
                                                c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                                c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                                calcularFollow('.', c1, c2);
                                                nodoHoja.calcularFirstLast('.', c1, c2);
                                                nodoHoja.texto = ".";
                                                String first = pasarAString(nodoHoja.first);
                                                String last = pasarAString(nodoHoja.last);
                                                textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                                            }

                                            else
                                            {
                                                if (Regex.IsMatch(expregular, expreg6))//   ...cuant
                                                {
                                                    nodoHoja = new Elementos();
                                                    int posicion = -1;
                                                    var match = Regex.Match(expregular, cuantificadores);
                                                    if (match.Success)
                                                    {
                                                        posicion = match.Index;
                                                    }
                                                    String expRegularA = expregular.Substring(0, posicion).TrimStart().TrimEnd();
                                                    String expRegularB = expregular.Substring(posicion + 1).TrimStart().TrimEnd();
                                                    char cuantificador = Convert.ToChar(expregular.Substring(posicion, 1));
                                                    c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                                    c1.calcularFirstLast(cuantificador, c1, null);
                                                    c1.texto = cuantificador.ToString();
                                                    c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                                    if (c2 == null)
                                                    {
                                                        calcularFollow(cuantificador, c1, c2);
                                                        nodoHoja = c1;
                                                        String first = pasarAString(nodoHoja.first);
                                                        String last = pasarAString(nodoHoja.last);
                                                        textoImprimir = textoImprimir + "\r\n" + "" + cuantificador + "----------------------------> First: " + first + " Last: " + last;
                                                    }
                                                    else
                                                    {
                                                        calcularFollow('.', c1, c2);
                                                        nodoHoja.calcularFirstLast('.', c1, c2);
                                                        nodoHoja.texto = ".";
                                                        String first = pasarAString(nodoHoja.first);
                                                        String last = pasarAString(nodoHoja.last);
                                                        textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;

                                                    }
                                                }
                                                else
                                                {

                                                    if (Regex.IsMatch(expregular, expreg7))//   ' ... '
                                                    {
                                                        nodoHoja = new Elementos();
                                                        String expRegularA = expregular.Substring(1, 1).TrimStart().TrimEnd();
                                                        String expRegularB = expregular.Substring(3).TrimStart().TrimEnd();
                                                        c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                                        c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                                        if (c2 == null)
                                                        {
                                                            nodoHoja = c1;
                                                        }
                                                        else
                                                        {
                                                            calcularFollow('.', c1, c2);
                                                            nodoHoja.calcularFirstLast('.', c1, c2);
                                                            nodoHoja.texto = ".";
                                                            String first = pasarAString(nodoHoja.first);
                                                            String last = pasarAString(nodoHoja.last);
                                                            textoImprimir = textoImprimir + "\r\n" + ".  ----------------------------> First: " + first + " Last: " + last;
                                                        }

                                                    }
                                                    else
                                                    {
                                                        if (Regex.IsMatch(expregular, expreg8))//  "..."
                                                        {
                                                            nodoHoja = new Elementos();
                                                            String expRegularA = expregular.Substring(1, 1).TrimStart().TrimEnd();
                                                            String expRegularB = expregular.Substring(3).TrimStart().TrimEnd();
                                                            c1 = revisarExpReg(expRegularA, listConj, errorFound);
                                                            c2 = revisarExpReg(expRegularB, listConj, errorFound);
                                                            if (c2 == null)
                                                            {
                                                                nodoHoja = c1;
                                                            }
                                                            else
                                                            {
                                                                calcularFollow('.', c1, c2);
                                                                nodoHoja.calcularFirstLast('.', c1, c2);
                                                                nodoHoja.texto = ".";
                                                                String first = pasarAString(nodoHoja.first);
                                                                String last = pasarAString(nodoHoja.last);
                                                                textoImprimir = textoImprimir + "\r\n" + ".   ----------------------------> First: " + first + " Last: " + last;
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if (Regex.IsMatch(expregular, expregChar)) //char
                                                            {
                                                                nodoHoja = new Elementos();
                                                                error = false;
                                                                nodoHoja.errorFound = false;
                                                                //Creo el nodo
                                                                hoja++;
                                                                nodoHoja.texto = expregular;
                                                                nodoHoja.first.Add(hoja);
                                                                nodoHoja.last.Add(hoja);
                                                                hojasfollow.Add(hoja);
                                                                Follow ff = new Follow();
                                                                ff.agregarPosicion(hoja);
                                                                follow.Add(ff);
                                                                String first = pasarAString(nodoHoja.first);
                                                                String last = pasarAString(nodoHoja.last);
                                                                textoImprimir = textoImprimir + "\r\n" + "" + expregular + "" + " ---------------------------->First: " + first + " Last: " + last;
                                                            }
                                                            else
                                                            {
                                                                //hoja
                                                                if (Regex.IsMatch(expregular, expregID)) //ID
                                                                {
                                                                    nodoHoja = new Elementos();
                                                                    expregular = expregular.ToLower();
                                                                    if (listConj.Contains(expregular))
                                                                    {
                                                                        error = false;
                                                                        nodoHoja.errorFound = false;
                                                                        //Creo el nodo
                                                                        hoja++;
                                                                        nodoHoja.texto = expregular;
                                                                        nodoHoja.first.Add(hoja);
                                                                        nodoHoja.last.Add(hoja);
                                                                        hojasfollow.Add(hoja);
                                                                        Follow ff = new Follow();
                                                                        ff.agregarPosicion(hoja);
                                                                        follow.Add(ff);
                                                                        String first = pasarAString(nodoHoja.first);
                                                                        String last = pasarAString(nodoHoja.last);
                                                                        textoImprimir = textoImprimir + "\r\n" + "" + expregular + "" + "----------------------------> First: " + first + " Last: " + last;

                                                                    }
                                                                    else
                                                                    {
                                                                        nodoHoja = new Elementos();
                                                                        nodoHoja.errorFound = true;
                                                                        error = true;
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    nodoHoja = new Elementos();
                                                                    nodoHoja.errorFound = true;
                                                                    error = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                nodoHoja = new Elementos();
                nodoHoja.errorFound = true;
                error = true;
            }
        }
        return nodoHoja;
    }

    public String pasarAString(List<int> lista)
    {
        StringBuilder builder = new StringBuilder();
        string result = "0";
        if (lista != null)
        {
            foreach (int list in lista)
            {
                builder.Append(list).Append(", ");
            }
            result = builder.ToString();
        }
        return result;
    }

    public void calcularFollow(char tipo, Elementos c1, Elementos c2)
    {
        int[] c1array = c1.last.ToArray();
        int[] c2array = new int[2];
        if (c2 != null)
        {
            c2array = c2.first.ToArray();
        }
        Follow[] followArray = follow.ToArray();
        int posicion = 0;

        if (tipo == '.')
        {
            for (int i = 0; i < c1array.Length; i++)
            {
                int columna = c1array[i];
                posicion = hojasfollow.IndexOf(columna);
                Follow ff = new Follow();
                List<int> hojas = new List<int>();
                if (followArray[posicion].elementos != null)
                {
                    hojas = followArray[posicion].elementos;
                }
                for (int j = 0; j < c2array.Length; j++)
                {
                    int hojaF = c2array[j];
                    if (!hojas.Contains(hojaF))
                    {
                        hojas.Add(hojaF);
                    }
                }
                ff.agregarPosicion(columna);
                ff.agregarElementos(hojas);
                followArray[posicion] = ff;
            }
        }
        else
        {
            c2array = c1.first.ToArray();
            for (int i = 0; i < c1array.Length; i++)
            {
                int columna = c1array[i];
                posicion = hojasfollow.IndexOf(columna);
                Follow ff = new Follow();
                List<int> hojas = new List<int>();
                if (followArray[posicion].elementos != null)
                {
                    hojas = followArray[posicion].elementos;
                }
                for (int j = 0; j < c2array.Length; j++)
                {
                    int hojaF = c2array[j];
                    if (!hojas.Contains(hojaF))
                    {
                        hojas.Add(hojaF);
                    }
                }
                ff.agregarPosicion(columna);
                ff.agregarElementos(hojas);
                followArray[posicion] = ff;
            }
        }
        follow = followArray.ToList<Follow>();
    }

    public String followAString(List<Follow> _follow)
    {
        Follow[] arreglo = _follow.ToArray();
        String textFollow = "";
        for (int i = 0; i < arreglo.Length; i++)
        {
            textFollow = textFollow + "\r\n" + arreglo[i].posicion.ToString() + " ---------------------------->" + pasarAString(arreglo[i].elemnetosAList());
        }
        return textFollow;
    }

    public String getTextImprimir()
    {
        return textoImprimir;
    }

    public bool buscarO(String cadena)
    {
        bool hayO = false;
        int posicion = cadena.LastIndexOf(@"[)]([\*]|[\+]|[\?])");
        char[] caracteres = cadena.ToCharArray();
        for (int i = posicion + 1; i < caracteres.Length; i++)
        {
            if (!caracteres[i].Equals('+') && !caracteres[i].Equals('*') && !caracteres[i].Equals('?') && !caracteres[i].Equals(' '))
            {
                if (caracteres[i].Equals('|'))
                {
                    hayO = true;
                    i = caracteres.Length;
                }
                else
                {
                    hayO = false;
                    i = caracteres.Length;
                }
            }


        }
        return hayO;
    }

    public String remplazarOs(String expresion)
    {
        String nuevaExpresion = "";
        String copia = expresion + " ";

        while (copia.Contains('(') && copia.Contains(')'))
        {
            int parentesisAbre = copia.IndexOf('(');
            int parentesisCierra = copia.IndexOf(')');

            if (parentesisAbre >= 0 && parentesisCierra > 0)
            {
                Console.WriteLine(copia.Length);
                int longitud = parentesisCierra - parentesisAbre;
                String temporal = copia.Substring(parentesisAbre, longitud + 1);
                if (temporal.Contains('|'))
                {
                    temporal = temporal.Replace("|", "¬");
                    //temporal = temporal + ")";
                }
                copia = copia.Substring(parentesisCierra + 1);
                nuevaExpresion += temporal;
                int parentesisAbre2 = copia.IndexOf('(');
                if (parentesisAbre2 > 0)
                {
                    temporal = copia.Substring(0, parentesisAbre2);
                    nuevaExpresion += temporal;
                }
                else
                {
                    nuevaExpresion = nuevaExpresion + copia;
                }
            }
        }
        return nuevaExpresion;
    }
    
}
