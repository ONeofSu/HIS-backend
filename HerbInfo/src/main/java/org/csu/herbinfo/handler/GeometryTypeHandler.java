package org.csu.herbinfo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeometryTypeHandler extends BaseTypeHandler<Point> {
    private static final WKBReader wkbReader = new WKBReader();
    private static final WKBWriter wkbWriter = new WKBWriter();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Point parameter, JdbcType jdbcType) throws SQLException {
        PGobject pgObject = new PGobject();
        pgObject.setType("geometry");
        pgObject.setValue(parameter.toText());
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
                return (Point) wkbReader.read(WKBReader.hexToBytes(pgValue));
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Error parsing geometry", e);
        }
    }
}
