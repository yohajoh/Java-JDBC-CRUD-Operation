package JDBCcrudOperation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String databaseName;
        String tableName;
        String columnName;
//        arrayListToHoldDatabases
        ArrayList<String> aLTHDatabases = null;
//        arrayListToHoldTables
        ArrayList<String> aLTHTables = null;
        ArrayList<String> aLTHColumns = null;
        ArrayList<String> aLTHCTypes = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","");
            statement = connection.createStatement();
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("Error: "+e);
        }

        assert connection != null;
        MetadataAboutDatabase metadataAboutDatabase = new MetadataAboutDatabase(connection);
        CrudOperation crudOperation = new CrudOperation();

        String controller = "";
        while (!controller.equals("0")){
            aLTHDatabases = metadataAboutDatabase.getDatabases();
            System.out.println("Press -> 1  if you want to create new database\nPress -> 2  if you want to use the existing database\nPress -> 3  if you want to delete existing database");
            int i = scanner.nextInt();
            switch (i){
                case 1:
                    System.out.println("Enter database name: ");
                    scanner.nextLine();
                    databaseName = scanner.nextLine();
                    String query = "CREATE DATABASE "+databaseName;
                    try {
                        assert statement != null;
                        statement.executeUpdate(query);
                        System.out.println("successfully created!!!");
                    }catch (Exception e){ System.out.println("Error: "+e);}
                    break;
                case 2:
                    System.out.println("Currently existing databases in your machine are:");
                    crudOperation.diplay(aLTHDatabases);

                    System.out.println("Which one do you want to use?:");
                    int k = scanner.nextInt();
                    databaseName = aLTHDatabases.get(k-1);
                    crudOperation.useDatabase(statement,"USE "+databaseName);
                    System.out.println("Which operation do you want to do on "+databaseName+" database");
                    System.out.println("1. Create table operation\n2. Retrieve operation\n3. Update operation\n4. Delete operation");
                    int l = scanner.nextInt();
                    if (l == 1) {
                        crudOperation.createTable(statement);
                    } else if  (l == 2){
                        System.out.println("From which table do want to retrieve?");
                        aLTHTables = metadataAboutDatabase.getTables(databaseName);
                        crudOperation.diplay(aLTHTables);
                        int c = scanner.nextInt();
                        tableName = aLTHTables.get(c-1);
                        System.out.println("Press -> 1  if you want to retrieve all records\nPress -> 2  if you want to retrieve specific record");
                        int d = scanner.nextInt();
                        if (d == 1){
                            System.out.println("Press -> 1  if you want to retrieve all attributes data\nPress -> 2  if you want to retrieve specific attributes data");
                            int e = scanner.nextInt();
                            if (e == 1){
                                crudOperation.retrieveAll(databaseName,statement,tableName);
                            } else if (e == 2) {
                                System.out.println("These are attribute of the " + tableName+" table");
                                aLTHColumns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
                                crudOperation.diplay(aLTHColumns);
                                System.out.println("Choose the attributes. when you finish press '0' to get data");
                                ArrayList<String> cur = new ArrayList<>();
                                while (true){
                                    int ss = scanner.nextInt();
                                    if (ss != 0){
                                        cur.add(aLTHColumns.get(Integer.parseInt(String.valueOf(ss))-1));
                                    } else {break;}
                                }
                                crudOperation.retrievePartial(statement,tableName,cur);
                                aLTHColumns.clear();
                            } else {
                                System.out.println("Invalid input!!!");
                            }
                        } else if (d == 2) {
                            System.out.println("Press -> 1  if you want to retrieve all attributes of single record\nPress -> 2  if you want to retrieve specific attributes single record");
                            int f = scanner.nextInt();
                            if (f == 1){
                                columnName = metadataAboutDatabase.getPrimaryKey(tableName);
                                String pkType = metadataAboutDatabase.getPrimaryKeyType(tableName,columnName);
                                System.out.println("The primary key attribute Type is "+pkType+".\nEnter the id");
                                scanner.nextLine();
                                String st = scanner.nextLine();
                                crudOperation.retrieveSingle(databaseName,statement,tableName,columnName,st);

                            } else if (f == 2) {
                                System.out.println("These are attribute of the " + tableName+" table");
                                aLTHColumns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
                                crudOperation.diplay(aLTHColumns);
                                System.out.println("Choose the attributes. when you finish press '0' to get data");
                                ArrayList<String> cur = new ArrayList<>();
                                while (true){
                                    scanner.nextLine();
                                    int ss = scanner.nextInt();
                                    if (ss != 0){
                                        cur.add(aLTHColumns.get(Integer.parseInt(String.valueOf(ss))-1));
                                    } else {break;}
                                }
                                columnName = metadataAboutDatabase.getPrimaryKey(tableName);
                                String pkType = metadataAboutDatabase.getPrimaryKeyType(tableName,columnName);
                                System.out.println("The primary key attribute Type is "+pkType+".\nEnter the id");
                                scanner.nextLine();
                                String id = scanner.nextLine();
                                crudOperation.retrieveSSingle(statement,tableName,columnName,cur,id);
                            } else{System.out.println("Invalid input!!");}
                        } else {
                            System.out.println("Invalid input!!!");
                        }

                    } else if (l == 3) {
                        System.out.println("Press 1 -> to insert new record\nPress 2 -> to update existing record");
                        int g = scanner.nextInt();
                        if (g == 1){
                            ArrayList<String> data = new ArrayList<>();
                            System.out.println("These are the tables of "+databaseName+" database");
                            aLTHTables = metadataAboutDatabase.getTables(databaseName);
                            crudOperation.diplay(aLTHTables);
                            System.out.println("In which table do you want to insert record");
                            int h = scanner.nextInt();
                            tableName = aLTHTables.get(h-1);
                            aLTHColumns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
                            aLTHCTypes = metadataAboutDatabase.getColumnsTypes(databaseName,tableName);
                            scanner.nextLine();
                            for (int j = 0; j<aLTHColumns.size();j++){
                                System.out.println("Enter: "+aLTHColumns.get(j)+": type -> "+aLTHCTypes.get(j));
                                data.add(scanner.nextLine());
                            }
                            crudOperation.recordNewDataToDatabase(statement,tableName,data);
                            aLTHTables.clear();
                            aLTHColumns.clear();
                            aLTHCTypes.clear();
                        } else if (g == 2) {
                            System.out.println("These are the tables of "+databaseName+" database");
                            aLTHTables = metadataAboutDatabase.getTables(databaseName);
                            crudOperation.diplay(aLTHTables);
                            System.out.println("In which table do you want to update data");
                            int h = scanner.nextInt();
                            tableName = aLTHTables.get(h-1);
                            System.out.println("These are the columns of "+tableName+" Table");
                            aLTHColumns = MetadataAboutDatabase.getColumnsName(databaseName,tableName);
                            crudOperation.diplay(aLTHColumns);
                            System.out.println("On which attribute do want to update data?");
                            int i1 = scanner.nextInt();
                            columnName = aLTHColumns.get(i1-1);
                            String pkey = metadataAboutDatabase.getPrimaryKey(tableName);
                            String pkType = metadataAboutDatabase.getPrimaryKeyType(tableName,pkey);
                            aLTHCTypes = metadataAboutDatabase.getColumnsTypes(databaseName,tableName);
                            System.out.println("Enter the ID to differentiate the targeted record from the rest TYPE -> "+pkType+" Then enter the "+columnName+" Type -> "+aLTHCTypes.get(i1-1));
                            scanner.nextLine();
                            String pkeyData = scanner.nextLine();
                            String data = scanner.nextLine();
                            crudOperation.updateRecord(statement,tableName,columnName,pkeyData,pkey,data);
                            aLTHTables.clear();
                            aLTHColumns.clear();
                            aLTHCTypes.clear();
                        } else {
                            System.out.println("Invalid input!!!");
                        }
                    } else if (l == 4) {
                        System.out.println("What do you want to delete:\n1. database itself\n2. table\n3. record");
                        int m = scanner.nextInt();
                        if (m == 1){
                            crudOperation.deleteDatabase(statement,databaseName);
                        } else if (m == 2) {
                            aLTHTables = metadataAboutDatabase.getTables(databaseName);
                            System.out.println("These are the tables in the "+databaseName+" database");
                            crudOperation.diplay(aLTHTables);
                            System.out.println("Which table do you want to delete?:");
                            int a = scanner.nextInt();
                            crudOperation.deleteTable(statement,aLTHTables.get(a-1));
                            aLTHTables.clear();
                        } else if (m == 3) {
                            aLTHTables = metadataAboutDatabase.getTables(databaseName);
                            System.out.println("These are the tables in the "+databaseName+" database");
                            crudOperation.diplay(aLTHTables);
                            System.out.println("From which table you want to delete data?:");
                            int b = scanner.nextInt();
                            tableName = aLTHTables.get(b-1);
                            columnName = metadataAboutDatabase.getPrimaryKey(tableName);
                            String pkType = metadataAboutDatabase.getPrimaryKeyType(tableName,columnName);
                            System.out.println("The type of primary key is "+pkType+" enter the primary key that you want to delete:");
                            if (Objects.equals(pkType, "TINYINT")){
                                int id = scanner.nextInt();
                                crudOperation.deleteRecord(statement,tableName,columnName,id);
                            }
                            else {
                                scanner.nextLine();
                                String s = scanner.nextLine();
                                crudOperation.deleteRecord(statement,tableName,columnName,s);
                            }
                        }else {
                            System.out.println("Invalid input!!!");
                        }
                    } else {
                        System.out.println("Invalid input!!!");
                    }
                    break;
                case 3:
                    System.out.println("Currently existing databases in your machine are:");
                    crudOperation.diplay(aLTHDatabases);
                    System.out.println("Which database do you want to delete?:");
                    int n = scanner.nextInt();
                    databaseName = aLTHDatabases.get(n-1);
                    crudOperation.deleteDatabase(statement,databaseName);
                    break;
                default:
                    System.out.println("Invalid input!!!");
            }
            aLTHDatabases.clear();
            System.out.println("Do you want to continue? Press any character except  0");
            controller = scanner.nextLine();
        }
    }
}
