package JDBCcrudOperation;

import java.util.ArrayList;

public interface MetaData {
    public ArrayList<String> getDatabases();
    public ArrayList<String> getTables(String databaseName);
    public String getPrimaryKeyType(String tableName,String columnName);
    public String getPrimaryKey(String tableName);
    public ArrayList<String> getColumnsTypes(String databaseName,String tableName);
}
