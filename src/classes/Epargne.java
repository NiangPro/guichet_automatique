package classes;

import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;


public class Epargne extends Hypothecaire{
    
    public Epargne(Float solde, int etat, String code_client, String type, Date dateOuverture) {
        super(solde, etat, code_client, type, dateOuverture);
    }
    
    public void retrait(float montant, JFrame frame){
        boolean valid = true;
        float exactMontant = 0;
        
        ResultSet res = db.guichet();
        try {
            if(res.next()){
                if(res.getFloat("balance") >= montant){
                    if(montant > this.solde){
                        ResultSet rs = this.db.getCompteByType("Marge Credit", this.getClient().getCode());
                        try {
                            if(rs.next()){
                                int result = JOptionPane.showConfirmDialog(null, "Votre montant est insuffisant, Voulez-vous utiliser la marge de credit", "Retrait", JOptionPane.ERROR_MESSAGE);
                                if(result == 0){
                                    if(this.db.depot(rs.getString("numCpt"), (montant - this.solde+rs.getFloat("solde")))){
                                        valid = true;
                                    }else{
                                        valid = false;
                                    }
                                }else if(result == 1){
                                    frame.dispose();
                                }
                            }else{
                                valid = false;
                                JOptionPane.showMessageDialog(null, "Votre compte est insuffisant", "Retrait", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Epargne.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        exactMontant = this.solde -montant;
                    }
                    
                    
                    if(valid){
                            if(this.db.retrait(this.numCompte, exactMontant)){
                                this.db.ajoutArgent(res.getFloat("balance")-montant, res.getInt("nbreBillet")-((int)montant/10));
                                Operation opt = new Operation(this.getNumCompte(), montant, "Retrait", new Date(), (int)montant/10);
                                this.db.addOperation(opt);
                                this.db.addHistorique(new Historique(new Date(), opt.getNumOpt()));
                                JOptionPane.showMessageDialog(null, "Retrait avec succès");
                            }else{
                                JOptionPane.showMessageDialog(null, "Erreur de Retrait", "Retrait", JOptionPane.ERROR_MESSAGE);
                            }
                            frame.dispose();
                        }
                }else{
                    valid = false;
                    JOptionPane.showMessageDialog(null, "Le guichet n'a pas suffisamment d'argent", "Retrait", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Erreur de retrait", "Retrait", JOptionPane.ERROR_MESSAGE);
                frame.dispose();
            }
       
        
        } catch (SQLException ex) {
            Logger.getLogger(Epargne.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
