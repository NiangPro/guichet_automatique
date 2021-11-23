package classes;

import dataBase.Connexion;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Operation {
    private Float montant;
    private String typeOpt;
    private Date date;    
    private String numCpt;
    private Connexion db;
    private Compte compte;
    private int nbreBillet;
    private String numOpt;

    public String getNumOpt() {
        return numOpt;
    }

    public final void setNumOpt(String numOpt) {
        this.numOpt = numOpt;
    }

    public int getNbreBillet() {
        return nbreBillet;
    }

    public void setNbreBillet(int nbreBillet) {
        this.nbreBillet = nbreBillet;
    }

    
    public String getTypeOpt() {
        return typeOpt;
    }

    public void setTypeOpt(String typeOpt) {
        this.typeOpt = typeOpt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Connexion getDb() {
        return db;
    }

    public void setDb(Connexion db) {
        this.db = db;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String getNumCpt() {
        return numCpt;
    }

    public void setNumCpt(String numCpt) {
        this.numCpt = numCpt;
    }


    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }


    public Operation(String numCpt, Float montant, String typeOpt, Date date, int nbreBillet) {
        this.montant = montant;
        this.typeOpt = typeOpt;
        this.date = date;
        this.numCpt = numCpt;
        this.nbreBillet = nbreBillet;
        
        try {
            this.db = new Connexion();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ResultSet rs = this.db.getCompteByNumCpt(this.numCpt);
        
        try {
            if(rs.next()){
                this.compte = new Compte(rs.getFloat("solde"), rs.getInt("etat"), rs.getString("code_client"), rs.getString("type"), rs.getDate("dateOuverture"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int nbre = this.db.nbreOperations()+1;
        this.setNumOpt("Opt"+nbre);
    }
            
    public static enum TypeOp{
        Depot, Retrait, Transfert,PaiementFacture
    }
}
