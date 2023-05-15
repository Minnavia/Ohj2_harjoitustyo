package model;

public class Kirja {
	private int kirja_id;
	private String kirja_nimi, kirjailija_etunimi, kirjailija_sukunimi, genre, sidosasu;

	public Kirja() {
		super();
	}

	public Kirja(int kirja_id, String kirja_nimi, String kirjailija_etunimi, String kirjailija_sukunimi, String genre,
			String sidosasu) {
		super();
		this.kirja_id = kirja_id;
		this.kirja_nimi = kirja_nimi;
		this.kirjailija_etunimi = kirjailija_etunimi;
		this.kirjailija_sukunimi = kirjailija_sukunimi;
		this.genre = genre;
		this.sidosasu = sidosasu;
	}

	public int getKirja_id() {
		return kirja_id;
	}

	public void setKirja_id(int kirja_id) {
		this.kirja_id = kirja_id;
	}

	public String getKirja_nimi() {
		return kirja_nimi;
	}

	public void setKirja_nimi(String kirja_nimi) {
		this.kirja_nimi = kirja_nimi;
	}

	public String getKirjailija_etunimi() {
		return kirjailija_etunimi;
	}

	public void setKirjailija_etunimi(String kirjailija_etunimi) {
		this.kirjailija_etunimi = kirjailija_etunimi;
	}

	public String getKirjailija_sukunimi() {
		return kirjailija_sukunimi;
	}

	public void setKirjailija_sukunimi(String kirjailija_sukunimi) {
		this.kirjailija_sukunimi = kirjailija_sukunimi;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSidosasu() {
		return sidosasu;
	}

	public void setSidosasu(String sidosasu) {
		this.sidosasu = sidosasu;
	}

	@Override
	public String toString() {
		return "Kirja [kirja_id=" + kirja_id + ", kirja_nimi=" + kirja_nimi + ", kirjailija_etunimi="
				+ kirjailija_etunimi + ", kirjailija_sukunimi=" + kirjailija_sukunimi + ", genre=" + genre
				+ ", sidosasu=" + sidosasu + "]";
	}
	
}