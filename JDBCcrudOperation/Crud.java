package JDBCcrudOperation;

import java.sql.Statement;
import java.util.ArrayList;

public interface Crud {
    public void useDatabase(Statement statement, String query);
    public void createTable(Statement statement);
    public void deleteDatabase(Statement statement, String databaseName);
    public void deleteTable(Statement statement, String tableName);
    public void deleteRecord(Statement statement,String tableName,String columnName,int id);
    public void deleteRecord(Statement statement,String tableName,String columnName,String id);
    public void retrieveAll(String databaseName, Statement statement,String tableName);
    public void retrievePartial(Statement statement, String tableName, ArrayList<String> columns);
    public void retrieveSingle(String databaseName, Statement statement, String tableName, String columnName, String id);
    public void retrieveSSingle(Statement statement, String tableName, String columnName, ArrayList<String> columns, String id);
    public void recordNewDataToDatabase(Statement statement, String tableName,ArrayList<String> data);
    public void updateRecord(Statement statement, String tableName, String columnName, String pkeyData,  String pkey, String data);
    public void diplay(ArrayList<String> arrayList);
}
