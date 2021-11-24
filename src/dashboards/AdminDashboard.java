/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package dashboards;

import classes.Admin;
import classes.Client;
import classes.Config;
import classes.Historique;
import com.lampfallDev.Main;
import dataBase.Connexion;
import forms.AddClient;
import forms.AddCompte;
import forms.AddMoney;
import forms.FormBlockAccess;
import forms.FormPrelevement;
import forms.FormUnlockAccess;
import java.awt.Color;
import java.awt.print.PrinterException;
import java.util.logging.Level;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author HP
 */
public class AdminDashboard extends javax.swing.JFrame {
    /**
     * Creates new form AdminDashboard
     */
    private Admin admin;
    public Connexion db;
    
    public AdminDashboard(Client client) throws ClassNotFoundException {
        initComponents();
        this.admin = new Admin(client.getCode(), client.getNip(), client.getNom(), client.getPrenom(), client.getTel(), client.getCourriel(), client.getSexe(),client.getEstAdmin());
        new Config(this);
        DateFormat df = new SimpleDateFormat("dd/MM/YYYY à H:mm");
        loginDate.setText(df.format(new Date()));
        this.db = new Connexion();
        chargerTableauClient();
        chargerTableauCompte();
        chargerTableTransaction();
        chargerTableHistorique();
        guichet();
        
        int nclient = this.db.nbreClients();        
        int ncpt = this.db.nbreComptes();
        int nopt = this.db.nbreOperations();

        user_name.setText(admin.getCode());
        nbreClients.setText(""+nclient+"");

        nbreComptes.setText(""+ncpt+"");
        nbreOperations.setText(nopt+"");

        
        subHeaderTitle.setText(sDashboard.getText());
        subHeaderTitle.setIcon(sDashboard.getIcon());
        body.setSelectedIndex(0);
    }

    private AdminDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setSelectedMenuItem(JLabel label){
        label.setOpaque(true);
        label.setBackground(new Color(0,102,204));
        subHeaderTitle.setText(label.getText());
        subHeaderTitle.setIcon(label.getIcon());
    }
    
    private void resetDesign(JLabel label){
        label.setOpaque(false);
        label.setBackground(new Color(240,240,240));
    }
    
