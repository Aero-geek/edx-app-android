package org.edx.mobile.view.custom;

public class MyListData {
    private String name;
    private String price;
    private String image;
    private String jina;


    public MyListData(String name, String price, String image, String jina) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.jina = jina;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJina() {
        return jina;
    }

    public void setJina(String jina) {
        this.jina = jina;
    }
}
