package de.unidue.inf.is.domain;


public final class Schreibt {

    private Integer projektKennung;
    private String benutzer;
    private Integer kommentarId;

    public Schreibt(Integer projektKennung, String benutzer,
                   Integer kommentarId)
    {
        this.projektKennung = projektKennung;
        this.benutzer = benutzer;
        this.kommentarId = kommentarId;
    }
    public Integer getProjektKennung() {
        return projektKennung;
    }
    public String getBenutzer() {
        return benutzer;
    }
    public Integer getKommentarId(){
        return kommentarId;
    }
}