    private void guichet(){
        ResultSet rs = db.guichet();
        try {
            if(rs.next()){
                balance.setText(rs.getFloat("balance")+" $");
                nbreBillet.setText(rs.getInt("nbreBillet")+"");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void chargerTableauCompte(){
        tabCompte.removeAll();
        ResultSet rs = null;
        DefaultTableModel dtm = new DefaultTableModel();
        String colonnes[] = {"N°Compte","Code Client", "Type", "Solde", "Etat", "Date Ouverture"};
        
        for (int i = 0; i<colonnes.length;i++) {
            dtm.addColumn(colonnes[i]);
        }
        
        rs = db.comptes();
        
        try {
            while(rs.next()){
                Float solde = rs.getFloat(1);
                int etat = rs.getInt(2);
                String type = rs.getString(3);
                String code_client = rs.getString(4);
                String numCpte = rs.getString(6);

                
                Date dateOuverture = rs.getDate(5);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String newDate = sdf.format(dateOuverture);
                String newEtat = etat == 0 ? " Désactivé" : "Activé";
                
                String entree [] = {numCpte,code_client, type, solde+" $", newEtat, newDate};
                dtm.addRow(entree);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tabCompte.setModel(dtm);
    }
    
    private void chargerTableauClient(){
        tabClient.removeAll();
        ResultSet rs = null;
        DefaultTableModel dtm = new DefaultTableModel();
        String colonnes[] = {"code", "Prénom", "Nom", "Téléphone", "Courriel", "Sexe", "Etat"};
        
        for (String colonne : colonnes) {
            dtm.addColumn(colonne);
        }
        
        rs = db.clients();
        
        try {
            while(rs.next()){
                String code = rs.getString(1);
                String prenom = rs.getString(2);
                String nom = rs.getString(3);
                String tel = rs.getString(4);
                String courriel = rs.getString(5);
                String sexe = rs.getString(6);
                String etat = rs.getInt(9)== 0 ? "Désactivé" : "Activé" ;

                
                String entree [] = {code, prenom, nom, tel, courriel, sexe,etat };
                dtm.addRow(entree);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tabClient.setModel(dtm);
    }
    
    private void chargerTableTransaction(){
        tabTransaction.removeAll();
        
        DefaultTableModel dtm = new DefaultTableModel();
         DateFormat df = new  SimpleDateFormat("dd/MM/YYYY");
        
        String entete []={"N°Opération","N° Compte", "Type", "Montant", "Date"}; 
        for (String entete1 : entete) {
            dtm.addColumn(entete1);
        }
        
        ResultSet rs = db.operations();
        
        try {
            while(rs.next()){
                String entrees [] = {rs.getString("numOpt"), rs.getString("numCpt"), rs.getString("typeOpt"), rs.getFloat("montant")+" $", df.format(rs.getDate("date"))};
                dtm.addRow(entrees);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        tabTransaction.setModel(dtm);
    }
    
    private void chargerTableHistorique(){
        tabHistoriques.removeAll();
        this.r_numCpt.setText("");
        
        DefaultTableModel dtm = new DefaultTableModel();
         DateFormat df = new  SimpleDateFormat("dd/MM/YYYY");
        
        String entete []={"N°Opération","N° Compte", "Type", "Montant","Prénom","Nom", "Date"}; 
        for (String entete1 : entete) {
            dtm.addColumn(entete1);
        }
        
        ResultSet rs = db.historiques();
        
        try {
            while(rs.next()){
                Historique h = new Historique(rs.getDate("date"), rs.getString("numOpt"));
                String entrees [] = {rs.getString("numOpt"), h.getOperation().getNumCpt(), h.getOperation().getTypeOpt(), h.getOperation().getMontant()+" $", h.getOperation().getCompte().getClient().getPrenom(), h.getOperation().getCompte().getClient().getNom(), df.format(h.getOperation().getDate())};
                dtm.addRow(entrees);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        tabHistoriques.setModel(dtm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sHistory = new javax.swing.JLabel();
        sDashboard = new javax.swing.JLabel();
        sClients = new javax.swing.JLabel();
        sComptes = new javax.swing.JLabel();
        sTransaction = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        user_name = new javax.swing.JLabel();
        loginDate = new javax.swing.JLabel();
        subHeader = new javax.swing.JPanel();
        subHeaderTitle = new javax.swing.JLabel();
        body = new javax.swing.JTabbedPane();
        bDashboard = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        nbreClients = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nbreComptes = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        nbreOperations = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        nbreBillet = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        bclient = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        scrollTabClient = new javax.swing.JScrollPane();
        tabClient = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        bCompte = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabCompte = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        bTransaction = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabTransaction = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        history = new javax.swing.JPanel();
        historyTitle = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabHistoriques = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        r_numCpt = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sidebar.setBackground(new java.awt.Color(0, 51, 153));
        sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lg.png"))); // NOI18N
        sidebar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 7, 210, 60));

        sHistory.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sHistory.setForeground(new java.awt.Color(255, 255, 255));
        sHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/histo.png"))); // NOI18N
        sHistory.setText("Historiques");
        sHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sHistoryMouseClicked(evt);
            }
        });
        sidebar.add(sHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 220, 50));

        sDashboard.setBackground(new java.awt.Color(0, 102, 204));
        sDashboard.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sDashboard.setForeground(new java.awt.Color(255, 255, 255));
        sDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dashboard.png"))); // NOI18N
        sDashboard.setText("Tableau de bord");
        sDashboard.setOpaque(true);
        sDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sDashboardMouseClicked(evt);
            }
        });
        sidebar.add(sDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 220, 50));

        sClients.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sClients.setForeground(new java.awt.Color(255, 255, 255));
        sClients.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clients.png"))); // NOI18N
        sClients.setText("Clients");
        sClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sClientsMouseClicked(evt);
            }
        });
        sidebar.add(sClients, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 220, 50));

        sComptes.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sComptes.setForeground(new java.awt.Color(255, 255, 255));
        sComptes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/compte.png"))); // NOI18N
        sComptes.setText("Comptes");
        sComptes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sComptesMouseClicked(evt);
            }
        });
        sidebar.add(sComptes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 220, 50));

        sTransaction.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        sTransaction.setForeground(new java.awt.Color(255, 255, 255));
        sTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/transaction.png"))); // NOI18N
        sTransaction.setText("Transactions");
        sTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sTransactionMouseClicked(evt);
            }
        });
        sidebar.add(sTransaction, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 220, 50));

        jPanel1.add(sidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 530));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        user_name.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        user_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user_name.png"))); // NOI18N

        loginDate.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        loginDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginDate.setText("jLabel9");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(user_name, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addComponent(loginDate, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(user_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loginDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 750, 50));

        subHeader.setBackground(new java.awt.Color(0, 51, 153));
        subHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subHeaderTitle.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        subHeaderTitle.setForeground(new java.awt.Color(255, 255, 255));
        subHeaderTitle.setText("jLabel3");
        subHeader.add(subHeaderTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 4, 400, 40));

        jPanel1.add(subHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 750, 50));

        bDashboard.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clients.png"))); // NOI18N
        jLabel3.setText("Clients");

        nbreClients.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        nbreClients.setForeground(new java.awt.Color(255, 255, 255));
        nbreClients.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nbreClients.setText("10");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(nbreClients, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nbreClients, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 0));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/compte.png"))); // NOI18N
        jLabel5.setText("Comptes");

        nbreComptes.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        nbreComptes.setForeground(new java.awt.Color(255, 255, 255));
        nbreComptes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nbreComptes.setText("10");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(nbreComptes, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nbreComptes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(153, 0, 102));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/transaction.png"))); // NOI18N
        jLabel7.setText("Transactions");

        nbreOperations.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        nbreOperations.setForeground(new java.awt.Color(255, 255, 255));
        nbreOperations.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nbreOperations.setText("10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(nbreOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nbreOperations, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Système");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/balance.png"))); // NOI18N
        jLabel9.setText("Balance :");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/billet.png"))); // NOI18N
        jLabel10.setText("Nombre de Billet:");

        nbreBillet.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nbreBillet.setForeground(new java.awt.Color(255, 255, 255));
        nbreBillet.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        balance.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        balance.setForeground(new java.awt.Color(255, 255, 255));
        balance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nbreBillet, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nbreBillet, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jButton8.setBackground(new java.awt.Color(51, 204, 0));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        jButton8.setText("Ajouter Agent Papier");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 153, 204));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/refresh.png"))); // NOI18N
        jButton9.setText("Actualiser");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bDashboardLayout = new javax.swing.GroupLayout(bDashboard);
        bDashboard.setLayout(bDashboardLayout);
        bDashboardLayout.setHorizontalGroup(
            bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bDashboardLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bDashboardLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bDashboardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(bDashboardLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton9)
                            .addComponent(jButton8))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        bDashboardLayout.setVerticalGroup(
            bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bDashboardLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(bDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bDashboardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(bDashboardLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        body.addTab("tab1", bDashboard);

        bclient.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(51, 153, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        jButton1.setText("Ajouter");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lock.png"))); // NOI18N
        jButton2.setText("Bloquer");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setText("Liste des clients");

        tabClient.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        tabClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollTabClient.setViewportView(tabClient);

        jButton4.setBackground(new java.awt.Color(153, 153, 0));
        jButton4.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/refresh.png"))); // NOI18N
        jButton4.setText("Acutaliser");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(153, 153, 0));
        jButton7.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/unlock.png"))); // NOI18N
        jButton7.setText("Debloquer");
        jButton7.setActionCommand("");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bclientLayout = new javax.swing.GroupLayout(bclient);
        bclient.setLayout(bclientLayout);
        bclientLayout.setHorizontalGroup(
            bclientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bclientLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(bclientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(bclientLayout.createSequentialGroup()
                        .addGroup(bclientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(scrollTabClient, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bclientLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(218, 218, 218)))
                        .addGap(525, 525, 525))
                    .addGroup(bclientLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(60, 60, 60)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(59, 59, 59)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(504, 504, 504))))
        );
        bclientLayout.setVerticalGroup(
            bclientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bclientLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(bclientLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(scrollTabClient, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
        );

        body.addTab("tab2", bclient);

        bCompte.setBackground(new java.awt.Color(255, 255, 255));

        tabCompte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabCompte);

        jButton5.setBackground(new java.awt.Color(0, 102, 102));
        jButton5.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Liste des comptes clients");

        jButton6.setBackground(new java.awt.Color(153, 0, 204));
        jButton6.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/refresh.png"))); // NOI18N
        jButton6.setText("Actualiser");
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 51, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Charger Intérêt Marges Credits");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(102, 102, 0));
        jButton10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Payer Intérêt Comptes cheques");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(51, 0, 51));
        jButton11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Prelever des montants hypothecaires");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bCompteLayout = new javax.swing.GroupLayout(bCompte);
        bCompte.setLayout(bCompteLayout);
        bCompteLayout.setHorizontalGroup(
            bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bCompteLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bCompteLayout.createSequentialGroup()
                        .addGroup(bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bCompteLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jButton3))
                            .addGroup(bCompteLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jButton5)))
                        .addGroup(bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bCompteLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jButton11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bCompteLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6)
                                .addGap(46, 46, 46)
                                .addComponent(jButton10)
                                .addContainerGap(67, Short.MAX_VALUE))))))
            .addGroup(bCompteLayout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jLabel6)
                .addContainerGap(229, Short.MAX_VALUE))
        );
        bCompteLayout.setVerticalGroup(
            bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bCompteLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bCompteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        body.addTab("tab3", bCompte);

        bTransaction.setBackground(new java.awt.Color(255, 255, 255));
        bTransaction.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tabTransaction);

        bTransaction.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 660, 330));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel11.setText("Liste des dernières Transactions");
        bTransaction.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 480, 30));

        body.addTab("tab4", bTransaction);

        history.setBackground(new java.awt.Color(255, 255, 255));

        historyTitle.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        historyTitle.setText("Liste des dernières Historiques");

        tabHistoriques.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tabHistoriques);

        jLabel13.setFont(new java.awt.Font("Agency FB", 0, 18)); // NOI18N
        jLabel13.setText("Recherche Par N° Compte :");

        jButton12.setBackground(new java.awt.Color(153, 153, 0));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/print.png"))); // NOI18N
        jButton12.setText("Iimprimer");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(0, 102, 51));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Rechercher");
        jButton13.setBorder(null);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(0, 153, 255));
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/refresh.png"))); // NOI18N
        jButton14.setText("Actualiser");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historyLayout = new javax.swing.GroupLayout(history);
        history.setLayout(historyLayout);
        historyLayout.setHorizontalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(historyTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14))
                    .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(historyLayout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(r_numCpt, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton12))
                        .addGroup(historyLayout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        historyLayout.setVerticalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(r_numCpt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(historyTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addContainerGap())
        );

        body.addTab("tab5", history);

        jPanel1.add(body, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 750, 460));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        try {
            // TODO add your handling code here:
            int response = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr ?", "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
              
            if(response == 0){
                new Main().setVisible(true);
                this.dispose();
            }
                
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_jLabel2MouseClicked

    private void sDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sDashboardMouseClicked
        // TODO add your handling code here:
        body.setSelectedIndex(0);
        setSelectedMenuItem(sDashboard);
        resetDesign(sClients);
        resetDesign(sComptes);
        resetDesign(sTransaction);
        resetDesign(sHistory);      
    }//GEN-LAST:event_sDashboardMouseClicked

    private void sClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sClientsMouseClicked
        // TODO add your handling code here:
        body.setSelectedIndex(1);
        setSelectedMenuItem(sClients);
        resetDesign(sDashboard);
        resetDesign(sComptes);
        resetDesign(sTransaction);
        resetDesign(sHistory);       
    }//GEN-LAST:event_sClientsMouseClicked

    private void sComptesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sComptesMouseClicked
        // TODO add your handling code here:
        body.setSelectedIndex(2);
        setSelectedMenuItem(sComptes);
        resetDesign(sDashboard);
        resetDesign(sClients);
        resetDesign(sTransaction);
        resetDesign(sHistory);      
    }//GEN-LAST:event_sComptesMouseClicked

    private void sTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sTransactionMouseClicked
        // TODO add your handling code here:
                body.setSelectedIndex(3);
                setSelectedMenuItem(sTransaction);
        resetDesign(sDashboard);
        resetDesign(sComptes);
        resetDesign(sClients);
        resetDesign(sHistory);      
    }//GEN-LAST:event_sTransactionMouseClicked

    private void sHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sHistoryMouseClicked
        // TODO add your handling code here:
                body.setSelectedIndex(4);
                setSelectedMenuItem(sHistory);
        resetDesign(sDashboard);
        resetDesign(sComptes);
        resetDesign(sTransaction);
        resetDesign(sClients);      
    }//GEN-LAST:event_sHistoryMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         JFrame form;
        try {
            form = new AddClient();
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargerTableauClient();
  
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        chargerTableauClient();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        JFrame form;
        try {
            form = new AddCompte();
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        chargerTableauCompte();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        chargerTableauCompte();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        JFrame form = new FormUnlockAccess();
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JFrame form = new FormBlockAccess();
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        JFrame form = new AddMoney();
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        guichet();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        JFrame form;
        try {
            form = new FormPrelevement(admin);
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            form.setResizable(false);
            form.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr?");
        if(result == 0){
            this.admin.payerInteretComptesCheques();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr?");
        if(result == 0){
            this.admin.chargerInteretMargesCredits();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        
        MessageFormat entete = new MessageFormat(historyTitle.getText());
        MessageFormat pied = new MessageFormat("Page {0, number, integer}-Guichet Automatique");
        
        try {
            tabHistoriques.print(JTable.PrintMode.FIT_WIDTH, entete, pied);
            JOptionPane.showMessageDialog(null, "Impression avec succes");

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Erreur :"+ex.getMessage(),"Impression", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        var numCpt = r_numCpt.getText();
        
        if(numCpt.isEmpty()){
            JOptionPane.showMessageDialog(null, "Veillez remplir le champ N°Compte","Recherche", JOptionPane.ERROR_MESSAGE);            
        }else{
            ResultSet rs = db.getCompteByNumCpt(numCpt);
            try {
                if(rs.next()){
                    tabHistoriques.removeAll();
        
                    DefaultTableModel dtm = new DefaultTableModel();
                     DateFormat df = new  SimpleDateFormat("dd/MM/YYYY");

                    String entete []={"N°Opération","N° Compte", "Type", "Montant","Prénom","Nom", "Date"}; 
                    for (String entete1 : entete) {
                        dtm.addColumn(entete1);
                    }

                    ResultSet res = db.historiquesCompte(numCpt);

                    try {
                        while(res.next()){
                            Historique h = new Historique(res.getDate("date"), res.getString("numOpt"));
                            String entrees [] = {res.getString("numOpt"), h.getOperation().getNumCpt(), h.getOperation().getTypeOpt(), h.getOperation().getMontant()+" $", h.getOperation().getCompte().getClient().getPrenom(), h.getOperation().getCompte().getClient().getNom(), df.format(h.getOperation().getDate())};
                            dtm.addRow(entrees);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ClientDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    tabHistoriques.setModel(dtm);
                }else{
                   JOptionPane.showMessageDialog(null, "N°Compte inexistant","Recherche", JOptionPane.ERROR_MESSAGE);                               
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        chargerTableHistorique();
    }//GEN-LAST:event_jButton14ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bCompte;
    private javax.swing.JPanel bDashboard;
    private javax.swing.JPanel bTransaction;
    private javax.swing.JLabel balance;
    private javax.swing.JPanel bclient;
    private javax.swing.JTabbedPane body;
    private javax.swing.JPanel header;
    private javax.swing.JPanel history;
    private javax.swing.JLabel historyTitle;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel loginDate;
    private javax.swing.JLabel nbreBillet;
    private javax.swing.JLabel nbreClients;
    private javax.swing.JLabel nbreComptes;
    private javax.swing.JLabel nbreOperations;
    private javax.swing.JTextField r_numCpt;
    private javax.swing.JLabel sClients;
    private javax.swing.JLabel sComptes;
    private javax.swing.JLabel sDashboard;
    private javax.swing.JLabel sHistory;
    private javax.swing.JLabel sTransaction;
    private javax.swing.JScrollPane scrollTabClient;
    private javax.swing.JPanel sidebar;
    private javax.swing.JPanel subHeader;
    private javax.swing.JLabel subHeaderTitle;
    private javax.swing.JTable tabClient;
    private javax.swing.JTable tabCompte;
    private javax.swing.JTable tabHistoriques;
    private javax.swing.JTable tabTransaction;
    private javax.swing.JLabel user_name;
    // End of variables declaration//GEN-END:variables
}
