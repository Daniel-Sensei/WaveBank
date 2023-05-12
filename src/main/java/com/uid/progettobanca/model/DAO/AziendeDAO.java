package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Azienda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AziendeDAO {

    private Connection conn;

    public AziendeDAO(Connection conn) {
        this.conn = conn;
    }


    // Inserimenti:

    //inserimento tramite oggetto azienda
    public void insert(Azienda azienda) throws SQLException {
        String query = "INSERT INTO aziende(p_iva, nome, indirizzo, iban) VALUES(?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, azienda.getPiva());
            stmt.setString(2, azienda.getNome());
            stmt.setString(3, azienda.getIndirizzo());
            stmt.setString(4, azienda.getIban());
            stmt.executeUpdate();
        }
    }


    // getting:

    //selezione di un'azienda tramite partita iva
    public Azienda selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM aziende WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Azienda(
                            rs.getString("p_iva"),
                            rs.getString("nome"),
                            rs.getString("indirizzo"),
                            rs.getString("iban"));
                } else {
                    return null;
                }
            }
        }
    }

    //selezione di tutte le aziende
    public List<Azienda> selectAll() throws SQLException {
        String query = "SELECT * FROM aziende";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Azienda> aziende = new ArrayList<>();
            while (rs.next()) {
                aziende.add(new Azienda(
                        rs.getString("p_iva"),
                        rs.getString("nome"),
                        rs.getString("indirizzo"),
                        rs.getString("iban")
                ));
            }
            rs.close();
            return aziende;
        }
    }


    // Aggiornamenti:

    //aggiornamento tramite oggetto azienda
    public void update(Azienda azienda) throws SQLException {
        String query = "UPDATE aziende SET nome=?, indirizzo=?, iban=? WHERE p_iva=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, azienda.getNome());
            stmt.setString(2, azienda.getIndirizzo());
            stmt.setString(3, azienda.getIban());
            stmt.setString(4, azienda.getPiva());
            stmt.executeUpdate();
        }
    }

    // update generico per evitare duplicazione di codice
    public void updateField(String fieldName, String fieldValue, String idName, String idValue) throws SQLException {
        String query = "UPDATE aziende SET " + fieldName + " = ? WHERE " + idName + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fieldValue);
            stmt.setString(2, idValue);
            stmt.executeUpdate();
        }
    }

    public void updateNome(String piva, String nome) throws SQLException {
        updateField("nome", nome, "p_iva", piva);
    }

    public void updateIndirizzo(String piva, String indirizzo) throws SQLException {
        updateField("indirizzo", indirizzo, "p_iva", piva);
    }

    public void updateIban(String piva, String iban) throws SQLException {
        updateField("iban", iban, "p_iva", piva);
    }


    // Rimozione:

    public void delete(String piva) throws SQLException {
        String query = "DELETE FROM aziende WHERE p_iva = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, piva);
            stmt.executeUpdate();
        }
    }
}
