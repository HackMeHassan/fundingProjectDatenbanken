package de.unidue.inf.is.domain;

public class ShowGespendet
{
    private String projektTitel;
    private Integer projektKennung;
    private Double limit;
    private String statu;
    private Double gespendetBetrag;
    public ShowGespendet(String projektTitel,
                         Integer projektKennung,
                         Double limit,
                         String statu,
                         Double gespendetBetrag)
    {
        this.projektTitel = projektTitel;
        this.projektKennung = projektKennung;
        this.limit = limit;
        this.statu = statu;
        this.gespendetBetrag = gespendetBetrag;
    }
    public String getProjektTitel()
    {
        return projektTitel;
    }
    public Integer getProjektKennung()
    {
        return projektKennung;
    }
    public Double getLimit()
    {
        return limit;
    }
    public String getStatu()
    {
        return statu;
    }
    public Double getGespendetBetrag()
    {
        return gespendetBetrag;
    }
}
