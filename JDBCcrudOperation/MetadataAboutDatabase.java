package JDBCcrudOperation;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MetadataAboutDatabase implements MetaData{
    static DatabaseMetaData databaseMetaData;
    public MetadataAboutDatabase(Connection connection){
        try {
            databaseMetaData = connection.getMetaData();
        } catch (SQLException e) {
            System.out.println("There is no connection to database metadata");
        }
    }

    @Override
    public ArrayList<String> getDatabases() {
        ArrayList<String> arrayList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = databaseMetaData.getCatalogs();
            while (resultSet.next()){
                arrayList.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return arrayList;
    }

    @Override
    public ArrayList<String> getTables(String databaseName){
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            ResultSet resultSet = databaseMetaData.getTables(databaseName,null,null, new String[]{"table"});
            while (resultSet.next()){
                arrayList.add(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return arrayList;
    }

    public static ArrayList<String> getColumnsName(String databaseName,String tableName){
        ArrayList<String> columns = new ArrayList<>();
        try {
            ResultSet resultSet = databaseMetaData.getColumns(databaseName,null,tableName,null);
            while (resultSet.next()){
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return columns;
    }

    @Override
    public String getPrimaryKeyType(String tableName,String columnName){
        ResultSet columns = null;
        try {
            columns = databaseMetaData.getColumns(null, null, tableName, columnName);
            if (columns.next()) {
                return columns.getString("TYPE_NAME");
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return "Unknown";
    }

    @Override
    public String getPrimaryKey(String tableName) {
        String schema = null;
        ResultSet resultSet = null;
        try {
            resultSet = databaseMetaData.getPrimaryKeys(null,schema,tableName);
            resultSet.next();
            return resultSet.getString("COLUMN_NAME");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return "unknown";
    }

    @Override
    public ArrayList<String> getColumnsTypes(String databaseName,String tableName){
        ArrayList<String> columnsType = new ArrayList<>();
        try {
            ResultSet resultSet = databaseMetaData.getColumns(databaseName,null,tableName,null);
            while (resultSet.next()){
                columnsType.add(resultSet.getString("TYPE_NAME"));
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return columnsType;
    }
}
