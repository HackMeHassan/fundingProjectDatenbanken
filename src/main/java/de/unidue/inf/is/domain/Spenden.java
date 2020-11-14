package de.unidue.inf.is.domain;

import java.util.Date;

public final class Spenden {

    private Integer projektKennung;
    private String spender;
    private Double spendenBetrag;
    private String sichtbarkeit;

    public Spenden(Integer projektKennung, String spender,
                     Double spendenBetrag, String sichtbarkeit)
    {
        this.projektKennung = projektKennung;
        this.spender = spender;
        this.spendenBetrag = spendenBetrag;
        this.sichtbarkeit = sichtbarkeit;
    }
    public Integer getProjektKennung() {
        return projektKennung;
    }
    public String getSpender() {
        return spender;
    }
    public void setSpender(String s){ spender = s;}
    public Double getSpendenBetrag(){
        return spendenBetrag;
    }
    public String getSichtbarkeit()
    {
        return sichtbarkeit;
    }
}