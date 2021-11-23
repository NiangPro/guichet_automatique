package classes;


public class Client {
    
    protected String nom;
    protected String prenom;
    protected String tel;
    protected String courriel;
    protected String sexe;
    protected String code;
    protected int nip;
    private int estAdmin;

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
    

    public Client(String code, int nip, String nom, String prenom, String tel, String courriel, String sexe, int estAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.courriel = courriel;
        this.sexe = sexe;
        this.code = code;
        this.nip = nip;
        this.estAdmin = estAdmin;
    }
    
    public static enum TypeSexe{
        M, F
    }
    
    public int getEstAdmin() {
        return estAdmin;
    }

    public void setEstAdmin(int estAdmin) {
        this.estAdmin = estAdmin;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    public int getNip() {
        return nip;
    }
    
    public void setNip(int nip) {
        this.nip = nip;
    }
 
    public String getNom() {
        return nom;
    }
    
public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }
    
 public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
 
    public String getTel() {
        return tel;
    }
    
public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCourriel() {
        return courriel;
    }
        
public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public void depot(float montant){
        
    }

}
