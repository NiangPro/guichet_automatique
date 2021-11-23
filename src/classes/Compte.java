package classes;

import dataBase.Connexion;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Compte {
    protected Float solde;
    protected int etat;   
    protected Date dateOuverture;
    protected String code_client;    
    protected String type;
    protected String numCompte;    
    protected Client client;


    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    protected Connexion db;

    public Connexion getDb() {
        return db;
    }

    public void setDb(Connexion db) {
        this.db = db;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }


    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getCode_client() {
        return code_client;
    }

    public void setCode_client(String code_client) {
        this.code_client = code_client;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }
    
    private void generateNumCompte(){
        if(this.type.equals("Compte Credit")){
            this.numCompte = "CCR00"+(1+this.db.nbreComptesByType(this.type));
        }else if(this.type.equals("Compte Hypothecaire")){
            this.numCompte = "CHT00"+(1+this.db.nbreComptesByType(this.type));
        }else if(this.type.equals("Marge Credit")){
            this.numCompte = "CMC00"+(1+this.db.nbreComptesByType(this.type));
        }else{
            this.numCompte = "CCH00"+(1+this.db.nbreComptesByType(this.type));
        }
    }
    

    /**
     *
     * @param solde
     * @param etat
     * @param code_client
     * @param type
     * @param dateOuverture
     */
    public Compte(Float solde, int etat, String code_client,String type, Date dateOuverture) {
        this.solde = solde;
        this.etat = etat;
        this.type = type;
        this.dateOuverture = dateOuverture;
        this.code_client = code_client;
        
        try {
            this.db = new Connexion();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.generateNumCompte();
        
        ResultSet rs = this.db.getClientByCode(this.code_client);
        
        try {
            if(rs.next()){
                this.client = new Client(rs.getString("code"), rs.getInt("nip"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tel"), rs.getString("courriel"), rs.getString("sexe"), rs.getInt("estAdmin"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public Float getSolde() {
        return solde;
    }

    public void setSolde(Float solde) {
        this.solde = solde;
    }

}
