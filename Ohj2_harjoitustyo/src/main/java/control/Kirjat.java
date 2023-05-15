package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import control.JsonStrToObj;
import model.Kirja;
import model.dao.Dao;

/**
 * Servlet implementation class Kirjat
 */
@WebServlet("/Kirjat/*")
public class Kirjat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Kirjat() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Kirjat.doGet()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /haeyksi/5			
		System.out.println("polku: "+pathInfo);		
		String strJSON="";
		ArrayList<Kirja> kirjat;
		Dao dao = new Dao();
		if(pathInfo==null) { //Haetaan kaikki kirjat 
			kirjat = dao.listaaKaikki();
			System.out.println("Kirjat" + kirjat);
			strJSON = new JSONObject().put("Kirjat", kirjat).toString();	
		}else if(pathInfo.indexOf("haeyksi")!=-1) {		//polussa on sana "haeyksi", eli haetaan yhden asiakkaan tiedot
			int kirja_id = Integer.parseInt(pathInfo.replace("/haeyksi/", "")); //poistetaan polusta "/haeyksi/", j�ljelle j�� id		
			Kirja kirja = dao.etsiKirja(kirja_id);
			if(kirja==null){ //Jos asiakasta ei l�ytynyt, niin palautetaan tyhj� objekti
				strJSON = "{}";
			}else{	
				JSONObject JSON = new JSONObject();
				JSON.put("kirja_id", kirja.getKirja_id());
				JSON.put("kirja_nimi", kirja.getKirja_nimi());
				JSON.put("kirjailija_etunimi", kirja.getKirjailija_etunimi());
				JSON.put("kirjailija_sukunimi", kirja.getKirjailija_sukunimi());
				JSON.put("genre", kirja.getGenre());	
				JSON.put("sidosasu", kirja.getSidosasu());	
				strJSON = JSON.toString();
			}			
		}else{ //Haetaan hakusanan mukaiset kirjat
			String hakusana = pathInfo.replace("/", "");	
			kirjat = dao.listaaKaikki(hakusana);			
			strJSON = new JSONObject().put("Kirjat", kirjat).toString();				
		}	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Kirjat.doPost()");
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		Kirja kirja = new Kirja();		
		kirja.setKirja_nimi(jsonObj.getString("kirja_nimi"));
		kirja.setKirjailija_etunimi(jsonObj.getString("kirjailija_etunimi"));
		kirja.setKirjailija_sukunimi(jsonObj.getString("kirjailija_sukunimi"));
		kirja.setGenre(jsonObj.getString("genre"));
		kirja.setSidosasu(jsonObj.getString("sidosasu"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.lisaaKirja(kirja)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan lis��minen onnistui {"response":1}
		}else{
			out.println("{\"response\":0}");  //Asiakkaan lis��minen ep�onnistui {"response":0}
		}		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Kirjat.doPut()");		
		JSONObject jsonObj = new JsonStrToObj().convert(request); //Muutetaan kutsun mukana tuleva json-string json-objektiksi			
		Kirja kirja = new Kirja();	
		kirja.setKirja_id(Integer.parseInt(jsonObj.getString("kirja_id"))); //kirja_id on String, joka pit�� muuttaa int
		kirja.setKirja_nimi(jsonObj.getString("kirja_nimi"));
		kirja.setKirjailija_etunimi(jsonObj.getString("kirjailija_etunimi"));
		kirja.setKirjailija_sukunimi(jsonObj.getString("kirjailija_sukunimi"));
		kirja.setGenre(jsonObj.getString("genre"));
		kirja.setSidosasu(jsonObj.getString("sidosasu"));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.muutaKirja(kirja)) { //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Tietojen p�ivitys onnistui {"response":1}	
		}else {
			out.println("{\"response\":0}");  //Tietojen p�ivitys ep�onnistui {"response":0}	
		} 		
	}
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Kirjat.doDelete()");
		String pathInfo = request.getPathInfo();	//haetaan kutsun polkutiedot, esim. /5		
		int kirja_id = Integer.parseInt(pathInfo.replace("/", "")); //poistetaan polusta "/", j�ljelle j�� id, joka muutetaan int		
		Dao dao = new Dao();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();		    
		if(dao.poistaKirja(kirja_id)){ //metodi palauttaa true/false
			out.println("{\"response\":1}"); //Asiakkaan poisto onnistui {"response":1}
		}else {
			out.println("{\"response\":0}"); //Asiakkaan poisto ep�onnistui {"response":0}
		}		
	}

}
