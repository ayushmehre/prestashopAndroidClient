package com.develeno.prestashopandroidclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

public class WebService {
    public static final String BASE_URL = "http://www.organicdolchi.com/api";
    public static final String CATEGORIES_URL = BASE_URL + "/categories";
    public static final int NO_SUCH_EMAIL_ERROR = 6535465;
    public static final int NULL_RESPONSE_ERROR = 545610;

    private WebService() {
    }

    public static void search(CharSequence sequence, OnBackgroundTaskCompleted onBackgroundTaskCompleted, Activity activity) {
        new ArrayList();
        NetworkConnection.getDataInBackground(BASE_URL + "/search?query=" + sequence + "&language=1", new OnGotResponse() {
            public void getResponse(String response) {
            }
        });
    }

    public static void signIn(String email, final String password, final OnBackgroundTaskCompleted taskCompleted) {
        NetworkConnection.getDataInBackground(BASE_URL + "/customers?display=[id,firstname,lastname,passwd,email,id_gender,birthday]&filter[email]=[" + email + "]", new OnGotResponse() {
            public void getResponse(String response) {
                if (response == null) {
                    taskCompleted.getResult(Integer.valueOf(WebService.NULL_RESPONSE_ERROR));
                } else if (response.contains("passwd")) {
                    boolean b = false;
                    try {
                        b = matchPassword(password, (String) Parser.parse(response, Parser.PARSE_PASSWORD));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if (b) {
                        taskCompleted.getResult(response);
                    } else {
                        taskCompleted.getResult(null);
                    }
                } else {
                    taskCompleted.getResult(Integer.valueOf(WebService.NO_SUCH_EMAIL_ERROR));
                }
            }

            private boolean matchPassword(String password, String encryptedPassword) throws NoSuchAlgorithmException {
                return true;
            }
        });
    }

    public static void getNewUserBlankForm(final OnBackgroundTaskCompleted backgroundTaskCompleted) {
        NetworkConnection.getDataInBackground(BASE_URL + "/customers?schema=blank", new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    backgroundTaskCompleted.getResult(response);
                } else {
                    backgroundTaskCompleted.getResult(Integer.valueOf(WebService.NULL_RESPONSE_ERROR));
                }
            }
        });
    }

    public static void createNewUser(NewUserObject newUserObject, final Context context, final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        NetworkConnection.postDataInBackground(BASE_URL + "/customers", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n  <customer>\n    <id_default_group xlink:href=\"http://example.com/api/groups/3\"><![CDATA[3]]></id_default_group>\n    <id_lang xlink:href=\"http://example.com/api/languages/1\"><![CDATA[1]]></id_lang>\n    <passwd><![CDATA[" + newUserObject.getPassword() + "]]></passwd>\n" + "    <lastname><![CDATA[" + newUserObject.getLastname() + "]]></lastname>\n" + "    <firstname><![CDATA[" + newUserObject.getFirstname() + "]]></firstname>\n" + "    <email><![CDATA[" + newUserObject.getEmail() + "]]></email>\n" + "    <id_gender><![CDATA[" + newUserObject.getGender() + "]]></id_gender >\n " + "    <associations>\n" + "      <groups node_type=\"group\">\n" + "        <group xlink:href=\"http://example.com/api/groups/3\">\n" + "          <id><![CDATA[3]]></id>\n" + "        </group>\n" + "      </groups>\n" + "    </associations>\n" + "  </customer>\n" + "</prestashop>", new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    Response response = (Response) result;
                    if (response.getResponseCode() == 201) {
                        Data.setCurrentUser((Customer) Parser.parse(response.getValue(), Parser.PARSE_CUSTOMER), context);
                        onBackgroundTaskCompleted.getResult(Boolean.valueOf(true));
                        return;
                    }
                    onBackgroundTaskCompleted.getResult(Boolean.valueOf(false));
                    return;
                }
                onBackgroundTaskCompleted.getResult(Boolean.valueOf(false));
            }

            public void refresh(Object result) {
            }
        });
    }

    public static void saveUser(Customer customer, final Context context, final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        NetworkConnection.postDataInBackground(customer.getUrl(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n  <customer>\n<id><![CDATA[38]></id>    <passwd><![CDATA[" + customer.getPassword() + "]]></passwd>\n" + "    <lastname><![CDATA[" + customer.getLastName() + "]]></lastname>\n" + "    <firstname><![CDATA[" + customer.getFirstName() + "]]></firstname>\n" + "    <email><![CDATA[" + customer.getEmail() + "]]></email>\n" + "    <id_gender><![CDATA[" + customer.getGender() + "]]></id_gender >\n " + "  </customer>\n" + "</prestashop>", new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    Response response = (Response) result;
                    if (response.getResponseCode() == 201) {
                        Data.setCurrentUser((Customer) Parser.parse(response.getValue(), Parser.PARSE_CUSTOMER), context);
                        onBackgroundTaskCompleted.getResult(Boolean.valueOf(true));
                        return;
                    }
                    onBackgroundTaskCompleted.getResult(Boolean.valueOf(false));
                    return;
                }
                onBackgroundTaskCompleted.getResult(Boolean.valueOf(false));
            }

            public void refresh(Object result) {
            }
        });
    }

    public static void fetchCategories(final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        final ArrayList<Category> categoryArrayList = new ArrayList<>();
        NetworkConnection.getDataInBackground(CATEGORIES_URL, new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    WebService.generateCategoryObjectsFromLinks((ArrayList) Parser.parse(response, Parser.PARSE_CATEGORIES), categoryArrayList, onBackgroundTaskCompleted);
                } else {
                    WebService.generateCategoryObjectsFromLinks(null, categoryArrayList, onBackgroundTaskCompleted);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static void generateCategoryObjectsFromLinks(final ArrayList<String> links, final ArrayList<Category> categoryArrayList, final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        if (links != null) {
            Iterator it = links.iterator();
            while (it.hasNext()) {
                NetworkConnection.getDataInBackground((String) it.next(), new OnGotResponse() {
                    public void getResponse(String response) {
                        categoryArrayList.add((Category) Parser.parse(response, Parser.PARSE_CATEGORY));
                        if (categoryArrayList.size() == links.size()) {
                            onBackgroundTaskCompleted.getResult(categoryArrayList);
                        }
                    }
                });
            }
            return;
        }
        onBackgroundTaskCompleted.getResult(null);
    }

    public static void fetchTheseProducts(final ArrayList<String> linksOfProducts, final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        final ArrayList<Product> productArrayList = new ArrayList<>();
        Iterator it = linksOfProducts.iterator();
        while (it.hasNext()) {
            NetworkConnection.getDataInBackground((String) it.next(), new OnGotResponse() {
                public void getResponse(String response) {
                    productArrayList.add((Product) Parser.parse(response, Parser.PARSE_PRODUCT));
                    onBackgroundTaskCompleted.refresh(productArrayList);
                    if (linksOfProducts.size() == productArrayList.size()) {
                        onBackgroundTaskCompleted.getResult(productArrayList);
                    }
                }
            });
        }
    }

    public static void fetchTheseProductsWithImages(ArrayList<String> linksOfProducts, final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        final ArrayList<Product> productArrayList = new ArrayList<>();
        Iterator it = linksOfProducts.iterator();
        while (it.hasNext()) {
            NetworkConnection.getDataInBackground((String) it.next(), new OnGotResponse() {
                public void getResponse(String response) {
                    final Product product = (Product) Parser.parse(response, Parser.PARSE_PRODUCT);
                    if (product.getImageLink() != null) {
                        NetworkConnection.fetchImageInBackground(product.getImageLink(), new OnBackgroundTaskCompleted() {
                            public void getResult(Object result) {
                                product.setImageBitmap((Bitmap) result);
                                productArrayList.add(product);
                                onBackgroundTaskCompleted.refresh(productArrayList);
                            }

                            public void refresh(Object result) {
                            }
                        }, false);
                        return;
                    }
                    productArrayList.add(product);
                    onBackgroundTaskCompleted.refresh(productArrayList);
                }
            });
        }
    }

    public static void fetchAllProducts(final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        final String url = BASE_URL + "/products?display=[id,id_manufacturer,id_supplier,id_category_default,new,cache_default_attribute,id_default_image,id_default_combination,id_tax_rules_group,position_in_category,manufacturer_name,quantity,type,id_shop_default,reference,supplier_reference,location,width,height,depth,weight,quantity_discount,ean13,upc,cache_is_pack,cache_has_attachments,is_virtual,on_sale,online_only,ecotax,minimal_quantity,price,wholesale_price,unity,unit_price_ratio,additional_shipping_cost,customizable,text_fields,uploadable_files,active,redirect_type,available_for_order,available_date,condition,show_price,indexed,visibility,advanced_stock_management,date_add,date_upd,pack_stock_type,meta_description,meta_keywords,meta_title,link_rewrite,name,description,description_short,available_now,available_later]";
        NetworkConnection.getDataInBackground(url, new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    ArrayList<Product> products = new ArrayList<>();
                    for (String res : response.split("</product>")) {
                        products.add((Product) Parser.parse(res, Parser.PARSE_PRODUCT));
                    }
                    onBackgroundTaskCompleted.getResult(products);
                    return;
                }
                onBackgroundTaskCompleted.getResult(null);
            }
        });
    }

    public static void fetchAllStocksAvailableInfo(final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        NetworkConnection.getDataInBackground(BASE_URL + "/stock_availables?display=[id_product,id,quantity]", new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    ArrayList<StockInfo> stockInfos = new ArrayList<>();
                    for (String res : response.split("</stock_available>")) {
                        stockInfos.add((StockInfo) Parser.parse(res, Parser.PARSE_STOCK_INFO));
                    }
                    onBackgroundTaskCompleted.getResult(stockInfos);
                    return;
                }
                onBackgroundTaskCompleted.getResult(null);
            }
        });
    }

    public static void fetchAllCategories(final OnBackgroundTaskCompleted onBackgroundTaskCompleted) {
        NetworkConnection.getDataInBackground(BASE_URL + "/categories?display=[id,id_parent,level_depth,nb_products_recursive,nleft,nright,active,id_shop_default,is_root_category,position,date_add,date_upd,name,link_rewrite,description,meta_title,meta_description,meta_keywords]", new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    ArrayList<Category> categories = new ArrayList<>();
                    for (String res : response.split("</category>")) {
                        Category category = (Category) Parser.parse(res, Parser.PARSE_CATEGORY);
                        if (category != null) {
                            categories.add(category);
                        }
                    }
                    onBackgroundTaskCompleted.getResult(categories);
                    return;
                }
                onBackgroundTaskCompleted.getResult(null);
            }
        });
    }

    public static void fetchAllAddresses(final OnBackgroundTaskCompleted taskCompleted, Context context) {
        NetworkConnection.getDataInBackground(BASE_URL + "/addresses?display=[id,id_customer,id_manufacturer,id_supplier,id_warehouse,id_country,id_state,alias,company,lastname,firstname,vat_number,address1,address2,postcode,city,other,phone,phone_mobile,dni,deleted,date_add,date_upd]&filter[id_customer]=[" + Data.getCurrentUser(context).getId() + "]", new OnGotResponse() {
            public void getResponse(String response) {
                if (response != null) {
                    ArrayList<Address> addresses = new ArrayList<>();
                    String[] responses = response.split("</address>");
                    if (response.contains("</address>")) {
                        for (String res : responses) {
                            Address address = (Address) Parser.parse(res, Parser.PARSE_ADDRESS);
                            if (address != null) {
                                addresses.add(address);
                            }
                        }
                    }
                    taskCompleted.getResult(addresses);
                    return;
                }
                taskCompleted.getResult(null);
            }
        });
    }

    public static void addNewAddress(Address address, Context context, final OnBackgroundTaskCompleted taskCompleted) {
        Customer currentUser = Data.getCurrentUser(context);
        NetworkConnection.postDataInBackground(BASE_URL + "/addresses", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n<address>\n\t<id_customer>" + currentUser.getId() + "</id_customer>\n" + "\t<id_country>110</id_country>\n" + "\t<id_state>326</id_state>\n" + "\t<alias>" + address.getTitle() + "</alias>\n" + "\t<lastname>" + currentUser.getLastName() + "</lastname>\n" + "\t<firstname>" + currentUser.getFirstName() + "</firstname>\n" + "\t<address1>" + address.getLine1() + "</address1>\n" + "\t<address2>" + address.getLine2() + "</address2>\n" + "\t<postcode>" + address.getPinCode() + "</postcode>\n" + "\t<city>Jabalpur</city>\n" + "\t<phone>0000000000</phone>\n" + "\t<phone_mobile>0000000000</phone_mobile>\n" + "</address>\n" + "</prestashop>\n", new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    Response response = (Response) result;
                    if (response.getResponseCode() == 201) {
                        Data.addToAddresses((Address) Parser.parse(response.getValue(), Parser.PARSE_ADDRESS));
                        taskCompleted.getResult(Boolean.valueOf(true));
                        return;
                    }
                    taskCompleted.getResult(Boolean.valueOf(false));
                    return;
                }
                taskCompleted.getResult(Boolean.valueOf(false));
            }

            public void refresh(Object result) {
            }
        });
    }

    public static void placeOrder(final Context context, final OnBackgroundTaskCompleted taskCompleted) {
        NetworkConnection.postDataInBackground(BASE_URL + "/orders", generateOrderXML(context), new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                String msg = "";
                String str = "";
                if (result != null) {
                    Response response = (Response) result;
                    if (response.getResponseCode() == 201) {
                        msg = "Successful";
                        Data.addToAddresses((Address) Parser.parse(response.getValue(), Parser.PARSE_ADDRESS));
                        taskCompleted.getResult(Boolean.valueOf(true));
                    } else {
                        //msg = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED;
                        taskCompleted.getResult(Boolean.valueOf(false));
                    }
                } else {
                    msg = "failed\nGot Null Response";
                    taskCompleted.getResult(Boolean.valueOf(false));
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            public void refresh(Object result) {
            }
        });
    }

    private static String generateOrderXML(Context context) {
        Order order = Data.getCurrentOrder();
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n<order>\n\t<id></id>\n\t<id_address_delivery>" + order.getAddress().getId() + "</id_address_delivery>\n" + "\t<id_address_invoice>" + order.getAddress().getId() + "</id_address_invoice>\n" + "\t<id_cart>" + order.getCartId() + "</id_cart>\n" + "\t<id_currency>2</id_currency>\n" + "\t<id_lang>1</id_lang>\n" + "\t<id_customer>" + Data.getCurrentUser(context).getId() + "</id_customer>\n" + "\t<id_carrier>" + order.getCarrierId() + "</id_carrier>\n" + "\t<current_state></current_state>\n" + "\t<module>" + "cashondelivery" + "</module>\n" + "\t<invoice_number></invoice_number>\n" + "\t<invoice_date></invoice_date>\n" + "\t<delivery_number></delivery_number>\n" + "\t<delivery_date></delivery_date>\n" + "\t<valid></valid>\n" + "\t<date_add></date_add>\n" + "\t<date_upd></date_upd>\n" + "\t<shipping_number></shipping_number>\n" + "\t<id_shop_group></id_shop_group>\n" + "\t<id_shop></id_shop>\n" + "\t<secure_key></secure_key>\n" + "\t<payment>" + order.getPaymentMethod() + "</payment>\n" + "\t<recyclable></recyclable>\n" + "\t<gift></gift>\n" + "\t<gift_message></gift_message>\n" + "\t<mobile_theme></mobile_theme>\n" + "\t<total_discounts></total_discounts>\n" + "\t<total_discounts_tax_incl></total_discounts_tax_incl>\n" + "\t<total_discounts_tax_excl></total_discounts_tax_excl>\n" + "\t<total_paid>" + order.getTotalAmountPayable() + "</total_paid>\n" + "\t<total_paid_tax_incl></total_paid_tax_incl>\n" + "\t<total_paid_tax_excl></total_paid_tax_excl>\n" + "\t<total_paid_real>" + order.getTotalAmountPayable() + "</total_paid_real>\n" + "\t<total_products>" + Cart.getTotal() + "</total_products>\n" + "\t<total_products_wt>" + Cart.getTotal() + "</total_products_wt>\n" + "\t<total_shipping></total_shipping>\n" + "\t<total_shipping_tax_incl></total_shipping_tax_incl>\n" + "\t<total_shipping_tax_excl></total_shipping_tax_excl>\n" + "\t<carrier_tax_rate></carrier_tax_rate>\n" + "\t<total_wrapping></total_wrapping>\n" + "\t<total_wrapping_tax_incl></total_wrapping_tax_incl>\n" + "\t<total_wrapping_tax_excl></total_wrapping_tax_excl>\n" + "\t<round_mode></round_mode>\n" + "\t<conversion_rate>" + "1.00" + "</conversion_rate>\n" + "\t<reference></reference>\n" + "<associations>\n" + "<order_rows>\n" + "\t<order_row>\n" + "\t<id></id>\n" + "\t<product_id></product_id>\n" + "\t<product_attribute_id></product_attribute_id>\n" + "\t<product_quantity></product_quantity>\n" + "\t<product_name></product_name>\n" + "\t<product_reference></product_reference>\n" + "\t<product_ean13></product_ean13>\n" + "\t<product_upc></product_upc>\n" + "\t<product_price></product_price>\n" + "\t<unit_price_tax_incl></unit_price_tax_incl>\n" + "\t<unit_price_tax_excl></unit_price_tax_excl>\n" + "\t</order_row>\n" + "</order_rows>\n" + "</associations>\n" + "</order>\n" + "</prestashop>";
    }

    public static void createNewCart(Context context, final OnBackgroundTaskCompleted taskCompleted) {
        NetworkConnection.postDataInBackground(BASE_URL + "/carts", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<prestashop xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n<cart>\n\t<id_address_delivery>" + Data.getCurrentOrder().getAddress().getId() + "</id_address_delivery>\n" + "\t<id_address_invoice>" + Data.getCurrentOrder().getAddress().getId() + "</id_address_invoice>\n" + "\t<id_currency>2</id_currency>\n" + "\t<id_customer>" + Data.getCurrentUser(context).getId() + "</id_customer>\n" + "\t<id_guest></id_guest>\n" + "\t<id_lang>1</id_lang>\n" + "\t<id_shop_group></id_shop_group>\n" + "\t<id_shop></id_shop>\n" + "\t<id_carrier></id_carrier>\n" + "\t<recyclable></recyclable>\n" + "\t<gift></gift>\n" + "\t<gift_message></gift_message>\n" + "\t<mobile_theme></mobile_theme>\n" + "\t<delivery_option></delivery_option>\n" + "\t<secure_key></secure_key>\n" + "\t<allow_seperated_package></allow_seperated_package>\n" + "\t<date_add></date_add>\n" + "\t<date_upd></date_upd>\n" + "<associations>\n" + "<cart_rows>\n" + "\t<cart_row>\n" + "\t<id_product></id_product>\n" + "\t<id_product_attribute></id_product_attribute>\n" + "\t<id_address_delivery></id_address_delivery>\n" + "\t<quantity></quantity>\n" + "\t</cart_row>\n" + generateCartRows() + "</cart_rows>\n" + "</associations>\n" + "</cart>\n" + "</prestashop>", new OnBackgroundTaskCompleted() {
            public void getResult(Object result) {
                if (result != null) {
                    taskCompleted.getResult(Parser.parse(((Response) result).getValue(), Parser.PARSE_CART));
                    return;
                }
                taskCompleted.getResult(null);
            }

            public void refresh(Object result) {
            }
        });
    }

    private static String generateCartRows() {
        String string = "";
        Iterator it = Data.getCurrentOrder().getItems().iterator();
        while (it.hasNext()) {
            string = string + "\n" + generateCartRow((CartItem) it.next());
        }
        return string;
    }

    private static String generateCartRow(CartItem item) {
        return "<cart_row>\n\t<id_product>" + item.getItem().getId() + "</id_product>\n" + "\t<id_product_attribute>0</id_product_attribute>\n" + "\t<id_address_delivery>" + Data.getCurrentOrder().getAddress().getId() + "</id_address_delivery>\n" + "\t<quantity>" + item.getQuantity() + "</quantity>\n" + "\t</cart_row>";
    }
}
