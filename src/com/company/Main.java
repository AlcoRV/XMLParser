package com.company;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {
    public static void main(String args[])throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter path for your xml:");
        String path = reader.readLine();

        XMLHandler handler = new XMLHandler();
        parser.parse(path, handler);
        System.out.println("Your files in:");
        handler.Print();

    }

    private static class XMLHandler extends DefaultHandler{
        String attr;
        LinkedList<String> catalog;
        ArrayList<String> files;
        Boolean isFile;

        @Override
        public void startDocument() throws SAXException {
            attr = new String();
            catalog = new LinkedList<>();
            files = new ArrayList<String>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            attr = qName;
            String attrValue = attributes.getValue(0);
            if(attrValue != null)
                isFile = attrValue.equals("true");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);
            information = information.replace("\n", "").trim();
            if(!information.isEmpty())
                if(attr == "name") {
                    catalog.add(information);
                }

            if(isFile && attr == "name"){
                String file = "";
                for(String It : catalog)
                    if(It == catalog.getFirst() || It == catalog.getLast())
                        file += It;
                    else
                        file += (It + "/");
                    files.add(file);
                isFile = !isFile;
            }
        }

        public void Print(){
            for(String It : files)
                System.out.printf("%s\n", It);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws  SAXException{
            if(qName.contains("child") && !catalog.isEmpty())
                catalog.removeLast();
        }
    }
}