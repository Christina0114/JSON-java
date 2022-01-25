package org.json.junit;

import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONPointerException;
import org.json.XML;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class ReplaceXMLTest {
    @Test
    public void shouldHandleEmptyXML() {
        String xml = "";
        JSONObject replacement = XML.toJSONObject("<street>Ave of the Arts</street>\n");
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xml),new JSONPointer("/contact/address/street/"),replacement);
        assertTrue("jsonObject should be empty", jsonObject.isEmpty());
    }

    @Test
    public void shouldHandleInvalidPath() {
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "Wenjun"+
                "</contact>";
        JSONObject replacement = XML.toJSONObject("<contact>CC</contact>\n");
        try {
            XML.toJSONObject(new StringReader(xml),new JSONPointer("Ch/contact/"),replacement);
            fail("Expecting a JSONException");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("Expecting an exception message",
                    "a JSON pointer should start with '/' or '#/'",
                    e.getMessage());
        }
    }
    @Test
    public void shouldHandleNoMatchPath() {
        String xml =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";
        JSONObject replacement = XML.toJSONObject("<contact>CC</contact>\n");
        try {
            System.out.println("Given replacement: " + replacement);
            JSONObject jobj = XML.toJSONObject(new StringReader(xml), new JSONPointer("/contact/f/street/"), replacement);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("Expecting an exception message",
                    "no match JsonPointer",
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
        JSONObject replacement = XML.toJSONObject("<zipcode>888888</zipcode>\n");
        String expectedStr = "{\"contact\":{\"nick\":\"Crista\",\"address\":{\"zipcode\":{\"zipcode\":888888},\"street\":\"Ave of Nowhere\"},\"name\":\"Crista Lopes\"}}";
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xml),new JSONPointer("/contact/address/zipcode/"), replacement);
        JSONObject expectedJsonObject = new JSONObject(expectedStr);
        Util.compareActualVsExpectedJsonObjects(jsonObject,expectedJsonObject);
    }
    @Test
    public void handleUnorderReplacement() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";
        JSONObject replacement = XML.toJSONObject("<street>Ave of the Arts</street>\n");
        String expectedStr = "{\"contact\":{\"nick\":\"Crista\",\"address\":{\"zipcode\":92614,\"street\":{\"street\":\"Ave of the Arts\"}},\"name\":\"Crista Lopes\"}}" ;
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xmlString),new JSONPointer("/contact/address/street/"),replacement);
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
        JSONObject replacement = XML.toJSONObject("  <address>\n" +
                "    <street>new place</street>\n" +
                "    <zipcode>94086</zipcode>\n" +
                "  </address>");
        String expectedStr = "{\"contact\":{\"nick\":\"Crista\",\"address\":{\"address\":{\"zipcode\":94086,\"street\":\"new place\"}},\"name\":\"Crista Lopes\"}}";
        JSONObject jsonObject = XML.toJSONObject(new StringReader(xmlString),new JSONPointer("/contact/address/"),replacement);
        JSONObject expectedJsonObject = new JSONObject(expectedStr);
        Util.compareActualVsExpectedJsonObjects(jsonObject,expectedJsonObject);
    }

}
