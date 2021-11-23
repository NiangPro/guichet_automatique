package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Cheque extends Epargne {
    
    final float FRAIS = (float) 1.25;
    
    public Cheque(Float solde, int etat, String code_client,String type, Date dateOuverture) {
        super(solde, etat, code_client,type, dateOuverture);
    }
    
    public void transfert(String numCpt2, float montant, JFrame frame){
        if(montant > this.solde){
            JOptionPane.showMessageDialog(null, "Transfert refusé, Votre compte est insuffisant", "Transfert", JOptionPane.ERROR_MESSAGE);
        }else{
            ResultSet rs = db.getCompteByNumCpt(numCpt2);
            try {
                if(rs.next()){
                    if(rs.getString("type").equals("Marge Credit")){
                        db.depot(numCpt2, (rs.getFloat("solde") - montant));
                    }else{
                        db.depot(numCpt2, (rs.getFloat("solde")+montant));
                    }
                            if(this.db.retrait(this.numCompte, (this.getSolde()-montant))){
                                Operation opt = new Operation(this.getNumCompte(), montant, "Transfert", new Date(), 0);
                                this.db.addOperation(opt);
                                this.db.addHistorique(new Historique(new Date(), opt.getNumOpt()));
                                JOptionPane.showMessageDialog(null, "Transfert avec succès");
                            }else{
                                JOptionPane.showMessageDialog(null, "Erreur de Transfert", "Transfert", JOptionPane.ERROR_MESSAGE);
                            }
                       
                }else{
                    JOptionPane.showMessageDialog(null, "Erreur de Transfert", "Transfert", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Cheque.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
     public void paiementFacture(float montant, JFrame frame){
        if((montant+this.FRAIS) > this.solde){
            JOptionPane.showMessageDialog(null, "Paiement refusé, Votre compte est insuffisant", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
        }else{
            ResultSet rs = db.getCompteByNumCpt(this.getNumCompte());
            int result = JOptionPane.showConfirmDialog(null, "Le frais est de 1,25$");
            if(result == 0){
                if(this.db.retrait(this.numCompte, (this.getSolde()-(montant+this.FRAIS)))){
                    Operation opt = new Operation(this.getNumCompte(), montant+this.FRAIS, "Paiement Facture", new Date(), 0);
                    this.db.addOperation(opt);
                    this.db.addHistorique(new Historique(new Date(), opt.getNumOpt()));
                    JOptionPane.showMessageDialog(null, "Paiement de facture avec succès");
                }else{
                    JOptionPane.showMessageDialog(null, "Erreur de paiement", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        frame.dispose();
     }
    
}
