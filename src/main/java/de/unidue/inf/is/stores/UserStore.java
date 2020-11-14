package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unidue.inf.is.domain.Schreibt;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;



public final class UserStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public UserStore() throws StoreException {
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
    public void addUser(User userToAdd) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                            .prepareStatement("insert into dbp032.benutzer (name,email,beschreibung) values (?, ?, ?)");
            preparedStatement.setString(1, userToAdd.getFirstname()+
                    userToAdd.getLastname());
            preparedStatement.setString(2, userToAdd.getEmail());
            preparedStatement.setString(3, userToAdd.getExplanation());
            preparedStatement.executeUpdate();

        preparedStatement.close();
        complete();
        close();
    } catch (SQLException | IOException e) {
        throw new StoreException(e);
    }
    }
    public List<Schreibt> findenSchreibtVonProjekt(Integer kennung) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from dbp032.schreibt where projekt = ?");
            preparedStatement.setInt(1,kennung);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Schreibt> result = new ArrayList<>();
            while (resultSet.next())
            {
                result.add(new Schreibt(resultSet.getInt("projekt"),
                        resultSet.getString("benutzer"),
                        resultSet.getInt("kommentar")));
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
    public void deleteSchreibtMitKommId(List<Integer> komList) throws StoreException
    {
        makeConn();
        try {
            for (int k = 0; k < komList.size(); k++) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("delete from dbp032.schreibt where kommentar = ?");
                preparedStatement.setInt(1, komList.get(k));
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }

            complete();
            close();
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public void deleteKommentarMitKommId(List<Integer> komList) throws StoreException
    {
        makeConn();
        try {
            for (int kk = 0; kk < komList.size(); kk++) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("delete from dbp032.kommentar where id = ?");
                preparedStatement.setInt(1, komList.get(kk));
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }

            complete();
            close();
        }catch (SQLException | IOException e)
        {
            throw new StoreException(e);
        }
    }
    public String findenTextVomKommentar(Integer id) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select text from dbp032.kommentar where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String resultText = null;
            if (resultSet.next()) {
                resultText = resultSet.getString(1);
            }
            resultSet.close();
            preparedStatement.close();
            complete();
            close();
            return resultText;
        } catch (SQLException | IOException e) {
            throw new StoreException(e);
        }
    }
    public String findenBenutzername(String email) throws StoreException
    {
        makeConn();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select name from dbp032.benutzer where email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = null;
            if (resultSet.next()) {
                result = resultSet.getString(1);
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
    public HashMap<String,String> werSagteWas(List<Schreibt> result) throws IOException
    {
        makeConn();
        try {
        if(result != null)
        {
            HashMap<String,String> resultInMap = new HashMap<>();
            for (int j = 0; j < result.size(); j++)
            {
                resultInMap.put(result.get(j).getBenutzer(),
                        findenTextVomKommentar(result.get(j).getKommentarId()));
            }
            complete();
            close();
            return resultInMap;
        }
        else
        {
            complete();
            close();
            return null;
        }
    }catch (IOException e)
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
