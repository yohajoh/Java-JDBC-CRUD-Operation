package JDBCcrudOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class CrudOperation implements Crud{
    Scanner scanner;
    public CrudOperation(){
        scanner = new Scanner(System.in);
    }

    @Override
    public void useDatabase(Statement statement, String query){
        try {
            assert statement != null;
            statement.executeUpdate(query);
        }catch (Exception e){System.out.println("Error: "+e);}
    }

    @Override
    public void createTable(Statement statement){
        System.out.println("write a query to create table:\nfollow this format <create table 'tableName'(list atributes, primary key(one of the atributes that identify each row uniquely))>");
        String query = scanner.nextLine();
        try {
            statement.executeUpdate(query);
            System.out.println("successfully created!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void deleteDatabase(Statement statement, String databaseName){
        try {
            statement.executeUpdate("drop database "+databaseName);
            System.out.println("Successfully "+databaseName+" database is deleted!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void deleteTable(Statement statement, String tableName){
        try {
            statement.executeUpdate("drop table "+tableName);
            System.out.println("Successfully "+tableName+" table is deleted!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void deleteRecord(Statement statement,String tableName,String columnName,int id){
        String query = "delete from "+tableName+" where "+columnName+" = "+id;
        try {
            statement.executeUpdate(query);
            System.out.println("Successfully deleted!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void deleteRecord(Statement statement,String tableName,String columnName,String id){
        String query = "delete from "+tableName+" where "+columnName+" = "+id;
        try {
            statement.executeUpdate(query);
            System.out.println("Successfully deleted!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void retrieveAll(String databaseName, Statement statement,String tableName){
        String query = "select * from "+tableName;
        ArrayList<String> columns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                for (String col:columns){
                    System.out.print(resultSet.getString(col)+",  ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void retrievePartial(Statement statement, String tableName,ArrayList<String> columns){
        StringBuffer query = new StringBuffer("select ");
        query.append(columns.get(0));
        String d = columns.remove(0);
        for (String item:columns){
            query.append(", ").append(item);
        }
        query.append(" from ").append(tableName);
        try {
            columns.add(0,d);
            ResultSet resultSet = statement.executeQuery(String.valueOf(query));
            while (resultSet.next()){
                for (String col:columns){
                    System.out.print(resultSet.getString(col)+",  ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void retrieveSingle(String databaseName, Statement statement, String tableName, String columnName, String id){
        String query = "select * from "+tableName + " where "+columnName+" = "+id;
        ArrayList<String> columns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
        try {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            for (String col:columns){
                System.out.print(resultSet.getString(col)+",  ");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void retrieveSSingle(Statement statement, String tableName, String columnName, ArrayList<String> columns, String id){
        StringBuffer query = new StringBuffer("select ");
        query.append(columns.get(0));
        String d = columns.remove(0);
        for (String item:columns){
            query.append(", ").append(item);
        }
        query.append(" from ").append(tableName).append(" where ").append(columnName).append(" = ").append(id);
        try {
            columns.add(0,d);
            ResultSet resultSet = statement.executeQuery(String.valueOf(query));
            resultSet.next();
            for (String col:columns){
                System.out.print(resultSet.getString(col)+",  ");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void recordNewDataToDatabase(Statement statement, String tableName,ArrayList<String> data){
        StringBuffer query = new StringBuffer("insert into "+tableName+" values(");
        query.append(data.get(0));
        data.remove(0);
        for (int i = 0; i<data.size();i++){
            query.append(", ").append(data.get(i));
        }
        query.append(")");
        System.out.println(query);
        try {
            statement.executeUpdate(String.valueOf(query));
            System.out.println("successfully inserted!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void updateRecord(Statement statement, String tableName, String columnName, String pkeyData,  String pkey, String data){
        String query = "update "+tableName+" set "+columnName+" = "+data+" where "+pkey+" = "+pkeyData;
        try {
            statement.executeUpdate(query);
            System.out.println("successfully updated!!!");
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public void diplay(ArrayList<String> arrayList){
        for (int j = 0;j<arrayList.size();j++){
            System.out.println(j+1+". "+arrayList.get(j));
        }
    }
}
