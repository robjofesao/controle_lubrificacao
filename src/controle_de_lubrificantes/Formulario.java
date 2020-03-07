/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle_de_lubrificantes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author ENGEMAQ
 */
public class Formulario extends javax.swing.JFrame {
      
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String opcao; // auxilia na escolha do relatório

    /**
     * Creates new form Formulario
     */
    public Formulario() {
        initComponents();
         this.setIconImage(new ImageIcon(getClass().getResource("/controle_de_lubrificantes/g891.png")).getImage());
         popular_combo_num_equip();
         popular_combo_nome_func();
         popular_combo_lubrificantes();
    }
    
    public void requisitar_lubrificante(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
       float estoque, remocao, resultado;
                estoque = Float.parseFloat(label_ultimo_estoque.getText());
                remocao = Float.parseFloat(campo_quantidade.getText());
                resultado = estoque - remocao;
        
        
                try {
                ps = con.prepareStatement("UPDATE public.lubrificantes\n" +
"	SET quantidade_lub=?\n" +
"	WHERE nome_lub = ?;");
                ps.setFloat(1,resultado);
                ps.setString(2, combo_lubrificante.getSelectedItem().toString());
                ps.executeUpdate();
                
                
               
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                ConnectionFactory.closeConnection(con, ps);
            }
    }
    
    public void buscar_estoque_lub(){//buscar o estoque do lubrificante selecionado
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
           
        try {
            ps = con.prepareStatement("SELECT * FROM public.lubrificantes where nome_lub = ?;");
            ps.setString(1, combo_lubrificante.getSelectedItem().toString());
            rs = ps.executeQuery();
            
            while(rs.next()){
                label_ultimo_estoque.setText(String.valueOf(rs.getFloat(3)));
                
            }
            
           
            
            
        } catch (Exception e) {
            System.out.println("erro busca nome equi combo "+e);
        }finally{
            ConnectionFactory.closeConnection(con, ps, rs);
        }
        
    }
    
