package classes;

import dataBase.Connexion;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Admin extends Client{
    private Connexion db;
    
    public Admin(String code, int nip, String nom, String prenom, String tel, String courriel, String sexe, int estAdmin) {
        super(code, nip, nom, prenom, tel, courriel, sexe, estAdmin);
        
        try {
            this.db = new Connexion();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void payerInteretComptesCheques(){
        ResultSet rs = this.db.getCompteByTypeOnly("Compte Cheque");
        
        try {
            while(rs.next()){
                float montant = rs.getFloat("solde")/100;
                db.depot(rs.getString("numCpt"), montant+rs.getFloat("solde"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Opération avec succès");
    }
    
    public void chargerInteretMargesCredits(){
        ResultSet rs = this.db.getCompteByTypeOnly("Marge Credit");
        
        try {
            while(rs.next()){
                db.depot(rs.getString("numCpt"), 5+rs.getFloat("solde"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Opération avec succès");

    }
    
    public void preleverMontantsHypothecaires(float montant, JFrame frame){
        ResultSet rs = this.db.getCompteByTypeOnly("Compte Hypothecaire");
        
        try {
            while(rs.next()){
                db.retrait(rs.getString("numCpt"), rs.getFloat("solde")-montant);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JOptionPane.showMessageDialog(null, "Opération avec succès");
        frame.dispose();
    }
}
