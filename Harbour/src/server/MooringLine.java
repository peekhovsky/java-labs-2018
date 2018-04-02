package server;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import main.*;

public class MooringLine {


    private  SimpleIntegerProperty number;
    private  SimpleStringProperty status;
    private  SimpleStringProperty shipName;
    private  SimpleStringProperty goods;

    private Ship ship;
    private boolean isOpen;

    public MooringLine(Integer number) {
        this.number = new SimpleIntegerProperty(number);
        this.status = new SimpleStringProperty("empty");
        this.shipName = new SimpleStringProperty("");
        this.goods = new SimpleStringProperty("");

        isOpen = true;
        this.ship = null;
    }

    //number
    public Integer getNumber(){
        return number.get();
    }


    public void setNumber(Integer number){
        this.number.set(number);
    }

    //status
    public String getStatus() {
        return status.get();
    }


    private void setStatus(String status) {
        this.status.set(status);
    }

    //shipName
    public String getShipName() {
        return shipName.get();
    }

    private void setShipName(String shipName) {
        this.shipName.set(shipName);
    }

    //goods
    public String getGoods(){
        return goods.get();
    }

    public void setGoods(String goods){
        this.goods.set(goods);
    }

    //ship
    public void setShip(Ship ship){
        this.ship = ship;
        setShipName(ship.name); System.out.println(ship.name);
        setGoods(ship.goods); System.out.println(ship.goods);
        setStatus("busy");
    }

    public Ship getShip(){
        return ship;
    }


    public void freeLine(){
        setGoods("");
        setShipName("");
        setStatus("empty");
        ship = null;
    }

    //isOpen
    boolean isOpen(){
        return isOpen;
    }
}
