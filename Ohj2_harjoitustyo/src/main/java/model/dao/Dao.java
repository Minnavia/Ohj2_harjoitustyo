package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Kirja;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Kirjat0.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");  
    	System.out.println("Tomcat polku: " + path);
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//System.out.println("Polku on: " + path);
    	//path += "/webapps/"; //Tuotannossa. Laita kanta webapps-kansioon.
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Kirja> listaaKaikki(){
		ArrayList<Kirja> kirjat = new ArrayList<Kirja>();
		sql = "SELECT kirja_id, kirja_nimi, kirjailija_etunimi, kirjailija_sukunimi, genre, sidosasu FROM Kirjat";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui									
					while(rs.next()){
						Kirja kirja = new Kirja();
						kirja.setKirja_id(rs.getInt(1));
						kirja.setKirja_nimi(rs.getString(2));
						kirja.setKirjailija_etunimi(rs.getString(3));
						kirja.setKirjailija_sukunimi(rs.getString(4));
						kirja.setGenre(rs.getString(5));
						kirja.setSidosasu(rs.getString(6));
						kirjat.add(kirja);
						}					
				}				
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return kirjat;
	}
	
	public ArrayList<Kirja> listaaKaikki(String hakusana){
		ArrayList<Kirja> kirjat = new ArrayList<Kirja>();
		sql = "SELECT kirja_id, kirja_nimi, kirjailija_etunimi, kirjailija_sukunimi, genre, sidosasu FROM Kirjat WHERE kirja_nimi LIKE ? or kirjailija_etunimi LIKE ? or kirjailija_sukunimi LIKE ? or genre LIKE ? or sidosasu LIKE ?";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);  
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");   
				stmtPrep.setString(3, "%" + hakusana + "%");
				stmtPrep.setString(4, "%" + hakusana + "%");   
				stmtPrep.setString(5, "%" + hakusana + "%"); 
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui							
					while(rs.next()){
						Kirja kirja = new Kirja();
						kirja.setKirja_id(rs.getInt(1));
						kirja.setKirja_nimi(rs.getString(2));
						kirja.setKirjailija_etunimi(rs.getString(3));
						kirja.setKirjailija_sukunimi(rs.getString(4));
						kirja.setGenre(rs.getString(5));
						kirja.setSidosasu(rs.getString(6));
						kirjat.add(kirja);
					}						
				}
				con.close();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return kirjat;
	}
		
	public boolean lisaaKirja(Kirja kirja){
		boolean paluuArvo=true;
		sql="INSERT INTO Kirjat(kirja_nimi, kirjailija_etunimi, kirjailija_sukunimi, genre, sidosasu) VALUES(?,?,?,?,?)";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, kirja.getKirja_nimi());
			stmtPrep.setString(2, kirja.getKirjailija_etunimi());
			stmtPrep.setString(3, kirja.getKirjailija_sukunimi());
			stmtPrep.setString(4, kirja.getGenre());
			stmtPrep.setString(5, kirja.getSidosasu());
			stmtPrep.executeUpdate();
			//System.out.println("Uusin id on " + stmtPrep.getGeneratedKeys().getInt(1));
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	public boolean poistaKirja(int kirja_id){ //Oikeassa el�m�ss� tiedot ensisijaisesti merkit��n poistetuksi.
		boolean paluuArvo=true;
		sql="DELETE FROM Kirjat WHERE kirja_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, kirja_id);			
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}	
	
	public Kirja etsiKirja(int kirja_id){
		Kirja kirja = null;
		sql = "SELECT kirja_id, kirja_nimi, kirjailija_etunimi, kirjailija_sukunimi, genre, sidosasu FROM Kirjat WHERE kirja_id=?";       
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setInt(1, kirja_id);
        		rs = stmtPrep.executeQuery();  
        		if(rs.isBeforeFirst()){ //jos kysely tuotti dataa, eli rekno on k�yt�ss�
        			//rs.next();
        			kirja = new Kirja(rs.getInt("kirja_id"), rs.getString("kirja_nimi"), rs.getString("kirjailija_etunimi"), rs.getString("kirjailija_sukunimi"), rs.getString("genre"), rs.getString("sidosasu"));       			
				}	
        		con.close(); 
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return kirja;		
	}
	
	public boolean muutaKirja(Kirja kirja){
		boolean paluuArvo=true;
		sql="UPDATE kirjat SET kirja_nimi=?, kirjailija_etunimi=?, kirjailija_sukunimi=?, genre=?, sidosasu=? WHERE kirja_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, kirja.getKirja_nimi());
			stmtPrep.setString(2, kirja.getKirjailija_etunimi());
			stmtPrep.setString(3, kirja.getKirjailija_sukunimi());
			stmtPrep.setString(4, kirja.getGenre());
			stmtPrep.setString(5, kirja.getSidosasu());
			stmtPrep.setInt(6, kirja.getKirja_id());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (SQLException e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
}