package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.Spenden;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;



public final class ProjektStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public ProjektStore() throws StoreException {
        /*try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
            setComplete();
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }*/
    }
    public void setComplete()
    {
        complete = false;
    }
    public Connection makeConn() throws StoreException
    {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
            setComplete();
            return connection;
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    public void addProjekt(Projekt projektToAdd) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp032.projekt (kennung,titel,beschreibung,vorgaenger,kategorie,status,finanzierungslimit,ersteller) values (?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, projektToAdd.getKennung());
            preparedStatement.setString(2, projektToAdd.getTitel());
            preparedStatement.setString(3, projektToAdd.getBeschreibung());
            if (projektToAdd.getVorgaenger() == null)
            {
                preparedStatement.setObject(4, null);
            }
            else {
                preparedStatement.setInt(4, projektToAdd.getVorgaenger());
            }
            preparedStatement.setInt(5, projektToAdd.getKategorie());
            preparedStatement.setString(6, projektToAdd.getStatus());
            preparedStatement.setDouble(7, projektToAdd.getFinanzierungslimit());
            preparedStatement.setString(8, projektToAdd.getErsteller());
            preparedStatement.executeUpdate();

        preparedStatement.close();
        complete();
        close();
    } catch (SQLException | IOException e) {
        throw new StoreException(e);
    }
    }
    public void addSpenden(Spenden neuSpenden) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp032.spenden (spender,projekt,spendenbetrag,sichtbarkeit) values (?,?,?,?)");
            preparedStatement.setString(1, neuSpenden.getSpender());
            preparedStatement.setInt(2, neuSpenden.getProjektKennung());
            preparedStatement.setDouble(3, neuSpenden.getSpendenBetrag());
            preparedStatement.setString(4, neuSpenden.getSichtbarkeit());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public void addKommentar(Kommentar neuKomment) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp032.kommentar (id, text, datum, sichtbarkeit) values (?,?,?,?)");
            preparedStatement.setInt(1, neuKomment.getId());
            preparedStatement.setString(2, neuKomment.getText());
            preparedStatement.setString(3, neuKomment.getDatum());
            preparedStatement.setString(4, neuKomment.getSichtbarkeit());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public void addSchreibt(Integer kommId, Integer proKennung,
                            String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp032.schreibt (benutzer, projekt, kommentar) values (?,?,?)");
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, proKennung);
            preparedStatement.setInt(3, kommId);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public void editProjekt(Projekt projektToAdd) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update dbp032.projekt set titel = ?, beschreibung = ?, vorgaenger = ?, kategorie = ?, status = ?, finanzierungslimit = ?, ersteller = ? where kennung = ?");

            preparedStatement.setString(1, projektToAdd.getTitel());
            preparedStatement.setString(2, projektToAdd.getBeschreibung());
            //preparedStatement.setInt(3, projektToAdd.getVorgaenger());
            if (projektToAdd.getVorgaenger() == null)
            {
                preparedStatement.setObject(3, null);
            }
            else {
                preparedStatement.setInt(3, projektToAdd.getVorgaenger());
            }
            preparedStatement.setInt(4, projektToAdd.getKategorie());
            preparedStatement.setString(5, projektToAdd.getStatus());
            preparedStatement.setDouble(6, projektToAdd.getFinanzierungslimit());
            preparedStatement.setString(7, projektToAdd.getErsteller());
            preparedStatement.setInt(8, projektToAdd.getKennung());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public Integer findenLetzteKennung() throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select kennung from dbp032.projekt order by kennung desc fetch first rows only");
            ResultSet resultSet = preparedStatement.executeQuery();

            Integer result = null;
            if (resultSet.next()){
            result = resultSet.getInt(1);
            }
        resultSet.close();
        preparedStatement.close();
        complete();
        close();
        return result;
    } catch (SQLException | IOException e) {
        throw new StoreException(e);
    }
    }
    public Integer findenLetzteId() throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select id from dbp032.kommentar order by id desc fetch first rows only");
            ResultSet resultSet = preparedStatement.executeQuery();

            Integer result = null;
            if (resultSet.next()){
                result = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public List<Projekt> findenOffeneProjekte() throws StoreException
    {
            makeConn();
            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("select * from dbp032.projekt where status = ?");

                preparedStatement.setString(1, "offen");
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Projekt> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(new Projekt(resultSet.getInt("kennung"),
                            resultSet.getString("titel"),
                            resultSet.getString("beschreibung"),
                            resultSet.getDouble("finanzierungslimit"),
                            resultSet.getString("status"),
                            resultSet.getString("ersteller"),
                            resultSet.getInt("vorgaenger"),
                            resultSet.getInt("kategorie")));
                }
                if (result.isEmpty())
                {
                    result.add(new Projekt(0,
                            "Es gibt kein offenes Projekt!",
                            "",
                            0.00,
                            "offen",
                            "",
                            null,
                            1));
                }
                resultSet.close();
                preparedStatement.close();
                complete();
                close();
                return result;
            } catch (SQLException | IOException e) {
                throw new StoreException(e);
            }
    }

    public List<Projekt> findenAbgeschlosseneProjekte() throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from dbp032.projekt where status != ?");
            preparedStatement.setString(1, "offen");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Projekt> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new Projekt(resultSet.getInt("kennung"),
                        resultSet.getString("titel"),
                        resultSet.getString("beschreibung"),
                        resultSet.getDouble("finanzierungslimit"),
                        resultSet.getString("status"),
                        resultSet.getString("ersteller"),
                        resultSet.getInt("vorgaenger"),
                        resultSet.getInt("kategorie")));
            }
            if (result.isEmpty())
            {
                result.add(new Projekt(0,
                        "Es gibt kein abgeschlossenes Projekt!",
                        "",
                        0.00,
                        "offen",
                        "",
                        null,
                        1));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Projekt> findenErstellteProjekteVon(String email) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from dbp032.projekt where ersteller = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Projekt> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(new Projekt(resultSet.getInt("kennung"),
                        resultSet.getString("titel"),
                        resultSet.getString("beschreibung"),
                        resultSet.getDouble("finanzierungslimit"),
                        resultSet.getString("status"),
                        resultSet.getString("ersteller"),
                        resultSet.getInt("vorgaenger"),
                        resultSet.getInt("kategorie")));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Projekt> findenProjektMitKennung(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from dbp032.projekt where kennung = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Projekt> result = new ArrayList<>();
            while (resultSet.next()) {
                 result.add(new Projekt(resultSet.getInt("kennung"),
                         resultSet.getString("titel"),
                         resultSet.getString("beschreibung"),
                         resultSet.getDouble("finanzierungslimit"),
                         resultSet.getString("status"),
                         resultSet.getString("ersteller"),
                         resultSet.getInt("vorgaenger"),
                         resultSet.getInt("kategorie")));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public boolean obProjektIstVorgaenger(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select kennung from dbp032.projekt where vorgaenger = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> result = new ArrayList<>();
            boolean res;
            while (resultSet.next()) {
                result.add(resultSet.getInt(1));
            }
            if ((result == null) || (result.isEmpty()))
            {
                res = false;
            }
            else
            {
                res = true;
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return res;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Projekt> findenProjektMitListVonKennung(List<Integer> kennungList) throws StoreException
    {
        makeConn();
        try
        {
            List<Projekt> result = new ArrayList<>();
            for (int p = 0; p < kennungList.size(); p++) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("select * from dbp032.projekt where kennung = ?");
                preparedStatement.setInt(1, kennungList.get(p));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(new Projekt(resultSet.getInt("kennung"),
                            resultSet.getString("titel"),
                            resultSet.getString("beschreibung"),
                            resultSet.getDouble("finanzierungslimit"),
                            resultSet.getString("status"),
                            resultSet.getString("ersteller"),
                            resultSet.getInt("vorgaenger"),
                            resultSet.getInt("kategorie")));
                }
                resultSet.close();
                preparedStatement.close();
            }
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public Integer findenKennungVon(String titel) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select kennung from dbp032.projekt where titel = ?");
            preparedStatement.setString(1, titel);
            ResultSet resultSet = preparedStatement.executeQuery();
            Integer result = null;
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Double> findenGuthaben(String username) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select guthaben from dbp032.konto where inhaber = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Double> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getDouble("guthaben"));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void reduzierenGuthaben(String username, Double betrag) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update dbp032.konto set guthaben = ? where inhaber = ?");
            preparedStatement.setDouble(1, betrag);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Integer> findenUnterstuezteProjekteVon(String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select projekt from dbp032.spenden where spender = ? and sichtbarkeit = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, "oeffentlich");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(resultSet.getInt(1));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public Double findenGespendet(String email, Integer kennung) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select spendenbetrag from dbp032.spenden where spender = ? and projekt = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Double> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(resultSet.getDouble(1));
            }

            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result.get(0);

        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Projekt> findenProjekteDurchSuchen(String suchWort1, String suchWort2) throws StoreException
    {
        makeConn();
        try {
            char[] suchWortLow = suchWort1.toCharArray();
            char[] suchWortUp = suchWort2.toCharArray();
            //for (int i = 0; i < suchWortLow.length;) {

                PreparedStatement preparedStatement = connection
                        .prepareStatement("select * from dbp032.projekt where titel like ? or titel like ?");
                preparedStatement.setString(1, suchWortLow[0] + "%");
                preparedStatement.setString(2, suchWortUp[0] + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Projekt> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(new Projekt(resultSet.getInt("kennung"),
                            resultSet.getString("titel"),
                            resultSet.getString("beschreibung"),
                            resultSet.getDouble("finanzierungslimit"),
                            resultSet.getString("status"),
                            resultSet.getString("ersteller"),
                            resultSet.getInt("vorgaenger"),
                            resultSet.getInt("kategorie")));
                }
                resultSet.close();
                preparedStatement.close();
                if ((result == null) || (result.isEmpty()))
                {
                    complete();
                    close();
                    return null;
                }
                else
                {
                    List<Projekt> realResult = new ArrayList<>();
                    for (int z = 0; z < result.size(); z++)
                    {
                        if ((result.get(z).getTitel().toLowerCase().startsWith(suchWort1)))
                        {
                            realResult.add(result.get(z));
                        }
                    }
                    complete();
                    close();
                    return realResult;
                }

        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void deleteSpenden(Integer projekt, String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                        .prepareStatement("delete from dbp032.spenden where projekt = ? and spender = ?");
            preparedStatement.setInt(1, projekt);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void deleteProjekt(Integer kennung) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from dbp032.projekt where kennung = ?");
            preparedStatement.setInt(1, kennung);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public Integer findenAnzahlErstellteProjekteVon(String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select count(*) from dbp032.projekt where ersteller = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            Integer result = null;
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public Integer findenAnzahlUnterstuezteProjekteVon(String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select count(*) from dbp032.spenden where spender = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            Integer result = null;
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }

    public List<String> vorgaengerList(String email) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select titel from dbp032.projekt where ersteller = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(resultSet.getString(1));
            }
            return result;
        }catch (SQLException e)
        {
            throw new StoreException(e);
        }
    }
    public String findenBeschreibungVon(String titel) throws StoreException
    {
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select beschreibung from projekt where titel = ?");
            preparedStatement.setString(1, titel);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = resultSet.getString(1);
            return result;
        }catch (SQLException e)
        {
            throw new StoreException(e);
        }
    }
    public Double findenFinanzierungsLimitVon(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select finanzierungslimit from dbp032.projekt where kennung = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            Double result = null;
            if (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void wechselStatus(Integer kennung) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update dbp032.projekt set status = ? where kennung = ?");
            preparedStatement.setString(1, "geschlossen");
            preparedStatement.setInt(2, kennung);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public Double findenTotalSpendeVomProjekt(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select sum(spendenbetrag) from dbp032.spenden where projekt = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            Double result = null;
            if (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public String findenSichtbarkeitVomKommentar(Integer komId) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select sichtbarkeit from dbp032.kommentar where id = ?");
            preparedStatement.setInt(1, komId);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = null;
            if (resultSet.next())
            {
                result = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public String findenTitelVomProjekt(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select titel from dbp032.projekt where kennung = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = null;
            if (resultSet.next())
            {
                result = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Spenden> findenSpenderVomProjekt(Integer kennung) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from dbp032.spenden where projekt = ?");
            preparedStatement.setInt(1, kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Spenden> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(new Spenden(resultSet.getInt("projekt"),
                        resultSet.getString("spender"),
                        resultSet.getDouble("spendenbetrag"),
                        resultSet.getString("sichtbarkeit")));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public List<Double> findenSpendenbetragVomProjektMitEmail(Integer kennung, String email) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select spendenbetrag from dbp032.spenden where projekt = ? and spender = ?");
            preparedStatement.setInt(1, kennung);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Double> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(resultSet.getDouble("spendenbetrag"));
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public boolean wennSpenderExistiert(String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select count(*) from dbp032.spenden where spender = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            Integer r = null;
            boolean result = true;
            if (resultSet.next())
            {
                r = resultSet.getInt(1);
            }
            if ((r == null) || (r == 0))
            {
                result = false;
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return result;
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void updateSpendenbetragVomProjektMitEmail(Double betrag, Integer kennung, String email) throws StoreException
    {
        makeConn();
        try
        {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update dbp032.spenden set spendenbetrag = ? where projekt = ? and spender = ?");
            preparedStatement.setDouble(1, betrag);
            preparedStatement.setInt(2, kennung);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            complete();
            close();
        } catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }


    public void complete() {
        complete = true;
    }


    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                throw new StoreException(e);
            }
            finally {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}
