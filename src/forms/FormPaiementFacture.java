package forms;

import classes.Cheque;
import classes.Client;
import classes.Config;
import dataBase.Connexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



public class FormPaiementFacture extends javax.swing.JFrame {
    private Client client;
    private Connexion db;
    /**
     * Creates new form FormBlockAccess
     * @param client
     */
    public FormPaiementFacture(Client client) throws ClassNotFoundException {
        initComponents();
        this.client = client;
        new Config(this);
        this.db = new Connexion();
        getComptes();
    }

    private FormPaiementFacture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        private void getComptes(){
            ResultSet rs = db.getCompteByType("Compte Cheque",client.getCode());
            try {
                while(rs.next()){
                    numCpt.addItem(rs.getString("numCpt"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(FormTransfert.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        numCpt = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        montant = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Agency FB", 0, 18)); // NOI18N
        jLabel1.setText("N° Compte:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 30));

        jButton1.setBackground(new java.awt.Color(0, 153, 153));
        jButton1.setFont(new java.awt.Font("Agency FB", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 70, 30));

        jPanel1.add(numCpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 220, 30));

        jLabel3.setFont(new java.awt.Font("Agency FB", 0, 18)); // NOI18N
        jLabel3.setText("Montant :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 60, 30));
        jPanel1.add(montant, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 220, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        var depot_numCpt = numCpt.getSelectedItem();
        var mt = montant.getText();

        
        String regex = "^[0-9]+$";
        
        if(depot_numCpt== null){
            
            JOptionPane.showMessageDialog(null, "Vous n'avait pas de compte cheque pour le moment", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
        }else{
            if(mt.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Veuillez donner un montant", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(mt.matches(regex)){
                            ResultSet rs = db.getCompteByNumCpt(depot_numCpt.toString());
                            try {
                                if(rs.next()){
                                    Cheque cpt = new Cheque(rs.getFloat("solde"), rs.getInt("etat"),rs.getString("code_client"),rs.getString("type"), rs.getDate("dateOuverture"));
                                        cpt.setNumCompte(rs.getString("numCpt"));
                                        cpt.paiementFacture(Float.parseFloat(mt), this);
                                }else{
                                    JOptionPane.showMessageDialog(null, "Erreur de paiement", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(FormPaiementFacture.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }else{
                        JOptionPane.showMessageDialog(null, "Veuillez entrer un montant positif", "Paiement Facture", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(FormPaiementFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPaiementFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPaiementFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPaiementFacture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPaiementFacture().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField montant;
    private javax.swing.JComboBox<String> numCpt;
    // End of variables declaration//GEN-END:variables
}