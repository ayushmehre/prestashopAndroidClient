package com.develeno.prestashopandroidclient;

import android.util.Log;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Parser {
    public static final int PARSE_ADDRESS = 6565656;
    public static final int PARSE_CART = 4545561;
    public static final int PARSE_CATEGORIES = 654867865;
    public static final int PARSE_CATEGORY = 666786785;
    public static final int PARSE_CUSTOMER = 6454654;
    public static final int PARSE_PASSWORD = 5465465;
    public static final int PARSE_PRODUCT = 646786564;
    public static final int PARSE_STOCK_INFO = 15664;
    private static XmlPullParser parser;

    public static Object parse(String xmlString, int parseCommand) {
        InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(stream, HTTP.UTF_8);
            switch (parseCommand) {
                case PARSE_STOCK_INFO /*15664*/:
                    return parseStockInfo();
                case PARSE_CART /*4545561*/:
                    Log.d("EXCEPTION_OCCURED", xmlString);
                    return parseCartXML();
                case PARSE_PASSWORD /*5465465*/:
                    return parsePasswordXML();
                case PARSE_CUSTOMER /*6454654*/:
                    return parseCustomerXML();
                case PARSE_ADDRESS /*6565656*/:
                    return parseAddressXML();
                case PARSE_PRODUCT /*646786564*/:
                    return parseProductXML();
                case PARSE_CATEGORIES /*654867865*/:
                    return parseCategoriesXML();
                case PARSE_CATEGORY /*666786785*/:
                    return parseCategoryXML();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StockInfo parseStockInfo() throws XmlPullParserException, IOException {
        StockInfo stockInfo = new StockInfo();
        boolean getId = false;
        boolean getQuantity = false;
        boolean getProductID = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String startTag = parser.getName();
                    if (startTag.contentEquals("quantity")) {
                        getQuantity = true;
                    }
                    /*if (startTag.contentEquals(ShareConstants.WEB_DIALOG_PARAM_ID)) {
                        getId = true;
                    }*/
                    if (!startTag.contentEquals("id_product")) {
                        break;
                    } else {
                        getProductID = true;
                        break;
                    }
                case 4:
                    String text = parser.getText();
                    if (getQuantity && text != null) {
                        stockInfo.setQuantity(Integer.parseInt(text));
                        getQuantity = false;
                    }
                    if (getId && text != null) {
                        stockInfo.setId(Integer.parseInt(text));
                        getId = false;
                    }
                    if (getProductID && text != null) {
                        stockInfo.setProductId(Integer.parseInt(text));
                        getProductID = false;
                        break;
                    }
            }
        }
        return stockInfo;
    }

    private static CartObject parseCartXML() throws XmlPullParserException, IOException {
        CartObject cartObject = new CartObject();
        boolean getId = false;
        boolean getcartRow = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String tagName = parser.getName();
                   /* if (tagName.contentEquals(ShareConstants.WEB_DIALOG_PARAM_ID)) {
                        getId = true;
                    }*/
                    if (!tagName.contentEquals("cart_row")) {
                        break;
                    } else {
                        getcartRow = true;
                        break;
                    }
                case 4:
                    String text = parser.getText().replaceAll("\n", "").replaceAll("\t", "");
                    boolean isValid = text != null && !text.isEmpty();
                    if (getId && isValid) {
                        cartObject.setId(Integer.parseInt(text));
                        getId = false;
                    }
                    if (getcartRow && isValid) {
                        getcartRow = false;
                        break;
                    }
                    break;
            }
        }
        return cartObject;
    }

    private static CartItem parseCartRow(String cartrow) throws XmlPullParserException, IOException {
        InputStream stream = new ByteArrayInputStream(cartrow.getBytes());
        XmlPullParser parsertemp = XmlPullParserFactory.newInstance().newPullParser();
        parsertemp.setInput(stream, null);
        boolean getProductId = false;
        boolean getQuantity = false;
        Product product = null;
        int quantity = 0;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parsertemp.next()) {
            switch (eventType) {
                case 2:
                    String tagName = parser.getName();
                    if (tagName.contentEquals("id_product ")) {
                        getProductId = true;
                    }
                    if (!tagName.contentEquals("quantity")) {
                        break;
                    } else {
                        getQuantity = true;
                        break;
                    }
                case 4:
                    String text = parser.getText();
                    if (getProductId && text != null) {
                        product = Data.getProductByURL("http://www.organicdolchi.com/api/products/" + text);
                        getProductId = false;
                    }
                    if (getQuantity && text != null) {
                        quantity = Integer.parseInt(text);
                        getQuantity = false;
                        break;
                    }
            }
        }
        return new CartItem(product, quantity);
    }

    private static Address parseAddressXML() throws XmlPullParserException, IOException {
        Address address = new Address();
        boolean getId = false;
        boolean getTitle = false;
        boolean getLine1 = false;
        boolean getLine2 = false;
        boolean getPinCode = false;
        boolean getPhoneNumber = false;
        boolean getMobileNumber = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String tagName = parser.getName();
                  /*  if (tagName.contentEquals(ShareConstants.WEB_DIALOG_PARAM_ID)) {
                        getId = true;
                    }*/
                    if (tagName.contentEquals("alias")) {
                        getTitle = true;
                    }
                    if (tagName.contentEquals("address1")) {
                        getLine1 = true;
                    }
                    if (tagName.contentEquals("address2")) {
                        getLine2 = true;
                    }
                    if (tagName.contentEquals("postcode")) {
                        getPinCode = true;
                    }
                    if (tagName.contentEquals("phone_mobile")) {
                        getMobileNumber = true;
                    }
                    if (!tagName.contentEquals("phone")) {
                        break;
                    } else {
                        getPhoneNumber = true;
                        break;
                    }
                case 4:
                    String text = parser.getText();
                    if (getPhoneNumber && text != null) {
                        address.setPhoneNumber(text);
                        getPhoneNumber = false;
                    }
                    if (getId && text != null) {
                        address.setId(Integer.parseInt(text));
                        getId = false;
                    }
                    if (getMobileNumber && text != null) {
                        address.setMobileNumber(text);
                        getMobileNumber = false;
                    }
                    if (getPinCode && text != null) {
                        address.setPinCode(text);
                        getPinCode = false;
                    }
                    if (getLine2 && text != null) {
                        address.setLine2(text);
                        getLine2 = false;
                    }
                    if (getLine1 && text != null) {
                        address.setLine1(text);
                        getLine1 = false;
                    }
                    if (getTitle && text != null) {
                        address.setTitle(text);
                        getTitle = false;
                        break;
                    }
            }
        }
        return address;
    }

    private static Customer parseCustomerXML() throws XmlPullParserException, IOException {
        Customer customer = new Customer();
        int count = 0;
        boolean getId = false;
        boolean getPassword = false;
        boolean getFirstName = false;
        boolean getLastName = false;
        boolean getEmail = false;
        boolean getGender = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String tagName = parser.getName();
                   /* if (tagName.contentEquals(ShareConstants.WEB_DIALOG_PARAM_ID) && count == 0) {
                        getId = true;
                        count++;
                    }*/
                    if (tagName.contentEquals("passwd")) {
                        getPassword = true;
                    }
                    if (tagName.contentEquals("email")) {
                        getEmail = true;
                    }
                    if (tagName.contentEquals("lastname")) {
                        getLastName = true;
                    }
                    if (tagName.contentEquals("firstname")) {
                        getFirstName = true;
                    }
                    if (!tagName.contentEquals("id_gender")) {
                        break;
                    } else {
                        getGender = true;
                        break;
                    }
                case 4:
                    String text = parser.getText();
                    if (getPassword && text != null) {
                        customer.setPassword(text);
                        getPassword = false;
                    }
                    if (getEmail && text != null) {
                        customer.setEmail(text);
                        getEmail = false;
                    }
                    if (getFirstName && text != null) {
                        customer.setFirstName(text);
                        getFirstName = false;
                    }
                    if (getLastName && text != null) {
                        customer.setLastName(text);
                        getLastName = false;
                    }
                    if (getGender && text != null) {
                      /*  if (text.contains(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                            customer.setGender(0);
                        } else {
                            customer.setGender(1);
                        }*/
                        getGender = false;
                    }
                    if (getId && text != null) {
                        customer.setId(Integer.parseInt(text));
                        customer.setUrl("http://www.organicdolchi.com/api/customers/" + text);
                        getId = false;
                        break;
                    }
            }
        }
        return customer;
    }

    private static String parsePasswordXML() throws XmlPullParserException, IOException {
        boolean getPassword = false;
        String password = "";
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    if (parser.getName().contentEquals("passwd")) {
                        getPassword = true;
                        break;
                    }
                    break;
                case 4:
                    break;
            }
            String text = parser.getText();
            if (getPassword && text != null) {
                password = text;
                getPassword = false;
            }
        }
        return password;
    }

    private static Product parseProductXML() throws XmlPullParserException, IOException {
        Product product = new Product();
        boolean nameTagStarted = false;
        boolean descTagStarted = false;
        boolean getPrice = false;
        boolean getName = false;
        boolean getDesc = false;
        boolean getSale = false;
        boolean getMinimalQuantity = false;
        boolean getunitName = false;
        boolean getId = false;
        boolean gotImgLink = false;
        boolean gotCategory = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String tagName = parser.getName();
                    if (tagName.contentEquals("name")) {
                        nameTagStarted = true;
                    } else if (!tagName.contentEquals("language") || !nameTagStarted) {
                        nameTagStarted = false;
                        getName = false;
                    } else {
                        getName = true;
                    }
                    if (tagName.contentEquals("id")) {
                        getId = true;
                    }
                    if (tagName.contentEquals("description")) {
                        descTagStarted = true;
                    } else if (!tagName.contentEquals("language") || !descTagStarted) {
                        descTagStarted = false;
                        getDesc = false;
                    } else {
                        getDesc = true;
                    }
                    if (tagName.contentEquals("price")) {
                        getPrice = true;
                    }
                    if (tagName.contentEquals("id_default_image") && !gotImgLink) {
                        product.setImageLink(parser.getAttributeValue(0));
                        gotImgLink = true;
                    }
                    if (tagName.contentEquals("on_sale")) {
                        getSale = true;
                    }
                    if (tagName.contentEquals("minimal_quantity")) {
                        getMinimalQuantity = true;
                    }
                    if (tagName.contentEquals("unity")) {
                        getunitName = true;
                    }
                    if (tagName.contentEquals("id_category_default") && !gotCategory) {
                        if (parser.getAttributeCount() > 0) {
                            Category categoryByURL = Data.getCategoryByURL(parser.getAttributeValue(0));
                            if (categoryByURL != null) {
                                product.setCategoryId(Integer.valueOf(categoryByURL.getId()));
                            }
                        }
                        gotCategory = true;
                        break;
                    }
                    break;
                case 4:
                    String text = parser.getText();
                    if (getName) {
                        product.setProductName(text);
                        getName = false;
                    }
                    if (getDesc) {
                        product.setDescription(text);
                        getDesc = false;
                    }
                    if (getPrice) {
                        product.setPrice(text);
                        getPrice = false;
                    }
                    if (getSale) {
                       /// product.setIsOnSale(text.contentEquals(AppEventsConstants.EVENT_PARAM_VALUE_YES));
                       // getSale = false;
                    }
                    if (getMinimalQuantity) {
                        try {
                            product.setMinQuantity(Integer.parseInt(text));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        getMinimalQuantity = false;
                    }
                    if (getunitName) {
                        product.setUnit(text);
                        getunitName = false;
                    }
                    if (!getId) {
                        break;
                    } else {
                        product.setId(Integer.parseInt(text));
                        getId = false;
                        break;
                    }
            }
        }
        return product;
    }

    private static ArrayList<String> parseCategoriesXML() throws XmlPullParserException, IOException {
        ArrayList<String> links = new ArrayList<>();
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    if (!parser.getName().contentEquals("category")) {
                        break;
                    } else {
                        links.add(parser.getAttributeValue(1));
                        break;
                    }
            }
        }
        return links;
    }

    private static Category parseCategoryXML() throws XmlPullParserException, IOException {
        boolean parseText = false;
        Category category = new Category();
        boolean gotName = false;
        boolean getDepth = false;
        boolean getId = false;
        for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
            switch (eventType) {
                case 2:
                    String startTag = parser.getName();
                    if (startTag.contentEquals("language")) {
                        parseText = true;
                    }
                    if (startTag.contentEquals("level_depth")) {
                        getDepth = true;
                    }
                    if (startTag.contentEquals("id")) {
                        getId = true;
                    }
                    if (startTag.contentEquals("id_parent") && parser.getAttributeCount() > 0) {
                        category.setParentCategoryURL(parser.getAttributeValue(0));
                        break;
                    }
                    break;
                case 4:
                    if (parseText && !gotName) {
                        category.setLabel(parser.getText());
                        gotName = true;
                    }
                    if (getDepth) {
                        try {
                            category.setDepth(Integer.parseInt(parser.getText()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            category.setDepth(0);
                        }
                        getDepth = false;
                    }
                    if (!getId) {
                        break;
                    } else {
                        category.setId(Integer.parseInt(parser.getText()));
                        getId = false;
                        break;
                    }
            }
        }
        return category;
    }
}
