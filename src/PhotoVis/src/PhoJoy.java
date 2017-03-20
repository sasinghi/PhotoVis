/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PhoJoy.java
 *
 * Created on 20 Mar, 2017, 1:29:07 AM
 */
package src;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

/**
 *
 * @author Preetama
 */
public class PhoJoy extends javax.swing.JFrame {

    /** Creates new form PhoJoy */
    public PhoJoy() {
        initComponents();
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        defaultPane = new javax.swing.JPanel();
        timelinePane = new javax.swing.JPanel();
        geoPane = new javax.swing.JPanel();
        geoPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        mosaicPane = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PhoJoy");
        setLocationByPlatform(true);
        setResizable(false);

        tabbedPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));
        tabbedPane.setToolTipText("");
        tabbedPane.setAutoscrolls(true);
        tabbedPane.setDoubleBuffered(true);
        tabbedPane.setMaximumSize(getMaximumSize());
        tabbedPane.setMinimumSize(getMinimumSize());
        tabbedPane.setPreferredSize(getPreferredSize());
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPaneMouseClicked(evt);
            }
        });

        defaultPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        javax.swing.GroupLayout defaultPaneLayout = new javax.swing.GroupLayout(defaultPane);
        defaultPane.setLayout(defaultPaneLayout);
        defaultPaneLayout.setHorizontalGroup(
            defaultPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1271, Short.MAX_VALUE)
        );
        defaultPaneLayout.setVerticalGroup(
            defaultPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Browse", defaultPane);

        timelinePane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true), 10));

        javax.swing.GroupLayout timelinePaneLayout = new javax.swing.GroupLayout(timelinePane);
        timelinePane.setLayout(timelinePaneLayout);
        timelinePaneLayout.setHorizontalGroup(
            timelinePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1253, Short.MAX_VALUE)
        );
        timelinePaneLayout.setVerticalGroup(
            timelinePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 579, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Timeline", timelinePane);

        geoPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        jLabel3.setText("Geo Tags come here");

        javax.swing.GroupLayout geoPanelLayout = new javax.swing.GroupLayout(geoPanel);
        geoPanel.setLayout(geoPanelLayout);
        geoPanelLayout.setHorizontalGroup(
            geoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geoPanelLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(jLabel3)
                .addContainerGap(1006, Short.MAX_VALUE))
        );
        geoPanelLayout.setVerticalGroup(
            geoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geoPanelLayout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jLabel3)
                .addContainerGap(481, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout geoPaneLayout = new javax.swing.GroupLayout(geoPane);
        geoPane.setLayout(geoPaneLayout);
        geoPaneLayout.setHorizontalGroup(
            geoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(geoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        geoPaneLayout.setVerticalGroup(
            geoPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(geoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPane.addTab("GeoTags", geoPane);

        mosaicPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        javax.swing.GroupLayout mosaicPaneLayout = new javax.swing.GroupLayout(mosaicPane);
        mosaicPane.setLayout(mosaicPaneLayout);
        mosaicPaneLayout.setHorizontalGroup(
            mosaicPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1271, Short.MAX_VALUE)
        );
        mosaicPaneLayout.setVerticalGroup(
            mosaicPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Mosaic", mosaicPane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1273, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Color grouping", jPanel1);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        jCheckBox1.setText("Face Recognition");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addContainerGap(1165, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1306)/2, (screenSize.height-729)/2, 1306, 729);
    }// </editor-fold>//GEN-END:initComponents

private void tabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneMouseClicked

}//GEN-LAST:event_tabbedPaneMouseClicked

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(PhoJoy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PhoJoy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PhoJoy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PhoJoy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PhoJoy().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel defaultPane;
    private javax.swing.JPanel geoPane;
    private javax.swing.JPanel geoPanel;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel mosaicPane;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel timelinePane;
    // End of variables declaration//GEN-END:variables
}