    public void popular_combo_lubrificantes(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List lista = new ArrayList();
        
        try {
            ps = con.prepareStatement("SELECT * FROM public.lubrificantes order by nome_lub;");
            rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getString(2));
            }
            DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(lista.toArray());
            combo_lubrificante.setModel(defaultComboBox);
            
            
        } catch (Exception e) {
            System.out.println("erro popula combo "+e);
        }finally{
            ConnectionFactory.closeConnection(con, ps, rs);
            
            
        }
        
    }
    
    private void popular_tabela_mov_equip() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement("SELECT *\n" +
"	FROM public.contorle_lubrificacao where num_equip = ? order by data_lub;");
            ps.setString(1, combo_num_equip.getSelectedItem().toString());
            rs = ps.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tabela_mov_equip.getModel();
            model.setNumRows(0);
            String data = null;
            while(rs.next()){
                data = sdf.format(rs.getDate(2)); //converte a data para o padrao dd/mm/yyyy
                model.addRow(new Object [] {
                       rs.getInt(1),
                       data,
                       rs.getString(4),
                       rs.getFloat(6)
                       
            
            });
                
                
            }
            
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println("erro popular tabela ");
            e.printStackTrace();
        }finally{
            ConnectionFactory.closeConnection(con, ps,rs);
        }
        
    }
    
    
    private void popular_tabela_mov_sistema() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = con.prepareStatement("SELECT *\n" +
"	FROM public.contorle_lubrificacao where num_equip = ? order by sistema;");
            ps.setString(1, combo_num_equip.getSelectedItem().toString());
            rs = ps.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) tabela_mov_sistema.getModel();
            model.setNumRows(0);
            String data = null;
            while(rs.next()){
                data = sdf.format(rs.getDate(2)); //converte a data para o padrao dd/mm/yyyy
                model.addRow(new Object [] {
                       rs.getInt(1),
                       rs.getString(4),
                       rs.getFloat(6),
                       data
             });
                
                
            }
            
            rs = ps.executeQuery();
        } catch (Exception e) {
            System.out.println("erro popular tabela ");
            e.printStackTrace();
        }finally{
            ConnectionFactory.closeConnection(con, ps,rs);
        }
        
    }
    
    
    public void popular_combo_num_equip(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List lista = new ArrayList();
        
        try {
            ps = con.prepareStatement("select * from public.equipamentos order by n_equipamento;");
            rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getString(1));
            }
            DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(lista.toArray());
            combo_num_equip.setModel(defaultComboBox);
            
            
        } catch (Exception e) {
            System.out.println("erro popula combo "+e);
        }finally{
            ConnectionFactory.closeConnection(con, ps, rs);
            
            
        }
        
    }
    
    public void popular_combo_nome_func(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List lista = new ArrayList();
        
        try {
            ps = con.prepareStatement("SELECT nome_empregado FROM public.\"Cadastro_geral\" order by nome_empregado;");
            rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(rs.getString(1));
            }
            DefaultComboBoxModel defaultComboBox = new DefaultComboBoxModel(lista.toArray());
            combo_nome_func.setModel(defaultComboBox);
            
            
        } catch (Exception e) {
            System.out.println("erro popula combo "+e);
        }finally{
            ConnectionFactory.closeConnection(con, ps, rs);
            
            
        }
        
    }
    
    public boolean verificar_campos(){
        
        if (campo_data.getDate() == null){
           return false;
        }else if(combo_num_equip.getSelectedItem().toString().equals("")){
            return false;
        }else if(combo_sistema.getSelectedItem().toString().equals("")){
            return false;
        }else if(combo_lubrificante.getSelectedItem().toString().equals("")){
            return false;
        }else if(campo_quantidade.getText().isEmpty()){
            return false;
        }else if(combo_nome_func.getSelectedItem().toString().equals("")){
            return false;
        }else{
            return true;
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

        jFrame1_seletor_data = new javax.swing.JFrame();
        campo_data_inicio = new com.toedter.calendar.JDateChooser();
        campo_data_fim = new com.toedter.calendar.JDateChooser();
        botao_confirmar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        campo_num_mov = new javax.swing.JTextField();
        botao_limpar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        campo_data = new com.toedter.calendar.JDateChooser();
        combo_num_equip = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        combo_sistema = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        combo_lubrificante = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        campo_quantidade = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        combo_nome_func = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        campo_observacoes = new javax.swing.JTextArea();
        jSeparator2 = new javax.swing.JSeparator();
        botao_buscar = new javax.swing.JButton();
        botao_salvar = new javax.swing.JButton();
        botao_alterar = new javax.swing.JButton();
        botao_excluir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela_mov_equip = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabela_mov_sistema = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        label_ultimo_estoque = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menu_rel_equip = new javax.swing.JMenuItem();
        menu_rel_sistema = new javax.swing.JMenuItem();
        rel_lub_geral = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jFrame1_seletor_data.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrame1_seletor_data.setTitle("ENGEMAQ - PERIODO DO RELATÓRIO");
        jFrame1_seletor_data.setIconImage(new ImageIcon(getClass().getResource("/controle_de_lubrificantes/g891.png")).getImage());
        jFrame1_seletor_data.setSize(new java.awt.Dimension(450, 250));
        jFrame1_seletor_data.getContentPane().setLayout(null);
        jFrame1_seletor_data.getContentPane().add(campo_data_inicio);
        campo_data_inicio.setBounds(150, 60, 190, 29);
        jFrame1_seletor_data.getContentPane().add(campo_data_fim);
        campo_data_fim.setBounds(150, 100, 190, 29);

        botao_confirmar.setText("CONFIRMAR");
        botao_confirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_confirmarActionPerformed(evt);
            }
        });
        jFrame1_seletor_data.getContentPane().add(botao_confirmar);
        botao_confirmar.setBounds(170, 150, 100, 32);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("DATA INICIO:");
        jFrame1_seletor_data.getContentPane().add(jLabel12);
        jLabel12.setBounds(50, 60, 80, 30);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("DATA FIM:");
        jFrame1_seletor_data.getContentPane().add(jLabel13);
        jLabel13.setBounds(50, 100, 80, 30);

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("SELECIONE O PERÍODO");
        jFrame1_seletor_data.getContentPane().add(jLabel14);
        jLabel14.setBounds(8, 16, 440, 20);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ENGEMAQ - CONTROLE DE LUBRIFICANTES");
        getContentPane().setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nº DO EQUIPAMENTO:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 80, 130, 25);
        getContentPane().add(campo_num_mov);
        campo_num_mov.setBounds(270, 10, 80, 25);

        botao_limpar.setText("LIMPAR");
        botao_limpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_limparActionPerformed(evt);
            }
        });
        getContentPane().add(botao_limpar);
        botao_limpar.setBounds(410, 310, 100, 30);
        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(10, 40, 580, 2);

        jLabel2.setText("Nº DO MOVIMENTO:");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(150, 10, 110, 25);
        getContentPane().add(campo_data);
        campo_data.setBounds(160, 50, 210, 25);

        combo_num_equip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combo_num_equip.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_num_equipItemStateChanged(evt);
            }
        });
        getContentPane().add(combo_num_equip);
        combo_num_equip.setBounds(160, 80, 210, 25);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("DATA:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 50, 130, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("SISTEMA:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 110, 130, 25);

        combo_sistema.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "MOTOR", "CÂMBIO / TRANSMISSÃO", "DIFERENCIAL DIANTEIRO", "DIFERENCIAL TRASEIRO", "SISTEMA HIDRÁULICO", "DIREÇÃO", "CUBO DIANTEIRO (DIREITO)", "CUBO DIANTEIRO (ESQUERDO)", "CUBO TRASEIRO (DIREITO)", "CUBO TRASEIRO (ESQUERDO)", "BOMBA DA CABINE", "FREIO", "EMBREAGEM", "CAIXA BOMBA", "ARREFECIMENTO", "CHASSI", "CORTADOR DE BASE", "CAIXA DO PICADOR", "CAIXA DE QUATRO FUROS" }));
        getContentPane().add(combo_sistema);
        combo_sistema.setBounds(160, 110, 210, 25);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("QUANTIDADE:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(20, 170, 130, 25);

        combo_lubrificante.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combo_lubrificanteItemStateChanged(evt);
            }
        });
        getContentPane().add(combo_lubrificante);
        combo_lubrificante.setBounds(160, 140, 210, 25);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("LUBRIFICANTE:");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 140, 130, 25);
        getContentPane().add(campo_quantidade);
        campo_quantidade.setBounds(160, 170, 210, 24);

        jLabel7.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MOVIMENTOS DO EQUIPAMENTO");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(10, 353, 580, 20);

        combo_nome_func.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(combo_nome_func);
        combo_nome_func.setBounds(160, 200, 320, 25);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("NOME FUNCIONÁRIO:");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(20, 200, 130, 25);

        campo_observacoes.setColumns(20);
        campo_observacoes.setRows(5);
        jScrollPane1.setViewportView(campo_observacoes);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(160, 230, 320, 70);
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(10, 350, 580, 2);

        botao_buscar.setText("BUSCAR");
        botao_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_buscarActionPerformed(evt);
            }
        });
        getContentPane().add(botao_buscar);
        botao_buscar.setBounds(360, 10, 90, 25);

        botao_salvar.setText("SALVAR");
        botao_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_salvarActionPerformed(evt);
            }
        });
        getContentPane().add(botao_salvar);
        botao_salvar.setBounds(80, 310, 100, 30);

        botao_alterar.setText("ALTERAR");
        botao_alterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_alterarActionPerformed(evt);
            }
        });
        getContentPane().add(botao_alterar);
        botao_alterar.setBounds(190, 310, 100, 30);

        botao_excluir.setText("EXCLUIR");
        botao_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botao_excluirActionPerformed(evt);
            }
        });
        getContentPane().add(botao_excluir);
        botao_excluir.setBounds(300, 310, 100, 30);

        tabela_mov_equip.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nº MOV.", "DATA", "SISTEMA", "QUANTIDADE"
            }
        ));
        jScrollPane2.setViewportView(tabela_mov_equip);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 380, 580, 90);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("OBSERVAÇÕES:");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(20, 230, 130, 25);

        jLabel10.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("MOVIMENTOS DO SISTEMA");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(10, 485, 580, 20);

        tabela_mov_sistema.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nº MOV", "SISTEMA", "QUANTIDADE", "DATA"
            }
        ));
        jScrollPane3.setViewportView(tabela_mov_sistema);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(10, 510, 580, 90);

        jLabel11.setText(" ESTOQUE ATUAL:");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(380, 140, 110, 25);

        label_ultimo_estoque.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        getContentPane().add(label_ultimo_estoque);
        label_ultimo_estoque.setBounds(500, 140, 60, 25);

        jMenu1.setText("FUNÇÕES");

        jMenuItem1.setText("ENTRADA DE ESTOQUE DE LUBRIFICANTE");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        menu_rel_equip.setText("RELATÓRIO POR EQUIPAMENTO");
        menu_rel_equip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_rel_equipActionPerformed(evt);
            }
        });
        jMenu1.add(menu_rel_equip);

        menu_rel_sistema.setText("RELATÓRIO POR SISTEMA");
        menu_rel_sistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_rel_sistemaActionPerformed(evt);
            }
        });
        jMenu1.add(menu_rel_sistema);

        rel_lub_geral.setText("RELATÓRIO POR LUBRIFICANTE");
        rel_lub_geral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rel_lub_geralActionPerformed(evt);
            }
        });
        jMenu1.add(rel_lub_geral);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("SOBRE");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(616, 669));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botao_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_salvarActionPerformed
        // TODO add your handling code here:
        if(campo_num_mov.getText().isEmpty()){
            if(verificar_campos()==true){
                System.out.println("salva");
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement("INSERT INTO public.contorle_lubrificacao\n" +
"(data_lub, num_equip, sistema, lubrificante, quantidade_lub, nome_func, observacao)\n" +
"VALUES(?, ?, ?, ?, ?, ?, ?);");
                    ps.setDate(1,new java.sql.Date(campo_data.getDate().getTime()));
                    ps.setString(2, combo_num_equip.getSelectedItem().toString());
                    ps.setString(3,combo_sistema.getSelectedItem().toString());
                    ps.setString(4,combo_lubrificante.getSelectedItem().toString());
                    ps.setFloat(5,Float.parseFloat(campo_quantidade.getText()));
                    ps.setString(6,combo_nome_func.getSelectedItem().toString());
                    ps.setString(7,campo_observacoes.getText());
                    
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
                } catch (Exception e) {
                }finally{
                    ConnectionFactory.closeConnection(con,ps);
                    //requisitar quantidade 
                    requisitar_lubrificante();
                }
            }else{
                JOptionPane.showMessageDialog(this,"Verifique os campos para salvar");
            }
        }else{
            JOptionPane.showMessageDialog(this,"Para salvar o campo Nº Mov. deve estar vazio");
        }
        
        
    }//GEN-LAST:event_botao_salvarActionPerformed

    private void botao_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_buscarActionPerformed
        // buscar cadastro
        if(campo_num_mov.getText().isEmpty()){//verifica se o campo está vazio
            JOptionPane.showMessageDialog(null, "Preencha o campo de busca");
        }else{
            System.out.println("busca");
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            try {
                ps = con.prepareStatement("SELECT * FROM public.contorle_lubrificacao WHERE num_movimento=?;");
                ps.setInt(1,Integer.parseInt(campo_num_mov.getText()));
                
                rs = ps.executeQuery();
                while(rs.next()){
                    campo_data.setDate(rs.getDate(2));
                    combo_num_equip.setSelectedItem(rs.getString(3));
                    combo_sistema.setSelectedItem(rs.getString(4));
                    combo_lubrificante.setSelectedItem(rs.getString(5));
                    campo_quantidade.setText(String.valueOf(rs.getFloat(6)));
                    combo_nome_func.setSelectedItem(rs.getString(7));
                    campo_observacoes.setText(rs.getString(8));
                    
                }
            } catch (Exception e) {
                
            } finally{
                
            }
             
        }
    }//GEN-LAST:event_botao_buscarActionPerformed

    private void botao_alterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_alterarActionPerformed
        // alteração de cadastro
        if(campo_num_mov.getText().isEmpty()){
            System.out.println("não salva");
        
        }else{
            System.out.println("salva");
            if(verificar_campos()==true){
                System.out.println("salva 2");
                
                Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement("UPDATE public.contorle_lubrificacao\n" +
"	SET data_lub=?, num_equip=?, sistema=?, lubrificante=?, quantidade_lub=?, nome_func=?, observacao=?\n" +
"	WHERE num_movimento=?;");
                   ps.setDate(1,new java.sql.Date(campo_data.getDate().getTime()));
                   ps.setString(2, combo_num_equip.getSelectedItem().toString());
                   ps.setString(3, combo_sistema.getSelectedItem().toString());
                   ps.setString(4, combo_lubrificante.getSelectedItem().toString());
                   ps.setFloat(5, Float.parseFloat(campo_quantidade.getText()));
                   ps.setString(6, combo_nome_func.getSelectedItem().toString());
                   ps.setString(7, campo_observacoes.getText());
                   ps.setInt(8, Integer.parseInt(campo_num_mov.getText()));
                   
                   ps.executeUpdate();
                   JOptionPane.showMessageDialog(this, "Alterado com sucesso!");
                    
                } catch (Exception e) {
                    System.out.println("erro alteração");
                }finally{
                    ConnectionFactory.closeConnection(con,ps);
                }
                
                
            }else{
                System.out.println("não salva 2");
            }
        }
    }//GEN-LAST:event_botao_alterarActionPerformed

    private void botao_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_excluirActionPerformed
        // excluir cadastro
        if(campo_num_mov.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Preencha o Nº do movimento para excluir");
        }else{
            System.out.println("exclui");
             Object[] options = { "Sim", "Não" }; 
            int i = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement ps = null;
            
           if (i == JOptionPane.YES_OPTION) {
                try {
                    ps = con.prepareStatement("DELETE FROM public.contorle_lubrificacao\n" +
"	WHERE num_movimento=?;");
                    ps.setInt(1, Integer.parseInt(campo_num_mov.getText()));
                    ps.executeUpdate();
                    
                    JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
                } catch (Exception e) {
                    System.out.println("erro na exclusao "+e);
                }finally{
                    ConnectionFactory.closeConnection(con, ps);
                }
                
            }else{
                System.out.println("nao exclui");
            }
            
            
        }
        
    }//GEN-LAST:event_botao_excluirActionPerformed

    private void botao_limparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_limparActionPerformed
        this.dispose();
        new Formulario().setVisible(true);
    }//GEN-LAST:event_botao_limparActionPerformed

    private void combo_num_equipItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_num_equipItemStateChanged
        // TODO add your handling code here:
        popular_tabela_mov_equip();
        popular_tabela_mov_sistema();
    }//GEN-LAST:event_combo_num_equipItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
       new Estoque_lubrificante().setVisible(true);
       
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void combo_lubrificanteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combo_lubrificanteItemStateChanged
        buscar_estoque_lub();
    }//GEN-LAST:event_combo_lubrificanteItemStateChanged

    private void menu_rel_equipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_rel_equipActionPerformed
        jFrame1_seletor_data.setVisible(true);
        jFrame1_seletor_data.setLocationRelativeTo(null);
        opcao = "rel_equip";
    }//GEN-LAST:event_menu_rel_equipActionPerformed

    private void botao_confirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botao_confirmarActionPerformed
        switch (opcao){
            case "rel_equip":
                // confirmar a data do periodo para impressao do relatório
                if (campo_data_inicio.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else if (campo_data_fim.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else {
                    
                    HashMap parametros = new HashMap<>();
                    parametros.put("DATA_INICIO", campo_data_inicio.getDate());
                    parametros.put("DATA_FIM", campo_data_fim.getDate());

                    String relatorioJasper;
                    relatorioJasper =  "C:\\Sistema Engemaq\\relatorios controle combustivel\\lub_equipamentos.jasper";
                    System.out.println(relatorioJasper);
                    Connection con = ConnectionFactory.getConnection();
                
                    /* Carrega o arquivo */
                    try{
                    JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametros, con);
                    
                    JasperExportManager.exportReportToPdfFile(impressoraJasper, "c:/Relatorio lub_equip.pdf");
                    Desktop.getDesktop().open(new File("c:/Relatorio lub_equip.pdf"));
                    
                    }catch(JRException e){
                    e.printStackTrace();
                    } catch (IOException ex) {
                        Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                    ConnectionFactory.closeConnection(con);
                    }

                      
                }
                opcao = null;
            break;
             case "rel_sistema":
                // confirmar a data do periodo para impressao do relatório
                if (campo_data_inicio.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else if (campo_data_fim.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else {
                    
                    HashMap parametros = new HashMap<>();
                    parametros.put("DATA_INICIO", campo_data_inicio.getDate());
                    parametros.put("DATA_FIM", campo_data_fim.getDate());

                    String relatorioJasper;
                    relatorioJasper =  "C:\\Sistema Engemaq\\relatorios controle combustivel\\sistema_lub.jasper";
                    System.out.println(relatorioJasper);
                    Connection con = ConnectionFactory.getConnection();
                
                    /* Carrega o arquivo */
                    try{
                    JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametros, con);
                    
                    JasperExportManager.exportReportToPdfFile(impressoraJasper, "c:/Relatorio sistema_lub.pdf");
                    Desktop.getDesktop().open(new File("c:/Relatorio sistema_lub.pdf"));
                    
                    }catch(JRException e){
                    e.printStackTrace();
                    } catch (IOException ex) {
                        Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                    ConnectionFactory.closeConnection(con);
                    }

                      
                }
                opcao = null;
            break;
            case "rel_lub_geral":
                // confirmar a data do periodo para impressao do relatório
                if (campo_data_inicio.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else if (campo_data_fim.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Verifique as datas");
                } else {
                    
                    HashMap parametros = new HashMap<>();
                    parametros.put("DATA_INICIO", campo_data_inicio.getDate());
                    parametros.put("DATA_FIM", campo_data_fim.getDate());

                    String relatorioJasper;
                    relatorioJasper =  "C:\\Sistema Engemaq\\relatorios controle combustivel\\rel_lub_geral.jasper";
                    System.out.println(relatorioJasper);
                    Connection con = ConnectionFactory.getConnection();
                
                    /* Carrega o arquivo */
                    try{
                    JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametros, con);
                    
                    JasperExportManager.exportReportToPdfFile(impressoraJasper, "c:/Relatorio rel_lub_geral.pdf");
                    Desktop.getDesktop().open(new File("c:/Relatorio rel_lub_geral.pdf"));
                    
                    }catch(JRException e){
                    e.printStackTrace();
                    } catch (IOException ex) {
                        Logger.getLogger(Formulario.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                    ConnectionFactory.closeConnection(con);
                    }

                      
                }
                opcao = null;
            break;
             default:
                JOptionPane.showMessageDialog(this, "Verifique o tipo de relatório");
        }
    }//GEN-LAST:event_botao_confirmarActionPerformed

    private void menu_rel_sistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_rel_sistemaActionPerformed
        jFrame1_seletor_data.setVisible(true);
        jFrame1_seletor_data.setLocationRelativeTo(null);
        opcao = "rel_sistema";
    }//GEN-LAST:event_menu_rel_sistemaActionPerformed

    private void rel_lub_geralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rel_lub_geralActionPerformed
        jFrame1_seletor_data.setVisible(true);
        jFrame1_seletor_data.setLocationRelativeTo(null);
        opcao = "rel_lub_geral";
    }//GEN-LAST:event_rel_lub_geralActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Formulario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botao_alterar;
    private javax.swing.JButton botao_buscar;
    private javax.swing.JButton botao_confirmar;
    private javax.swing.JButton botao_excluir;
    private javax.swing.JButton botao_limpar;
    private javax.swing.JButton botao_salvar;
    private com.toedter.calendar.JDateChooser campo_data;
    private com.toedter.calendar.JDateChooser campo_data_fim;
    private com.toedter.calendar.JDateChooser campo_data_inicio;
    private javax.swing.JTextField campo_num_mov;
    private javax.swing.JTextArea campo_observacoes;
    private javax.swing.JTextField campo_quantidade;
    private javax.swing.JComboBox<String> combo_lubrificante;
    private javax.swing.JComboBox<String> combo_nome_func;
    private javax.swing.JComboBox<String> combo_num_equip;
    private javax.swing.JComboBox<String> combo_sistema;
    private javax.swing.JFrame jFrame1_seletor_data;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_ultimo_estoque;
    private javax.swing.JMenuItem menu_rel_equip;
    private javax.swing.JMenuItem menu_rel_sistema;
    private javax.swing.JMenuItem rel_lub_geral;
    private javax.swing.JTable tabela_mov_equip;
    private javax.swing.JTable tabela_mov_sistema;
    // End of variables declaration//GEN-END:variables
}
