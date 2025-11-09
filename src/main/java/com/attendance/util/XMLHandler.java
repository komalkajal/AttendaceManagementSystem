package com.attendance.util;

import com.attendance.model.Attendance;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {
    public static List<Attendance> parseAttendanceXML(InputStream inputStream) throws Exception {
        List<Attendance> attendanceList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("attendance");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Attendance attendance = new Attendance();
                attendance.setStudentId(Integer.parseInt(getTagValue("studentId", element)));
                attendance.setSubjectId(Integer.parseInt(getTagValue("subjectId", element)));
                attendance.setClassId(Integer.parseInt(getTagValue("classId", element)));
                attendance.setDate(Date.valueOf(getTagValue("date", element)));
                attendance.setStatus(getTagValue("status", element));
                attendance.setNotes(getTagValue("notes", element));
                attendance.setCheckInTime(getTagValue("checkInTime", element));
                attendance.setCheckOutTime(getTagValue("checkOutTime", element));
                attendanceList.add(attendance);
            }
        }
        return attendanceList;
    }

    public static void exportAttendanceToXML(List<Attendance> attendanceList, OutputStream outputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("attendances");
        document.appendChild(root);

        for (Attendance attendance : attendanceList) {
            Element attendanceElement = document.createElement("attendance");
            root.appendChild(attendanceElement);

            createElement(document, attendanceElement, "id", String.valueOf(attendance.getId()));
            createElement(document, attendanceElement, "studentId", String.valueOf(attendance.getStudentId()));
            createElement(document, attendanceElement, "subjectId", String.valueOf(attendance.getSubjectId()));
            createElement(document, attendanceElement, "classId", String.valueOf(attendance.getClassId()));
            createElement(document, attendanceElement, "date", attendance.getDate().toString());
            createElement(document, attendanceElement, "status", attendance.getStatus());
            if (attendance.getNotes() != null) {
                createElement(document, attendanceElement, "notes", attendance.getNotes());
            }
            if (attendance.getCheckInTime() != null) {
                createElement(document, attendanceElement, "checkInTime", attendance.getCheckInTime());
            }
            if (attendance.getCheckOutTime() != null) {
                createElement(document, attendanceElement, "checkOutTime", attendance.getCheckOutTime());
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(source, result);
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) {
            return "";
        }
        Node node = nodeList.item(0);
        if (node == null) {
            return "";
        }
        NodeList childNodes = node.getChildNodes();
        if (childNodes.getLength() == 0) {
            return "";
        }
        Node textNode = childNodes.item(0);
        return textNode != null && textNode.getNodeValue() != null ? textNode.getNodeValue() : "";
    }

    private static void createElement(Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value));
        parent.appendChild(element);
    }
}

