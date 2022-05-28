package com.example.pressnews.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
public class XMLService {
    public String parseUSD(String today) {
        String usd = "";
        try {
            String url = "https://www.nationalbank.kz/rss/get_rates.cfm?fdate="+today;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url);

            doc.getDocumentElement().normalize();

            NodeList nodelist = doc.getElementsByTagName("title");
            NodeList nodelist2 = doc.getElementsByTagName("description");

            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node  = nodelist.item(i);
                Node node2 = nodelist2.item(i);
                if (node.getTextContent().equals("USD")) {
                    usd = node2.getTextContent();
                }
            }

        } catch (Exception ex) {

        }
        return usd;
    }

    public String parseEUR(String today) {
        String eur = "";
        try {
            String url = "https://www.nationalbank.kz/rss/get_rates.cfm?fdate="+today;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url);

            doc.getDocumentElement().normalize();

            NodeList nodelist = doc.getElementsByTagName("title");
            NodeList nodelist2 = doc.getElementsByTagName("description");

            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node  = nodelist.item(i);
                Node node2 = nodelist2.item(i);
                if (node.getTextContent().equals("EUR")) {
                    eur = node2.getTextContent();
                }
            }

        } catch (Exception ex) {

        }
        return eur;
    }

    public String parseRUB(String today) {
        String rub = "";
        try {
            String url = "https://www.nationalbank.kz/rss/get_rates.cfm?fdate="+today;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url);

            doc.getDocumentElement().normalize();

            NodeList nodelist = doc.getElementsByTagName("title");
            NodeList nodelist2 = doc.getElementsByTagName("description");

            for (int i = 0; i < nodelist.getLength(); i++) {
                Node node  = nodelist.item(i);
                Node node2 = nodelist2.item(i);
                if (node.getTextContent().equals("RUB")) {
                    rub = node2.getTextContent();
                }
            }

        } catch (Exception ex) {

        }
        return rub;
    }
}
