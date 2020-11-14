package de.unidue.inf.is.domain;

public class ShowKomment {
    private String benutzer;
    private Integer komid;
    private String komtext;
    public ShowKomment(String benutzer, Integer komid,
                       String komtext)
    {
        this.benutzer = benutzer;
        this.komid = komid;
        this.komtext = komtext;
    }
    public String getBenutzer()
    {
        return benutzer;
    }
    public void setBenutzer(String s){ benutzer = s;}
    public String getKomtext(){return komtext;}
    public Integer getKomid(){return komid;}
}
