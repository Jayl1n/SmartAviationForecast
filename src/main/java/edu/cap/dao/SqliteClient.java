package edu.cap.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;

/**
 * Sqlite 数据库连接
 *
 * @author Jaylin
 */
@Component
public class SqliteClient {

    private static final Logger logger = Logger.getLogger(SqliteClient.class);
    private static final String separator = File.separator;
    private static SqliteClient INSTANCE;

    private PreparedStatement ps;
    private Connection connection;

    private SqliteClient() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(
                    "jdbc:sqlite::resource:"
                            + "sqlite" + separator + "visualization.db");
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFound");
        } catch (SQLException e) {
            logger.error("SQLException");
        }
    }

    /**
     * 通过地名查找城市ID
     *
     * @param areaName 地名
     * @return 城市ID
     */
    public Integer selectCityIdByName(String areaName) {
        String sql = "SELECT city_id FROM place WHERE name LIKE ? OR city_name LIKE ? OR province_name LIKE ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + areaName + "%");
            ps.setString(2, "%" + areaName + "%");
            ps.setString(3, "%" + areaName + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("city_id");
            }
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            logger.debug("SQLException:" + e.getSQLState());
        }
        return null;
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 通过地名查找坐标
     *
     * @param areaName 　地名
     * @return 坐标
     */
    public String selectLngAndLatByName(String areaName) {
        String sql =
                "SELECT\n" +
                        "    province.name,\n" +
                        "    province.longitude AS lng,\n" +
                        "    province.latitude AS lat,\n" +
                        "    '1' AS level\n" +
                        "FROM province\n" +
                        "WHERE name LIKE ?\n" +
                        "UNION\n" +
                        "SELECT\n" +
                        "    city.name,\n" +
                        "    city.longitude  AS lng,\n" +
                        "    city.latitude AS lat,\n" +
                        "    '2' AS level\n" +
                        "FROM city\n" +
                        "WHERE city.name LIKE ?\n" +
                        "UNION\n" +
                        "SELECT\n" +
                        "    place.name,\n" +
                        "    place.lng AS lng,\n" +
                        "    place.lat AS lat,\n" +
                        "    '3' AS level\n" +
                        "FROM place\n" +
                        "WHERE name LIKE ? OR city_name LIKE ? OR province_name LIKE ?\n" +
                        "ORDER BY level\n" +
                        "LIMIT 1";
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 5; i++) {
                ps.setString(i + 1, "%" + areaName + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lng = decimalFormat.format(rs.getFloat("lng"));
                String lat = decimalFormat.format(rs.getFloat("lat"));
                return lng + "," + lat;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //未查到则返回null
        return null;
    }

    /**
     * 获取SqliteClient实例
     *
     * @return SqliteClient
     */
    public static SqliteClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqliteClient();
        }
        return INSTANCE;
    }
}
