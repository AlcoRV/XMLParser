package com.company;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class XMLHandler extends DefaultHandler {
    private String tag;
    private LinkedList<String> catalog;
    private ArrayList<String> files;
    private Boolean isFile;

    public void Start() {
        catalog = new LinkedList<>();
        files = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        tag = qName;
        String attrValue = attributes.getValue(0);
        if(attrValue != null)
            isFile = attrValue.equals(XConstant.TRUE);
    }

    private void SaveFile(){
        StringBuilder nameFile = new StringBuilder();
        for(String dir : catalog)
            if(dir.equals(catalog.getFirst()) || dir.equals(catalog.getLast())) {
                nameFile.append(dir);
            } else{
                nameFile.append(dir).append(XConstant.SPLIT_DIR);
            }
        files.add(nameFile.toString());
        isFile = !isFile;
    }

    private void saveDir(String attribute){
        attribute = attribute.replace("\n", "").trim();
        if(!attribute.isEmpty()) {
            if (tag.equals(XConstant.ACTIVE_NODE)) {
                catalog.add(attribute);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        saveDir(new String(ch, start, length));

        if(isFile && tag.equals(XConstant.ACTIVE_NODE)){
            SaveFile();
        }
    }

    public ArrayList<String> GetFiles(){
        return files;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(qName.equals(XConstant.INCLUDE_NODE) && !catalog.isEmpty())
            catalog.removeLast();
    }
}
