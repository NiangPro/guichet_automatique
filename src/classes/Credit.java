package classes;

import java.util.Date;


public class Credit extends Compte {
    
    private Float marge;
    private int existeDeja;
    
    public Credit(Float solde, int etat, String code_client,String type, Date dateOuverture) {
        super(solde, etat, code_client, type, dateOuverture);
    }
    
}
