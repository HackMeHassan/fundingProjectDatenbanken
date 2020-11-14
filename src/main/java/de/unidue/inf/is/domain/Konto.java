package de.unidue.inf.is.domain;

public final class Konto {

    private String inhaber;
    private String geheimzahl;
    private Double guthaben;

    public Konto(String inhaber, String geheimzahl,
                Double guthaben)
    {
        this.inhaber = inhaber;
        this.geheimzahl = geheimzahl;
        this.guthaben = guthaben;
    }
    public String getInhaber() {
        return inhaber;
    }
    public String getGeheimzahl() {
        return geheimzahl;
    }
    public Double getGuthaben(){
        return guthaben;
    }
}