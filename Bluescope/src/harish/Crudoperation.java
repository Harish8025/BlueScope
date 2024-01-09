package harish;


	import java.sql.*;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Scanner;

	public class Crudoperation {

	    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/student";
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "balaji322my";

	    public static void main(String[] args) {
	        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
	            // Example Read Operation
	            List<Map<String, Object>> studentList = readOperation(connection);
	            System.out.println(studentList);
	            System.out.println("Enter number:");
	            Scanner sc = new Scanner(System.in);
	            int num = sc.nextInt();
	            
	            int newStudentId = createOperation(connection, "newName", "10th", 95);
	            System.out.println("New student created with ID: " + newStudentId);
	            switch(num) {
	            case 1:
	              // code block
	              break;
	            case 2:
	            	updateOperation(connection, newStudentId, "updatedName", "11th", 85);
	              break;
	            case 3:
	            	deleteOperation(connection, newStudentId-1);
	                break;
	            default:
	              // code block
	          }
	            sc.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    }

	   public static List<Map<String, Object>> readOperation(Connection connection) throws SQLException {
	        List<Map<String, Object>> resultList = new ArrayList<>();
	        String selectQuery = "SELECT * FROM student";

	        try {
	        	Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery(selectQuery);

	           while (resultSet.next()) {
	                Map<String, Object> rowMap = new HashMap<>();
	                ResultSetMetaData metaData = resultSet.getMetaData();
	                int columnCount = metaData.getColumnCount();

	                for (int i = 1; i <= columnCount; i++) {
	                    String columnName = metaData.getColumnName(i);
	                    Object columnValue = resultSet.getString(i);
	                    rowMap.put(columnName, columnValue);
	                }

	                resultList.add(rowMap);
	            }
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return resultList;
	   }

	    public static int createOperation(Connection connection, String name, String Class, int mark) {
	        String insertQuery = "INSERT INTO student (Name, Class, Mark) VALUES (?, ?, ?)";
	        int affectedRows=0;
	        try {
	        	PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
	            preparedStatement.setString(1, name);
	            preparedStatement.setString(2, Class);
	            preparedStatement.setInt(3, mark);
	            
	            preparedStatement.executeUpdate();
	            Statement st = connection.createStatement();
	            ResultSet id = st.executeQuery("select id from student where name = "+"'"+name+"'");
	            while(id.next()) {
	            affectedRows = id.getInt("Id");
	            }
	           
	            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    return generatedKeys.getInt(1);
	                } else {
	                    throw new SQLException("Creating student failed, no ID obtained.");
	                }
	            }
	        }catch(Exception ex) {
	        	
	        }
	        return affectedRows;
	    }

	    public static void updateOperation(Connection connection, int studentId, String name, String className, int mark) throws SQLException {
	        String updateQuery = "UPDATE student SET Name=?, Class=?, Mark=? WHERE id=?";

	        try {
	        	PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
	            preparedStatement.setString(1, name);
	            preparedStatement.setString(2, className);
	            preparedStatement.setInt(3, mark);
	            preparedStatement.setInt(4, studentId);

	            int affectedRows = preparedStatement.executeUpdate();

	            if (affectedRows == 0) {
	                throw new SQLException("Updating student failed, no rows affected.");
	            }
	        }catch(Exception ex) {
	        	
	        }
	    }

	    public static void deleteOperation(Connection connection, int studentId) throws SQLException {
	        String deleteQuery = "DELETE FROM student WHERE id=?";

	        try {
	        	PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
	            preparedStatement.setInt(1, studentId);

	            int affectedRows = preparedStatement.executeUpdate();

	            if (affectedRows == 0) {
	                throw new SQLException("Deleting student failed, no rows affected.");
	            }
	        }catch(Exception ex) {
	        	
	        }
	    }
	}