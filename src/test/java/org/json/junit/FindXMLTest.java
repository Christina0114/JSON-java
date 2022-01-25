package org.json.junit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONPointer;
import org.json.JSONObject;
import org.json.XML;
import org.json.JSONObject;
import org.json.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FindXMLTest {
    @Test
    public void shouldHandleEmptyXML() {
        String xml = "";
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xml),new JSONPointer("/contact/"));
        assertTrue("jsonObject should be empty", jsonObject.isEmpty());
    }

    @Test
    public void shouldHandleEmptyPath() {
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "Wenjun"+
                "</contact>";
        try {
            JSONObject jsonObject = XML.toJSONObject(new StringReader(xml),new JSONPointer("/"));
        }catch (Exception e)
        {
            assertEquals("Expecting an exception message",
                    "Invalid Json Pointer format",
                    e.getMessage());
        }
    }

    @Test
    public void shouldHandleInvalidPath() {
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "Wenjun"+
                "</contact>";
        try {
            XML.toJSONObject(new StringReader(xml),new JSONPointer("Ch/contact/"));
            fail("Expecting a JSONException");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("Expecting an exception message",
                    "a JSON pointer should start with '/' or '#/'",
                    e.getMessage());
        }
    }

    @Test
    public void handleoMostInsidebject() {
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";
        String expectedStr = "{content:92614}";
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xml),new JSONPointer("/contact/address/zipcode/"));
        JSONObject expectedJsonObject = new JSONObject(expectedStr);
        Util.compareActualVsExpectedJsonObjects(jsonObject,expectedJsonObject);
    }
    @Test
    public void handleUnorderFind() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";
        String expectedStr = " {content:Crista Lopes}" ;
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xmlString),new JSONPointer("/contact/name/"));
        JSONObject expectedJsonObject = new JSONObject(expectedStr);
        Util.compareActualVsExpectedJsonObjects(jsonObject,expectedJsonObject);
    }



    @Test
    public void handleSubObject() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";
        String expectedStr = " {street:Ave of Nowhere;\n" +
                                "zipcode:92614}";
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xmlString),new JSONPointer("/contact/address/"));
        JSONObject expectedJsonObject = new JSONObject(expectedStr);
        Util.compareActualVsExpectedJsonObjects(jsonObject,expectedJsonObject);
    }

}
