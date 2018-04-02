package main;

import java.io.Serializable;

/**This class is used to provide an object that will be with
 * papameters like ship.
 * Has just three variables: name goods and boolean busy
 * @author Rostislav Pekhovksy 2018*/
public class Ship implements Serializable {

    /**Name of ship*/
    public String name;
    /**Goods of ship described in string*/
    public String goods;
    /**This var determines is that ship is used by server (true) or not (false)*/
    boolean busy;

    public boolean auto;

    /**Constructor
     * @param name name of ship
     * @param goods goods in ship*/
    public Ship(String name, String goods) {
        this.name = name;
        this.goods = goods;
        this.busy = false;
        this.auto = false;
    }

    /**Constructor (for ship without goods)
     * @param name name of ship*/
    public Ship(String name) {
        this.name = name;
        this.goods = "";
        this.busy = false;
    }

    /**@return is that ship is used by server (port) or not*/
    public boolean isBusy(){
        return busy;
    }
    /**@param busy setting the var that determines is that ship is used by server*/
    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    /**@return name of this ship (override is used for ListView)*/
    @Override
    public String toString() {
        return name;
    }
}
