package classes;

import dataBase.Connexion;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Historique {
    private Date date;
    private Operation operation;
    private String numOpt;
    private Connexion db;

    public Connexion getDb() {
        return db;
    }

    public void setDb(Connexion db) {
        this.db = db;
    }

    public String getNumOpt() {
        return numOpt;
    }

    public void setNumOpt(String numOpt) {
        this.numOpt = numOpt;
    }

    public Historique(Date date, String numOpt) {
        this.date = date;
        this.numOpt = numOpt;
        
        try {
            this.db = new Connexion();
            
            ResultSet rs = db.getOperationByNumOpt(this.numOpt);
            try {
                if(rs.next()){
                    this.operation = new Operation(rs.getString("numCpt"), rs.getFloat("montant") , rs.getString("typeOpt") , rs.getDate("date") , rs.getInt("nbreBillet"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
