/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lenguajestraducido;

/**
 *
 * @author FalconJecm
 */
public class Gramatica {
    
        //TOKENS
        private String AnalizarTokens = "\\b(T|t)(O|o)(K|k)(E|e)(N|n)(S|s)";
        public Boolean Analizar_Tokens(String Tokens)
        {
            return Tokens.matches(AnalizarTokens);            
        }
        //TOKEN
        private String AnalizarToken = "\\b(T|t)(O|o)(K|k)(E|e)(N|n)";
        public Boolean Analizar_Token(String Token)
        {
            return Token.matches(AnalizarToken);
        }
        //DIGITOS 0-9        
        public Boolean Analizar_Digito(String Digito)
        {
            return Digito.matches("\\d");
        }
        //DIGITOS 1-9
        private String AnalizarDigito1_9 = "\\b(1|2|3|4|5|6|7|8|9)";
        public Boolean Analizar_Digito1_9(String Digito1_9)
        {
            return Digito1_9.matches(AnalizarDigito1_9);
        }

        //ESPACIO        
        public Boolean Analizar_Espacio(String Espacio)
        {
            return Espacio.matches("\\s");
        }
        //CHR
        private String AnalizarCHR = "\\b(C|c)(H|h)(R|r)";
        public Boolean Analizar_CHR(String CHR)
        {
            return CHR.matches(AnalizarCHR);
        }
        //ID
        private String AnalizarID = "\\b([A-Z]|[a-z]|_|\\d*)+(([A-Z]|[a-z]|_)|(\\d*))*(\\b)";
        public Boolean Analizar_ID(String ID)
        {
            return ID.matches(AnalizarID);
        }
        public Boolean Analizar_CS(String CS)
        {
            if (CS.contentEquals("+") || CS.contentEquals("*") || CS.contentEquals("?"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        //END del archivo
        private String Final = "(e|E)(n|N)(d|D)";
        public Boolean Analizar_End(String End)
        {
            return End.matches(Final);
        }
    
}
