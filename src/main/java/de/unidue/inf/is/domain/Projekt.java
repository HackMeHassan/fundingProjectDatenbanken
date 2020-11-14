package de.unidue.inf.is.domain;

public final class Projekt
{
    private Integer kennung;
    private String titel;
    private String beschreibung;
    private Double finanzierungslimit;
    private String status;
    private String ersteller;
    private Integer vorgaenger;
    private Integer kategorie;

    public Projekt(Integer kennung, String titel,
                   String beschreibung,
                   Double finanzierungslimit,
                   String status, String ersteller,
                   Integer vorgaenger, Integer kategorie)
    {
        this.kennung = kennung;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.finanzierungslimit = finanzierungslimit;
        this.status = status;
        this.ersteller = ersteller;
        this.vorgaenger = vorgaenger;
        this.kategorie = kategorie;
    }

    public Integer getKennung() {
        return kennung;
    }
    public void setKennung(Integer k){kennung = k;}

    public String getTitel() {
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Double getFinanzierungslimit() {
        return finanzierungslimit;
    }
    public void setFinanzierungslimit(Double a)
    {
        finanzierungslimit = a;
    }
    public String getStatus() {
        return status;
    }

    public String getErsteller() {
        return ersteller;
    }

    public Integer getVorgaenger() {
        return vorgaenger;
    }

    public Integer getKategorie() {
        return kategorie;
    }
}
