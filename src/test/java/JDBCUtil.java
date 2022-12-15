import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.sql.*;

public class JDBCUtil {
    private static final String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String driverName = "oracle.jdbc.driver.OracleDriver";
    private static final String name = "jhd";
    private static final String password = "jiahongda";
    private static Connection connection;
    private static final String quotes = "'";
    private static final String space = " ";
    private static final String COMMENT_COLUMN = "COMMENT ON COLUMN ";
    private static final String COMMENT_TABLE = "COMMENT ON TABLE ";

    static {
        try {
            Class.forName(JDBCUtil.driverName);
            connection = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getJsonObjStr(String name, JSONObject object, JSONObject array) {
        JSONObject jsonObject = new JSONObject();
        object.forEach(
                (k, v) -> {
                    if (v instanceof JSONArray) {

                        getJsonObjStr(k.trim().toUpperCase(), ((JSONArray) v).getJSONObject(0), array);
                    }
                    if (v instanceof String) {
                        jsonObject.put(k.replace(" ", "").toUpperCase(), v.toString());
                    }
                });
        array.put(name, jsonObject);
    }

    public static String query(String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            int columnSize = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                int row = resultSet.getRow();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i < columnSize; i++) {
                    String string = resultSet.getString(i);
                    System.out.print(string + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean analysisJSONStr(String name, String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        // JSONObject comment_table = jsonObject.getJSONObject("COMMENT_TABLE").clone();
        jsonObject.remove("COMMENT_TABLE");
        JSONObject objects = new JSONObject();
        getJsonObjStr(name, jsonObject, objects);
        System.out.println(objects);
        objects.forEach(
                (k, v) -> {
                    if (v instanceof JSONObject) {
                        createTableByJsonString(name + "_" + k, ((JSONObject) v).toJSONString());
                    }
                });
        return false;
    }

    public static boolean createTableByJsonString(String table, String jsonstr) {
        Statement statement = null;
        try {
            JSONObject table_filed = JSON.parseObject(jsonstr);
            if (!table_filed.isEmpty()) {
                StringBuffer sql = new StringBuffer("CREATE TABLE ");
                sql.append(name.toUpperCase() + "." + table.toUpperCase() + " (");
                StringBuffer columnSql = new StringBuffer();

                table_filed.forEach(
                        (key, value) -> {
                            sql.append(key.toUpperCase() + space + "VARCHAR2(128 BYTE),");
                            columnSql.append(
                                    COMMENT_COLUMN
                                            + "JHD."
                                            + table.toUpperCase()
                                            + "."
                                            + key
                                            + space
                                            + " IS "
                                            + quotes
                                            + value.toString()
                                            + quotes
                                            + ";");
                        });

                StringBuffer createSql = sql.delete(sql.length() - 1, sql.length());
                createSql.append(");");
                System.out.println(createSql.toString());
                System.out.println(columnSql.toString());

                statement = connection.createStatement();
                int createSqlExecute = statement.executeUpdate(createSql.toString());
                int columnSqlExecute = statement.executeUpdate(columnSql.toString());

                return createSqlExecute > 0 & columnSqlExecute > 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  statement.close();

        }
        return false;
    }
}
