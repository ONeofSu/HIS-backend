package org.csu.herbinfo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKBWriter;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeometryTypeHandler extends BaseTypeHandler<Point> {
    private static final WKTReader wktReader = new WKTReader();
    private static final WKBReader wkbReader = new WKBReader();
    private static final WKBWriter wkbWriter = new WKBWriter();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Point parameter, JdbcType jdbcType) throws SQLException {
        PGobject pgObject = new PGobject();
        pgObject.setType("geometry");
        // 使用WKB格式存储
        pgObject.setValue(WKBWriter.toHex(wkbWriter.write(parameter)));
        ps.setObject(i, pgObject);
    }

    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseGeometry(rs.getObject(columnName));
    }

    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseGeometry(rs.getObject(columnIndex));
    }

    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseGeometry(cs.getObject(columnIndex));
    }

    private Point parseGeometry(Object value) throws SQLException {
        if (value == null) return null;
        try {
            if (value instanceof PGobject) {
                String pgValue = ((PGobject) value).getValue();
                // 处理带有SRID的WKT格式
                if (pgValue.startsWith("SRID=")) {
                    // 去掉SRID部分，只保留WKT部分
                    int semicolonIndex = pgValue.indexOf(';');
                    if (semicolonIndex > 0) {
                        pgValue = pgValue.substring(semicolonIndex + 1);
                    }
                }
                // 尝试解析为WKT格式
                if (pgValue.startsWith("POINT")) {
                    Geometry geometry = wktReader.read(pgValue);
                    if (geometry instanceof Point) {
                        return (Point) geometry;
                    }
                    throw new SQLException("Expected Point geometry but got: " + geometry.getGeometryType());
                }
                // 如果不是WKT，尝试WKB（向后兼容）
                try {
                    return (Point) wkbReader.read(WKBReader.hexToBytes(pgValue));
                } catch (IllegalArgumentException e) {
                    throw new SQLException("Geometry value is neither valid WKT nor WKB format: " + pgValue);
                }
            }
            return null;
        } catch (ParseException e) {
            throw new SQLException("Error parsing geometry", e);
        }
    }

}
