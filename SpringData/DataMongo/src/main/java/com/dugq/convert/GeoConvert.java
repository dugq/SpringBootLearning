package com.dugq.convert;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.geo.GeoJsonGeometryCollection;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoConvert {


    static List<Double> toList(Point point) {
        return Arrays.asList(point.getX(), point.getY());
    }

    public enum GeoJsonToDocumentConverter implements Converter<GeoJson<?>, Document> {

        INSTANCE;

        @Override
        public Document convert(GeoJson<?> source) {

            if (source == null) {
                return null;
            }

            Document dbo = new Document("type", source.getType());

            if (source instanceof GeoJsonGeometryCollection) {

                List<Object> dbl = new ArrayList<>();

                for (GeoJson<?> geometry : ((GeoJsonGeometryCollection) source).getCoordinates()) {
                    dbl.add(convert(geometry));
                }

                dbo.put("geometries", dbl);

            } else {
                dbo.put("coordinates", convertIfNecessary(source.getCoordinates()));
            }

            return dbo;
        }

        private Object convertIfNecessary(Object candidate) {

            if (candidate instanceof GeoJson) {
                return convertIfNecessary(((GeoJson<?>) candidate).getCoordinates());
            }

            if (candidate instanceof Iterable<?>) {

                List<Object> dbl = new ArrayList<>();

                for (Object element : (Iterable<?>) candidate) {
                    dbl.add(convertIfNecessary(element));
                }

                return dbl;
            }

            if (candidate instanceof Point) {
                return toList((Point) candidate);
            }

            return candidate;
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.7
     */
    public enum DocumentToGeoJsonPointConverter implements Converter<Document, GeoJsonPoint> {

        INSTANCE;

        @Override
        @SuppressWarnings("unchecked")
        public GeoJsonPoint convert(Document source) {

            if (source == null) {
                return null;
            }

            Assert.isTrue(ObjectUtils.nullSafeEquals(source.get("type"), "Point"),
                    String.format("Cannot convert type '%s' to Point", source.get("type")));

            List<Number> dbl = (List<Number>) source.get("coordinates");
            return new GeoJsonPoint(toPrimitiveDoubleValue(dbl.get(0)), toPrimitiveDoubleValue(dbl.get(1)));
        }

        private static double toPrimitiveDoubleValue(Object value) {

            Assert.isInstanceOf(Number.class, value, "Argument must be a Number");
            return NumberUtils.convertNumberToTargetClass((Number) value, Double.class);
        }
    }
}
