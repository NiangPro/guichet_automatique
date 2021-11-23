package classes;

import java.util.Date;
import javax.swing.JOptionPane;


public class Hypothecaire extends Compte
{
    
    public Hypothecaire(Float solde, int etat, String code_client,String type, Date dateOuverture) {
        super(solde, etat, code_client, type, dateOuverture);
    }
    
    
    public void depot(float montant){
        if(this.db.depot(this.numCompte, (this.solde +montant))){
            Operation opt = new Operation(this.getNumCompte(), montant, "Depot", new Date(), 0);
            this.db.addOperation(opt);
            this.db.addHistorique(new Historique(new Date(), opt.getNumOpt()));
            JOptionPane.showMessageDialog(null, "Dépôt avec succès");
        }else{
            JOptionPane.showMessageDialog(null, "Erreur de Dépôt");
        }
    }
}
